package hr.fer.zemris.optjava.dz8.Algorithm;

import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.BaseVector.BestBaseVector;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.BaseVector.DEABaseVectorFunction;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Cross.DEACrossFunction;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Diff.DEADifferentionFunction;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Selection.DEASelectionFunction;
import hr.fer.zemris.optjava.dz8.Data.Data;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.INeuralNetwork;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Differential evolution algorithm
 */
public class DEA implements IAlgorithm {

    private int populationSize;
    private int maxIterations;
    private double minError;

    private DEADifferentionFunction differentionFunction;
    private DEACrossFunction crossFunction;
    private DEASelectionFunction selectionFunction;
    private DEABaseVectorFunction baseVectorFunction;

    private Data data;
    private INeuralNetwork function;

    private static final int MAX_VAL = 1;
    private static final int MIN_VAL = -1;

    public DEA(int populationSize, int maxIterations, double minError,DEABaseVectorFunction deaBaseVectorFunction, DEADifferentionFunction differentionFunction, DEACrossFunction crossFunction, DEASelectionFunction selectionFunction, Data data, INeuralNetwork function) {
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.minError = minError;
        this.differentionFunction = differentionFunction;
        this.crossFunction = crossFunction;
        this.selectionFunction = selectionFunction;
        this.data = data;
        this.function = function;
        this.baseVectorFunction = deaBaseVectorFunction;
    }

    @Override
    public void run() {
        Random random = new Random();
        List<Unit> population = createInitialPopulation(random);

        int iteration = 0;
        Unit.globalBestScore = Double.MAX_VALUE;
        while(Unit.globalBestScore > minError && iteration < maxIterations){

            population.stream().forEach( u -> u.evaluate());

            List<Unit> nextGen = new ArrayList<>();
            for(int i = 0; i< population.size(); i++){
                Unit u = baseVectorFunction.apply(population);

                List<Unit> chosen = findTwoDifferentVectors(population, random, population.indexOf(u));

                RealVector mutant = differentionFunction.apply(
                        u.getVector(),
                        chosen.get(0).getVector(),
                        chosen.get(1).getVector(),
                        population
                );

                RealVector cross = crossFunction.apply(
                        u.getVector(),
                        mutant);

                Unit selected = selectionFunction.apply(
                        u,
                        cross);

                nextGen.add(selected);
            }

            String out =String.format("Iteration: %4d , score: %3.4f , curr: %3.4f", iteration,
                    Unit.globalBestScore, BestBaseVector.findBest(population).evaluate());
            System.out.println(out);

            population = nextGen;
            iteration++;
        }
    }

    private List<Unit> findTwoDifferentVectors(List<Unit> population, Random random, int u) {
        List<Unit> unitList = new ArrayList<>();

        int n1;
        do {
            n1 = random.nextInt(population.size());
        }while(n1 == u);
        unitList.add(population.get(n1));

        int n2;
        do{
            n2 = random.nextInt(population.size());
        } while (n1 == n2 || n2 == u);
        unitList.add(population.get(n2));

        return unitList;
    }

    private List<Unit> createInitialPopulation (Random random) {

        List<Unit> particlelist = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            particlelist.add(new Unit(randomVector(random), function, data));
        }

        return particlelist;
    }

    private RealVector randomVector(Random random) {

        double[] vec = new double[function.getWeightsCount()];
        for(int i =0 ; i < function.getWeightsCount(); i++){
           vec[i] = random.nextDouble()*(MAX_VAL - MIN_VAL) + MIN_VAL;
        }

        return new ArrayRealVector(vec);
    }
}
