package hr.fer.zemris.optjava.dz9.algorithm.crossover;

import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

public interface ICrossover<T extends MultipleObjectiveSolution> {
	T cross(T firstParent, T secondParent);
}
