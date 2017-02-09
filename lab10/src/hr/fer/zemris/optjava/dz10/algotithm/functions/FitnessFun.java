package hr.fer.zemris.optjava.dz10.algotithm.functions;

import java.util.LinkedList;

import hr.fer.zemris.optjava.dz10.MOOP;
import hr.fer.zemris.optjava.dz10.problems.MOOPProblem;
import hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces.IMoopOptimizationFunction;
import hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2.NSGASolution;

public class FitnessFun implements IMoopOptimizationFunction {

	public MOOPProblem moopProblem;
	public LinkedList<LinkedList<Integer>> dominatedSolutions;
	public int[] solutions;
	public LinkedList<LinkedList<Integer>> paretoFronts;
	public LinkedList<LinkedList<NSGASolution>> paretoSolFronts;
	public double[][] solutionRelations;
	public boolean objective;
	
	public FitnessFun(MOOPProblem moopProblem, boolean objective) {
		this.moopProblem = moopProblem;
		this.objective = objective;
	}
	
	@Override
	public int getDecisionSpaceDim() {
		return this.moopProblem.getDecisionSpaceDim();
	}

	@Override
	public int getObjectiveSpaceDim() {
		return this.moopProblem.getObjectiveSpaceDim();
	}

	@Override
	public double[] getMinDomainVals() {
		return this.moopProblem.getMinDomainVals();
	}

	@Override
	public double[] getMaxDomainVals() {
		return this.moopProblem.getMaxDomainVals();
	}

	@Override
	public void evaluatePopulation(LinkedList<NSGASolution> population,
			double alpha, double sigma) {
		for (NSGASolution solution : population) {
			double[] objectives = new double[moopProblem.getDecisionSpaceDim()];
			moopProblem.evaluate(solution.values, objectives);
			solution.objSolutionValues = (objectives);
		}
		nonDominantSorting(population, alpha, sigma);
	}

	@Override
	public MOOPProblem getMOOPProblem() {
		return this.moopProblem;
	}

	@Override
	public LinkedList<LinkedList<NSGASolution>> getParetoFronts(
			LinkedList<NSGASolution> population) {
		return this.paretoSolFronts;
	}
	
	private double[] getNcForSubPop(LinkedList<NSGASolution> subPopulation, double alpha, double sigma) {
		calculateDistance(subPopulation);
		calculateSharingValues(subPopulation.size(), alpha, sigma);

		double[] nc = new double[subPopulation.size()];

		for (int i = 0; i < subPopulation.size(); i++)
			for (int j = 0; j < subPopulation.size(); j++)
				nc[i] += solutionRelations[i][j];

		int i = 0;
		for (NSGASolution solution : subPopulation) {
			solution.fitness= (solution.fitness / nc[i++]);
		}
		return nc;
	}
	
	
	protected void calculateSharingValues(int populationSize, double alpha, double sigma) {
		for (int i = 0; i < populationSize; i++)
			for (int j = 0; j < populationSize; j++) {
				if (solutionRelations[i][j] < sigma) {
					double tmp = solutionRelations[i][j] / sigma;
					tmp = Math.pow(tmp, alpha);
					solutionRelations[i][j] = 1 - tmp;
				} else {
					solutionRelations[i][j] = 0;
				}
			}
	}

	
	public void calculateDistance(LinkedList<NSGASolution> population){
		if(objective){
			calculateObjective(population);
		}else{
			calculateDecision(population);
		}
	}

	private void calculateDecision(LinkedList<NSGASolution> population) {
		solutionRelations = new double[population.size()][population.size()];

		double[] minValVect = new double[moopProblem.getDecisionSpaceDim()];
		double[] maxValVect = new double[moopProblem.getDecisionSpaceDim()];

		for (int popSubjIndex = 0; popSubjIndex < population.size(); popSubjIndex++) {
			NSGASolution solution = population.get(popSubjIndex);
			double[] vector = solution.values;

			if (popSubjIndex == 0) {
				for (int vecIndex = 0; vecIndex < vector.length; vecIndex++) {
					minValVect[vecIndex] = vector[vecIndex];
					maxValVect[vecIndex] = vector[vecIndex];
				}
			} else {
				for (int vecIndex = 0; vecIndex < vector.length; vecIndex++) {
					if (minValVect[vecIndex] > vector[vecIndex]) {
						minValVect[vecIndex] = vector[vecIndex];
					}
					if (maxValVect[vecIndex] < vector[vecIndex]) {
						maxValVect[vecIndex] = vector[vecIndex];
					}
				}
			}
		}

		for (int firstSubjectIndex = 0; firstSubjectIndex < population.size(); firstSubjectIndex++) {
			for (int secondSubjectIndex = firstSubjectIndex + 1; secondSubjectIndex < population
					.size(); secondSubjectIndex++) {

				NSGASolution firstSolution = population.get(firstSubjectIndex);
				NSGASolution secondSolution = population.get(secondSubjectIndex);

				double[] firstValues = firstSolution.values;
				double[] secondValues = secondSolution.values;

				double d = 0;

				for (int dim = 0; dim < firstValues.length; dim++) {
					double tmp = (firstValues[dim] - secondValues[dim]) / (maxValVect[dim] - minValVect[dim]);
					tmp = Math.pow(tmp, 2);
					d += tmp;
				}
				solutionRelations[firstSubjectIndex][secondSubjectIndex] = solutionRelations[secondSubjectIndex][firstSubjectIndex] = Math
						.sqrt(d);

			}
		}
	}

