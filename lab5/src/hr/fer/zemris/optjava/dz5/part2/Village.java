package hr.fer.zemris.optjava.dz5.part2;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

class Village implements Runnable, Callable<Village> {

    private boolean convergedOrMetMaxIterations = false;

    private List<Chromosome> generation = new ArrayList<>();
    private ISelector selector; //turnament
    private IFunction function;
    private Random random;
    private Comparator<Chromosome> comparator;

    private int iteration;

    private static final Integer MAXITERATIONS = 200;
    private static final Double SELECTIONPRESSURE = 3.0;
    private static final Integer MAXEFFORT = (int) (MAXITERATIONS * SELECTIONPRESSURE);
    private static final int MINPOPULATION = 2;

    private static final double MUTATIONPROBABILITY = 0.0001;
    private static final double CROSSFACTOR = 0.05;

    public Village(Integer sizeOfpopulation, Integer sizeOfChromosome, ISelector selector, IFunction function, Comparator<hr.fer.zemris.optjava.dz5.part2.Chromosome> comparator) {
        this.selector = selector;
        this.function = function;
        this.comparator = comparator;
        this.random = new Random();
        this.iteration = MAXITERATIONS/20;
        this.convergedOrMetMaxIterations = false;
        this.generation = generateInitialPopulation(sizeOfpopulation, sizeOfChromosome);
    }


    public Chromosome bestChromosome() {
        return Collections.max(generation, comparator);
    }

    @Override
    public void run() {
        //run next generation
        double comparisonFactor = 1. /
                (1 + Math.exp(((double) - iteration + MAXITERATIONS / 2)
                        / Math.sqrt(MAXITERATIONS) / Math.PI));

        List<Chromosome> newGeneration = new ArrayList<>();

        int maxEffortInteration = 0;
        while (maxEffortInteration <= MAXEFFORT) {
            Chromosome parent1 = selector.pickParent(generation);
            Chromosome parent2 = selector.pickParent(generation);

            // ensure that they are different

            //Chromosome child = cross(parent1, parent2, random);
            Chromosome child = parent1;

            double parentOneFitness = function.getFitness(parent1);
            double parentTwoFitness = function.getFitness(parent2);

            double targetFitness = targetFitness(Math.min(parentOneFitness, parentTwoFitness),
                    Math.max(parentOneFitness, parentTwoFitness),
                    comparisonFactor);

            child = mutate(child, random);

            if (function.getFitness(child) > targetFitness) {
                if (!newGeneration.contains(child)) {
                    newGeneration.add(child);
                }
            }

            maxEffortInteration++;
        }

        if (!newGeneration.isEmpty()) {
            generation = newGeneration;
        }

        if (generation.size() <= MINPOPULATION) {
            convergedOrMetMaxIterations = true;
        }

        iteration++;

    }

    private double targetFitness(double min, double max, double comparisonFactor) {
        return min + comparisonFactor * (max - min);
    }

    private Chromosome mutate(Chromosome child, Random random) {

        for (int i = 0; i < child.getSize(); i++) {

            double generate = random.nextDouble();

            //replace two
            if (generate < MUTATIONPROBABILITY) {

                int index1 = random.nextInt(child.getSize());
                int index2 = random.nextInt(child.getSize());

                int tmp = child.getSolution().get(index1);
                child.getSolution().set(index1, child.getSolution().get(index2));
                child.getSolution().set(index2, tmp);
            }
        }

        return child;
    }

    private Chromosome cross(Chromosome parent1, Chromosome parent2, Random random) {

        int splitIndex = random.nextInt(parent1.getSize());
        Chromosome copy = parent1.copy();

        for (int i = splitIndex; i < parent1.getSize(); i++) {
            int value = parent1.getSolution().get(i);
            if (random.nextDouble() < CROSSFACTOR) {
                value = parent2.getSolution().get(i);
            }
            copy.getSolution().set(i, value);
        }

        return copy;
    }

    public boolean isConvergedOrMetMaxIterations() {
        return convergedOrMetMaxIterations;
    }

    public int size() {
        return generation.size();
    }

    public void addOneFrom(Village toRemove) {

        if (toRemove.size() == 0) {
            return;
        }

        generation.add(toRemove.getChromosomeAtIndex(0));

    }

    private Chromosome getChromosomeAtIndex(int i) {
        return generation.get(i);
    }

    public List<Chromosome> generateInitialPopulation(Integer sizeOfPopulation, int chromosomeSize) {

        List<Chromosome> chromosomes = new ArrayList<>();

        for (int i = 0; i < sizeOfPopulation; i++) {
            chromosomes.add(Chromosome.generateRandom(chromosomeSize, random));
        }
        return chromosomes;
    }

    @Override
    public Village call() throws Exception {
        run();
        return null;
    }
}