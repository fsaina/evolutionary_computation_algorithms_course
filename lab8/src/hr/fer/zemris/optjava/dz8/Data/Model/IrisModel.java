package hr.fer.zemris.optjava.dz8.Data.Model;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

public class IrisModel {
    private RealVector inputVector;
    private RealVector outputVector;

    public IrisModel(double v, double v1, double v2, double v3, int i, int i1, int i2) {
        double[] input = {v, v1, v2, v3};
        double[] output = {i, i1, i2};
        inputVector = MatrixUtils.createRealVector(input);
        outputVector = MatrixUtils.createRealVector(output);
    }

    public RealVector getInputVector() {
        return inputVector;
    }

    public RealVector getOutputVector() {
        return outputVector;
    }
}
