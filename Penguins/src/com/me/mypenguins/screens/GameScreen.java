package com.me.mypenguins.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
<<<<<<< HEAD
=======
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
>>>>>>> master
import com.me.mypenguins.model.GameWorld;
import com.me.mypenguins.view.WorldRenderer;
import com.me.mypenguins.screens.TiledMapHelper;

public class GameScreen implements Screen, InputProcessor{
	
	private GameWorld w;
	private WorldRenderer renderer;
	private long lastRender;
	private TiledMapHelper tiledMapHelper;
	//private Box2DDebugRenderer debugRenderer;
	public static final float PIXELS_PER_METER = 60.0f;
    
	private int width;
	private int height;
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 * 
	 * This is the game loop.
	 */
	public void render(float delta) {
		long now = System.nanoTime();
		
		//world.getWorld().step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0.5f, 0.9f, 0);
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

		tiledMapHelper.getCamera().update();
		tiledMapHelper.render();

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
		renderer.render();//and render
		//w.update();
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
		w.BOX_TO_WORLD_WIDTH = width / renderer.getCam().viewportHeight;
		w.BOX_TO_WORLD_HEIGHT = height / renderer.getCam().viewportHeight;
		
	}

	@Override
	/*
       Game is initialized here
	*/
	public void show() {
		//w = new GameWorld();
		renderer = new WorldRenderer(w, true);
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		Gdx.input.setInputProcessor(this);
		
		tiledMapHelper = new TiledMapHelper();
		
		tiledMapHelper = new TiledMapHelper();

		tiledMapHelper.setPackerDirectory("data/packer");

		tiledMapHelper.loadMap("data/world/level1/level.tmx");

		tiledMapHelper.prepareCamera(width, height);

		w = new GameWorld(new Vector2(0.0f, -10.0f), true);

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
<<<<<<< HEAD
		//boolean firstFingerTouching = Gdx.input.isTouched(0);
        //w.addDynamicCircle(x / w.BOX_TO_WORLD_WIDTH, (height - y) / w.BOX_TO_WORLD_HEIGHT, 5f);
		w.checkSlope(x / w.BOX_TO_WORLD_WIDTH, (height - y) / w.BOX_TO_WORLD_HEIGHT);
		w.checkBall(x / w.BOX_TO_WORLD_WIDTH, (height - y) / w.BOX_TO_WORLD_HEIGHT);//hacky function to apply force to the ball, should be removed eventually
		System.out.println("X: " + (x / w.BOX_TO_WORLD_WIDTH) +" Y: " + (height - y) / w.BOX_TO_WORLD_HEIGHT);
=======
		boolean firstFingerTouching = Gdx.input.isTouched(0);
		
		float xpos = x / w.BOX_TO_WORLD_WIDTH;
		float ypos = (height - y) / w.BOX_TO_WORLD_HEIGHT;
		
		xpos += (renderer.getCam().position.x - (renderer.CAMERA_VIEW_WIDTH / 2));
		ypos += (renderer.getCam().position.y - (renderer.CAMERA_VIEW_HEIGHT / 2));

		w.checkBall(xpos, ypos);
		if(!w.penguinClicked){
			   w.checkSlope(xpos, ypos);//checks to see if the pointer is trying to drag the slope
			}
>>>>>>> master
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
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
		float xpos = x / w.BOX_TO_WORLD_WIDTH;
		float ypos = (height - y) / w.BOX_TO_WORLD_HEIGHT;
		
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
