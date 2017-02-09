package hr.fer.zemris.ropaeruj.iter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * TriSatSolver tests and solves valid PRODUCT OF SUMS(POS) expressions.
 * It contains implementations:
 * cnf - solves ALL possible solutions from a given POS problem by testing the problem on all
 * solutions. This is done in big Oh n^2 time
 * <p>
 * alg2 - an greedy iterative algorithm whose nearest neighbor functions is defined as a single bit
 * difference.
 * <p>
 * alg3 - greedy iterative algorithm with statistical counting of elements
 *
 * @Author Filip Saina
 * @JMBAG 0036479300
 */

/**
 *   BITNO ZA ISPRAVLJACA - Svi algoritmi rade i implementirani su korektno ali nisu implementirani po
 *   danom templateu na zadnjoj stranici zadace(razlog: rjesavao sam sve zadatak po zadatak sve u komadu bez
 *   da sam procitao cijeli dokument prvi puta pa nisam ocekivao da postoji programska forma koje se trebamo
 *   pridrzavati vec samo problemska. Kako je ispalo imam sve klasne apstrakcije kao sto je predlozeno ali
 *   nisu implementirane na dani nacin).
 *   Pripazit cu na ovo ubuduce.
 */

public class TriSatSolver {

    //SAT expression abstraction loaded from file
    private static SATFormula expression;

    /**
     * Application entry point
     * @param args 2 arguments provided to the program - algorithm use and path to problem definition
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong number of arguments provide, please specify the index of the wanter" +
                    "algorithm and the path to the file defining the problem");
            System.exit(-1);
        }

        Path problemPath = Paths.get(args[1]);
        String problem = args[0].trim();

        try {
            loadData(problemPath);

        } catch (Exception e) {
          
            System.err.println("Error while reading the input file: " + problemPath);
            System.exit(-1);
        }

        Algorithm algorithm = null;

        switch (problem) {

            case "1": {
                algorithm = new CnfAlgorithm(expression);
                break;
            }
            case "2": {
                algorithm = new IterativeSearchAlgorithm(expression);
                break;
            }
            case "3": {
                algorithm = new PercentageIterativeSearchAlgorithm(expression);
                break;
            }
            default: {
                System.err.println("Wrong algorithm parameter provided, please choose" +
                        "between:  1 2 3");
            }
        }
        algorithm.run();

    }

    /**
     * Method loads data to the SATFormula abstraction class
     *
     * @param problemPath Path to the file to be loaded
     * @throws IOException Thrown if the file could not be read
     */
    private static void loadData(Path problemPath) throws IOException {

        List<String> lines = Files.readAllLines(problemPath);


        for (String line : lines) {

            String[] lineSplit = line.trim().replaceAll("\\s+", " ").split(" ");

            if (line.startsWith("c")) {
                continue;
            } else if (line.startsWith("p")) {
                int numberOfVariables = Integer.parseInt(lineSplit[2]);
                int numberOfClauses = Integer.parseInt(lineSplit[3]);

                expression = new SATFormula(numberOfVariables, numberOfClauses);

            } else if (line.startsWith("%")) {
                break;
            } else {
                //read clause
                String initialString = new String(new char[expression.getNumberOfVariablesInASingleClause()])
                        .replace('\0', 'x');
                char[] clauseArray = initialString.toCharArray();

                for (int i = 0; i < lineSplit.length - 1; i++) {

                    int integerValue = Integer.parseInt(lineSplit[i]);
                    if (integerValue > 0) {

                        clauseArray[integerValue - 1] = '1';
                    } else {
                        clauseArray[Math.abs(integerValue) - 1] = '0';
                    }
                }

                Clause clause = new Clause(new String(clauseArray));
                expression.addClause(clause);
            }
        }
    }
}