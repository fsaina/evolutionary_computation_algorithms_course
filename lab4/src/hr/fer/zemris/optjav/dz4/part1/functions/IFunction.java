package hr.fer.zemris.optjav.dz4.part1.functions;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 10/12/16.
 */
public interface IFunction {
    public int numberOfVariables();
    public double calculateValueIn(RealVector point);
    public RealVector calculateGradientValueIn(RealVector point);
}
