package hr.fer.zemris.optjav.dz4.part1.algorithms;

import hr.fer.zemris.optjav.dz4.part1.ChromosomeElement;
import hr.fer.zemris.optjav.dz4.part1.functions.IHFunction;
import hr.fer.zemris.optjav.dz4.part1.selector.ISelector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.*;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public class GeneticAlgorithm {

    private int populationSize;
    private int minError;
    private int maxGenerations;
    private double sigma;
    private ISelector selector;
    private IHFunction costFunction;

    private static final Double ALPHA = 0.85;


    public GeneticAlgorithm(int populationSize, int minError, int maxGenerations, double sigma,
                            ISelector selector,
                            IHFunction costFunction) {
        this.populationSize = populationSize;
        this.minError = minError;
        this.maxGenerations = maxGenerations;
        this.sigma = sigma;
        this.selector = selector;
        this.costFunction = costFunction;
    }

    public void run() {

        List<ChromosomeElement> population = generateInitialPopulation(6, 5, -5);

        /*
         * Smallest elements in the population get to the top, largest to the bottom.
         * The metric for ranking is determined by the cost function score,
         * so the less the "score" is the better. Therefore, best score is on top.
         */
        evaluate(population);

        Random random = new Random();

        int generation = 1;
        do {

            List<ChromosomeElement> populationPrime = new ArrayList<>();
            populationPrime.addAll(bestTwoParents(population));

            while (populationPrime.size() < populationSize) {

                List<ChromosomeElement> selected = selector.select(population);
                ChromosomeElement crossed = cross(selected, random);
                ChromosomeElement mutated = mutate(crossed, random);

                populationPrime.add(mutated);
            }

            population = populationPrime;

            evaluate(population);

            ChromosomeElement bestChromosome = population.get(0);

            if(generation % 100 == 0) {
                System.out.println("Generation: " + generation + " ,values: "+ bestChromosome.getRealVector() + ", score: " + bestChromosome.getScore());
            }

            generation++;
        } while (generation < maxGenerations && !(population.get(0).getScore() < minError));

        System.out.println("\n\nFinal best chromosome:");
        System.out.println(population.get(0).getRealVector() + ", with Score: "
                + population.get(0).getScore());
    }

    private ChromosomeElement mutate(ChromosomeElement crossed, Random random) {

        RealVector chromosome = crossed.getRealVector();

        for (int i = 0; i < chromosome.getDimension(); i++) {

            chromosome.setEntry(i, chromosome.getEntry(i) + random.nextGaussian() * sigma);
        }

        ChromosomeElement ret = new ChromosomeElement(chromosome);
        return ret;
    }

    //BLX-alpha crossover
    private ChromosomeElement cross(List<ChromosomeElement> selected, Random random) {

        if (selected.size() != 2) {
            System.err.println("Wrong number of parents selected for crossing: " + selected.size());
            System.exit(-1);
        }

        ChromosomeElement parent1 = selected.get(0);
        ChromosomeElement parent2 = selected.get(1);
        RealVector child = new ArrayRealVector(parent1.getRealVector().getDimension());

        for (int i = 0; i < parent1.getRealVector().getDimension(); i++) {

            double cMin = Math.min(parent1.getRealVector().getEntry(i),
                    parent2.getRealVector().getEntry(i));

            double cMax = Math.max(parent1.getRealVector().getEntry(i),
                    parent2.getRealVector().getEntry(i));

            double diffI = cMax - cMin;

            double max = (cMax + diffI* ALPHA);
            double min = (cMin - diffI * ALPHA);
            double value = random.nextDouble() * (max - min) + min;

            child.setEntry(i, value);
        }

        return new ChromosomeElement(child);
    }

    private Collection<? extends ChromosomeElement> bestTwoParents(List<ChromosomeElement> population) {

        List<ChromosomeElement> twoParentList = new ArrayList<>();

        twoParentList.add(population.get(0));
        twoParentList.add(population.get(1));

        return twoParentList;
    }

    private void evaluate(List<ChromosomeElement> population) {
        for (ChromosomeElement chromosome : population) {
            chromosome.evaluate(costFunction);
        }

        Collections.sort(population);
    }

    private List<ChromosomeElement> generateInitialPopulation(int chromosomeSize, double max, double min) {

        List<ChromosomeElement> list = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < populationSize; i++) {

            RealVector chromosome = new ArrayRealVector(chromosomeSize);
            for (int m = 0; m < chromosomeSize; m++) {
                chromosome.setEntry(m, random.nextDouble() * (max - min) + min);
            }

            list.add(new ChromosomeElement(chromosome));
        }

        return list;
    }


}
