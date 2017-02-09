package hr.fer.zemris.optjava.dz10.algotithm.functions;

import hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces.IFunction;

public class RatioFunc implements IFunction {

	@Override
	public double[] f(double[] v) {
		if (v.length < 2)
			throw new IllegalArgumentException("Vector dimension too small.");
		return new double[] { (v[1] + 1) / v[0] };
	}

}
