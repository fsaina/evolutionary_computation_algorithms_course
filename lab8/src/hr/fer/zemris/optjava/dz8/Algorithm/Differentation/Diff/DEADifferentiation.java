package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Diff;

import hr.fer.zemris.optjava.dz8.Algorithm.Unit;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

/**
 * Created by fsaina-lenovo on 12/20/16.
 */
public class DEADifferentiation implements DEADifferentionFunction {

    private static final double F = 1.7;

    @Override
    public RealVector apply(RealVector u, RealVector unit, RealVector unit1, List<Unit> population) {

        return u.add((unit.subtract(unit1)).mapMultiply(F));
    }
}
