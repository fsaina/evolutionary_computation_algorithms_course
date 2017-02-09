package hr.fer.zemris.optjava.dz9.algorithm.selection;

import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

public interface ISelection<T extends MultipleObjectiveSolution> {
	public int select(T[] population);
}
