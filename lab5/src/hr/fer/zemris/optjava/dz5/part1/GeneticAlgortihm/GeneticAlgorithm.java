package hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm;

import hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.selector.ISelector;
import hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.selector.TurnamentSelector;
import hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm.selector.UniformSelector;

import java.util.*;

/**
 * RAPGA algorithm implementation.
 * Assignment information available in README.txt
 *
 * Sample to run:
 * 100
 *
 * @author Filip Saina
 */
public class GeneticAlgorithm {

    //size of bitSet
    private int n;
    private ISelector selector1;
    private ISelector selector2;
    private MaxOnesFunction function;
    private Comparator<Chromosome> comparator;

    private static final int TURNAMENTSIZE = 5;
    private static final Integer MAXITERATIONS = 400;
    private static final Integer MAXPOPULATION = 1000;
    private static final Double SELECTIONPRESSURE = 3.0;
    private static final Integer MAXEFFORT = (int) (MAXITERATIONS * SELECTIONPRESSURE);
    private static final int MINPOPULATION = 2;

    private static final double MUTATIONPROBABILITY = 0.0001;

    public GeneticAlgorithm(int n, ISelector selector1, ISelector selector2, MaxOnesFunction function,
                            Comparator<Chromosome> comparator) {
        this.n = n;
        this.selector1 = selector1;
        this.selector2 = selector2;
        this.function = function;
        this.comparator = comparator;
    }

    public void run() {

        List<Chromosome> generation = generateInitialSetOfSize(MAXPOPULATION / 2);
        Random random = new Random();

        double comparisonFactor;

        int iteration = 0;
        while (iteration < MAXITERATIONS) {

            //TODO check
            comparisonFactor = 1. /
                    (1 + Math.exp(((double) -iteration + MAXITERATIONS / 2)
                            / Math.sqrt(MAXITERATIONS) / Math.PI));


            List<Chromosome> newGeneration = new ArrayList<>();

            int maxEffortInteration = 0;
            while (maxEffortInteration < MAXEFFORT) {
                Chromosome parent1 = selector1.pickParent(generation);
                Chromosome parent2 = selector2.pickParent(generation);

                // ensure that they are different
                while (parent1.equals(parent2)) {
                    parent2 = selector2.pickParent(generation);
                }

                Chromosome child = cross(parent1, parent2, random);

                double parentOneFitness = function.getFitness(parent1);
                double parentTwoFitness = function.getFitness(parent2);

                double targetFitness = targetFitness(Math.min(parentOneFitness, parentTwoFitness),
                        Math.max(parentOneFitness, parentTwoFitness),
                        comparisonFactor);

                child = mutate(child, random);

                if (function.getFitness(child) >= targetFitness) {
                    if (!newGeneration.contains(child)) {
                        newGeneration.add(child);
                    }
                }

                if (newGeneration.size() > MAXPOPULATION) {
                    break;
                }
                maxEffortInteration++;
            }

            if (!newGeneration.isEmpty()) {
                generation = newGeneration;
            }


            Chromosome bestChromosome = Collections.max(generation, comparator);

            System.out.format("Generation: %d, fitness: %f\n", iteration, function.getFitness(bestChromosome));

            if (function.isOptimum(bestChromosome)) {
                break;
            }

            if (newGeneration.size() < MINPOPULATION) {
                System.out.println("Population has decreesed to less than allowed");
                System.exit(-1);
            }

            iteration++;
        }

        Chromosome winner = Collections.max(generation, comparator);

        if(function.isOptimum(winner)) {
            System.out.println("\n\n Winner is: " + winner);
            System.out.println("Fitness: " + function.getFitness(winner));
        } else {
            System.out.println("\nSorry, no optimal solution found");
        }
    }

    private Chromosome mutate(Chromosome child, Random random) {

        for (int i = 0; i < child.getSize(); i++) {

            double generate = random.nextDouble();

            //flip
            if (generate < MUTATIONPROBABILITY) {
                child.getBitSet().set(i, !child.getBitSet().get(i));
            }
        }

        return child;
    }

    private Chromosome cross(Chromosome parent1, Chromosome parent2, Random random) {

        int splitIndex = random.nextInt(parent1.getSize());
        Chromosome copy = parent1.copy();

        for (int i = splitIndex; i < parent1.getSize(); i++) {
            copy.getBitSet().set(i, parent2.getBitSet().get(i));
        }

        return copy;
    }

    private List<Chromosome> generateInitialSetOfSize(int generationIterations) {

        List<Chromosome> generation = new ArrayList<>();
        Random random = new Random();

        while (generation.size() < generationIterations) {
            Chromosome element = Chromosome.generateRandom(n, random);
            generation.add(element);
        }

        return generation;
    }

    private double targetFitness(double min, double max, double comparisonFactor) {
        return min + comparisonFactor * (max - min);
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Wrong number of arguments provided");
            System.exit(-1);
        }

        int n = 0;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Cant read the input parameter");
            System.exit(-1);
        }

        MaxOnesFunction function = new MaxOnesFunction(n);
        Comparator<Chromosome> comparator = new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome chromosome, Chromosome t1) {
                if (function.getFitness(chromosome) > function.getFitness(t1)) {
                    return 1;
                } else if (function.getFitness(chromosome) < function.getFitness(t1)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        ISelector selector1 = new UniformSelector(new Random());
        ISelector turnament = new TurnamentSelector(new Random(),
                comparator,
                TURNAMENTSIZE);

        /*
         * BITNO ! Za odabir selekcija, samo predati odgovarajuce argumente
         * u konstruktor GeneticAlgorithm-a.
         */

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(n,
                turnament,
                turnament,
                function,
                comparator);

        geneticAlgorithm.run();

    }

}
