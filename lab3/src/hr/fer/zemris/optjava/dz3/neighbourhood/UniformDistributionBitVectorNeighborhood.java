package hr.fer.zemris.optjava.dz3.neighbourhood;

import hr.fer.zemris.optjava.dz3.representations.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;

import java.util.Random;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public class UniformDistributionBitVectorNeighborhood implements INeighbourhood {


    private Random random;

    public UniformDistributionBitVectorNeighborhood(){
        random = new Random();
    }

    @Override
    public SingleObjectiveSolution randomNeighbour(SingleObjectiveSolution input) {

        if (!(input instanceof BitVectorSolution)){
            throw new IllegalArgumentException("Not valid solution given class, needs to be BitVector");
        }

        BitVectorSolution solution = ((BitVectorSolution) input).duplicate();
        solution.randomize(random);


        return solution;
    }
}
