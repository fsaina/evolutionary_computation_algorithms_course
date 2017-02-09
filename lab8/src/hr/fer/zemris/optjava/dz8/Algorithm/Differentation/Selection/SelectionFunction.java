package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Selection;

import hr.fer.zemris.optjava.dz8.Algorithm.Unit;
import org.apache.commons.math3.linear.RealVector;

public class SelectionFunction implements DEASelectionFunction {
    @Override
    public Unit apply(Unit original, RealVector cross) {

        Unit corssUnit = new Unit(cross, original.getFunction(), original.getData());

        if(corssUnit.getCurrentError() <= original.getCurrentError()){
            return corssUnit;
        }

        return original;
    }
}
