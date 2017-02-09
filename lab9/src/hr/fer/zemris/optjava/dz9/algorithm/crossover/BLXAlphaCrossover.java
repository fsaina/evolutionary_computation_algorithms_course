package hr.fer.zemris.optjava.dz9.algorithm.crossover;

import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

import java.util.Random;

public class BLXAlphaCrossover implements ICrossover<MultipleObjectiveSolution> {
	
	private double alpha;
	private Random rand;
	
	public BLXAlphaCrossover(double alpha) {
		this.alpha = alpha;
		this.rand = new Random();
	}

	@Override
	public MultipleObjectiveSolution cross(MultipleObjectiveSolution firstParent, 
			MultipleObjectiveSolution secondParent) {
		double[] child = new double[firstParent.dimension];
		
		for(int j = 0; j < child.length; j++) {
			double cMin = Math.min(firstParent.values[j], secondParent.values[j]);
			double cMax = Math.max(firstParent.values[j], secondParent.values[j]);
			double I = alpha * (cMax - cMin);
			
			cMin -= I;
			cMax += I;
			
			child[j] = cMin + rand.nextDouble() * (cMax - cMin);
		}
		
		return new MultipleObjectiveSolution(child);
	}

}
