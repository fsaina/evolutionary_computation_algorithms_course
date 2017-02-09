package hr.fer.zemris.optjava.dz10.algotithm.functions;

import hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces.IFunction;

public class LinearFunc implements IFunction {

	@Override
	public double[] f(double[] v) {
		if (v.length < 1)
			throw new IllegalArgumentException("Vector dimension too small.");
		return new double[] { v[0] };
	}

}
