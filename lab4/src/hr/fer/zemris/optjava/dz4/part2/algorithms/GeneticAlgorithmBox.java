package hr.fer.zemris.optjava.dz4.part2.algorithms;

import hr.fer.zemris.optjava.dz4.part2.chromosomes.BoxFillElement;
import hr.fer.zemris.optjava.dz4.part2.functions.BoxFillFunction;
import hr.fer.zemris.optjava.dz4.part2.selectors.IBoxSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public class GeneticAlgorithmBox {

    private int populationSize;
    private int minError;
    private int maxGenerations;
    private IBoxSelector selector;
    private BoxFillFunction costFunction;
    private int width;
    private int height = 20;
    private boolean change;
    private int numberOfMutations;
    private int n;
    private int m;

    private List<Integer> sticks;

    public GeneticAlgorithmBox(int populationSize, int minError, int maxGenerations, IBoxSelector selector,
                               BoxFillFunction costFunction, int n, int m, List<Integer> sticks,
                               boolean change, int numberOfMutations, int width) {
        this.populationSize = populationSize;
        this.minError = minError;
        this.maxGenerations = maxGenerations;
        this.selector = selector;
        this.costFunction = costFunction;
        this.width = width;
        this.sticks = sticks;
        this.change = change;
        this.numberOfMutations = numberOfMutations;
        this.n = n;
        this.m = m;
    }

    public void run() {

        List<BoxFillElement> population = generateInitialPopulation(sticks,
                width,
                height,
                populationSize);

        evaluate(population);

        Random random = new Random();
        BoxFillElement best = null;

        int generation = 1;
        do {

            List<BoxFillElement> selected = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                selected.add(selector.select(population, n, true));
            }
            BoxFillElement crossed = cross(selected, random);
            BoxFillElement mutated = mutate(crossed, random);

            evaluate(population);

            BoxFillElement worst = selector.select(population, m, false);

            if (change) {
                if (worst.getScore() < mutated.getScore()) {
                    population.remove(worst);
                    population.add(mutated);
                }
            } else {
                population.remove(worst);
                population.add(mutated);
            }

            System.out.println("Current best score: " + population.get(0).getScore());
            best = population.get(0);

            generation++;
        } while (generation < maxGenerations && !(population.get(0).getScore() > minError));


        System.out.println(best);

        int column = 1;
        for (List<Integer> columns : best.getSolution()) {
            System.out.print("column: " + column + " | ");
            for (Integer i : columns) {

                System.out.print(i + " ");

            }

            column++;
            System.out.println();
        }

        if (generation >= maxGenerations) {
            System.out.println("DID NOT FIND OPTIMAL SOLUTION, PASSED THE ITERATION COUNT");
        } else {
            System.out.println("Finished with correct solution!");
        }
    }

    /**
     * Mutation is made by getting some element in the child-s column and row,
     * and replacing it with some another element in the child-s column and row.
     * They are both determined random. That is repeated 'numberOfMutaton' times.
     */
    private BoxFillElement mutate(BoxFillElement crossed, Random random) {

        List<List<Integer>> solution = crossed.getSolution();

        //20 is the number of mutations
        for (int i = 0; i < numberOfMutations; i++) {

            int column = random.nextInt(solution.size());
            int column2 = random.nextInt(solution.size());

            List<Integer> col1 = solution.get(column);
            List<Integer> col2 = solution.get(column2);

            if (col1.size() == 0 || col2.size() == 0) {
                continue;
            }

            int row1 = random.nextInt(col1.size());
            int row2 = random.nextInt(col2.size());

            int tmp = col1.get(row1);
            col1.set(row1, col2.get(row2));
            col2.set(row2, tmp);

            solution.set(column, col1);
            solution.set(column2, col2);
        }

        return new BoxFillElement(solution);
    }

    /*
     * Cross two parents by containing the most of the first one, with some elements
     * of the other parent
     */
    private BoxFillElement cross(List<BoxFillElement> selected, Random random) {

        if (selected.size() != 2) {
            System.err.println("Wrong number of parents: " + selected.size());
            System.exit(-1);
        }

        List<Integer> sticks_copy = new ArrayList<>(sticks);

        BoxFillElement parent1 = selected.get(0);
        BoxFillElement parent2 = selected.get(1);

        BoxFillElement child = new BoxFillElement(width, height);

        for (int i = 0; i < width; i++) {

            List<Integer> row1 = parent1.getRow(i);
            List<Integer> row2 = parent2.getRow(i);

            double num = random.nextDouble();
            if(num < 0.9){

                child.setRow(i, row1);
            } else {
                child.setRow(i, row2);
            }
        }

        return parent1;

    }

    /**
     * The only metric that is important is the number of free spaces,
     * therefore that is the fitness value sorted by - score.
     */
    private void evaluate(List<BoxFillElement> population) {

        for (BoxFillElement element : population) {
            element.evaluate(costFunction);
        }

        Collections.sort(population);
    }

    private List<BoxFillElement> generateInitialPopulation(List<Integer> sticks, int widthOfContainer,
                                                           int heightOfContainer, int population) {
        //set up the field
        List<BoxFillElement> solution = new ArrayList<>(population);

        Random random = new Random();

        for (int i = 0; i < population; i++) {

            BoxFillElement element = new BoxFillElement(widthOfContainer, heightOfContainer);

            for (Integer stick : sticks) {

                int randomIndex = random.nextInt(widthOfContainer);

                List<Integer> row = element.getSolution().get(randomIndex);
                row.add(stick);
                element.setRow(randomIndex, row);
            }

            solution.add(element);
        }

        return solution;
    }


}
