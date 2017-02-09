package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.part2.Chromosome;

import java.util.List;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public interface ISelector {
    Chromosome pickParent(List<Chromosome> generationMap);

}
