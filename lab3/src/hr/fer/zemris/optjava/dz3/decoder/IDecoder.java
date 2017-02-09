package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public interface IDecoder{
    double[] decode(SingleObjectiveSolution input);
    void decode(SingleObjectiveSolution input, double[] field);
}
