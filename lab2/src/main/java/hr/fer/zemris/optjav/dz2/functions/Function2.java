package hr.fer.zemris.optjav.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 10/17/16.
 */
public class Function2 implements IHFunction {

    @Override
    public int numberOfVariables() {
        return 2;
    }

    @Override
    public double calculateValueIn(RealVector point) {

        double[] array = point.toArray();
        return Math.pow(array[0] - 1, 2) + 10 * Math.pow(array[1] - 2, 2);

    }

    @Override
    public RealVector calculateGradientValueIn(RealVector point) {
        double[] array = point.toArray();

        array[0] = 2 * array[0] -2;
        array[1] = 20 * (array[1] - 2);

        RealVector gradient = new ArrayRealVector(array.length);
        int i = 0;
        for (double d : array) {
            gradient.setEntry(i, d);
            i++;
        }

        return gradient;
    }

    @Override
    public RealMatrix calculateHesseMatrixIn(RealVector point) {

        Array2DRowRealMatrix array2DRowRealMatrix = new Array2DRowRealMatrix(2, 2);

        array2DRowRealMatrix.setEntry(0, 0, 2);
        array2DRowRealMatrix.setEntry(1, 0, 0);
        array2DRowRealMatrix.setEntry(0, 1, 0);
        array2DRowRealMatrix.setEntry(1, 1, 20);

        return array2DRowRealMatrix;
    }
}
