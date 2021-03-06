package hr.fer.zemris.optjava.dz10.problems;

import hr.fer.zemris.optjava.dz10.MOOP;
import hr.fer.zemris.optjava.dz10.algotithm.functions.PowerFunc;
import hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces.IFunction;

import java.util.LinkedList;

public class MOOPProb1 implements MOOPProblem {
	
	public LinkedList<IFunction> functions = new LinkedList<>();
	public int solutionSpaceDimension;
	public int objectiveSpaceDimension;
	public double[] minBounds;
	public double[] maxBounds;

	public MOOPProb1(int problem1DomainDim, double[] prob1Min, double[] prob1Max) {
		if(prob1Min.length != prob1Max.length && prob1Min.length == problem1DomainDim){
			throw new IllegalArgumentException("Boundaries need to have same number of dimensions");
		}
		for(int i=0;i< problem1DomainDim;i++){
			functions.add(new PowerFunc(i));
		}
		this.minBounds = prob1Min;
		this.maxBounds = prob1Max;
		this.solutionSpaceDimension = problem1DomainDim;
		this.objectiveSpaceDimension = functions.size();
	}
	
	private boolean isWithinBoundaries(double value, int index){
		if(index<0 || index >=minBounds.length){
			throw new IllegalArgumentException("index to check the boundaries is invalid");
		}
		return (minBounds[index] <= value && value <= maxBounds[index]) ? true : false;
	}

	@Override
	public void evaluate(double[] solution, double[] objectives) {
		if (solution.length != functions.size())
			throw new RuntimeException("Given solution vector is too small.");

		for (int i = 0; i < solution.length; i++)
			if (!isWithinBoundaries(solution[i], i))
				throw new RuntimeException("Value out of bound.");

		for (int i = 0; i < objectives.length; i++)
			objectives[i] = functions.get(i).f(solution)[0]; 
		return;
	}

	@Override
	public int getDecisionSpaceDim() {
		return solutionSpaceDimension;
	}

	@Override
	public int getObjectiveSpaceDim() {
		return this.objectiveSpaceDimension;
	}

	@Override
	public double[] getMinDomainVals() {
		return this.minBounds.clone();
	}

	@Override
	public double[] getMaxDomainVals() {
		return this.maxBounds.clone();
	}

	@Override
	public double[] getObjectiveMin() {
		double[] constraint = {0,0,0,0};
		return constraint;
	}

	@Override
	public double[] getObjectiveMax() {
		double value = MOOP.UPPER_PROB2_X1 * MOOP.UPPER_PROB2_X1;
		double[] constraint = {value,value,value,value};
		return constraint;
	}

}
