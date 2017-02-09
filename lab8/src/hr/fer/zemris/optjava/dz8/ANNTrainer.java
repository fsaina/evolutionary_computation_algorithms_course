package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.Algorithm.DEA;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.BaseVector.BestBaseVector;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Cross.BinaryCross;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Diff.TargetToBest;
import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Selection.SelectionFunction;
import hr.fer.zemris.optjava.dz8.Algorithm.IAlgorithm;
import hr.fer.zemris.optjava.dz8.Algorithm.Unit;
import hr.fer.zemris.optjava.dz8.Data.Data;
import hr.fer.zemris.optjava.dz8.Data.Model.LaserModel;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.EFFANN;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.FFANN;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.Functions.ITransferFunction;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.Functions.StepFunction;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.Functions.TanhFunction;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.INeuralNetwork;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.util.List;

/**
 * Differentiation evolution algorithm implementation.
 * Implemented ELMAN and TDNN algorithms. Example runs:
 *
 * data/08-Laser-generated-data.txt tdnn-5x10x1 200 0.02 40
 *
 * data/08-Laser-generated-data.txt elman-1x12x1 200 0.02 40
 */
public class ANNTrainer {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Wrong number of arguments " +
                    "provided");
            System.exit(-1);
        }

        String filePath = args[0];
        String algorithmString = args[1];
        int populationSize = Integer.parseInt(args[2]);
        double maxError = Double.parseDouble(args[3]);
        int maxIterations = Integer.parseInt(args[4]);

        String nnArhitecture = algorithmString.split("\\-")[1];
        String[] numbers = nnArhitecture.split("x");

        int[] dim = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            dim[i] = Integer.parseInt(numbers[i]);
        }

        ITransferFunction[] field = new ITransferFunction[numbers.length - 1];
        for (int i = 0; i < numbers.length - 1; i++) {
            field[i] = new TanhFunction();
        }

        INeuralNetwork ffann;
        if (algorithmString.toLowerCase().startsWith("tdnn")) {
            ffann = new FFANN(
                    dim,
                    field,
                    new StepFunction()
            );
        } else {
             ffann = new EFFANN(
                    dim,
                    field,
                    new StepFunction()
            );
        }

        if (dim[dim.length - 1] != 1) {
            System.err.println("Invalid neural network arhitecture");
            System.exit(-1);
        }

        Data data = new Data(filePath, dim[0], 800);

        List<LaserModel> trainingModels = null;

        try {
            trainingModels = data.loadIrisData();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading iris data model!");
            System.exit(-1);
        }

        IAlgorithm algorithm =  new DEA(
                populationSize,
                maxIterations,
                maxError,
                new BestBaseVector(),
                new TargetToBest(),
                new BinaryCross(),
                new SelectionFunction(),
                data,
                ffann
        );

        System.out.println("=== TRAINING ===\n");
        algorithm.run();

        System.out.println("\nBest unit cost function :\n");
        System.out.println(Unit.globalBestScore);

        System.out.println("\n\n=== EVALUATION ===\n");
        printSolution(data, ffann);
    }

    private static void printSolution( Data data, INeuralNetwork ffann) {
        int correct = 0;
        for (LaserModel model : data.getTestSet()) {
            RealVector prediction = ffann.calcOutputs(model.getInputVector(), Unit.best, true);

            double p1 = data.fromNormalized(prediction.getEntry(0));
            double p2 = data.fromNormalized(model.getOutputVector().getEntry(0));

            if (Math.abs(p1 - p2) < 1) {
                correct++;
            }
        }

        System.out.println("Correct predictions: " + correct);
        System.out.println("Incorrect predictions: " + (data.getTestSet().size() - correct));
    }
}
