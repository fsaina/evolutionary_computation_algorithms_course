package hr.fer.zemris.optjava.dz8.NeuralNetwork;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 12/20/16.
 */
public interface INeuralNetwork {

    public RealVector calcOutputs(RealVector input, RealVector weights, boolean training);
    public int getWeightsCount();
}
