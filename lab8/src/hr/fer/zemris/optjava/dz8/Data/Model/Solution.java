package hr.fer.zemris.optjava.dz8.Data.Model;

import hr.fer.zemris.optjava.dz8.Data.Data;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.FFANN;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.DoubleSummaryStatistics;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution implements Comparable<Solution> {

    private RealVector solution;
    private int numberOfWeights;

    private double currentError = Double.MAX_VALUE;

    //Global variables
    public static RealVector globalBestPosition;
    public static double globalBestScore;

    private static final double XMAX = 1.0;
    private static final double XMIN = -1;

    public Solution(Random random, int numberOfWeights) {
        this.numberOfWeights = numberOfWeights;
        solution = generateRandomWeightVector(random);
        globalBestPosition = solution;
        globalBestScore = Double.MAX_VALUE;
    }

    private Solution() {}

    public double getCurrentError() {
        return currentError;
    }
    private RealVector generateRandomWeightVector(Random random) {
        RealVector solution = new ArrayRealVector(numberOfWeights);

        IntStream.range(0, numberOfWeights).forEach(n -> {
            solution.setEntry(n, random.nextDouble() * XMAX + XMIN);
        });

        return solution;
    }

    public double evaluate(Data data, FFANN function) {
        DoubleSummaryStatistics stats = data.getModels().stream()
                .collect(Collectors.summarizingDouble(m -> {
                    RealVector prediction = function.calcOutputs(m.getInputVector(), solution, true);
                    return FFANN.meanSquaredError(prediction, m.getOutputVector(), data.getNumberOfElements());
                }));

        double error = stats.getSum();

        if (error < globalBestScore) {
            globalBestPosition = solution;
            globalBestScore = error;
        }

        currentError = error;
        return error;
    }

    public void setSolution(RealVector solution) {
        this.solution = solution;
    }

    public RealVector getSolution() {
        return solution;
    }

    public int getNumberOfWeights() {
        return numberOfWeights;
    }

    @Override
    public int compareTo(Solution solution) {
        if(currentError > solution.currentError){
            return 1;
        } else if (currentError < solution.currentError){
            return -1;
        } else {
            return 0;
        }
    }

    public Solution copy() {
        Solution sol = new Solution();
        sol.solution = solution.copy();
        sol.numberOfWeights = numberOfWeights;
        sol.currentError = currentError;
        return sol;
    }
}

