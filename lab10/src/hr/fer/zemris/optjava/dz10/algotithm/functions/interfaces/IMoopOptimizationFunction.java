package hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces;


import hr.fer.zemris.optjava.dz10.problems.MOOPProblem;
import hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2.NSGASolution;

import java.util.LinkedList;

public interface IMoopOptimizationFunction {

	public int getDecisionSpaceDim();

	public int getObjectiveSpaceDim();

	public double[] getMinDomainVals();

	
	public double[] getMaxDomainVals();

	
	public void evaluatePopulation(LinkedList<NSGASolution> population, double alpha, double sigma);

	public MOOPProblem getMOOPProblem();
	
	public LinkedList<LinkedList<NSGASolution>> getParetoFronts(LinkedList<NSGASolution> population);
}
