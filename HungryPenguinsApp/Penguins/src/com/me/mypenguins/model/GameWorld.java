package com.me.mypenguins.model;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import aurelienribon.bodyeditor.BodyEditorLoader;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameWorld {
	
	public int FISH_SCHOOL = 6;
	public int FISH_COUNT = FISH_SCHOOL;
	public float BOX_TO_WORLD_WIDTH;//pixels per meter
	public float BOX_TO_WORLD_HEIGHT;
	public float slopeMuly = 0.62857142f;
	
	private SpriteBatch batch = new SpriteBatch();
	private BitmapFont font;
	
	private World w;
	private Body slopeBody,
				 pBody;
	
	private Body [] fish = new Body[FISH_SCHOOL];
	
	private Fixture slopeFixture;
					//circleFixture;
	
	private Vector2 slopeBodyPos;
	private Vector2 slopeBodyDim;//width and height of slope
	public Vector2 originalPengPos;
	
	public Sprite rampSprite,
				  pSprite,
				  fSprite,
				  fontSprite;
	
	public boolean penguinClicked = false;
	private Vector2 vertices[];
	private Texture texture;

	List<Body> bodies;//keeps track of the world's bodies

	public GameWorld(){
		w = new World(new Vector2(0, -.50f), true);
		//w = new World(new Vector2(0, -5f), true);
		initializeTheSlope(4.20f, 4.40f, 3.70f, 3.75f);
		//createRamp(100, 200);
		pBody = addPenguin();
		originalPengPos = new Vector2(pBody.getPosition().x, pBody.getPosition().y);
		setSprite();
		createCollisionListener();
		
		for(int i = 0; i < FISH_SCHOOL; i++)
			fish[i] = addFish((10+i), 1.5f, .2f);
	
	}

	public GameWorld(Vector2 v2, boolean bool){
		w = new World(v2, bool);
		
		initializeTheSlope(4.2f, 6.4f, 3.7f, 3.75f);
		createCollisionListener();
		
		pBody = addPenguin();
		originalPengPos = new Vector2(pBody.getPosition().x, pBody.getPosition().y);
		
		setSprite();
		
		for(int i = 0; i < FISH_SCHOOL; i++)
			fish[i] = addFish((10+i), 1.5f, .2f);
	}
	
	private void createCollisionListener() {
        w.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
            	
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                
                
                //Gdx.app.log("beginContact", "between " + fixtureA.getBody().getUserData() + " and " + fixtureB.getBody().getUserData());
                if( fixtureA.getBody().getUserData() == "fish" && fixtureB.getBody().getUserData() == "penguin"){
                	fixtureA.getBody().setUserData("true");
                }
                else if( fixtureB.getBody().getUserData() == "fish" && fixtureA.getBody().getUserData() == "penguin"){
                	fixtureB.getBody().setUserData("true");
                }
                	
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
               // Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });
    }

	private void initializeTheSlope(float xpos1, float xpos2, float ypos1, float ypos2) {
		BodyDef bodydef = new BodyDef();
		
		slopeBodyPos = new Vector2(xpos1, ypos1);
		slopeBodyDim = new Vector2((xpos2 - xpos1), (ypos2 - ypos1));
		bodydef.position.set(slopeBodyPos);
		slopeBody = w.createBody(bodydef);
		changeTheSlopeTo(slopeBodyDim.x, slopeBodyDim.y);	
	}

	public void changeTheSlopeTo(float width, float height) {


		vertices = new Vector2[3];
		vertices[0] = new Vector2(0, 0);

		vertices[1] = new Vector2(width, 0);
		vertices[2] = new Vector2(width, height);

		PolygonShape temp = new PolygonShape();
		temp.set(vertices);
		if(slopeFixture != null)
			slopeBody.destroyFixture(slopeFixture);
		slopeFixture = slopeBody.createFixture(temp, 10);

		temp.dispose();
	}

	public boolean checkSlopeTouch( float xpos, float ypos ){
		float x = slopeBodyPos.x;
		float y = slopeBodyPos.y;
		if( Math.abs(xpos - x) < 3 && Math.abs(ypos - y ) < 3){
			return true;
		}
		return false;
	}
	
	public boolean moveSlope(float xpos, float ypos){
		if(slopeBody != null){
			/*if(ypos > slopeBodyPos.y 
					&& xpos >= ((slopeBodyPos.x + slopeBodyDim.x) - 2) 
					&& xpos <=((slopeBodyPos.x + slopeBodyDim.x)) +2) 
			{	
				System.out.println("changing slope");
				changeTheSlopeTo(slopeBodyDim.x, ypos - slopeBodyPos.y);
				return true;

			}*/
			float x = slopeBodyPos.x;
			float y = slopeBodyPos.y;
			//if( Math.abs(xpos - x) < 2 && Math.abs(ypos - y ) < 2){
				//System.out.println("changing slope");
				changeTheSlopeTo(slopeBodyDim.x, ypos - slopeBodyPos.y);
				return true;
			//}

		}
		return false;
	}

	public boolean checkPenguinTouch(float xpos, float ypos) {

		if(pBody != null)
		{
			float x = pBody.getPosition().x;
			float y = pBody.getPosition().y;
			//Gdx.app.log("touchPosition", "touchDown at " + xpos + " " + ypos);
			//Gdx.app.log("penguinPosition", "penguin at " + x + " " + y);
			
			//if( Math.abs(xpos - x) < 2 && Math.abs(ypos - y ) < 2){
			ArrayList<Fixture> fixtures = pBody.getFixtureList();
			for( Fixture a : fixtures ){
				if( a.testPoint(xpos, ypos) == true){
					penguinClicked = true;
					return true;
				}	
				//pBody.applyForceToCenter(new Vector2(3000f, 0));
			}
		}
		
		return false;
	}
	
	public void applyForceToPenguin(float xpos, float ypos){
		float x = pBody.getPosition().x;
		//float y = pBody.getPosition().y;
		ArrayList<Fixture> fixtures = pBody.getFixtureList();
		for( Fixture a : fixtures ){
			if( a.testPoint(xpos, ypos) == true && originalPengPos.x - x == 0)
			{
				pBody.applyForceToCenter(new Vector2(1000, 0));
			    return;
			}
		}
		if( originalPengPos.x - x < 1)
			pBody.applyForceToCenter(new Vector2( 2000f, 0));
		else if( originalPengPos.x - x < 4)
			pBody.applyForceToCenter(new Vector2( 3000f,0));
		else if( originalPengPos.x - x <8)
			pBody.applyForceToCenter(new Vector2( 4000f,0));
		else
			pBody.applyForceToCenter(new Vector2( 5000f,0));
		
	}
	public Body addPenguin(){

		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("sidePenguin.json"));

		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(1, 3);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 15f;
		fd.friction = 0f;
		fd.restitution = 0f;
		
		Body body = w.createBody(bd);
		body.setFixedRotation(true);
		body.setUserData("penguin");
		body.setTransform(new Vector2(1, 4), -0.5f);
		
		loader.attachFixture(body, "sidePeng", fd, 2f);
		return body;


	}

	public void addStaticRectangle(float posx, float posy, float width, float height){

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(posx, posy));
		
		//create the static ground body
		Body groundBody = w.createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();

		groundBox.setAsBox(width, height);
		groundBody.createFixture(groundBox, 0.0f);
		groundBox.dispose();

	}

	public void addKinematicRectangle(float posx, float posy, float width, float height, Vector2 velocity){

		BodyDef kinematicBodyDef = new BodyDef();
		kinematicBodyDef.type = BodyType.KinematicBody;
		kinematicBodyDef.position.set(new Vector2(posx, posy));
		Body kinematicBody = w.createBody(kinematicBodyDef);

		PolygonShape kinematicBox = new PolygonShape();
		kinematicBox.setAsBox(width, height);
		kinematicBody.createFixture(kinematicBox, 0.0f);
		kinematicBody.setLinearVelocity(velocity.x, velocity.y);

	}

	public Body addFish(float posx, float posy, float radius){
		
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("epicFish.json"));

		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(posx, .3f);
		Body body = w.createBody(bd);
		body.setUserData("fish");
		
		FixtureDef fd = new FixtureDef();
		fd.density = 21.21f;
		fd.friction = 0.4f;
		fd.restitution = 0f;

		loader.attachFixture(body, "epicFishies", fd, 2f);
		return body;

	}
	public void setSprite(){
		
		//add texture for penguin sprite
		texture = new Texture(Gdx.files.internal("penguin_side.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		//set penguin sprite
		pSprite = new Sprite(texture);
		pSprite.setSize(35, 35);
		//texture.dispose();
		
		//add texture for fish sprite
		texture = new Texture(Gdx.files.internal("epicFish.png"));
		texture.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		
		//set fish sprite
		fSprite = new Sprite(texture);
		fSprite.setSize(50,50);
		//texture.dispose();
		
		//add texture for scoreboard font
		//texture = new Texture(Gdx.files.internal("data/"))
		
	}

	public Sprite getPenguinSprite(){
		return pSprite;
	}
	
	public Sprite getFishSprite(){
		return fSprite;
	}
	public World getWorld(){
		return w;
	}

	public Body getPenguin(){
		return pBody;
	}
	
	public void setPenguinPosition(float xpos/*, float ypos*/){
		pBody.setTransform(new Vector2( xpos, pBody.getPosition().y), pBody.getAngle());
	}
	public Body getFish( int i){
		return fish[i];
	}

	public Vector2[] getSlopeVects(){
		return vertices ;
	}

	public Vector2 getSlopeBodyPos(){
		return slopeBodyPos;
	}

	public void update()
	{
		if(w != null)
		{
			w.step(1/60f, 6, 2);
			for( int i = 0; i < FISH_SCHOOL; ++i ){
				if( fish[i].getUserData() == "true")
					w.destroyBody(fish[i]);
					FISH_COUNT--;
					/*batch.begin();
					font = new BitmapFont(Gdx.files.internal("vemana2000.fnt"),
							Gdx.files.internal("vemana2000.png"), false);
					font.draw(batch, Integer.toString(w.FISH_COUNT), 200f, 200f);
					batch.end();*/
			}
			
		}

	}


}
