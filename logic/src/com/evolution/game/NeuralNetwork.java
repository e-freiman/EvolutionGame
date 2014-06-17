package com.evolution.game;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {	
	
	private static final int SIZE_OF_RESULT = 2;
	private double[][] coeffs;
	private Random rand = new Random(System.nanoTime());
	
	NeuralNetwork(int layers, int neuronsPerLayer) {
		coeffs = new double[layers][neuronsPerLayer];
		
		for (int i = 0; i < layers; i++) {
			for (int j = 0; j < neuronsPerLayer; j++) {
				coeffs[i][j] = 2.0 * rand.nextDouble() - 1.0;
			}
		}
	}

	//Randomness which is is from 0 to 1 means the ratio on which each coefficient can be changed randomly
	NeuralNetwork(NeuralNetwork[] parents, double randomness) {
		int layers = parents[0].coeffs.length,
			neuronsPerLayer = parents[0].coeffs[0].length;
		
		coeffs = new double[layers][neuronsPerLayer];
		
		for (int i = 0; i < layers; i++) {
			for (int j = 0; j < neuronsPerLayer; j++) {
				for (int k = 0; k < parents.length; k++) {
					//Average value of coefficient
					coeffs[i][j] += parents[k].coeffs[i][j] / parents.length;
					//Changing value of average coefficient according to randomness parameter 
					coeffs[i][j] = coeffs[i][j] * (1.0 - randomness) + 
								   coeffs[i][j] * randomness * (2.0 * rand.nextDouble() - 1.0);
				}
			}
		}
	}
	
	double[] doThink(double[] input) {
		
		//Two buffers, topBuffer is for input values for current layer,
		//botBuffer for output values after signals went through current layer
		double[] topBuffer = new double[coeffs[0].length],
				 botBuffer = new double[coeffs[0].length];
		
		//Mapping input as input values for first layer
		for (int j = 0; j < input.length; j++) {
			topBuffer[j % topBuffer.length] += input[j]; 
		}
		
		//Passing through inner layers except for the last
		for (int i = 0; i < coeffs.length - 1; i++) {

			Arrays.fill(botBuffer, 0.0);

			//each input value
			for (int j1 = 0; j1 < topBuffer.length; j1++)
				//is broadcasting to all output values with a coefficient
				for (int j2 = 0; j2 < topBuffer.length; j2++)
					botBuffer[j2] += topBuffer[j1] * coeffs[i][j1]; 
		
			//swapping buffers
			double[] temp = topBuffer; 
			
			topBuffer = botBuffer;
			botBuffer = temp;
		}
		
		double[] resBuffer = new double[SIZE_OF_RESULT];
				
		//The last buffer has a different size, so it should be processed separately
		//each input value in the last layer
		for (int j1 = 0; j1 < topBuffer.length; j1++) {
			//is broadcasting to output array
			for (int j2 = 0; j2 < resBuffer.length; j2++) {
				resBuffer[j2] += topBuffer[j1] * coeffs[coeffs.length - 1][j1]; 
			}
		}
		
		return resBuffer;		
	}
	
	

	
}
