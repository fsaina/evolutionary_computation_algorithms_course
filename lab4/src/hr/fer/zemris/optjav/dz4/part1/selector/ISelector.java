package hr.fer.zemris.optjav.dz4.part1.selector;

import hr.fer.zemris.optjav.dz4.part1.ChromosomeElement;

import java.util.List;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public interface ISelector {

    public List<ChromosomeElement> select(List<ChromosomeElement> population);


}
