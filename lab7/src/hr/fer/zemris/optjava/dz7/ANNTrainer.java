package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.Algorithm.ClonAlg;
import hr.fer.zemris.optjava.dz7.Algorithm.IAlgorithm;
import hr.fer.zemris.optjava.dz7.Algorithm.PSOAlgorithm;
import hr.fer.zemris.optjava.dz7.Data.*;
import hr.fer.zemris.optjava.dz7.Data.Model.IrisModel;
import hr.fer.zemris.optjava.dz7.Data.Model.Solution;
import hr.fer.zemris.optjava.dz7.NeuralNetwork.Functions.ITransferFunction;
import hr.fer.zemris.optjava.dz7.NeuralNetwork.Functions.SigmoidFunction;
import hr.fer.zemris.optjava.dz7.NeuralNetwork.Functions.StepFunction;
import hr.fer.zemris.optjava.dz7.NeuralNetwork.FFANN;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.util.List;

/**
 * Particle swarm optimization algorithm implementation (PSO Algorithm).
 * And a Clonal Selection Algorithm implementation.
 * Cost function is defined via a Artificial neural network implementation where
 * instead backpropagation - optimization alorithms are used.
 * Dataset used is the Iris dataset (http://en.wikipedia.org/wiki/Iris_flower_data_set)
 *
 * PSO with neighbour size 24(12*2):
 *      data/07-iris-formatirano.data pso-a-12 50 0.12 400
 *
 * Clonal Selection Algorithm (ClonAlg)
 *      data/07-iris-formatirano.data clonalg 50 0.12 400
 *
 * @author Filip Saina
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

        Data data = new Data(filePath);

        List<IrisModel> trainingModels = null;

        try {
            trainingModels = data.loadIrisData();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading iris data model!");
            System.exit(-1);
        }

        FFANN ffann = new FFANN(
                new int[]{4, 5, 3, 3},
                new ITransferFunction[]{
                        new SigmoidFunction(),
                        new SigmoidFunction(),
                        new SigmoidFunction(),
                },
                new StepFunction()
        );

        IAlgorithm algorithm = determineAlgorithmFromParameters(
                algorithmString.trim(),
                populationSize,
                maxError,
                maxIterations,
                ffann,
                data,
                trainingModels);

        System.out.println("=== TRAINING ===\n");
        algorithm.run();

        System.out.println("\n\n=== EVALUATION ===\n");
        printSolution(trainingModels, data, ffann);
    }

    private static void printSolution(List<IrisModel> trainingModels, Data data, FFANN ffann) {
        int correct = 0;

        for (IrisModel model : trainingModels) {
            RealVector prediction = ffann.calcOutputs(model.getInputVector(), Solution.globalBestPosition, false);
            //System.out.println(prediction  +"   " + model.getOutputVector());
            if (prediction.equals(model.getOutputVector())) {
                correct++;
            }
        }

        System.out.println("Correct classifications: " + correct);
        System.out.println("Incorrect classifications: " + (data.getNumberOfElements() - correct));
    }

    private static IAlgorithm determineAlgorithmFromParameters(String algorithmString, int populationSize, double maxError, int maxIterations, FFANN ffann, Data data, List<IrisModel> trainingModels) {

        if (algorithmString.toLowerCase().startsWith("pso")) {

            String[] split = algorithmString.split("-");
            if (split.length == 2) {
                //a variant
                return new PSOAlgorithm(
                        populationSize,
                        populationSize / 2,
                        maxIterations,
                        maxError,
                        ffann,
                        data);

            } else if (split.length == 3) {
                //b
                Integer nSize = Integer.parseInt(split[2]);

                if (2 * nSize > populationSize) {
                    System.err.println("Neighboorhood size must be at most (populationSize / 2)");
                    System.exit(-1);
                }

                return new PSOAlgorithm(
                        populationSize,
                        nSize,
                        maxIterations,
                        maxError,
                        ffann,
                        data
                );
            } else {
                System.err.println("Illegat pso argument format");
                System.exit(-2);
            }


        } else if (algorithmString.toLowerCase().startsWith("clonalg")) {
            return new ClonAlg(
                    ffann,
                    populationSize,
                    maxIterations,
                    maxError,
                    data
            );

        } else {
            System.err.println("Undefined algorithm provided to execute! -> " + algorithmString);
            System.exit(-1);
        }

        throw new IllegalStateException("Undefined execution state");
    }
}
