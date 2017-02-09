package hr.fer.zemris.optjava.dz8.Data.Model;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

/**
 * Created by fsaina-lenovo on 12/19/16.
 */
public class LaserModel {
    private RealVector inputVector;
    private RealVector outputVector;

    public LaserModel(List<Double> inputs, Double output){

        double[] array = inputs.stream().mapToDouble(i->i).toArray();
        double[] outA = {output};

        inputVector = MatrixUtils.createRealVector(array);
        outputVector = MatrixUtils.createRealVector(outA);
    }

    public RealVector getInputVector() {
        return inputVector;
    }

    public RealVector getOutputVector() {
        return outputVector;
    }
}
