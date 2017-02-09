package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.representations.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public class NaturalBinaryDecoder extends BitVectorDecoder {

    public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public double[] decode(SingleObjectiveSolution inputSolution) {

        if(!(inputSolution instanceof BitVectorSolution)){
            throw new IllegalArgumentException("Wrong argument");
        }

        BitVectorSolution input = (BitVectorSolution) inputSolution;

        double[] solution = new double[input.getSize()];

        for (int i = 0; i < input.getSize(); i++) {

            String element = input.getBits()[i];

            int value = Integer.parseInt(element, 2);

            solution[i] = mins[i] + (value) / (Math.pow(2, bits[i]) - 1) * (maxs[i] - mins[i]);

        }

        return solution;
    }


    @Override
    public void decode(SingleObjectiveSolution input, double[] field) {
        //TODO
    }
}
