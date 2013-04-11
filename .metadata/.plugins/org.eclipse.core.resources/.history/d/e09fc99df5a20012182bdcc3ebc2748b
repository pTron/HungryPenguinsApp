package com.me.mypenguins.model;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import aurelienribon.bodyeditor.BodyEditorLoader;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameWorld {
	
	public int FISH_SCHOOL = 10;
	public float BOX_TO_WORLD_WIDTH;//pixels per meter
	public float BOX_TO_WORLD_HEIGHT;
	private static float slopYMul = 0.5285714f;
	private static float slopXMul = 0.35f;
	private static float waterYMul = 0.35f;
	
	private World w;
	private Body slopeBody,
				circleBody,
				pBody;
	private Body [] fish = new Body[FISH_SCHOOL];
	
	private Fixture slopeFixture;
					//circleFixture;
	
	private Vector2  slopeBodyPos;
	private Vector2 slopeBodyDim;//width and height of slope
	
	public Sprite rampSprite,
				  pSprite,
				  fSprite;
	
	public boolean penguinClicked = false;
	private Vector2 vertices[];
	private Texture texture;

	List<Body> bodies;//keeps track of the world's bodies

	public GameWorld(){
		w = new World(new Vector2(0, -.50f), true);
		//w = new World(new Vector2(0, -5f), true);
		initializeTheSlope(4.20f, 4.40f, 3.70f, 3.75f);
		//createRamp(100, 200);
		circleBody = addDynamicCircle(.06f, .10f, .04f);
		setSprite();
		createCollisionListener();
		
		for(int i = 0; i < FISH_SCHOOL; i++)
			fish[i] = addFish((10+i), 1.5f, .2f);
	
	}

	public GameWorld(Vector2 v2, boolean bool){
		w = new World(v2, bool);
		
		initializeTheSlope(4.2f, 4.4f, 3.7f, 3.75f);
		createCollisionListener();
		
		circleBody = addDynamicCircle(1f, 5f, .4f);
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
                Gdx.app.log("beginContact", "between " + fixtureA.getBody().getUserData() + " and " + fixtureB.getBody().getUserData());
                if( fixtureA.getBody().getUserData() == "fish" && !w.isLocked() )
                	w.destroyBody(fish[2]);
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
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

		vertices[1] = new Vector2(width * 11, 0);
		vertices[2] = new Vector2(width * 11, height);

		PolygonShape temp = new PolygonShape();
		temp.set(vertices);
		if(slopeFixture != null)
			slopeBody.destroyFixture(slopeFixture);
		slopeFixture = slopeBody.createFixture(temp, 10);

		temp.dispose();
	}

	public boolean checkSlope(float xpos, float ypos){
		if(slopeBody != null){
			if(ypos > slopeBodyPos.y 
					&& xpos >= ((slopeBodyPos.x + slopeBodyDim.x) - 2) 
					&& xpos <=((slopeBodyPos.x + slopeBodyDim.x)) +2) 
			{	
				changeTheSlopeTo(slopeBodyDim.x, ypos - slopeBodyPos.y);
				return true;

			}

		}
		return false;
	}

	public void checkPenguin(float xpos, float ypos) {


		if(circleBody != null)
		{
			float radius = 0;
			ArrayList<Fixture> fixtures = circleBody.getFixtureList();
			for(Fixture a : fixtures){
				if(a.getShape().getType() == Type.Circle);
				{
					radius = a.getShape().getRadius();
				}
			}

			Vector2 b = circleBody.getLocalPoint(new Vector2(xpos, ypos));
			//if(radius > 0){
				//if(b.x <= radius && b.y <= radius){
					penguinClicked = true;
					circleBody.applyForceToCenter(new Vector2(3000f, 0));
				//}
			//}
		}
		/*Vector2 b = circleBody.getLocalPoint(new Vector2(xpos, ypos));

			if( circleBody.getPosition().x == xpos && circleBody.getPosition().y == ypos )
				Gdx.app.log("GestureDetectorTest2", "tap2 at " + b.x + ", " + b.y);
			    System.out.println("true");
				penguinClicked = true;
			    circleBody.applyForceToCenter(new Vector2(5000f, 0));
		    }*/
	}
	public Body addDynamicCircle(float posx, float posy, float radius){

		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("sidePenguin.json"));

		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(1, 3);
		
		FixtureDef fd = new FixtureDef();
		fd.density = 15f;
		fd.friction = 0f;
		fd.restitution = 0.5f;
		
		Body body = w.createBody(bd);
		body.setFixedRotation(true);
		body.setUserData("penguin");
		body.setTransform(new Vector2(1, 4), -0.5f);
		
		loader.attachFixture(body, "sidePeng", fd, 2f);
		return body;


	}
	/*
	public void createRamp( float xpos, float ypos ){
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("ramp.json"));
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(xpos, ypos);
		Body body = w.createBody(bd);
		
		FixtureDef fd = new FixtureDef();
		fd.friction = 15f;
		loader.attachFixture(body, "ramp", fd, 2f);
	}
	*/
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

		/*BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.position.set(posx, posy);
		//tell world to create a body
		Body body = w.createBody(bodydef);
		body.setFixedRotation(true);

		//create the shape that we will assign to our fixture
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);

		//set up fixture definition 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 39.8f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0f;

		//circleFixture = body.createFixture(fixtureDef);
		circle.dispose();
		return body;*/
		
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("epicFish.json"));

		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(posx, 1.5f);
		Body body = w.createBody(bd);
		body.setUserData("fish");
		
		FixtureDef fd = new FixtureDef();
		fd.density = 18f;
		fd.friction = 0.4f;
		fd.restitution = 0f;

		loader.attachFixture(body, "epicFishies", fd, 2f);
		return body;

	}
	public void setSprite(){
		texture = new Texture(Gdx.files.internal("penguin_side.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		pSprite = new Sprite(texture);
		pSprite.setSize(35, 35);
		
		texture = new Texture(Gdx.files.internal("epicFish.png"));
		texture.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		
		fSprite = new Sprite(texture);
		fSprite.setSize(50,50);
		
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
		return circleBody;
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
		}

	}


}
