package hr.fer.zemris.ropaeruj.iter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Greedy iterative algorithm whose nearest neighbor functions is defined as a single bit
 * difference.
 */
public class IterativeSearchAlgorithm implements Algorithm {

    private static final int MAXNUMBEROFITERATIONS = 100000;

    private SATFormula expression;

    public IterativeSearchAlgorithm(SATFormula expression) {
        this.expression = expression;
    }

    public void run() {

        String binarySolution = generateRandomStringFrom("01", expression.getNumberOfVariablesInASingleClause());
        int iteration = 0;

        while (true) {

            List<String> neighbourSolutions = generateNeighbours(binarySolution);
            List<String> bestBinarySolution = findBestFunction(neighbourSolutions.toArray(new String[0]));

            int score = fitnessFunction(bestBinarySolution.toArray(new String[0]));
            if (score < fitnessFunction(binarySolution)) {
                System.out.println("Local optimum hit!");
                System.exit(0);
            }

            if (iteration > MAXNUMBEROFITERATIONS) {
                System.out.println("Maximum number of iterations exceeded!");
                System.exit(0);
            }

            System.out.println("Iteration: " + iteration);
            System.out.println("Solution: " + bestBinarySolution.get(0) + "\n");

            binarySolution = bestBinarySolution.get(new Random().nextInt(bestBinarySolution.size()));
            iteration++;

            if (score == expression.getNumberOfClauses()) {
                System.out.println("Success!");
                break;
            }
        }

        System.out.println("Solution: " + binarySolution);
    }

    private List<String> findBestFunction(String... neighbourSolutions) {
        int max = 0;  //worst score
        List<String> solutions = new ArrayList<>();

        for (String s : neighbourSolutions) {
            int score = expression.numberOfValidClauses(s);

            if (score > max) {
                max = score;
            }
        }

        for (String s : neighbourSolutions) {
            int score = expression.numberOfValidClauses(s);

            if (score == max) {
                solutions.add(s);
            }
        }

        return solutions;
    }

    private int fitnessFunction(String... binarySolution) {

        for (String s : binarySolution) {
            // all elements must have the same fitness value, as they all are
            // equally valid solutions
            return expression.numberOfValidClauses(s);
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
