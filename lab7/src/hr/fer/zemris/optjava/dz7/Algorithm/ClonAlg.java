package hr.fer.zemris.optjava.dz7.Algorithm;

import hr.fer.zemris.optjava.dz7.Data.*;
import hr.fer.zemris.optjava.dz7.Data.Model.Solution;
import hr.fer.zemris.optjava.dz7.NeuralNetwork.FFANN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ClonAlg implements IAlgorithm {

    private static final double BETA = 3;
    private static final double RO = 0.3;
    private FFANN nn;
    private int numberOfWeights;
    private int populationSize;
    private int maxIterations;
    private double maxError;
    private Data data;

    private List<Solution> solutions;

    public ClonAlg(FFANN nn, int populationSize, int maxIterations, double maxError, Data data) {
        this.nn = nn;
        this.numberOfWeights = nn.getWeightsCount();
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.maxError = maxError;
        this.data = data;

        solutions = initializePopulationWithSize(populationSize);
    }

    @Override
    public void run() {

        Random random = new Random();
        int interation = 1;
        while (interation < maxIterations && maxError < Solution.globalBestScore) {

            solutions.stream().forEach(s -> s.evaluate(data, nn));

            cloneSolutions(solutions, BETA);
            hipermutate(solutions, random);

            solutions.addAll(initializePopulationWithSize(20));
            solutions.stream().forEach(s -> s.evaluate(data, nn));

            Collections.sort(solutions);
            reduceToNormalPopulation();

            System.out.println(String.format(" %4d / %4d , score ->  %2.3f",
                    interation,
                    maxIterations,
                    Solution.globalBestScore));

            interation++;
        }
    }

    private void reduceToNormalPopulation() {
        solutions = new ArrayList<>(solutions.subList(0, populationSize));
    }

    private void hipermutate(List<Solution> clones, Random random) {
        clones.stream().forEach(s -> {
            if (random.nextDouble() < mutationProbability(s)) {
                mutate(s, random);
            }
        });
    }

    private double mutationProbability(Solution solution) {
        return 1.0 / RO * Math.exp(-solution.getCurrentError());
    }

    private void mutate(Solution solution, Random random) {
        int i = random.nextInt(solution.getNumberOfWeights());
        int j = random.nextInt(solution.getNumberOfWeights());

        double tmp = solution.getSolution().getEntry(i);
        solution.getSolution().setEntry(i, j);
        solution.getSolution().setEntry(j, tmp);
    }

    private void cloneSolutions(List<Solution> solutions, double beta) {

        List<Solution> clones = new ArrayList<>();
        int cloneNTimes = calculateNumberOfClones(solutions.size(), beta);

        int cloneSum = 0;
        for(Solution s : solutions){
            int iter = (int) (1/(s.getCurrentError()));
            cloneSum+=iter;
            if(cloneSum > cloneNTimes){
                break;
            }
            IntStream.range(0, iter).forEach( i -> clones.add(s.copy()));
        }

        solutions.addAll(clones);
    }

    private int calculateNumberOfClones(int size, double beta) {

        int sum = 0;
        for (int i = 1; i < size; i++) {
            sum += Math.floor((beta * size) / i);
        }
        return sum;
    }


    private List<Solution> initializePopulationWithSize(int n) {
        List<Solution> solutions1 = new ArrayList<>();
        Random random = new Random();

        IntStream.range(0, n).forEach(i -> {
            solutions1.add(new Solution(random, numberOfWeights));
        });

        return solutions1;
    }
}
