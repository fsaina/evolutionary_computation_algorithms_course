package hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import hr.fer.zemris.optjava.dz10.MOOP;
import hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces.IMoopOptimizationFunction;
import hr.fer.zemris.optjava.dz10.algotithm.functions.operations.BLXCrossOperation;
import hr.fer.zemris.optjava.dz10.algotithm.functions.operations.MutationOperator;



public class NSGA2 {

	public int populationSize;
	public int maxIterations;
	public double sigma;
	public IMoopOptimizationFunction function;
	public NSGA2Population population;
	public GroupingSelection groupingpSel;
	public BLXCrossOperation crossoverOp;
	public MutationOperator mutationOp;
	
	
	public NSGA2(int sizeOfPop, int maxIter,
                 GroupingSelection groupingSelection, double sigma,
                 IMoopOptimizationFunction function) {
		this.populationSize = sizeOfPop;
		this.maxIterations = maxIter;
		this.groupingpSel = groupingSelection;
		this.sigma = sigma;
		this.function = function;
		Random rand = new Random(System.currentTimeMillis());
		this.crossoverOp = new BLXCrossOperation(function.getMinDomainVals(),function.getMaxDomainVals(), rand);
		this.mutationOp = new MutationOperator(function.getMinDomainVals(), function.getMaxDomainVals(), rand);
		
		population = new NSGA2Population(populationSize, function.getDecisionSpaceDim(), function.getObjectiveSpaceDim(),
				function.getMinDomainVals(), function.getMaxDomainVals());
		

		function.evaluatePopulation(population.population, MOOP.ALPHA, sigma);
		Collections.sort(population.population, Collections.reverseOrder());
	}


	public void run() {
		

		
		NSGASolution firstParent;
		NSGASolution secondParent;
		NSGASolution child;
		
		NSGASolution[] nextPopulation;
		int nextElement;
		int currentIter = 0;
		while(currentIter <this.maxIterations){

			if(currentIter%10==0){
				System.out.println("Current iteration: "+ currentIter);
			}
			currentIter++;
			nextElement=0;
			nextPopulation = new NSGASolution[this.populationSize*2];
			
			
			//dodaj pareto fronte
			groupingpSel.setPop(population, function.getParetoFronts(population.population));
			
			
			while(nextElement<populationSize*2){
				firstParent = groupingpSel.selectNext();
				secondParent = groupingpSel.selectNext();
				child = crossoverOp.cross(firstParent, secondParent, MOOP.BLX_Constant);
				child = mutationOp.mutation(child, MOOP.MUTATION_VALUE);
				double[] objectives = new double[function.getMOOPProblem().getObjectiveSpaceDim()];
				function.getMOOPProblem().evaluate(child.values, objectives);
				child.objSolutionValues = objectives;
				
				nextPopulation[nextElement] = child.duplicate();
				nextElement++;
			}
			
			NSGA2Population tempPop = new NSGA2Population(nextPopulation);
			function.evaluatePopulation(tempPop.population, MOOP.ALPHA, sigma);
			LinkedList<LinkedList<NSGASolution>> paretoFronts = function.getParetoFronts(population.population);
			
			population = new NSGA2Population(this.populationSize, population.domainDim, population.objectDim);
			
			int currentFront=0;
			for(LinkedList<NSGASolution> front : paretoFronts){
				if(population.population.size() < this.populationSize){
					if(population.population.size()+ front.size()<this.populationSize){
						for(NSGASolution nsgaSol : front){
							population.population.add(nsgaSol);
						}

					}else {
						LinkedList<NSGASolution> candidates = this.groupingpSel.getBestFromParetoFront(this.populationSize-population.population.size(), currentFront, front);
						for(NSGASolution nsgaSol : candidates){
							if(population.population.size()<this.populationSize){
								population.population.add(nsgaSol.duplicate());
								
							}else {
								break;
							}
						}
					}
				}else {

					break;
				}
				currentFront++;
			}

			
			
			function.evaluatePopulation(population.population, MOOP.ALPHA, sigma);
			Collections.sort(population.population, Collections.reverseOrder());
			
			
		}

		
		System.out.println("FINISHED");
		
	}
	
	public LinkedList<LinkedList<NSGASolution>> getFronts(){
		return this.function.getParetoFronts(population.population);
	}
}
