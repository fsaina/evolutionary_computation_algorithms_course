package hr.fer.zemris.optjava.dz10.problems;

public interface MOOPProblem {

	void evaluate(double[] solution, double[] objectives);
	
	public int getDecisionSpaceDim();
	
	int getObjectiveSpaceDim();

	public double[] getMinDomainVals();
	
	public double[] getMaxDomainVals();
	
	public double[] getObjectiveMin();
	
	public double[] getObjectiveMax();
}
