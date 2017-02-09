package hr.fer.zemris.optjava.dz3.neighbourhood;

import hr.fer.zemris.optjava.dz3.representations.DecimalSolution;
import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;
import org.apache.commons.math3.analysis.function.Sin;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public class GaussDecimalNeighbourhood implements INeighbourhood {
    @Override
    public SingleObjectiveSolution randomNeighbour(SingleObjectiveSolution input) {

        if(!(input instanceof DecimalSolution)){
            throw new IllegalArgumentException("input has to be in decimal representation");
        }

        DecimalSolution solution = (DecimalSolution) input;


        solution =  solution.getNeighbour();

        return solution;
    }
}
