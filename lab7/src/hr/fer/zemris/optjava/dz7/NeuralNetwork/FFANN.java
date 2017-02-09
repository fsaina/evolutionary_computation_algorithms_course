package hr.fer.zemris.optjava.dz7.NeuralNetwork;

import hr.fer.zemris.optjava.dz7.NeuralNetwork.Functions.ITransferFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Feedforward-artificial-neural-network implementation.
 */
public class FFANN {
    private int[] nnTopology;
    private ITransferFunction[] layerTransferFunctions;
    private int numberOfWeights;
    private ITransferFunction outputFunction;

    public FFANN(int[] nnTopology, ITransferFunction[] layerTransferFunctions, ITransferFunction outputFunction) {
        this.nnTopology = nnTopology;
        this.layerTransferFunctions = layerTransferFunctions;
        this.outputFunction = outputFunction;

        calculateNumberOfWeights();
    }

    private void calculateNumberOfWeights() {

        int previous = nnTopology[0];
        int number = 0;
        for (int i = 1; i < nnTopology.length; i++) {
            int current = nnTopology[1];
            number += previous * current;
            previous = current;
        }

        numberOfWeights = number;
    }

    public int getWeightsCount() {
        return numberOfWeights;
    }

    //method also applies the step function at the end
    public RealVector calcOutputs(RealVector input, RealVector weights, boolean training) {
        RealVector output = forwardPass(input, convertVectorToMatrixList(weights));
        if (training) {
            return output;
        }
        return applyOutputFunctionToVectors(output);
    }

    private List<RealMatrix> convertVectorToMatrixList(RealVector weights) {
        List<RealMatrix> weightMatrixList = new ArrayList<RealMatrix>();
        int prevVectorDim = nnTopology[0];
        int weightMatrixCumulativeSize = 0;

        for (int i = 1; i < nnTopology.length; i++) {
            int currentVectorDim = nnTopology[i];

            RealMatrix weightMatrix = new Array2DRowRealMatrix(prevVectorDim, currentVectorDim);
            for (int n = 0; n < prevVectorDim; n++) {
                for (int m = 0; m < currentVectorDim; m++) {
                    weightMatrix.setEntry(n, m, weights.getEntry(
                            weightMatrixCumulativeSize +
                                    n * weightMatrix.getColumnDimension() +
                                    m));
                }
            }
            weightMatrixCumulativeSize += prevVectorDim * currentVectorDim;

            weightMatrixList.add(weightMatrix);
            prevVectorDim = currentVectorDim;
        }

        return weightMatrixList;
    }

    private RealVector forwardPass(RealVector input, List<RealMatrix> weights) {
        RealVector iInput = input.copy();

        for (int i = 0; i < weights.size(); i++) {

            RealMatrix weight = weights.get(i);
            RealVector z = weight.preMultiply(iInput);

            iInput = applyTransferFunctionsToVectors(z, layerTransferFunctions[i]);

        }

        return iInput;
    }

    // n - size of traning inputs
    public static double meanSquaredError(RealVector iInput, RealVector output, int n) {
        if (iInput.getDimension() != output.getDimension()) {
            throw new IllegalArgumentException("Incorrect dimensions provided!");
        }

        double error = 0.0;
        for (int i = 0; i < iInput.getDimension(); i++) {
            error += Math.pow(iInput.getEntry(i) - output.getEntry(i), 2);
        }

        return error / (2 * n);
    }

    private RealVector applyTransferFunctionsToVectors(RealVector z, ITransferFunction layerTransferFunction) {
        for (int m = 0; m < z.getDimension(); m++) {
            z.setEntry(m, layerTransferFunction.solveForInput(z.getEntry(m)));
        }

        return z;
    }

    private RealVector applyOutputFunctionToVectors(RealVector z) {
        IntStream.range(0, z.getDimension()).forEach(n -> {
            z.setEntry(n, outputFunction.solveForInput(z.getEntry(n)));
        });

        return z;
    }
}
