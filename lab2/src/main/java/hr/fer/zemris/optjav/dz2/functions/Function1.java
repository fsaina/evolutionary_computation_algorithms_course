package hr.fer.zemris.optjav.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 10/14/16.
 */
public class Function1 implements IHFunction {

    public int numberOfVariables() {
        return 2;
    }

    public double calculateValueIn(RealVector point) {
        if (point.getDimension() != numberOfVariables()) {
            throw new IllegalStateException("Vector does not match the number" +
                    "of variables");
        }

        double[] array = point.toArray();

        return Math.pow(array[0], 2) + Math.pow(array[1] - 1, 2);
    }

    public RealVector calculateGradientValueIn(RealVector point) {

        if (point.getDimension() != numberOfVariables()) {
            throw new IllegalStateException("Vector does not match the number" +
                    "of variables");
        }

        double[] array = point.toArray();

        array[0] = 2 * Math.pow(array[0], 1);
        array[1] = 2 * (array[1] - 1);

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
        array2DRowRealMatrix.setEntry(1, 1, 2);

        return array2DRowRealMatrix;

    }


}
