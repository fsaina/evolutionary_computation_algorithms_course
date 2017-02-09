package hr.fer.zemris.optjava.dz7.NeuralNetwork.Functions;

public class SigmoidFunction implements ITransferFunction {

    /*
     * The bias term = - tershold, is usually included as a
     * additional perceptron in the neural net topology.
     */
    private static final double BIAS = -0.5;    //TODO check if needed (moves left right)

    public double solveForInput(double entry) {
        return sigmoid(entry+BIAS);
    }

    private double sigmoid(double input) {
        return 1 / (1 + Math.exp(-input));
    }
}
