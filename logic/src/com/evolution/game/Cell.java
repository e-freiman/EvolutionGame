package com.evolution.game;
import java.util.Iterator;

public final class Cell implements Iterable<Cell> {
	private int row, col;
	private int grass;
	private Animal animal;
	private Field field;
	
	void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
	void growGrass() {
		if (grass < Properties.GRASS_LIMIT)
			grass++;
	}
	
	int harvestGrass() {
		int value = grass;
		grass = 0;
		return value;
	}
	
	Cell(int row, int col, Field field) {
		this.col = col;
		this.row = row;
		this.field = field;
	}
	
	public Animal getAnimal() {
		return this.animal;
	}
	
	public int getGrass() {
		return grass;
	}

	@Override
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {
			private int index = 0;
			
			@Override
			public boolean hasNext() {
				return index < 9;
			}

			@Override
			public Cell next() {

				int i = col - 1 + index / 3;
				int j = row - 1 + index % 3;				
				index++;
				if (!field.isLegal(i, j))
					return null;
				
				return field.getCell(i, j);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();			
			}			
		};
	}
}
