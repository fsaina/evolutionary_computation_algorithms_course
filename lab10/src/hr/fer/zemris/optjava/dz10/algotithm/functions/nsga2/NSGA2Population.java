package hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2;



import java.util.LinkedList;
import java.util.Random;

public class NSGA2Population {
	public static Random rand = new Random(System.currentTimeMillis());;
	public LinkedList<NSGASolution> population = new LinkedList<>();
	public int domainDim;
	public int objectDim;
	
	public NSGA2Population(int populationSize, int domainDim, int objDim, double[] minComponentVals,
						   double[] maxComponentVals) {
		this.domainDim = domainDim;
		this.objectDim = objDim;
		randomize(populationSize, minComponentVals, maxComponentVals);
	}

	
	public NSGA2Population(NSGASolution[] initPopulation) {
		this.domainDim = initPopulation[0].values.length;
		for (int i = 0; i < initPopulation.length; i++){
			this.population.add(initPopulation[i].duplicate());
		}
	}
	public NSGA2Population(int populationSize, int domainDim, int objDim ){
		this.domainDim = domainDim;
		this.objectDim = objDim;
	}
	
	private void randomize(int populationSize, double[] minComponentVals, double[] maxComponentVals) {
		for (int i = 0; i < populationSize; i++) {
			NSGASolution s = new NSGASolution(domainDim, objectDim);
			s.randomize(rand, minComponentVals, maxComponentVals);
			population.add(s);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (NSGASolution s : population) {
			sb.append(s + "\n");
		}
		return sb.toString();
	}
}
