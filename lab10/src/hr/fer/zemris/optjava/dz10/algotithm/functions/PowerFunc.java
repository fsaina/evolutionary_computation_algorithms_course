package hr.fer.zemris.optjava.dz10.algotithm.functions;

import hr.fer.zemris.optjava.dz10.algotithm.functions.interfaces.IFunction;

public class PowerFunc implements IFunction {

	private int index;

	public PowerFunc(int index) {
		this.index = index;
	}

	@Override
	public double[] f(double[] v) {
		if (v.length < index + 1)
			throw new IllegalArgumentException("Vector dimension too small.");
		return new double[] { 
				v[index] * v[index]
		};
	}

}
