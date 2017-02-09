package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.part2.Chromosome;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public interface IFunction {

    public double getFitness(Chromosome chromosome);
    public boolean isOptimum(Chromosome chromosome);
}
