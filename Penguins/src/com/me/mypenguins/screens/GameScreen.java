package com.me.mypenguins.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.me.mypenguins.model.GameWorld;
import com.me.mypenguins.view.WorldRenderer;

public class GameScreen implements Screen, InputProcessor{
	
	private GameWorld w;
	private WorldRenderer renderer;
    
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
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render();//and render
		w.update();
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
		w = new GameWorld();
		renderer = new WorldRenderer(w, true);
		Gdx.input.setInputProcessor(this);
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
		boolean firstFingerTouching = Gdx.input.isTouched(0);
		
		float xpos = x / w.BOX_TO_WORLD_WIDTH;
		float ypos = (height - y) / w.BOX_TO_WORLD_HEIGHT;
		
		xpos += (renderer.getCam().position.x - (renderer.getCam().viewportWidth / 2));
		ypos += (renderer.getCam().position.y - (renderer.getCam().viewportHeight / 2));
		
		w.checkBall(xpos, ypos);
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
		
		//w.checkBall(xpos, ypos);
		w.checkSlope(xpos, ypos);//checks to see if the pointer is trying to drag the slope
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
