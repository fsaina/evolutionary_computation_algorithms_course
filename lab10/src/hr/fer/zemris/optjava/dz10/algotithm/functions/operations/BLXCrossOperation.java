package hr.fer.zemris.optjava.dz10.algotithm.functions.operations;

import hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2.NSGASolution;

import java.util.Random;

public class BLXCrossOperation {

	private double[] minDomainVal;
	private double[] maxDomainVal;
	private Random rand;
	
	public BLXCrossOperation(double[] minDomain, double[] maxDomain, Random rand){
		this.minDomainVal = minDomain;
		this.maxDomainVal = maxDomain;
		this.rand = rand;
	}
	
	public NSGASolution cross(NSGASolution first, NSGASolution second, double alpha){


		double cMin, cMax, I, upperBound, lowerBound;
		double[] firstParentValue = first.values;
		double[] secondParentValue = second.values;
		NSGASolution child = first.newLikeThis();
		int dimensions = first.values.length;
		double childValues[] = new double[dimensions];
		for(int i=0;i<dimensions;i++){
			cMin = Math.min(firstParentValue[i], secondParentValue[i]);
			cMax = Math.max(firstParentValue[i], secondParentValue[i]);
			I = cMax - cMin;
			upperBound = cMax + I*alpha;
			lowerBound = cMin - I*alpha;
			childValues[i] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;
			if(childValues[i]<minDomainVal[i]){
				childValues[i] = minDomainVal[i];
			}else if(childValues[i]>maxDomainVal[i]){
				childValues[i] = maxDomainVal[i];
			}
		}
		child.values = childValues;
		return child;
	}
}
