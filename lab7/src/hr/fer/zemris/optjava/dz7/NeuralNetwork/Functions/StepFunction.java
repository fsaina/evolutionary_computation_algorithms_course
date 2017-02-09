package hr.fer.zemris.optjava.dz7.NeuralNetwork.Functions;

public class StepFunction implements ITransferFunction {
    @Override
    public double solveForInput(double entry) {
        if(entry >= 0.5){
            return 1;
        }

        return 0;
    }
}
