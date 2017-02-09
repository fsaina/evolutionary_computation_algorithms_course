package hr.fer.zemris.optjava.dz9.algorithm.mutation;

import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

import java.util.Random;

public class Gauss implements IMutation<MultipleObjectiveSolution> {
	
	private double sigma;
	private double probability;
	private Random rand;
	
	public Gauss(double sigma, double probability) {
		this.sigma = sigma;
		this.probability = probability;
		this.rand = new Random();
	}

	@Override
	public void mutate(MultipleObjectiveSolution chromosome) {
		for(int j = 0; j < chromosome.values.length; j++) {
			if(rand.nextDouble() <= probability) {
				chromosome.values[j] += rand.nextGaussian() * sigma;
			}
		}
	}

}
