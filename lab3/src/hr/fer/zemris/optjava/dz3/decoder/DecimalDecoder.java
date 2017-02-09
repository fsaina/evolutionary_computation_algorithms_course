package hr.fer.zemris.optjava.dz3.decoder;


import hr.fer.zemris.optjava.dz3.representations.DecimalSolution;
import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public class DecimalDecoder implements IDecoder {
    @Override
    public double[] decode(SingleObjectiveSolution input) {

        return ((DecimalSolution) input).getSolutions();
    }

    @Override
    public void decode(SingleObjectiveSolution input, double[] field) {

    }
}