	private void calculateObjective(LinkedList<NSGASolution> population) {
		solutionRelations = new double[population.size()][population.size()];

		double[] minValVect = new double[moopProblem.getObjectiveSpaceDim()];
		double[] maxValVect = new double[moopProblem.getObjectiveSpaceDim()];

		for (int popSubjIndex = 0; popSubjIndex < population.size(); popSubjIndex++) {
			NSGASolution solution = population.get(popSubjIndex);
			double[] vector = new double[getObjectiveSpaceDim()];

			moopProblem.evaluate(solution.values, vector);

			if (popSubjIndex == 0) {
				for (int vecIndex = 0; vecIndex < vector.length; vecIndex++) {
					minValVect[vecIndex] = vector[vecIndex];
					maxValVect[vecIndex] = vector[vecIndex];
				}
			} else {
				for (int vecIndex = 0; vecIndex < vector.length; vecIndex++) {
					if (minValVect[vecIndex] > vector[vecIndex]) {
						minValVect[vecIndex] = vector[vecIndex];
					}
					if (maxValVect[vecIndex] < vector[vecIndex]) {
						maxValVect[vecIndex] = vector[vecIndex];
					}
				}
			}
		}

		for (int firstSubjectIndex = 0; firstSubjectIndex < population.size(); firstSubjectIndex++) {
			for (int secondSubjectIndex = firstSubjectIndex + 1; secondSubjectIndex < population
					.size(); secondSubjectIndex++) {

				NSGASolution firstSolution = population.get(firstSubjectIndex);
				NSGASolution secondSolution = population.get(secondSubjectIndex);

				double[] firstValues = new double[getObjectiveSpaceDim()];
				double[] secondValues = new double[getObjectiveSpaceDim()];

				moopProblem.evaluate(firstSolution.values, firstValues);
				moopProblem.evaluate(secondSolution.values, secondValues);

				double d = 0;

				for (int dim = 0; dim < firstValues.length; dim++) {
					double tmp = (firstValues[dim] - secondValues[dim]) / (maxValVect[dim] - minValVect[dim]);
					tmp = Math.pow(tmp, 2);
					d += tmp;
				}
				solutionRelations[firstSubjectIndex][secondSubjectIndex] = solutionRelations[secondSubjectIndex][firstSubjectIndex] = Math
						.sqrt(d);

			}
		}
	}
	
	public void nonDominantSorting(LinkedList<NSGASolution> population, double alpha, double sigma) {
		paretoFronts = new LinkedList<>();
		paretoSolFronts = new LinkedList<>();

		solutions = new int[population.size()];
		dominatedSolutions = new LinkedList<>();
		LinkedList<Integer> currentParetoFront = new LinkedList<>();
		LinkedList<NSGASolution> currentParetoSolFront = new LinkedList<>();

		
		for (int firstSubjectIndex = 0; firstSubjectIndex < population.size(); firstSubjectIndex++) {
			NSGASolution sol1 = population.get(firstSubjectIndex);
			solutions[firstSubjectIndex] = 0;
			LinkedList<Integer> sol1Dominated = new LinkedList<>();
			dominatedSolutions.add(sol1Dominated);

			for (int secondSubjectIndex = 0; secondSubjectIndex < population.size(); secondSubjectIndex++) {

				if (secondSubjectIndex == firstSubjectIndex) {
					continue;
				}

				NSGASolution sol2 = population.get(secondSubjectIndex);

				if (sol1.checkDomination(sol2)) {
					sol1Dominated.add(secondSubjectIndex);
				} else if (sol2.checkDomination(sol1)) {
					solutions[firstSubjectIndex]++;
				}
			}
			if (solutions[firstSubjectIndex] == 0) {
				NSGASolution updateSol = population.get(firstSubjectIndex);
				updateSol.frontNumber = 0;
				currentParetoFront.add(firstSubjectIndex);
				currentParetoSolFront.add(sol1);
			}
		}
		int frontCounter = 0;
		
		
		this.paretoSolFronts.add(frontCounter, currentParetoSolFront);
		paretoFronts.add(frontCounter, currentParetoFront);

		while (true) {
			if (currentParetoFront.size() == 0) {
				break;
			}
			LinkedList<Integer> qList = new LinkedList<>();
			LinkedList<NSGASolution> tempList = new LinkedList<>();
			for (int i = 0; i < currentParetoFront.size(); i++) {
				LinkedList<Integer> currentsSolutionSet = dominatedSolutions.get(currentParetoFront.get(i));
				for (int j = 0; j < currentsSolutionSet.size(); j++) {
					int workingIndex = currentsSolutionSet.get(j);
					solutions[workingIndex]--;
					if (solutions[workingIndex] == 0) {
						NSGASolution updateSol = population.get(workingIndex);
						updateSol.frontNumber = frontCounter;
						qList.add(workingIndex);
						tempList.add(population.get(workingIndex));
					}
				}
			}

			currentParetoFront = qList;
			frontCounter++;
			this.paretoSolFronts.add(frontCounter, tempList);
			paretoFronts.add(frontCounter, currentParetoFront);
		}
	
		
		paretoFronts.removeLast();
		
		int frontC = 1;
		double[] niceCount = getNcForSubPop(population, alpha, sigma);

		double currN = population.size();

		for (LinkedList<Integer> frontElements : paretoFronts) {
			double minCurrN = 0;
			for (int i = 0; i < frontElements.size(); i++) {
				int pos = frontElements.get(i);
				NSGASolution solution = population.get(pos);
				double fScaled = currN / niceCount[pos];
				if (i == 0)
					minCurrN = fScaled;
				else if (minCurrN > fScaled)
					minCurrN = fScaled;
				solution.fitness = (fScaled);
			}
			currN = minCurrN - MOOP.EPSILON;


			if(currN < 0){
				currN = 0;
			}
		}
	}

}
