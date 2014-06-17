package com.evolution.game;

import java.util.Random;

public abstract class World extends Thread {
	
	private final Field field = new Field();	
	private final Random rand = new Random(System.currentTimeMillis());
	
	public Field getField() {
		return field;
	}
	
	abstract Animal makeAnimal(Animal[] parents, int row, int col, Field field);
	
	World() {
		for (int i = 0; i < Properties.NUMBER_OF_ANIMALS; i++) {
			putAnimal(null);
		}
	}
	
	void putAnimal(Animal[] parents) {
		
		int row, col;		
		Cell cell;
		
		do {
			row = rand.nextInt(Properties.FIELD_HEIGHT);
			col = rand.nextInt(Properties.FIELD_WIDTH);
			cell = field.getCell(row, col);			
		} while (cell.getAnimal() != null);
		
		cell.setAnimal(makeAnimal(parents, row, col, field));
	}
		
	public void run() {
		for (int i = 0; i < field.getHeight(); i++) {
			for (int j = 0; j < field.getWidth(); j++)	{
				Cell cell = field.getCell(i, j);
				cell.growGrass();
				Animal animal = cell.getAnimal();
				if (animal != null) {
					
					if (animal.loseWeight()) {
						animal.eat();
						
						if (animal.tryReproduce()) {
							putAnimal(new Animal[]{animal});
						}
						
						Byte[] intent = animal.getIntent();
						animal.move(intent[0], intent[1]);
					}
				}
			}
		}
	}	
}
