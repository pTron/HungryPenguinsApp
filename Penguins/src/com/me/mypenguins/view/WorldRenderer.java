package com.me.mypenguins.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.me.mypenguins.model.GameWorld;

public class WorldRenderer {
	private GameWorld w;
	private boolean debug;
	private Box2DDebugRenderer debugRenderer;
    public final float CAMERA_VIEW_WIDTH = 1f;
    public final float CAMERA_VIEW_HEIGHT = 1f;
	private OrthographicCamera cam;

	public WorldRenderer(GameWorld w, boolean debug){
		this.w = w;
		this.debug = debug;
		debugRenderer = new Box2DDebugRenderer();
		cam = new OrthographicCamera(CAMERA_VIEW_WIDTH, CAMERA_VIEW_HEIGHT);
		cam.position.set(CAMERA_VIEW_WIDTH / 2, CAMERA_VIEW_HEIGHT / 2, 0);
		cam.update();
		
	}
	
	public void render(){
		if(debug && w != null){
			cam.position.set(w.getPenguin().getPosition().x, .5f ,0);
			cam.update();
			//debugRenderer.render(w.getWorld(), cam.combined);
		}
	}
	
	public OrthographicCamera getCam(){
		return cam;
	}
}
