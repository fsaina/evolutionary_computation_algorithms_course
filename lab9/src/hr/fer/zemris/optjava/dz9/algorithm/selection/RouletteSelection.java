package hr.fer.zemris.optjava.dz9.algorithm.selection;

import hr.fer.zemris.optjava.dz9.algorithm.selection.ISelection;
import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

import java.util.Random;

public class RouletteSelection implements ISelection<MultipleObjectiveSolution> {

	private Random rand;
	
	public RouletteSelection() {
		this.rand = new Random();
	}
	
	@Override
	public int select(MultipleObjectiveSolution[] population) {		
		double probability = rand.nextDouble();
		double offset = 0.0;
		double sum = 0.0;
		
		for(MultipleObjectiveSolution solution : population) {
			sum += solution.fitness;
		}
		
		for(int i = 0; i < population.length; i++) {
			offset += population[i].fitness / sum;
			
			if(offset >= probability) {
				return i;
			}
		}
		
		return -1;
	}

}
