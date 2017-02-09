package hr.fer.zemris.optjava.dz9.algorithm;

import hr.fer.zemris.optjava.dz9.algorithm.crossover.ICrossover;
import hr.fer.zemris.optjava.dz9.algorithm.mutation.IMutation;
import hr.fer.zemris.optjava.dz9.algorithm.selection.ISelection;
import hr.fer.zemris.optjava.dz9.algorithm.selection.RouletteSelection;
import hr.fer.zemris.optjava.dz9.models.MultipleObjectiveSolution;
import hr.fer.zemris.optjava.dz9.problems.MOOPProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Non-dominated Sorting Genetic Algorithm implementation
 */
public class NSGA {

	public static final double FITNESS_CONST = 0.95;

	private int maxGen;
	private boolean decisionSpaceFS;
	private double sigmaShare;
	private double alpha;

	private ISelection<MultipleObjectiveSolution> selection;
	private List<ICrossover<MultipleObjectiveSolution>> crossoverList;
	private MOOPProblem problem;
	private int population;
	private IMutation<MultipleObjectiveSolution> mutation;

	public NSGA(MOOPProblem problem, int populationSize,
			List<ICrossover<MultipleObjectiveSolution>> crossovers,
			IMutation<MultipleObjectiveSolution> mutation, int maxIterations,
			boolean decisionSpaceFS, double alpha, double sigmaShare) {

		this.problem = problem;
		this.population = populationSize;
		this.selection = new RouletteSelection();
		this.crossoverList = crossovers;
		this.mutation = mutation;
		this.maxGen = maxIterations / populationSize;
		this.decisionSpaceFS = decisionSpaceFS;
		this.alpha = alpha;
		this.sigmaShare = sigmaShare;
	}

	public List<List<MultipleObjectiveSolution>> run() {
		Random rand = new Random();
		MultipleObjectiveSolution[] population = new MultipleObjectiveSolution[this.population];
		
		for(int i = 0; i < this.population; i++) {
			population[i] = new MultipleObjectiveSolution(problem.randPoint());
			population[i].objectives = problem.evaluate(population[i].values);
		}
		
		for(int generation = 1; generation <= maxGen; generation++) {
			List<List<MultipleObjectiveSolution>> fronts = createNewFronts(population);
			
			double fitness = this.population;
			for(List<MultipleObjectiveSolution> front : fronts) {
				double min = Double.POSITIVE_INFINITY;
				
				for(MultipleObjectiveSolution solution : front) {
					solution.fitness = fitness;
					correctFitness(solution, population);
					min = Math.min(min, solution.fitness);
				}
				
				fitness = FITNESS_CONST * min;
			}
			
			MultipleObjectiveSolution[] nextPop = new MultipleObjectiveSolution[this.population];
			
			for(int i = 0; i < this.population; i++) {

				MultipleObjectiveSolution r1 = population[selection.select(population)];
				MultipleObjectiveSolution r2 = population[selection.select(population)];
				
				while(r1 == r2) {
					r2 = population[selection.select(population)];
				}
				
				int crossover = rand.nextInt(crossoverList.size());
				MultipleObjectiveSolution child = crossoverList.get(crossover).cross(r1, r2);
				
				mutation.mutate(child);

				problem.checkConstraints(child.values);
				child.objectives = problem.evaluate(child.values);
				
				nextPop[i] = child;
			}
			
			population = nextPop;
		}

		return createNewFronts(population);
	}
	
	private void correctFitness(MultipleObjectiveSolution solution,
			MultipleObjectiveSolution[] population) {
		double nc = 0.0;
		
		for(MultipleObjectiveSolution other : population) {
			double d = distance(solution, other);
			
			if(d <= sigmaShare) {
				nc += 1 - Math.pow(d / sigmaShare, alpha);
			}
		}
		
		solution.fitness /= nc;		
	}

	public List<List<MultipleObjectiveSolution>> createNewFronts(MultipleObjectiveSolution[] population) {

		List<List<MultipleObjectiveSolution>> fronts = new ArrayList<>();
		
		EntryFrontModel[] entries = new EntryFrontModel[this.population];

		initializeFronts(population, entries);
		checkDominance(entries);

		List<EntryFrontModel> nonDominated = extractNonDominated(entries);
		
		while(!nonDominated.isEmpty()) {
			List<EntryFrontModel> newNonDominated = new ArrayList<>();
			List<MultipleObjectiveSolution> front = new ArrayList<>();
			
			for(EntryFrontModel entry : nonDominated) {
				front.add(entry.solution);
				
				for(EntryFrontModel dominated : entry.dominates) {
					dominated.level--;
					
					if(dominated.level == 0) {
						newNonDominated.add(dominated);
					}
				}
			}
			
			nonDominated = newNonDominated;
			fronts.add(front);
		}
		
		return fronts;
	}

	private void initializeFronts(MultipleObjectiveSolution[] population, EntryFrontModel[] entries) {
		for(int i = 0; i < this.population; i++) {
			entries[i] = new EntryFrontModel(population[i]);
		}
	}

	private void checkDominance(EntryFrontModel[] entries) {
		for(int i = 0; i < this.population; i++) {
			for(int j = 0; j < this.population; j++) {
				if(i != j) {
					entries[i].checkDominance(entries[j]);
				}
			}
		}
	}

	private List<EntryFrontModel> extractNonDominated(EntryFrontModel[] entries) {
		List<EntryFrontModel> nonDominated = new ArrayList<>();
		for(EntryFrontModel entry : entries) {
			if(entry.level == 0) {
				nonDominated.add(entry);
			}
		}
		return nonDominated;
	}

	private double distance(MultipleObjectiveSolution first,
			MultipleObjectiveSolution second) {

		double[] secondVals = null;
		double[] firstVals = null;
		double[] ranges = null;
		
		if(decisionSpaceFS) {
			firstVals = first.values;
			secondVals = second.values;
			ranges = problem.getRanges();
		} else {
			firstVals = first.objectives;
			secondVals = second.objectives;
			ranges = problem.getObjectiveRanges();
		}
		
		double distance = 0.0;
		
		for(int i = 0; i < firstVals.length; i++) {
			double diff = (firstVals[i] - secondVals[i]) / ranges[i];
			distance += Math.pow(diff, 2);
		}
		
		return Math.sqrt(distance);
	}
	
	private class EntryFrontModel {
		
		private MultipleObjectiveSolution solution;
		private List<EntryFrontModel> dominates;
		private int level;

		public EntryFrontModel(MultipleObjectiveSolution solution) {
			this.solution = solution;
			this.level = 0;
			this.dominates = new ArrayList<>();
		}
		
		public void checkDominance(EntryFrontModel other) {
			if(solution.dominates(other.solution)) {

				other.level++;
				dominates.add(other);
			}
		}
	}
}
