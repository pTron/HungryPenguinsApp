package com.me.mypenguins.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.me.mypenguins.model.GameWorld;


public class WorldRenderer {
	private GameWorld w;
	private boolean debug;
	private Box2DDebugRenderer debugRenderer;
    private float TILE_SCALE_FACTOR = 60f;
	private OrthographicCamera cam;

	public WorldRenderer(GameWorld w, boolean debug){
		this.w = w;
		this.debug = debug;
		debugRenderer = new Box2DDebugRenderer();
		float camViewWidth = (float)Gdx.graphics.getWidth() / TILE_SCALE_FACTOR;
		float camViewHeight = (float)Gdx.graphics.getHeight() / TILE_SCALE_FACTOR;
		cam = new OrthographicCamera(camViewWidth, camViewHeight);
		cam.position.set(camViewWidth / 2, camViewHeight / 2, 0);
		cam.update();
		
	}
	
	public void render(){
		if(debug && w != null){
			debugRenderer.render(w.getWorld(), cam.combined);
		}
		else
			System.out.println("world or renderer does not exist");
	}
	
	public OrthographicCamera getCam(){
		return cam;
	}

	public void resizeCam(int width, int height) {
		cam.viewportWidth = width / TILE_SCALE_FACTOR;
		cam.viewportHeight = height / TILE_SCALE_FACTOR;
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
		
	}
}