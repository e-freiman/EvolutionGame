package com.evolution.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EvolutionGame extends ApplicationAdapter {
	SpriteBatch batch;
	
	Texture[] imgGrass = new Texture[Properties.GRASS_LIMIT + 1];
	Texture imgAnimal;
	
	MonoWorld world;	
	float cellSize;
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float cellSizeX = width / world.getField().getWidth(),
			  cellSizeY = height / world.getField().getHeight();
		
		cellSize = cellSizeX < cellSizeY ? cellSizeX : cellSizeY;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgAnimal = new Texture("animal.jpg");
		
		for (int i = 0; i <= Properties.GRASS_LIMIT; i++) {
			String name = "grass" + i + ".jpg";
			imgGrass[i] = new Texture(name);
		}
		
		world = new MonoWorld();
		world.start();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.lock.lock();
		
		batch.begin();
		
		for (int row = 0; row < world.getField().getHeight(); row++) {
			for (int col = 0; col < world.getField().getWidth(); col++) {
				Cell cell = world.getField().getCell(row, col);
				
				Texture tex = null;
				
				if (cell.getAnimal() != null) {
					tex = imgAnimal;
				} else {
					tex = imgGrass[cell.getGrass()];
				}
				
				batch.draw(tex, cellSize * col, cellSize * row, cellSize, cellSize);
			}
		}
		
		batch.end();
		
		world.lock.unlock();
	}
}
