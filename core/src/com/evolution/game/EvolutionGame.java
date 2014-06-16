package com.evolution.game;

import java.util.concurrent.locks.ReentrantLock;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EvolutionGame extends ApplicationAdapter {
	private int windowHeight;
	
	SpriteBatch batch;
	
	Texture[] imgGrass = new Texture[Properties.GRASS_LIMIT + 1];
	Texture imgAnimal;
	BitmapFont bitmapFont;
	
	WorldRunner worldRunner;
	MonoWorld world;
	float cellSize;
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		worldRunner.lock.lock();
		float cellSizeX = width / world.getField().getWidth(),
			  cellSizeY = height / world.getField().getHeight();
		worldRunner.lock.unlock();
		
		cellSize = cellSizeX < cellSizeY ? cellSizeX : cellSizeY;
		
		windowHeight = height;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgAnimal = new Texture("animal.jpg");
		bitmapFont = new BitmapFont();
		
		for (int i = 0; i <= Properties.GRASS_LIMIT; i++) {
			String name = "grass" + i + ".jpg";
			imgGrass[i] = new Texture(name);
		}
		
		world = new MonoWorld();
		worldRunner = new WorldRunner(world);
		Thread t = new Thread(worldRunner);		
		t.start();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		worldRunner.lock.lock();
		
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
		
		batch.begin(); 
		bitmapFont.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		bitmapFont.draw(batch, "population  " + world.getField().numberOfAnimals(), 0, windowHeight); 
		batch.end();
		
		worldRunner.lock.unlock();
	}
}

class WorldRunner implements Runnable {	
	static final int UPDATE_INTERVAL = 500;
	final ReentrantLock lock = new ReentrantLock();
	
	private World world;
		
	WorldRunner(World world) {
		this.world = world;
	}
	
	public void run() {			
		while(!Thread.interrupted()) {
			try {
				Thread.sleep(UPDATE_INTERVAL);				
				lock.lock();
				world.run();
				lock.unlock();				
			} catch (InterruptedException e) {
			}
		}
	}
}
