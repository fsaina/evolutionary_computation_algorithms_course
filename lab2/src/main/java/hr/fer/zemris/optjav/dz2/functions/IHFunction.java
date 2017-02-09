package hr.fer.zemris.optjav.dz2.functions;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 10/12/16.
 */
public interface IHFunction extends IFunction {

    public RealMatrix calculateHesseMatrixIn(RealVector point);

}
