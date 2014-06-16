package com.evolution.game;


public final class Field {
	
	private Cell[][] cells;
		
	Field() {
		cells = new Cell[Properties.FIELD_HEIGHT][Properties.FIELD_WIDTH];
		
		for (int i = 0; i < Properties.FIELD_HEIGHT; i++)
			for (int j = 0; j < Properties.FIELD_WIDTH; j++)
				cells[i][j] = new Cell(i, j, this);
	}
			
	boolean isLegal(int row, int col) {
		return row >= 0 && col >= 0 && row < getHeight() && col < getWidth();
	}
	
	public Cell getCell(int row, int col) {
		return cells[row][col];
	}
	
	public int getWidth() {
		return cells.length;
	}
	
	public int getHeight() {
		return cells[0].length;
	}	
	
	public int numberOfAnimals() {
		int num = 0;
		for (int i = 0; i < getHeight(); i++)
			for (int j = 0; j < getWidth(); j++)
				if (cells[i][j].getAnimal() != null)
					num++;
		
		return num;
	}
}
