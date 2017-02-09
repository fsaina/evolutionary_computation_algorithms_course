package hr.fer.zemris.optjava.dz10;


import hr.fer.zemris.optjava.dz10.algotithm.functions.FitnessFun;
import hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces.IMoopOptimizationFunction;
import hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2.GroupingSelection;
import hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2.NSGA2;
import hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2.NSGASolution;
import hr.fer.zemris.optjava.dz10.problems.MOOPProblem;
import hr.fer.zemris.optjava.dz10.problems.MOOPProb1;
import hr.fer.zemris.optjava.dz10.problems.MOOPProb2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

/**
 * NSGA-II algorithm implementation, example run:
 *
 *  1 30 300 0.1
 *
 *  last value being the mutation sigma
 */

public class MOOP {
	public static final double BLX_Constant = 0.5;
	public static final double MUTATION_VALUE = 0.25;
	public static final double ALPHA = 2;
	public static final double EPSILON = 1E-15;

	//problem 1
	public static final double LOWER_PROB1 = -5;
	public static final double UPPER_PROB1 = 5;

	//problem 2
	public static final double LOWER_PROB2_X1 = 0.1;
	public static final double LOWER_PROB2_X2 = 0;
	public static final double UPPER_PROB2_X1 = 1;
	public static final double UPPER_PROB2_X2 = 5;

	public static final int PROBLEM1_DOMAIN = 4;
	public static final int PROBLEM2_DOMAIN = 2;

	public static final int CONTENTERSNUM = 4;

	public static final String IZLAZ_DEC_TXT = "izlaz-dec.txt";
	public static final String IZLAZ_OBJ_TXT = "izlaz.obj.txt";

	public static void main(String[] args) {
		if(args.length!=4){
			System.out.println("Invalid number of arguments -> 4 required");
			System.exit(-1);
		}

		int problemNumber = Integer.parseInt(args[0]);
		int sizeOfPop = Integer.parseInt(args[1]);
		int maxIter = Integer.parseInt(args[2]);
		double sigma = Double.parseDouble(args[3]);

		IMoopOptimizationFunction function = new FitnessFun(problemType(problemNumber), true);

		NSGA2 algorithm = new NSGA2(sizeOfPop, maxIter, new GroupingSelection(new Random(System.currentTimeMillis()), CONTENTERSNUM, function.getMOOPProblem().getObjectiveMin(), function.getMOOPProblem().getObjectiveMax()), sigma, function);
		algorithm.run();
		savePointsToFile(algorithm);
	}

	private static void savePointsToFile(NSGA2 alg){
		BufferedWriter outDec = null;
		BufferedWriter outObj = null;

		try {
			FileWriter decStream = new FileWriter(IZLAZ_DEC_TXT, false);
			FileWriter objStream = new FileWriter(IZLAZ_OBJ_TXT, false);

			outDec = new BufferedWriter(decStream);
			outObj = new BufferedWriter(objStream);

			LinkedList<LinkedList<NSGASolution>> paretoFronts = alg.getFronts();

			int frontNum = 1;
			for (LinkedList<NSGASolution> front : paretoFronts) {
                frontNum++;
				System.out.println("number of front: "+frontNum +" has " + front.size() + "  elements");
				for (NSGASolution solution : front) {
					outDec.write(solution.valToString() + "\n");
					outObj.write(solution.objToString() + "\n");
				}
				outDec.write("\n");
				outObj.write("\n");
			}
		} catch (IOException ignorable) {
		} finally {
			if (outDec != null) {
				try {
					outDec.close();
				} catch (IOException e) {
				}
			}
			if (outObj != null) {
				try {
					outObj.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static MOOPProblem problemType(int label) {
		if (label == 1) {
			double[] prob1Min = new double[PROBLEM1_DOMAIN];
			double[] prob1Max = new double[PROBLEM1_DOMAIN];
			for (int i = 0; i < prob1Min.length; i++) {
				prob1Max[i] = UPPER_PROB1;
				prob1Min[i] = LOWER_PROB1;
			}
			return new MOOPProb1(PROBLEM1_DOMAIN, prob1Min, prob1Max);
		} else if (label == 2) {
			double[] prob2Min = new double[] {LOWER_PROB2_X1, LOWER_PROB2_X2};
			double[] prob2Max = new double[] {UPPER_PROB2_X1, UPPER_PROB2_X2};
			return new MOOPProb2(PROBLEM2_DOMAIN, prob2Min, prob2Max);
		} else
			throw new IllegalArgumentException("Error reading labels");
	}
}
