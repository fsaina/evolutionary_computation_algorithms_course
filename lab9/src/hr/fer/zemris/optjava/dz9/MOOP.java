package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.algorithm.NSGA;
import hr.fer.zemris.optjava.dz9.algorithm.crossover.BLXAlphaCrossover;
import hr.fer.zemris.optjava.dz9.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.dz9.algorithm.mutation.Gauss;
import hr.fer.zemris.optjava.dz9.algorithm.mutation.IMutation;
import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;
import hr.fer.zemris.optjava.dz9.problems.MOOPProblem;
import hr.fer.zemris.optjava.dz9.problems.MOOPProb1;
import hr.fer.zemris.optjava.dz9.problems.MOOPProb2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Homework09 problem set solution.
 * Multi-criteria problem optimization using the NSGA algorithm.
 *
 * example arguments:
 *      1 300 decision-space 500
 *
 */
public class MOOP {

    public static final String IZLAZ_DEC_TXT = "izlaz-dec.txt";
    public static final String IZLAZ_OBJ_TXT = "izlaz-obj.txt";

    private static final double ALPHA_BLX = 0.25;
    private static final double SIGMA = 0.3;
    private static final double ALPHA = 2.0;
    private static final double PM = 0.05;
    private static final double SIGMA_MUTUAL = 0.05;

    private static final int P_DIM = 4;
    private static final double P_MIN = -5.0;
    private static final double P_MAX = 5.0;

    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("Invalid number of arguments! 4 required");
            System.exit(-1);
        }

        MOOPProblem problem = null;
        int populationSize = Integer.parseInt(args[1]);
        String problemString = args[0].trim().toLowerCase();
        String type = args[2].trim().toLowerCase();
        int maxIter = Integer.parseInt(args[3]);

        if(problemString.equals("1")){
            problem = new MOOPProb1(P_DIM, P_MIN, P_MAX);
        }else if(problemString.equals("2")){
            problem = new MOOPProb2();
        }else{
            System.out.println("Invalid problem number : " + args[0]);
            System.exit(-1);
        }

        boolean decisionSpaceFS = false;
        if (type.equals("decision-space")) {
            decisionSpaceFS = true;
        } else if (!type.equals("objective-space")) {
            System.out.println("undefined type : " + args[2]);
            System.exit(-1);
        }

        List<ICrossover<MultipleObjectiveSolution>> crossovers = new ArrayList<>();
        IMutation mutation = new Gauss(SIGMA, PM);

        crossovers.add(new BLXAlphaCrossover(ALPHA_BLX));
        NSGA nsga = new NSGA(
                problem,
                populationSize,
                crossovers,
                mutation,
                maxIter,
                decisionSpaceFS,
                ALPHA,
                SIGMA_MUTUAL
        );

        List<List<MultipleObjectiveSolution>> fronts = nsga.run();
        outputSolutions(fronts);
        writeToOutputFiles(problem, fronts);
    }

    private static void outputSolutions(List<List<MultipleObjectiveSolution>> fronts) {
        for (int i = 0; i < fronts.size(); i++) {
            System.out.println("front " + i + " -> " + fronts.get(i).size());
        }
    }

    private static void writeToOutputFiles(MOOPProblem problem, List<List<MultipleObjectiveSolution>> fronts) {
        try {
            PrintWriter dsWriter = new PrintWriter(
                    new BufferedWriter(new FileWriter(IZLAZ_DEC_TXT)));

            PrintWriter osWriter = new PrintWriter(
                    new BufferedWriter(new FileWriter(IZLAZ_OBJ_TXT)));

            for (List<MultipleObjectiveSolution> front : fronts) {
                for (MultipleObjectiveSolution solution : front) {
                    solution.objectives = problem.evaluate(solution.values);

                    dsWriter.write(Arrays.toString(solution.values));
                    dsWriter.write("\n");

                    osWriter.write(Arrays.toString(solution.objectives));
                    osWriter.write("\n");
                }
            }

            dsWriter.flush();
            osWriter.flush();

            osWriter.close();
            dsWriter.close();

        } catch (IOException ex) {
            System.out.println("Writing error to output files!");
            System.exit(-1);
        }
    }
}
