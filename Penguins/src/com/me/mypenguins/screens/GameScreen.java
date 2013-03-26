package com.me.mypenguins.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
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
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);//clear screen
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
		w.setScale(width, height);
		
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
		//boolean firstFingerTouching = Gdx.input.isTouched(0);
        //w.addDynamicCircle(x / w.BOX_TO_WORLD_WIDTH, (height - y) / w.BOX_TO_WORLD_HEIGHT, 5f);
		w.checkSlope(x / w.BOX_TO_WORLD_WIDTH, (height - y) / w.BOX_TO_WORLD_HEIGHT);
		w.checkBall(x / w.BOX_TO_WORLD_WIDTH, (height - y) / w.BOX_TO_WORLD_HEIGHT);//hacky function to apply force to the ball, should be removed eventually
		System.out.println("X: " + (x / w.BOX_TO_WORLD_WIDTH) +" Y: " + (height - y) / w.BOX_TO_WORLD_HEIGHT);
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override

	public boolean touchDragged(int x, int y, int pointer) {
		w.checkSlope(x / w.BOX_TO_WORLD_WIDTH, (height - y) / w.BOX_TO_WORLD_HEIGHT);//checks to see if the pointer is trying to drag the slope
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
