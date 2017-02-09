package hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.selector;

import hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public interface ISelector {
    Chromosome pickParent(List<Chromosome> generationMap);

}
