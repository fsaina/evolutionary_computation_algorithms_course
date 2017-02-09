package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Cross;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 12/20/16.
 */
public interface DEACrossFunction {
    RealVector apply(RealVector u, RealVector mutant);
}
