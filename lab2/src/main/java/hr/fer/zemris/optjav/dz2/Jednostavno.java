package hr.fer.zemris.optjav.dz2;

import hr.fer.zemris.optjav.dz2.functions.Function1;
import hr.fer.zemris.optjav.dz2.functions.Function2;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Random;

/**
 * Simple application implementation of the search in given direction algorithms by
 * implementing:
 *              (1) Gradient descent method
 *              (2) Newton method
 * with the bisection algorithm.
 *
 * INFO: Input data are in data/* from the root of the project. Graphical output can
 * be skipped by providing gradientDescent() or newtonMethod() a false 4. argument
 *
 * @author Filip Saina
 */
public class Jednostavno {

    public static void main(String[] args) {

        Double pointFirst;
        Double pointSecond;
        ArrayList<Double> initialPoints = new ArrayList<>();

        if (!(args.length == 2 || args.length == 4)) {
            System.err.println("Invalid number of argumnets given: " + args.length);
            System.exit(-1);
        }

        String taskString = args[0].trim();
        Integer maximumNumberOfIterations = Integer.parseInt(args[1]);

        generateRandomPointsInVector(initialPoints, 2);

        if (args.length == 4) {
            initialPoints.clear();
            pointFirst = Double.parseDouble(args[2]);
            pointSecond = Double.parseDouble(args[3]);

            initialPoints.add(pointFirst);
            initialPoints.add(pointSecond);
        }

        RealVector result = null;

        switch (taskString) {

            case "1a": {
                result = NumOptAlgorithms.gradientDescent(
                        new Function1(),
                        maximumNumberOfIterations,
                        initialPoints,
                        true);
                break;
            }
            case "1b": {
                result = NumOptAlgorithms.newtonMethod(
                        new Function1(),
                        maximumNumberOfIterations,
                        initialPoints,
                        true);
                break;
            }
            case "2a": {
                result = NumOptAlgorithms.gradientDescent(
                        new Function2(),
                        maximumNumberOfIterations,
                        initialPoints,
                        true);
                break;
            }
            case "2b": {
                result = NumOptAlgorithms.newtonMethod(
                        new Function2(),
                        maximumNumberOfIterations,
                        initialPoints,
                        true
                );
                break;
            }
        }

        System.out.println("Final result: " + result);
    }

    public static void generateRandomPointsInVector(ArrayList<Double> initialPoints, int size) {
        //generate random initial points
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            initialPoints.add((random.nextDouble()*100) % 11 - 5);
        }
    }
}
