package com.me.mypenguins;


import com.badlogic.gdx.Game;
import com.me.mypenguins.screens.GameScreen;

public class Penguins extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
		
	}

}