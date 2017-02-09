package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Selection;

import hr.fer.zemris.optjava.dz8.Algorithm.Unit;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 12/20/16.
 */
public interface DEASelectionFunction {
    Unit apply(Unit u, RealVector cross);
}
