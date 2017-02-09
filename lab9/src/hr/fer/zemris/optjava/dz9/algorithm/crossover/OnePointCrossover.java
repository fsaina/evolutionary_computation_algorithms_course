package hr.fer.zemris.optjava.dz9.algorithm.crossover;

import hr.fer.zemris.optjava.dz9.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

import java.util.Random;

public class OnePointCrossover implements ICrossover<MultipleObjectiveSolution> {

	private Random rand;
	
	public OnePointCrossover() {
		this.rand = new Random();
	}
	
	@Override
	public MultipleObjectiveSolution cross(MultipleObjectiveSolution firstParent,
			MultipleObjectiveSolution secondParent) {
		if(rand.nextBoolean()) {
			MultipleObjectiveSolution tmp = firstParent;
			firstParent = secondParent;
			secondParent = tmp;
		}
		
		int point = rand.nextInt(firstParent.values.length - 1) + 1;
		
		MultipleObjectiveSolution child = firstParent.duplicate();
		for(int i = point; i < child.values.length; i++) {
			child.values[i] = secondParent.values[i];
		}
		
		return child;
	}

}
