package com.me.mypenguins.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;

public class GameWorld {
	private World w;
	private Body slopeBody;
	private Body circleBody;
	private Fixture circleFixture;
	private Fixture slopeFixture;
	private Vector2  slopeBodyPos;
	private Vector2 slopeBodyDim;//width and height of slope
	public float BOX_TO_WORLD_WIDTH;//pixels per meter
	public float BOX_TO_WORLD_HEIGHT;
	public boolean penguinClicked = false;
	
	List<Body> bodies;//keeps track of the world's bodies
		
		public GameWorld(){
		w = new World(new Vector2(0, -.50f), true);//creates a box2d world with the provided gravity acceleration
        initializeTheSlope(.20f, .40f, .10f, .15f);
		circleBody = addDynamicCircle(.06f, .10f, .04f);// x position, y position, radius
		addStaticRectangle(0f, 0.0f, 100f, .10f);// x position, y position, half width,  half height
		
		
		addStaticRectangle(3f, .2f, .01f, .10f);
		addStaticRectangle(3.24f, .2f, .01f, .10f);
		
		//addKinematicRectangle(0.0f, 45f, 30, 5, new Vector2(10f, 0.0f));//x pos, y pos, width, height, velocity

	}
		
		public GameWorld(Vector2 v2, boolean bool){
			w = new World(v2, bool);//creates a box2d world with the provided gravity acceleration
			initializeTheSlope(.20f, .40f, .10f, .15f);
			circleBody = addDynamicCircle(.06f, .10f, .04f);// x position, y position, radius
			addStaticRectangle(0f, 0.0f, 100f, .10f);// x position, y position, half width,  half height


			addStaticRectangle(3f, .2f, .01f, .10f);
			addStaticRectangle(3.24f, .2f, .01f, .10f);

			//addKinematicRectangle(0.0f, 45f, 30, 5, new Vector2(10f, 0.0f));//x pos, y pos, width, height, velocity

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

		Vector2 vertices[] = new Vector2[3];
		vertices[0] = new Vector2(0, 0);
		vertices[1] = new Vector2(width, 0);
		vertices[2] = new Vector2(width, height);
		PolygonShape slope = new PolygonShape();
		slope.set(vertices);
		if(slopeFixture != null)
			slopeBody.destroyFixture(slopeFixture);
		slopeFixture = slopeBody.createFixture(slope, 0);
		slope.dispose();
	}
	public void checkSlope(float xpos, float ypos){
		if(ypos > slopeBodyPos.y 
		   && xpos >= ((slopeBodyPos.x + slopeBodyDim.x) - 2) 
		   && xpos <=((slopeBodyPos.x + slopeBodyDim.x)) +2) 
		{	
	       changeTheSlopeTo(slopeBodyDim.x, ypos - slopeBodyPos.y);

		}
		
	}
	public void checkBall(float xpos, float ypos) {
		float radius = 0;
		ArrayList<Fixture> fixtures = circleBody.getFixtureList();
		for(Fixture a : fixtures){
			if(a.getShape().getType() == Type.Circle);
			{
				radius = a.getShape().getRadius();
			}
		}
		 Vector2 b = circleBody.getLocalPoint(new Vector2(xpos, ypos));
		if(radius > 0){
			if(b.x <= radius && b.y <= radius){
				penguinClicked = true;
				circleBody.applyForceToCenter(new Vector2(3.5f, 0));
			}
		}
		
	}
	public Body addDynamicCircle(float posx, float posy, float radius){
		
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.position.set(posx, posy);//50, 60
		//tell world to create a body
		Body body = w.createBody(bodydef);
		
		//create the shape that we will assign to our fixture
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);//6
		
		//set up fixture definition 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 6f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0f;
		
		circleFixture = body.createFixture(fixtureDef);
		circle.dispose();
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


	public World getWorld(){
		return w;
	}
	
	public Body getPenguin(){
		return circleBody;
	}
    public void update()
    {
    	if(w != null)
    	{
    		w.step(1/60f, 6, 2);
    	}

    }


}
