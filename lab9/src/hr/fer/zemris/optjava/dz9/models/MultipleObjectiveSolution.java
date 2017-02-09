package hr.fer.zemris.optjava.dz9.models;

import java.util.Arrays;

public class MultipleObjectiveSolution {

	public double[] values;
	public double[] objectives;
	public int dimension;
	public double fitness;

	public MultipleObjectiveSolution(double[] values) {
		this.dimension = values.length;
		this.values = values;
	}
	
	public boolean dominates(MultipleObjectiveSolution other) {

		boolean dominates = false;
		
		for(int i = 0; i < objectives.length; i++) {
			if(objectives[i] > other.objectives[i]) {
				return false;
			} else if(objectives[i] < other.objectives[i]) {
				dominates = true;
			}
		}
		
		return dominates;
	}
	
	public MultipleObjectiveSolution duplicate() {
		return new MultipleObjectiveSolution(Arrays.copyOf(values, dimension));
	}
	
}
