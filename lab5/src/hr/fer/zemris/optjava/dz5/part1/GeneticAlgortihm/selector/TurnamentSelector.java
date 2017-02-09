package hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.selector;

import hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.Chromosome;

import java.util.*;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public class TurnamentSelector implements ISelector {

    private Random random;
    private Comparator<Chromosome> comparable;
    int turnamentSize;

    public TurnamentSelector(Random random, Comparator<Chromosome> comparable, int turnamentSize) {
        this.random = random;
        this.comparable = comparable;
        this.turnamentSize = turnamentSize;
    }

    @Override
    public Chromosome pickParent(List<Chromosome> generationMap) {

        List<Chromosome> turnament = new ArrayList<>();

        for(int i =0 ; i < turnamentSize; i ++){
            turnament.add(generationMap.get(random.nextInt(generationMap.size())));
        }

        return Collections.max(turnament, comparable);
    }
}
