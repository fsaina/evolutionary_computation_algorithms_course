package hr.fer.zemris.optjava.dz5.part2;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

/**
 * Did not manage to solve it. Always converges on the suboptimal solution.
 *
 * Run sample arguments:
 * data/els19.dat 2000 5
 *
 * @author Filip Saina
 */
public class GeneticAlgortihm {

    private static final int TURNAMENTSIZE = 3 ;

    private DataReader dataReader;
    private int numberOfVillages; //number of runnables
    private int population;
    private int populationPerVillage;

    private List<Village> villages;

    private ISelector selector;
    private IFunction function;
    private Comparator<Chromosome> comparator;

    public GeneticAlgortihm(DataReader dataReader, int numberOfVillages, int population, ISelector selector, IFunction function, Comparator<Chromosome> comparator) {
        this.dataReader = dataReader;
        this.numberOfVillages = numberOfVillages;
        this.population = population;
        this.selector = selector;
        this.function = function;
        this.comparator = comparator;
    }

    private static final int MAXNUMBEROFGENERATIONS = 5;

    public void run() {

        populationPerVillage = (int) (population / (double) numberOfVillages);
        villages = generateInitalVillages(numberOfVillages, populationPerVillage);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        boolean areAllConvergedOrMetmaximumIterations =
                testIfAllConvergentOrMaxIter(villages);

        int currGeneration = 0;

        while (villages.size() != 1) {
            while (!areAllConvergedOrMetmaximumIterations && currGeneration < MAXNUMBEROFGENERATIONS) {

                //run a iteration
                List<Callable<Village>> todo = new ArrayList<>(villages.size());
                for (Village village : villages) {
                    todo.add(village);
                }


                try {
                    List<Future<Village>> list = executorService.invokeAll(todo);
                } catch (InterruptedException e) {
                    //TODO
                    System.err.println("Error!");
                    System.exit(-1);
                }

                areAllConvergedOrMetmaximumIterations =
                        testIfAllConvergentOrMaxIter(villages);

                System.out.println(currGeneration);
                currGeneration++;
            }

            populationPerVillage = (int) (population / (double) villages.size());
            currGeneration = 0;
            villages = joinAdjecentMembersAndReduceTheSizeByOne(populationPerVillage, villages.size() - 1, villages);
            System.out.println("Size: " + villages.size());
        }

        Village bestVillage = villages.get(0);  //the only one left
        System.out.println(bestVillage);
        Chromosome best = bestVillage.bestChromosome();
        System.out.println(best.toString());
        System.out.println(function.getFitness(best));
        //TODO show results

    }

    private List<Village> generateInitalVillages(int numberOfVillages, int populationPerVillage) {

        List<Village> villages = new ArrayList<>();

        for (int i = 0; i < numberOfVillages; i++) {
            Village village = new Village(populationPerVillage,
                    dataReader.getNumberOfVariables(),
                    selector,
                    function,
                    comparator);
            villages.add(village);
        }

        return villages;
    }

    private List<Village> joinAdjecentMembersAndReduceTheSizeByOne(int newSubPopulationSize,
                                                                   int newNumberOfVillages,
                                                                   List<Village> villages) {
        Village toRemove = null;

        for (int i = 0; i < villages.size(); i++) {
            if ((villages.get(i).isConvergedOrMetMaxIterations())) {
                toRemove = villages.get(i);
                villages.remove(i);
                break;
            }
        }

        if(toRemove == null)
            return villages;

        for (Village village : villages) {

            while (village.size() < newSubPopulationSize) {
                village.addOneFrom(toRemove);
            }
        }

        return villages;
    }

    private boolean testIfAllConvergentOrMaxIter(List<Village> villages) {
        for (Village village : villages) {
            if (!(village.isConvergedOrMetMaxIterations())) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Sorry, wrong number of arguemnts provided");
            System.exit(-1);
        }

        Path pathToFile = Paths.get(args[0]);
        Integer n = Integer.parseInt(args[1]);
        Integer subPopulations = Integer.parseInt(args[2]);

        DataReader dataReader = null;
        try {
            dataReader = new DataReader(pathToFile, 19);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while reading");
            System.exit(-1);
        }

        IFunction function = new QuadratticAssignment(dataReader);
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

        ISelector selector = new TurnamentSelector(
                new Random(),
                comparator,
                TURNAMENTSIZE);

        GeneticAlgortihm geneticAlgortihm = new GeneticAlgortihm(
                dataReader,
                subPopulations,
                n,
                selector,
                function,
                comparator
        );

        geneticAlgortihm.run();
    }

}
