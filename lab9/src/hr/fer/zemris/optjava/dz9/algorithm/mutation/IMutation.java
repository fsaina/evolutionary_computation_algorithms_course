package hr.fer.zemris.optjava.dz9.algorithm.mutation;

import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;

public interface IMutation<T extends MultipleObjectiveSolution> {
	public void mutate(T chromosome);
}
