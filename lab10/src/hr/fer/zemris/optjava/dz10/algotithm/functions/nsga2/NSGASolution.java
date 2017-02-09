package hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2;


import java.util.Random;

public class NSGASolution implements Comparable<NSGASolution> {
	public double[] values;
	public double fitness;
	public double[] objSolutionValues;
	public int frontNumber;
	public double groupingDistance;
	
	public NSGASolution(int dimensions, int objectDimension){
		values = new double[dimensions];
		objSolutionValues = new double[objectDimension];
		groupingDistance = 0;
	}
	
	public NSGASolution newLikeThis(){
		NSGASolution newSol = new NSGASolution(values.length, objSolutionValues.length);
		newSol.groupingDistance = this.groupingDistance;
		newSol.frontNumber = this.frontNumber;
		return newSol;
	}
	
	
	public double[] getObjSolutionValues() {
		return objSolutionValues;
	}

	public void setObjSolutionValues(double[] objSolValues) {
		this.objSolutionValues = objSolValues;
	}
	public NSGASolution duplicate() {
		NSGASolution copy = new NSGASolution(values.length, objSolutionValues.length);
		copy.fitness = this.fitness;
		copy.values = this.values.clone();
		copy.objSolutionValues = this.objSolutionValues.clone();
		return copy;
	}


	public void randomize(Random random, double[] minValues, double[] maxValues) {
		for (int i = 0; i < values.length; i++) {
			double newValue = random.nextDouble() * (maxValues[i] - minValues[i]) + minValues[i];
			this.values[i] = newValue;
		}
	}
	
	public String valToString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++){
			sb.append(String.format("%5.5f", values[i]) + " ");
		}
		return sb.toString();
	}

	public String objToString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < objSolutionValues.length; i++)
			sb.append(String.format("%5.5f", objSolutionValues[i]) + " ");

		return sb.toString();
	}

	
	public boolean checkDomination(NSGASolution other){
		double[] thisSolutionFitness = this.objSolutionValues;
		double[] otherSolutionFitness = other.objSolutionValues;
		boolean isBetter = false;
		double fitnessDifference;
		int numberOfBetter=0;
		int thisLength = thisSolutionFitness.length;
		if(thisLength != other.objSolutionValues.length){
			throw new IllegalArgumentException("Solutions being compared dont have the same number of dimensions");
		}
		for(int j=0;j<thisLength;j++){
			fitnessDifference = thisSolutionFitness[j] - otherSolutionFitness[j];
			if(fitnessDifference==0){
				numberOfBetter++;
			}else if(fitnessDifference<0){
				numberOfBetter++;
				isBetter=true;
			}else{
				isBetter=false;
				break;
			}
		}
		if(isBetter && numberOfBetter == thisLength){
			return true;
		}
		
		return false;
	}

	@Override
	public int compareTo(NSGASolution o) {
		return Double.compare(fitness, o.fitness);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Fitness: " + String.format("%5.5f", fitness) + " ");
		sb.append("Values: ");
		for (int i = 0; i < values.length; i++)
			sb.append(String.format("%5.5f", values[i]) + " ");
		sb.append("Solution objectives: ");
		for (int i = 0; i < objSolutionValues.length; i++)
			sb.append(String.format("%5.5f", objSolutionValues[i]) + " ");

		return sb.toString();
	}
	
}

