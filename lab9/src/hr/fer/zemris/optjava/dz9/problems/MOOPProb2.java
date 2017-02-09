package hr.fer.zemris.optjava.dz9.problems;

import java.util.Random;


public class MOOPProb2 implements MOOPProblem {

	private Random rand;

	private static final double[] mins = {0.1, 0.0};
	private static final double[] maxs = {1.0, 5.0};

	public MOOPProb2() {
		this.rand = new Random();
	}
	
	@Override
	public int dimension() {
		return 2;
	}

	@Override
	public double[] evaluate(double[] solution) {
		double[] result = new double[2];
		
		result[0] = solution[0];
		result[1] = (1.0 + solution[1]) / solution[0];

		return result;
	}

	@Override
	public void checkConstraints(double[] solution) {
		for(int i = 0; i < dimension(); i++) {
			if(solution[i] > maxs[i]) {
				solution[i] = maxs[i];
			} else if(solution[i] < mins[i]) {
				solution[i] = mins[i];
			}
		}
	}

	@Override
	public double[] randPoint() {
		double[] point = new double[dimension()];
		
		for(int i = 0; i < dimension(); i++) {
			point[i] = (maxs[i] - mins[i]) * rand.nextDouble() + mins[i];
		}
		
		return point;
	}

	@Override
	public double[] getRanges() {
		double[] ranges = new double[dimension()];
		
		for(int i = 0; i < dimension(); i++) {
			ranges[i] = maxs[i] - mins[i];
		}
		
		return ranges;
	}

	@Override
	public double[] getObjectiveRanges() {
		return new double[]{0.9, 59.0};
	}
}
