package com.evolution.game;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public abstract class World extends Thread {
	
	private final Field field = new Field();	
	private final Random rand = new Random(System.currentTimeMillis());
	
	public final ReentrantLock lock = new ReentrantLock();
	
	public Field getField() {
		return field;
	}
	
	abstract Animal makeAnimal(int row, int col, Field field);
	
	World() {
		for (int i = 0; i < Properties.NUMBER_OF_ANIMALS; i++) {
			putAnimal();
		}
	}
	
	void putAnimal() {
		
		int row, col;		
		Cell cell;
		
		do {
			row = rand.nextInt(Properties.FIELD_HEIGHT);
			col = rand.nextInt(Properties.FIELD_WIDTH);
			cell = field.getCell(row, col);			
		} while (cell.getAnimal() != null);
		
		cell.setAnimal(makeAnimal(row, col, field));
	}
	
	@Override
	public void run() {
		super.run();
		
		while(!Thread.interrupted()) {
			try {
				sleep(Properties.UPDATE_INTERVAL);
				
				lock.lock();
				runStep();
				lock.unlock();
				
			} catch (InterruptedException e) {
			}
		}
	}
	
	void runStep() {
		for (int i = 0; i < field.getHeight(); i++) {
			for (int j = 0; j < field.getWidth(); j++)	{
				Cell cell = field.getCell(i, j);
				cell.growGrass();
				Animal animal = cell.getAnimal();
				if (animal != null) {
					
					if (animal.loseWeight()) {
						animal.eat();
						
						if (animal.tryReproduce()) {
							putAnimal();
						}
						
						Byte[] intent = animal.getIntent();
						animal.move(intent[0], intent[1]);
					}
				}
			}
		}
	}	
}
