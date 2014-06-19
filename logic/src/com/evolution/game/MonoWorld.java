package com.evolution.game;

public class MonoWorld extends World {

	@Override
	Animal makeAnimal(Animal[] parents, int row, int col, Field field) {
		//return new RandomGrazer(parents, row, col, field);
		return new SmartGrazer(parents, row, col, field);
	}
}
