package com.evolution.game;

import java.util.Random;

class ValueObject
{
    double value;
}

class Synapse
{
    static Random rand = new Random(System.nanoTime());

    double weight;
    ValueObject previous;

    Synapse()
    {
        weight = 2.0 * rand.nextDouble() - 1.0;
    }
}

class Neuron extends ValueObject
{
    static final double A = 1;
    
    Synapse[] synapses;
    double delta;
           
    void calculateValue()
    {
        value = 0;

        for (Synapse s : synapses)
        {
            value += s.previous.value * s.weight;
        }

        value = F(value);
    }

    double F(double Y)
    {
        return 1.0 / (1.0 + Math.exp(-A * Y));
    }
}

class Layer
{
    Neuron[] neurons;
}

public class MultilayerPerceptron
{
	static final double ETA = 0.7;
	
    Layer[] layers;
    ValueObject[] sensors;

    void run()
    {
        for (int i = 0; i < layers.length; i++)
        {
            for (int j = 0; j < layers[i].neurons.length; j++)
            {
                layers[i].neurons[j].calculateValue();
            }
        }
    }

    double[] run(double[] input)
    {
        for (int i = 0; i < input.length; i++)
        {
            sensors[i].value = input[i];
        }

        run();

        double[] output = new double[layers[layers.length - 1].neurons.length];

        for (int i = 0; i < output.length; i++)
        {
            output[i] = layers[layers.length - 1].neurons[i].value;
        }

        return output;
    }


    MultilayerPerceptron(int inputSize, int outputSize, int hiddenLayers, int neuronsPerHiddenLayers)
    {
        layers = new Layer[hiddenLayers + 1];
        
        sensors = new ValueObject[inputSize];
        for (int i = 0; i < sensors.length; i++)
        {
            sensors[i] = new ValueObject();
        }

        for (int i = 0; i < layers.length; i++)
        {
            layers[i] = new Layer();

            if (i < layers.length - 1)
                layers[i].neurons = new Neuron[neuronsPerHiddenLayers];
            else
                layers[i].neurons = new Neuron[outputSize];

            for (int j = 0; j < layers[i].neurons.length; j++)
            {
                layers[i].neurons[j] = new Neuron();

                if (i == 0)
                {
                    layers[i].neurons[j].synapses = new Synapse[inputSize];

                    for (int k = 0; k < layers[i].neurons[j].synapses.length; k++)
                    {
                        layers[i].neurons[j].synapses[k] = new Synapse();
                        layers[i].neurons[j].synapses[k].previous = sensors[k];
                    }
                }
                else
                {
                    layers[i].neurons[j].synapses = new Synapse[neuronsPerHiddenLayers];

                    for (int k = 0; k < layers[i].neurons[j].synapses.length; k++)
                    {
                        layers[i].neurons[j].synapses[k] = new Synapse();
                        layers[i].neurons[j].synapses[k].previous = layers[i - 1].neurons[k];
                    }
                }
            }
        }            
    }
    
    public void teach(double[] input, double[] output)
    {
    	assert(layers.length > 1);

        for (int i = 0; i < input.length; i++)
        {
            sensors[i].value = input[i];
        }

        run();

        for (int q = 0; q < layers[layers.length - 1].neurons.length; q++)
        {
            Neuron nq = layers[layers.length - 1].neurons[q];
            nq.delta = nq.value * (1.0 - nq.value) * (output[q] - nq.value);

            for (int p = 0; p < nq.synapses.length; p++)
            {
                ValueObject np = nq.synapses[p].previous;

                nq.synapses[p].weight += ETA * nq.delta * np.value;
            }
        }

        for (int w = layers.length - 1; w > 0; w--)
        {
            for(int q = 0; q < layers[w - 1].neurons.length; q++)
            {
                layers[w - 1].neurons[q].delta = 0;
            }
            
            for (int k = 0; k < layers[w].neurons.length; k++)
            {
                Neuron nk = layers[w].neurons[k];

                for (int q = 0; q < nk.synapses.length; q++)
                {
                    Neuron nq = (Neuron)nk.synapses[q].previous;
                    nq.delta += nq.value * (1 - nq.value) * nk.delta * nk.synapses[q].weight;
                }
            }

            for (int q = 0; q < layers[w - 1].neurons.length; q++)
            {
                Neuron nq = layers[w - 1].neurons[q];

                for (int p = 0; p < nq.synapses.length; p++)
                {
                    ValueObject np = nq.synapses[p].previous;

                    nq.synapses[p].weight += ETA * nq.delta * np.value;
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        MultilayerPerceptron mp = new MultilayerPerceptron(2, 1, 1, 3);

        double[] input = new double[2];
        double[] output = new double[1];

        Random rand = new Random(System.nanoTime());

        for (int i = 0; i < 1000000; i++ )
        {
            boolean a = rand.nextInt(2) == 0,
            		b = rand.nextInt(2) == 1,
            		c = a ^ b; 

            if (a) input[0] = 1; else input[0] = 0;
            if (b) input[1] = 1; else input[1] = 0;
            if (c) output[0] = 1; else output[0] = 0;

            mp.teach(input, output);
        }

        for (int i = 0; i < 2; i++) {
        	for (int j = 0; j < 2; j++) {
		        input[0] = i;
		        input[1] = j;
		        double result = mp.run(input)[0];
		        System.out.println(result);
        	}
        }
    }
}