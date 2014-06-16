package com.evolution.game;

import java.util.Random;

public class Grazer extends Animal {
	
	private Random brain;

	Grazer(int row, int col, Field field) {
		super(row, col, field);		
		brain = new Random(System.nanoTime());
	}

	@Override
	Byte[] getIntent() {		
		return new Byte[]{
				(byte) (brain.nextInt(3) - 1), 
				(byte) (brain.nextInt(3) - 1)
			};
	}
}
