package hr.fer.zemris.optjava.dz9.problems;

public interface MOOPProblem {

    double[] evaluate(double[] solution);
    double[] randPoint();
    void checkConstraints(double[] solution);
    double[] getObjectiveRanges();
    double[] getRanges();
    int dimension();
}
