package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Diff;

import hr.fer.zemris.optjava.dz8.Algorithm.Differentation.BaseVector.BestBaseVector;
import hr.fer.zemris.optjava.dz8.Algorithm.Unit;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public class TargetToBest implements DEADifferentionFunction {

    public static final double F = 1.01;

    @Override
    public RealVector apply(RealVector u, RealVector unit, RealVector unit1, List<Unit> population) {
        Unit best = BestBaseVector.findBest(population);

        RealVector w = (best.getVector().subtract(u)).mapMultiply(F);
        RealVector v = ((unit.subtract(unit1)).mapMultiply(F));

        return u.add(v.add(w));
    }
}
