package hr.fer.zemris.optjav.dz4.part1;

import hr.fer.zemris.optjav.dz4.part1.algorithms.GeneticAlgorithm;
import hr.fer.zemris.optjav.dz4.part1.functions.IHFunction;
import hr.fer.zemris.optjav.dz4.part1.functions.PrijenosnaCostFunction;
import hr.fer.zemris.optjav.dz4.part1.selector.ISelector;
import hr.fer.zemris.optjav.dz4.part1.selector.RouletteSelector;
import hr.fer.zemris.optjav.dz4.part1.selector.TournamentSelection;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Genetic algorithm for solving the equation given in the 2nd homework.
 * Example attributes to run:
 *
 * 12 3000 1000000 tournament:5 0.55
 *
 * (1) - population size
 * (2) - minimal acceptable error
 * (3) - maximum number of generations
 * (4) - selection - either 'rouletteWheel' or 'tournament:n'
 * (5) - sigma used for gaussian distribution for mutations
 */
public class GeneticAlgorithmMain {


    public static void main(String[] args) {

        if (args.length != 5) {
            System.err.println("Wrong number of arguments provided");
            System.exit(-1);
        }

        int populationSize = Integer.parseInt(args[0]);
        int minErr = Integer.parseInt(args[1]);
        int maxNumberOfGenerations = Integer.parseInt(args[2]);

        String selection = args[3].toLowerCase();

        double sigma = Double.parseDouble(args[4]);

        IHFunction costFunction = null;
        try {
            costFunction = loadFunctionDataFromFile(Paths.get("data/zad-prijenosna.txt"), 20);
        } catch (IOException e) {
            System.err.println("Error reading the input file");
            System.exit(-1);
        }

        ISelector function = null;

        if(selection.equals("roulettewheel")) {
            function = new RouletteSelector();
        } else if(selection.startsWith("tournament")){

            String[] split = selection.split(":");
            int num = Integer.parseInt(split[1]);

            function = new TournamentSelection(num);

        } else {
            System.err.println("Wrong method name provieded: please specify either " +
                    "rouletterWheel or turnament:n");
        }

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                populationSize,
                minErr,
                maxNumberOfGenerations,
                sigma,
                function,
                costFunction
        );


        geneticAlgorithm.run();
    }


    public static IHFunction loadFunctionDataFromFile(Path path, int entiesNumber) throws IOException {

        List<String> lines = Files.readAllLines(path);
        int numberOfLines = 0;
        int numberOfVariables = 0;
        RealVector solutions = new ArrayRealVector(entiesNumber);
        ArrayList<ArrayList<Double>> constants = new ArrayList<>();

        for (String line : lines) {

            if (line.startsWith("#"))
                continue;

            //removing paretahesis
            String data = line.substring(1, line.length() - 1);

            ArrayList<Double> constantsList = new ArrayList<>();

            String[] splitList = data.split(",");
            numberOfVariables = splitList.length - 1;
            for (String value : splitList) {
                constantsList.add(Double.parseDouble(value));
            }

            Double solutionToEquation = constantsList.get(constantsList.size() - 1);
            constantsList.remove(constantsList.size() - 1);

            constants.add(constantsList);
            solutions.setEntry(numberOfLines, solutionToEquation);

            numberOfLines++;
        }

        RealMatrix constMatrix = new Array2DRowRealMatrix(numberOfLines, numberOfVariables);

        for (int i = 0; i < numberOfLines; i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                constMatrix.setEntry(i, j, constants.get(i).get(j));
            }
        }


        return new PrijenosnaCostFunction(constMatrix, solutions);
    }
}
