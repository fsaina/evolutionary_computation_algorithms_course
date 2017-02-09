package hr.fer.zemris.optjava.dz8.Algorithm;

import hr.fer.zemris.optjava.dz8.Data.Data;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.FFANN;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.INeuralNetwork;
import org.apache.commons.math3.linear.RealVector;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

/**
 * Created by fsaina-lenovo on 12/19/16.
 */
public class Unit {

    //global best
    public static RealVector best;
    public static double globalBestScore = Double.MAX_VALUE;

    private RealVector vector; // solution
    private INeuralNetwork function;
    private Data data;
    private double currentError;

    public Unit(RealVector vector, INeuralNetwork function, Data data) {
        this.vector = vector;
        this.function = function;
        this.data = data;
    }

    public RealVector getVector() {
        return vector;
    }

    public INeuralNetwork getFunction() {
        return function;
    }

    public Data getData() {
        return data;
    }

    public double getCurrentError() {
        return currentError;
    }

    public double evaluate() {
        DoubleSummaryStatistics stats = data.getModels().stream()
                .collect(Collectors.summarizingDouble(m -> {
                    RealVector prediction = function.calcOutputs(m.getInputVector(), vector, true);
                    double sum = FFANN.meanSquaredError(prediction, m.getOutputVector(), data.getNumberOfElements());
                    return sum;
                }));

        double error = stats.getSum();
        error = error / (data.getNumberOfElements());

        if (error < globalBestScore) {
            best = vector;
            globalBestScore = error;
        }

        currentError = error;
        return error;
    }
}
