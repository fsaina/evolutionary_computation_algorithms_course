package hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.selector;

import hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public class UniformSelector implements ISelector{

    private Random random;

    public UniformSelector(Random random) {
        this.random = random;
    }

    @Override
    public Chromosome pickParent(List<Chromosome> chromosomeList) {

        int index = random.nextInt(chromosomeList.size());

        return chromosomeList.get(index);
    }
}
