package com.me.mypenguins.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameWorld {
	private World w;
	private Body slopeBody;
	private Body circleBody;
	private Fixture slopeFixture;
	private Vector2  slopeBodyPos;
	private Vector2 slopeBodyDim;//width and height of slope
	public float BOX_TO_WORLD_WIDTH;//pixels per meter
	public float BOX_TO_WORLD_HEIGHT;
	
	
	List<Body> bodies;//keeps track of the world's bodies
		
		public GameWorld(){
		w = new World(new Vector2(0, -50), true);//creates a box2d world with the provided gravity acceleration
        initializeTheSlope(20f, 60f, 10f, 15f);
		circleBody = addDynamicCircle(6f, 10f, 6f);// x position, y position, radius
		addStaticRectangle(0.0f, 0.0f, 100f, 10f);// x position, y position, width, height
		//addKinematicRectangle(0.0f, 45f, 30, 5, new Vector2(10f, 0.0f));//x pos, y pos, width, height, velocity

		//addDynamicCircle(20f, 30f, 3f);
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
		if(xpos >= ((slopeBodyPos.x + slopeBodyDim.x) - 2) && xpos <=((slopeBodyPos.x + slopeBodyDim.x)) +2) {
			changeTheSlopeTo(slopeBodyDim.x, ypos - slopeBodyPos.y);
			System.out.println("Should be changing.");
		}
		
	}
	public void checkBall(float xpos, float ypos) {
		if((xpos>= 4 && xpos <= 8) && (ypos >= 8 && ypos <= 12)){
			circleBody.applyForceToCenter(new Vector2(500000000, 0));//world needs to be smaller so we don't have to apply such large forces
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
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.0f;
		
		Fixture fixture = body.createFixture(fixtureDef);
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

	public void setScale(float width, float height){
		BOX_TO_WORLD_WIDTH = width / 100;
		BOX_TO_WORLD_HEIGHT = height / 100;
	}
	public World getWorld(){
		return w;
	}
    public void update()
    {
    	if(w != null)
    	{
    		w.step(1/60f, 6, 2);
    	}

    }


}
