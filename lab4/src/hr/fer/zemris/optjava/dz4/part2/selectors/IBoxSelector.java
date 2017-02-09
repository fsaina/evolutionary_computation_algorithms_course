package hr.fer.zemris.optjava.dz4.part2.selectors;

import hr.fer.zemris.optjava.dz4.part2.chromosomes.BoxFillElement;

import java.util.List;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public interface IBoxSelector {


    BoxFillElement select(List<BoxFillElement> population, int n, boolean best);
}
