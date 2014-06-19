package com.evolution.game;

class GrazerTeacher implements Teacher {

	@Override
	public Lesson getLesson() {
		return null;
	}
	
}

public class SmartGrazer extends Animal {
	private static final int SIZE_OF_INPUT = 9;
	
	private MultilayerPerceptron brain;

	SmartGrazer(Animal[] parents, int row, int col, Field field) {
		super(parents, row, col, field);
		
		if (parents != null) {		
			MultilayerPerceptron[] parentsBrain = new MultilayerPerceptron[parents.length];
			for (int i = 0; i < parentsBrain.length; i++)
				parentsBrain[i] = ((SmartGrazer)parents[i]).brain;
			
			//brain = new MultilayerPerceptron(parentsBrain, Properties.VARIABILITY_RATIO);
		} else {
			//brain = new MultilayerPerceptron(Properties.BRAIN_LAYERS, Properties.BRAIN_NEURONS_PER_LAYER);
		}
	}

	@Override
	Byte[] getIntent() {
		
		/*double[] input = new double[SIZE_OF_INPUT];
		
		int counter = 0;		
		
		for (Cell cell : getCell()) {
			if (cell != null) {
				if (cell.getAnimal() != null)
					//animals
					input[counter] = -1;
				else
					//grass
					input[counter] = (double)cell.getGrass();				
			} else {
				//boundaries
				input[counter] = -2;
			}
		}
		
		//self weight
		input[4] = getWeight();
		
		//double[] doubleIntent = brain.doThink(input);
		
		Byte[] byteIntent = new Byte[2];
		
		for(int i = 0; i < 2; i++) {
			if (doubleIntent[i] < -Properties.THRESHOLD) byteIntent[i] = -1;
				else if (doubleIntent[i] > Properties.THRESHOLD) byteIntent[i] = 1;
					else byteIntent[i] = 0;
		}
		
		return byteIntent;*/
		
		return null;
	}	
}
