package hr.fer.zemris.optjava.dz8.NeuralNetwork.Functions;

public class TanhFunction implements ITransferFunction {

    /*
     * The bias term = - tershold, is usually included as a
     * additional perceptron in the neural net topology.
     */
    private static final double BIAS = -0.5;

    public double solveForInput(double entry) {
        return tanhValue(entry+BIAS);
    }

    private double tanhValue(double v) {
        return 2* sigmoid(v) -1;
    }

    private double sigmoid(double input) {
        return 1 / (1 + Math.exp(-input));
    }
}
