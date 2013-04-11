package com.me.mypenguins.screens;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.me.mypenguins.model.GameWorld;
import com.me.mypenguins.view.WorldRenderer;
import com.me.mypenguins.screens.TiledMapHelper;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameScreen implements Screen, InputProcessor{

	public static final float PIXELS_PER_METER = 60.0f;
	private final float WATER_LEVEL = 1.4f,
			  			BUOYANCY = 50,
			  			WATER_DRAG = 1;
	
	private float waterCurrent = 0,
				  fishSpeed = 2.5f;
	
	private GameWorld w;
	private WorldRenderer renderer;
	private long lastRender;
	
	public Sprite rampSprite,
				  pSprite,
				  fSprite;
	
	private SpriteBatch batch = new SpriteBatch();
	
	private TiledMapHelper tiledMapHelper;
	//private Box2DDebugRenderer debugRenderer;
	
	private Texture overallTexture;
	private TextureRegion region;
	private Mesh mesh = null;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	boolean firstTouch = true,
			firstRamp = true,
			fishTurn = false;
	
	private int touchUp = 0,
			    width,
			    height;

	@Override
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 * 
	 * This is the game loop.
	 */
	public void render(float delta) {
		
		long now = System.nanoTime();
		//float slopePoints[] = new float[18];
		//Vector2 slopevect[] = w.getSlopeVects();
		Vector2 slopevect[] = w.getSlopeVects();

		//world.getWorld().step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				Gdx.gl.glClearColor(0, 0.5f, 0.9f, 0);
				tiledMapHelper.getCamera().position.x = PIXELS_PER_METER * w.getPenguin().getPosition().x;


		if (tiledMapHelper.getCamera().position.x < Gdx.graphics.getWidth() / 2) {
			tiledMapHelper.getCamera().position.x = Gdx.graphics.getWidth() / 2;
		}
		if (tiledMapHelper.getCamera().position.x >= tiledMapHelper.getWidth()
				- Gdx.graphics.getWidth() / 2) {
			tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth()
					- Gdx.graphics.getWidth() / 2;
		}

		if (tiledMapHelper.getCamera().position.y < Gdx.graphics.getHeight() / 2) {
			tiledMapHelper.getCamera().position.y = Gdx.graphics.getHeight() / 2;
		}
		if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()
				- Gdx.graphics.getHeight() / 2) {
			tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight()
					- Gdx.graphics.getHeight() / 2;
		}
		
		/**
		 * Place fish in water and get them to swim end to end forever
		 */
		for(int i = 0; i < w.FISH_SCHOOL; i++) {
			if(w.getFish(i).getPosition().x >= 30)
					fishTurn = true;
			else if(w.getFish(i).getPosition().x <= 10)
					fishTurn = false;
			if(!fishTurn) {
				if(w.getFish(i).getPosition().y < WATER_LEVEL-.5)		// check if each fish is below water level
					w.getFish(i).applyForceToCenter(fishSpeed, BUOYANCY);
				w.getFish(i).setLinearDamping(WATER_DRAG);
			}
			else if(fishTurn) {
				if(w.getFish(i).getPosition().y < WATER_LEVEL-.5)		// check if each fish is below water level
					w.getFish(i).applyForceToCenter(-fishSpeed, BUOYANCY);
				w.getFish(i).setLinearDamping(WATER_DRAG);
			}
		}
		
		tiledMapHelper.getCamera().update();
		tiledMapHelper.render();

		tiledMapHelper.getCamera().position.x = PIXELS_PER_METER * w.getPenguin().getPosition().x;
		/*for (int i = 0,j = 0; i  < 3 && j  < 18; ++i) {
			slopePoints[j++] = slopevect[i].x + w.getSlopeBodyPos().x;
			slopePoints[j++] = slopevect[i].y + w.getSlopeBodyPos().y;
			slopePoints[j++] = 0;
			slopePoints[j++] = Color.toFloatBits(255, 255, 255, 255);
			slopePoints[j++] = 0;
			slopePoints[j++] = 0;
		}*/
		//mesh.setVertices(slopePoints);

		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		//mesh.render(GL10.GL_TRIANGLES, 0, 3);
		
		shapeRenderer.setProjectionMatrix(renderer.getCam().combined);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.begin(ShapeType.FilledTriangle);
		shapeRenderer.filledTriangle(slopevect[0].x + w.getSlopeBodyPos().x, slopevect[0].y + w.getSlopeBodyPos().y, slopevect[1].x + w.getSlopeBodyPos().x, slopevect[1].y + w.getSlopeBodyPos().y, slopevect[2].x + w.getSlopeBodyPos().x, slopevect[2].y + w.getSlopeBodyPos().y);
		shapeRenderer.end();
		
		if(w.getPenguin().getPosition().y < WATER_LEVEL) {				// check if penguin is below water line
			activateWater();
		}

		
		drawPenguinSprite();

		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */

		now = System.nanoTime();
		if (now - lastRender < 30000000) { // 30 ms, ~33FPS
			try {
				Thread.sleep(30 - (now - lastRender) / 1000000);
			} catch (InterruptedException e) {
			}
		}

		lastRender = now;
		renderer.render(tiledMapHelper.getWidth(), tiledMapHelper.getHeight());//and render
		w.update();
	}

	public void drawPenguinSprite(){
		
		pSprite = w.getPenguinSprite();
		fSprite = w.getFishSprite();
		
		float pen_x, 
			  pen_y; 
		
		float fish_x,
			  fish_y;
		
		batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		batch.begin();

		/*pen_x = PIXELS_PER_METER*(w.getPenguin().getPosition().x) - pSprite.getWidth()/2;
		pen_y = PIXELS_PER_METER*(w.getPenguin().getPosition().y) - pSprite.getHeight()/2;*/
		
		pen_x = PIXELS_PER_METER*(w.getPenguin().getWorldCenter().x) - pSprite.getWidth()/2;
		pen_y = PIXELS_PER_METER*(w.getPenguin().getWorldCenter().y) - pSprite.getHeight()/2;
		
		pSprite.setPosition(pen_x-5/*+60*/, pen_y+7/*+65*/);
		
		pSprite.setOrigin(w.getPenguin().getPosition().x/2, w.getPenguin().getPosition().y/2);
		pSprite.setRotation(MathUtils.radiansToDegrees*w.getPenguin().getAngle());
		pSprite.draw(batch);
		
		for( int i = 0; i < w.FISH_SCHOOL; i++ ){
			fish_x = PIXELS_PER_METER*(w.getFish(i).getPosition().x) - fSprite.getWidth()/2;
			fish_y = PIXELS_PER_METER*(w.getFish(i).getPosition().y) - fSprite.getHeight()/2;
			fSprite.setPosition(fish_x+55, fish_y+55);

			fSprite.setOrigin(w.getFish(i).getPosition().x/2, w.getFish(i).getPosition().y/2);
			fSprite.setRotation(MathUtils.radiansToDegrees*w.getFish(i).getAngle());
			fSprite.draw(batch);
		}
		batch.end();
	}
	public void activateWater() {
		w.getPenguin().applyForceToCenter(waterCurrent, BUOYANCY);
		w.getPenguin().setLinearDamping(WATER_DRAG);

//		makeBubbles();
//		world.getBubble().applyForceToCenter(waterCurrent, BUOYANCY);
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 * 
	 * Provides the dimensions, in pixels of the screen
	 */
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		System.out.println( width + " x " + height);
		tiledMapHelper.prepareCamera(width, height);
		renderer.resizeCam(width, height);
		w.BOX_TO_WORLD_WIDTH = width / renderer.getCam().viewportHeight;
		w.BOX_TO_WORLD_HEIGHT = height / renderer.getCam().viewportHeight;

	}

	@Override
	/*
       Game is initialized here
	 */
	public void show() {
		//w = new GameWorld();


		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		Gdx.input.setInputProcessor(this);

		tiledMapHelper = new TiledMapHelper();
/*
		tiledMapHelper = new TiledMapHelper();

		if (mesh == null) {
			mesh = new Mesh(true, 3, 3, 
					new VertexAttribute(Usage.Position, 3, "a_position"),
					new VertexAttribute(Usage.ColorPacked, 4, "a_color"),
					new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));

			mesh.setIndices(new short[] { 0, 1, 2 });

			FileHandle imageFileHandle = Gdx.files.internal("ramp.png"); 
			overallTexture = new Texture(imageFileHandle);
		}
*/
		tiledMapHelper.setPackerDirectory("data/packer");

		tiledMapHelper.loadMap("data/world/level1/level.tmx");

		tiledMapHelper.prepareCamera(width, height);

		w = new GameWorld(new Vector2(0.0f, -10.0f), true);
		renderer = new WorldRenderer(w, true);
		tiledMapHelper.loadCollisions("data/collisions.txt", w,
				PIXELS_PER_METER);

		//debugRenderer = new Box2DDebugRenderer();

		lastRender = System.nanoTime();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		
		Camera cam = new OrthographicCamera(5f, 3f);

		float xpos = x / renderer.TILE_SCALE_FACTOR;
		float ypos = (height - y) / renderer.TILE_SCALE_FACTOR;


		xpos += (renderer.getCam().position.x - (renderer.getCam().viewportWidth / 2));
		ypos += (renderer.getCam().position.y - (renderer.getCam().viewportHeight / 2));

		Vector3 touchDown = new Vector3( x, y, 0);
		cam.unproject(touchDown);


		boolean slopeCheck = false;
		if( firstRamp ){
			slopeCheck = w.checkSlope(xpos, ypos);
			firstRamp = false;//checks to see if the pointer is trying to drag the slope
		}
		Gdx.app.log("touchDownDetector", "touchDown at " + x + " "+ y);
		Gdx.app.log("slopeCheck", "slope check is  " + slopeCheck);
		if( !slopeCheck ){
			if( firstTouch && touchUp > 1){
				w.checkPenguin(/*touchDown.x, touchDown.y*/x, y);
				Gdx.app.log("GestureDetectorTest", "tap at " + touchDown.x + ", " + touchDown.y);
				Gdx.app.log("GestureDetectorTest2", "body at" + w.getPenguin().getPosition().x + " " + w.getPenguin().getPosition().y);
				firstTouch = false;
				
			}
		}
		//w.addDynamicCircle(3f, 3f, 4f);
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		Gdx.app.log("touchUpDetector", "touchUp at " + x + " "+ y);
	
		touchUp++;
		Gdx.app.log("touchUpCount: ", "count: " + touchUp);
		
			if( firstTouch && touchUp > 1){
				w.checkPenguin(/*touchDown.x, touchDown.y*/x, y);
				firstTouch = false;
			}
		
		return true;
	}

	@Override
	/*
	   The first part of this function is a little weird so I think it's 
	   worth explaining.

	   The x and y parameters that are received = the pixel coordinates.
	   To convert to the box2d coordinates(meters), they must be divided by the 
	   number of pixels per meter(which has been set elsewhere).

	   Since the converted coordinates are only relative to the current screen
	   they have to be added to the absolute box2d world coordinates that are 
	   displayed at (0,0) on the screen.   
	 */
	public boolean touchDragged(int x, int y, int pointer) {
		float xpos = x / renderer.TILE_SCALE_FACTOR;
		float ypos = (height - y) /renderer.TILE_SCALE_FACTOR;

		xpos += (renderer.getCam().position.x - (renderer.getCam().viewportWidth / 2));
		ypos += (renderer.getCam().position.y - (renderer.getCam().viewportHeight / 2));

		if(!w.penguinClicked){
			w.checkSlope(xpos, ypos);//checks to see if the pointer is trying to drag the slope
		}
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
