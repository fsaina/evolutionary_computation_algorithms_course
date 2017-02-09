package hr.fer.zemris.optjava.dz9.algorithm.crossover;

import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

public class ArithmeticCrossover implements ICrossover<MultipleObjectiveSolution> {

	@Override
	public MultipleObjectiveSolution cross(MultipleObjectiveSolution firstParent, MultipleObjectiveSolution secondParent) {
		MultipleObjectiveSolution child = firstParent.duplicate();
		
		for(int i = 0; i < child.values.length; i++) {
			child.values[i] += secondParent.values[i];
			child.values[i] /= 2.0;
		}
		
		return child;
	}

}
