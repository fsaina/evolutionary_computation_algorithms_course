package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Diff;

import hr.fer.zemris.optjava.dz8.Algorithm.Unit;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public interface DEADifferentionFunction {
     RealVector apply(RealVector u, RealVector unit, RealVector unit1, List<Unit> population);
}
