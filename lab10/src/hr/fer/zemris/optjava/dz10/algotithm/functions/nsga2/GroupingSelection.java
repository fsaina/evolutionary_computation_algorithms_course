package hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class GroupingSelection {  
	private Random rand;
	private NSGA2Population population;
	private int numOfContenders;
	private LinkedList<LinkedList<GroupingHelper>> helper;
	private LinkedList<LinkedList<NSGASolution>> paretoFronts;
	private double[] maxValue;
	private double[] minValue;
	
	public GroupingSelection(Random rand, int numOfContenders, double[] minValue, double[] maxValue){
		this.rand = rand;
		this.numOfContenders=numOfContenders;
		helper = new LinkedList<>();
		this.maxValue = maxValue;
		this.minValue = minValue;
	}
	
	public void setPop(NSGA2Population pop, LinkedList<LinkedList<NSGASolution>> paretoFronts){

		helper.clear();
		this.population = pop;
		this.paretoFronts = paretoFronts;
		calculateGroupingForPopulation();
	}
	
	public NSGASolution selectNext(){
		NSGASolution bestFound = null;
		for(int i=0;i<this.numOfContenders;i++){
			NSGASolution candidate = population.population.get(rand.nextInt(population.population.size()));
			if(bestFound == null || bestFound.frontNumber>candidate.frontNumber || (bestFound.frontNumber==candidate.frontNumber && bestFound.groupingDistance<candidate.groupingDistance)){
				bestFound = candidate;
			}
		}
		return bestFound;		
		
	}
	
	public LinkedList<NSGASolution> getBestFromParetoFront(int numberOfElementsNeeded, int frontNumber, LinkedList<NSGASolution> front){
//		LinkedList<NSGASolution> front = paretoFronts.get(frontNumber);
		LinkedList<NSGASolution> newElements = new LinkedList<>();
		Collections.sort(front, new Comparator<NSGASolution>() {

			@Override
			public int compare(NSGASolution o1, NSGASolution o2) {
				return -1*Double.compare(o1.groupingDistance, o2.groupingDistance);
			}
		});
		
		for(int i=0;i<numberOfElementsNeeded && i<front.size();i++){
			newElements.add(front.get(i));
		}
		
		return newElements;
		
	}
	
	public void loadFitnessAndSort(LinkedList<NSGASolution> subPop){
		
		for(int i=0;i<population.objectDim;i++){
			LinkedList<GroupingHelper> fitIndValueList = new LinkedList<>();
			int ind = 0;
			for(NSGASolution sol : subPop){
				fitIndValueList.add(new GroupingHelper(ind, sol.objSolutionValues[i]));
				ind++;
			}
			
			Collections.sort(fitIndValueList);
			helper.add(fitIndValueList);
		}
		

	}
	
	public void calculateGroupingForPopulation(){
		
		for(NSGASolution sol : population.population){
			sol.groupingDistance = 0;
		}
		
		for(LinkedList<NSGASolution> paretoFront : paretoFronts){
			helper.clear();
			loadFitnessAndSort(paretoFront);
			if(paretoFront.isEmpty()){
				continue;
			}
			for(int objSolInd=0;objSolInd<population.objectDim;objSolInd++){
				LinkedList<GroupingHelper> currentVector = helper.get(objSolInd);
				NSGASolution sol = paretoFront.get(currentVector.getFirst().index);
				sol.groupingDistance = Double.POSITIVE_INFINITY;
				sol = paretoFront.get(currentVector.getLast().index);
				sol.groupingDistance = Double.POSITIVE_INFINITY;
				
				
				
				for(int i=1;i<currentVector.size()-1;i++){
					sol = paretoFront.get(currentVector.get(i).index);
					int index = currentVector.get(i-1).index;
					NSGASolution solPrevious = paretoFront.get(index);
					index = currentVector.get(i+1).index;
					NSGASolution solNext = paretoFront.get(index);
					sol.groupingDistance += (solNext.objSolutionValues[objSolInd] - solPrevious.objSolutionValues[objSolInd]) / (this.maxValue[objSolInd]-this.minValue[objSolInd]);
				}
			}
		}
	}

}
