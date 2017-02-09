package hr.fer.zemris.ropaeruj.iter;

import java.util.*;

/**
 * Greedy iterative algorithm with statistical counting of elements
 */
public class PercentageIterativeSearchAlgorithm implements Algorithm {

    private static final int NUMBEROFBEST = 2;
    private static final double PERCENTAGECONSTANTUP = 0.01;
    private static final double PERCENTAGECONSTANTDOWN = 0.1;
    private static final int PERCENTAGEUNITAMOUNT = 50;

    private static final int MAXNUMBEROFITERATIONS = 100000;

    private SATFormula expression;
    private double[] post;

    public PercentageIterativeSearchAlgorithm(SATFormula expression) {
        this.expression = expression;
    }

    public void run() {

        String binarySolution = generateRandomStringFrom("01", expression.getNumberOfVariablesInASingleClause());
        post = new double[expression.getNumberOfClauses()];
        int iteration = 0;

        while (true) {

            incrementForValidClauses(binarySolution);

            List<String> neighbourSolutions = generateNeighbours(binarySolution);
            List<String> bestBinarySolution = findBestFunction(neighbourSolutions.toArray(new String[0]));

            if (iteration > MAXNUMBEROFITERATIONS) {
                System.out.println("Maximum number of iterations exceeded!");
                System.exit(0);
            }

            System.out.println("Iteration: " + iteration);
            System.out.println("Solution: " + bestBinarySolution.get(0) + "\n");

            binarySolution = bestBinarySolution.get(new Random().nextInt(bestBinarySolution.size()));
            iteration++;

            if (expression.evaluateWholeExpression(binarySolution)) {
                System.out.println("Success!");
                break;
            }
        }

        System.out.println("Solution: " + binarySolution);
    }

    private void incrementForValidClauses(String binarySolution) {

        int index = 0;
        for (Clause c : expression.getClauseList()) {
            if (c.evaluateClause(binarySolution)) {
                post[index] += (1 - post[index]) * PERCENTAGECONSTANTUP;
            } else {
                post[index] += (0 - post[index]) * PERCENTAGECONSTANTDOWN;
            }

            index++;
        }
    }

    private List<String> findBestFunction(String... neighbourSolutions) {
        List<String> finalString = new ArrayList<>();
        List<String> neighbourSol = Arrays.asList(neighbourSolutions);
        neighbourSol.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                if (fitnessFunction(s) > fitnessFunction(t1)) {
                    return -1;
                } else if (fitnessFunction(s) < fitnessFunction(t1)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        for (String s : neighbourSol) {
            finalString.add(s);

            if (finalString.size() == NUMBEROFBEST) {
                break;
            }
        }

        return finalString;
    }

    private int fitnessFunction(String... binarySolution) {

        for (String s : binarySolution) {
            // all elements must have the same fitness value, as they all are
            // equally valid solutions
            int Z = expression.numberOfValidClauses(s);

            int index = 0;
            for (Clause c : expression.getClauseList()) {
                if (c.evaluateClause(s)) {
                    Z += PERCENTAGEUNITAMOUNT * (1 - post[index]);
                } else {
                    Z -= PERCENTAGEUNITAMOUNT * (1 - post[index]);
                }

                index++;
            }

            return Z;
        }

        return 0;
    }

    private List<String> generateNeighbours(String... binarySolution) {

        List<String> neighbours = new ArrayList<>();

        for (String s : binarySolution) {

            neighbours.addAll(shiftFunctionPI(s));

        }

        return neighbours;

    }

    private List<String> shiftFunctionPI(String s) {

        List<String> singleChangeList = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {

            char[] tmp = s.toCharArray();

            if (tmp[i] == '0') {
                tmp[i] = '1';
            } else {
                tmp[i] = '0';
            }

            singleChangeList.add(new String(tmp));
        }

        return singleChangeList;
    }

    private String generateRandomStringFrom(String s, int numberOfVariablesInASingleClause) {

        Random rand = new Random();
        StringBuilder returnString = new StringBuilder();

        for (int i = 0; i < numberOfVariablesInASingleClause; i++) {
            int randomIndex = rand.nextInt(s.length());

            returnString.append(s.charAt(randomIndex));
        }

        return returnString.toString();
    }

}
