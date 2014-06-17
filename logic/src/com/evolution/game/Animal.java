package com.evolution.game;


public abstract class Animal {
	private int row, col;
	private int color;
	private int weight;
	private Field field;
		
	boolean loseWeight() {
		weight -= Properties.WEIGHT_LOSING;
		
		if ( weight <= 0 ) {
			field.getCell(row, col).setAnimal(null);
			return false;
		}
		
		return true;
	}

	void eat() {
		weight += field.getCell(row, col).harvestGrass();
	}

	
	boolean tryReproduce() {
		if (weight >= 2 * Properties.START_WEIGHT)
		{
			weight -= Properties.START_WEIGHT;
			return true;
		}
		return false;
	}
	
	boolean move(int dRow, int dCol) {
		
		if (dRow < 0) dRow = -1;
		if (dRow > 0) dRow = 1;
		if (dCol < 0) dCol = -1;
		if (dCol > 0) dCol = 1;
		
		int i = row + dRow,
			j = col + dCol;
		
		if (field.isLegal(i, j)) {		
			Cell desiredCell = field.getCell(i, j);			
			
			if (desiredCell.getAnimal() == null || 
			   (desiredCell.getAnimal().color != color && desiredCell.getAnimal().getPower() < getPower()))
			{
				desiredCell.setAnimal(this);
				
				Cell originalCell = field.getCell(row, col);
				originalCell.setAnimal(null);
				row = i;
				col = j;				
				
				return true;
			}
		}
		
		return false;		
	}
	
	Animal(Animal[] parents, int row, int col, Field field) {
		this.col = col;
		this.row = row;
		this.weight = Properties.START_WEIGHT;
		this.field = field;
	}
	
	int getPower() {
		int counter = 0;
		for (Cell cell : field.getCell(row, col)) {			
			if (cell.getAnimal() != null && cell.getAnimal().color == color)
				counter++;
		}
		return counter;
	}
	
	int getWeight() {
		return weight;
	}
	
	//!	This method should return an array of 2 integers from -1 to 1 which are for dRow and dCol
	abstract Byte[] getIntent();
}
