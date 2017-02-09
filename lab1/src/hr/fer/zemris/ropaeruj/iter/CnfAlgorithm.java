package hr.fer.zemris.ropaeruj.iter;

/**
 * Solves ALL possible solutions from a given POS problem by testing the problem on all
 * solutions. This is done in big Oh n^2 time
 */
public class CnfAlgorithm implements Algorithm {

    private SATFormula expression;

    public CnfAlgorithm(SATFormula expression) {
        this.expression = expression;
    }

    @Override
    public void run() {
        //test for all possible inputs
        for (int i = 0; i < Math.pow(2, expression.getNumberOfVariablesInASingleClause()) - 1; i++) {

            String binaryRepresentation = String.format("%" + expression.getNumberOfVariablesInASingleClause() + "s",
                    Integer.toBinaryString(i)).replace(" ", "0");

            boolean isValidExpressionForInput = expression.evaluateWholeExpression(binaryRepresentation);

            if (isValidExpressionForInput) {
                System.out.println(binaryRepresentation);
            }
        }
    }
}
