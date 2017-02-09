package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.algorithms.SimulatedAnnealing;
import hr.fer.zemris.optjava.dz3.decoder.DecimalDecoder;
import hr.fer.zemris.optjava.dz3.decoder.IDecoder;
import hr.fer.zemris.optjava.dz3.decoder.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.functions.IFunction;
import hr.fer.zemris.optjava.dz3.functions.PrijenosnaCostFunction;
import hr.fer.zemris.optjava.dz3.neighbourhood.GaussDecimalNeighbourhood;
import hr.fer.zemris.optjava.dz3.neighbourhood.INeighbourhood;
import hr.fer.zemris.optjava.dz3.neighbourhood.UniformDistributionBitVectorNeighborhood;
import hr.fer.zemris.optjava.dz3.representations.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.representations.DecimalSolution;
import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;
import hr.fer.zemris.optjava.dz3.temperature.GeometricTempSchedule;
import hr.fer.zemris.optjava.dz3.temperature.ITempSchedule;
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
import java.util.Random;

/**
 * Simulated annealing program implementation both in binary and decimal number representation.
 * Algorithm uses fixed values as alpha, initial temperature, number of iterations.
 * <p>
 * To run the example pleas provide two arguments:
 * (1) - path to the input file      e.g. "data/zad-prijenosna.txt"
 * (2) - method to be used           e.g. "decimal"
 * <p>
 * Currently there are 2 methods implemented. Choose between them with "binary:X" (X representing
 * the number of bits per variable) or "decimal"
 *
 * @author Filip Saina
 */
public class RegresijaSustava {

    private static final Integer INITIAL_TEMPERATURE = 10000;
    private static final Integer OUTER_LOOP = 10000;
    private static final Integer INNER_LOOP = 10000;
    private static final Double ALPHA_VALUE = 0.95;  //choose between 0.66 and 0.99

    private static final Integer NUMBER_OF_EQUATIONS = 20;
    private static final Integer NUMBER_OF_VARIABLES = 6;

    //binary minimum and maximum values that will be tested with the binary method
    private static final Integer BINARY_MIN_VALUE = -10;
    private static final Integer BINARY_MAX_VALUE = 10;

    //decimal minimum and maximum values for the decimal method
    private static final Integer DECIMAL_MIN_VALUE = -50;
    private static final Integer DECIMAL_MAX_VALUE = 50;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Wrong number of arguments provided");
            System.exit(-1);
        }

        String path = args[0];
        String method = args[1];

        IDecoder decoder = null;
        INeighbourhood neighbourhood = null;
        SingleObjectiveSolution initialValue = null;

        if (method.toLowerCase().startsWith("binary")) {
            int nBits = Integer.parseInt(method.substring(method.indexOf(':') + 1, method.length()));

            if(nBits > 30 || nBits < 6){
                System.err.println("Sorry, please define a greater number of bits than " + nBits);
                System.exit(-1);
            }

            double[] mins = new double[NUMBER_OF_VARIABLES];  //minimum value per variable
            double[] maxs = new double[NUMBER_OF_VARIABLES];        //maximum value per variable
            int[] bits = new int[NUMBER_OF_VARIABLES];

            for (int i = 0; i < NUMBER_OF_VARIABLES; i++) {
                bits[i] = nBits;
                mins[i] = BINARY_MIN_VALUE;
                maxs[i] = BINARY_MAX_VALUE;
            }

            decoder = new NaturalBinaryDecoder(
                    mins,
                    maxs,
                    bits,
                    NUMBER_OF_VARIABLES);

            neighbourhood = new UniformDistributionBitVectorNeighborhood();
            initialValue = new BitVectorSolution(bits).randomize(new Random());

        } else if (method.toLowerCase().startsWith("decimal")) {

            decoder = new DecimalDecoder();
            neighbourhood = new GaussDecimalNeighbourhood();
            initialValue = new DecimalSolution(
                    DECIMAL_MIN_VALUE,
                    DECIMAL_MAX_VALUE,
                    NUMBER_OF_VARIABLES);

        } else {
            System.err.println("Method to run the application is not supported! " +
                    "method: " + method);
            System.exit(-1);
        }

        IFunction function = null;
        try {
            function = loadFunctionDataFromFile(Paths.get(path), NUMBER_OF_EQUATIONS);
        } catch (IOException e) {
            System.err.println("Error reading the input file!");
            System.exit(-1);
        }

        ITempSchedule iTempSchedule = new GeometricTempSchedule(
                ALPHA_VALUE,
                INITIAL_TEMPERATURE,
                INNER_LOOP,
                OUTER_LOOP);

        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(decoder,
                neighbourhood,
                function,
                true,
                iTempSchedule,
                initialValue);

        //run the algorithm
        initialValue = simulatedAnnealing.run();

        double[] solutions = decoder.decode(initialValue);
        printTheResult(decoder, initialValue, function, solutions);
    }

    private static void printTheResult(IDecoder decoder, SingleObjectiveSolution initialValue, IFunction function, double[] solutions) {
        System.out.println();
        System.out.println("Points for [a..f]:");
        for (double d : solutions) {
            System.out.println(d);
        }

        System.out.println();
        System.out.println("Cost function value for the given point:");
        System.out.println(function.valueAt(decoder.decode(initialValue)));
    }

    public static IFunction loadFunctionDataFromFile(Path path, int entiesNumber) throws IOException {

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

        IFunction function = new PrijenosnaCostFunction(constMatrix, solutions);
        return function;
    }
}
