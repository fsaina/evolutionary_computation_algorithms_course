package hr.fer.zemris.optjava.dz3.neighbourhood;

import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public interface INeighbourhood {
    SingleObjectiveSolution randomNeighbour(SingleObjectiveSolution input);
}
