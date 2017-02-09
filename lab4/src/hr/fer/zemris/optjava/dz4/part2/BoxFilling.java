package hr.fer.zemris.optjava.dz4.part2;

import hr.fer.zemris.optjava.dz4.part2.algorithms.GeneticAlgorithmBox;
import hr.fer.zemris.optjava.dz4.part2.functions.BoxFillFunction;
import hr.fer.zemris.optjava.dz4.part2.selectors.IBoxSelector;
import hr.fer.zemris.optjava.dz4.part2.selectors.NTurnamentSelector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Genetic algorithm for solving the box filling problem. Box filling problem consists
 * of finding a solution that maximises free space in a container. A container has a fixed
 * height(20) and fixed width(number of "sicks"). Each stick whose length is represented with a
 * number in the data/kutije/* .dat files is sorted trough a "List matrix" which in turn represents
 * a single solution.
 *
 * What arguments to add to run the application, example:
 * data/kutije/problem-20-50-5.dat 20 20 60 true 1000000 50 10     //takes time
 *
 * or simple
 * data/kutije/problem-20-10-1.dat 20 20 60 true 1000000 10 10
 *
 *
 * (1) - path to the file
 * (2) - size of the population
 * (3) - Parent selection n-turnament size
 * (4) - Worst chromosome m-turnament selection
 * (5) - Uncoditional replacement of the worst element with the generated child
 * (6) - Maximum number of iterations
 * (7) - Acceptable number of free spaces that is "good enough"
 * (8) - Number of mutations to be made to a child
 *
 * @author Filip Saina
 */
public class BoxFilling {

    public static void main(String[] args) {

        if (args.length != 8) {
            System.err.println("Wrong number of arguments provided:");
            System.exit(-1);
        }

        String pathToFiles = args[0];
        int populationSize = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        Boolean p = Boolean.parseBoolean(args[4]);
        int maxNumberOfIterations = Integer.parseInt(args[5]);
        int acceptableContainerSize = Integer.parseInt(args[6]);
        int numberOfMutations = Integer.parseInt(args[7]);

        IBoxSelector selector = new NTurnamentSelector();

        List<Integer> sticks = readAFileFromPath(Paths.get(pathToFiles));
        int sum = 0;
        for(Integer i : sticks){
            sum += i;
        }

        BoxFillFunction function = new BoxFillFunction(sum);
        GeneticAlgorithmBox geneticAlgorithmBox = new GeneticAlgorithmBox(
                populationSize,
                acceptableContainerSize,
                maxNumberOfIterations,
                selector,
                function,
                n,
                m,
                sticks,
                p,
                numberOfMutations,
                sticks.size()+1
        );

        geneticAlgorithmBox.run();

    }

    private static List<Integer> readAFileFromPath(Path path) {

        List<String> lines = null;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        List<Integer> sticks = new ArrayList<>();

        for (String line : lines) {

            if (line.isEmpty()) {
                continue;
            }
            line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));

            for (String number : line.split(",")) {
                sticks.add(Integer.parseInt(number.trim()));
            }
        }
        return sticks;
    }
}
