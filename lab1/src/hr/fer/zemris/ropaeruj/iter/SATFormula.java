package hr.fer.zemris.ropaeruj.iter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of a SAT formula that consists of hr.fer.zemris.ropaeruj.iter.Clause elements
 */
public class SATFormula {

    private List<Clause> clauseList = new ArrayList<Clause>();
    private int numberOfClauses;
    private int numberOfVariablesInASingleClause;

    public SATFormula(int numberOfVariables, int numberOfClauses) {
        this.numberOfClauses = numberOfClauses;
        this.numberOfVariablesInASingleClause = numberOfVariables;
    }

    public void addClause(Clause clause) {
        clauseList.add(clause);
    }

    //this method can be used as the fitness function
    public int numberOfValidClauses(String binaryStringInput) {

        int valid = 0;

        for (int i = 0; i < clauseList.size(); i++) {
            if (clauseList.get(i).evaluateClause(binaryStringInput)) {
                valid++;
            }
        }

        return valid;
    }


    public List<Clause> getClauseList() {
        return clauseList;
    }

    public boolean evaluateWholeExpression(String binaryStringInput) {

        for (int n = 0; n < clauseList.size(); n++) {

            boolean evaluation = clauseList.get(n).evaluateClause(binaryStringInput);

            if (evaluation == false) {
                return false;
            }

        }

        return true;
    }


    public int getNumberOfClauses() {
        return numberOfClauses;
    }

    public void setNumberOfClauses(int numberOfClauses) {
        this.numberOfClauses = numberOfClauses;
    }

    public int getNumberOfVariablesInASingleClause() {
        return numberOfVariablesInASingleClause;
    }

    public void setNumberOfVariablesInASingleClause(int numberOfVariablesInASingleClause) {
        this.numberOfVariablesInASingleClause = numberOfVariablesInASingleClause;
    }
}



