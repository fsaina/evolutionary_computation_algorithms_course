package hr.fer.zemris.optjav.dz2.functions;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 10/17/16.
 */
public class LinearSystemCostFunction implements IHFunction {

    private RealMatrix coeficients;
    private RealVector solutions;

    public LinearSystemCostFunction(RealMatrix coeficients, RealVector solutions) {
        this.coeficients = coeficients;
        this.solutions = solutions;
    }

    @Override
    public int numberOfVariables() {
        return solutions.getDimension();
    }

    @Override
    public double calculateValueIn(RealVector x) {
        RealVector errorDifference = coeficients.operate(x).subtract(solutions);

        errorDifference = errorDifference.ebeMultiply(errorDifference);

        double sum = 0;
        for (int i = 0; i < numberOfVariables(); i++) {
            sum += errorDifference.getEntry(i);
        }

        return sum;
    }

    @Override
    public RealVector calculateGradientValueIn(RealVector point) {

        //derivation adds the >times 2<
        RealVector errorDifference = coeficients.operate(point).subtract(solutions).mapMultiply(2);
        RealVector gradient = new ArrayRealVector(numberOfVariables());

        // transpose(errorDiff) * coefficients, but Apache commons does not have vector.transpose() ?!
        for (int i = 0; i < numberOfVariables(); i++) {
            double value = 0.0;
            for (int j = 0; j < numberOfVariables(); j++) {
                value += errorDifference.getEntry(j) * coeficients.getEntry(j, i);
            }
            gradient.setEntry(i, value);
        }

        return gradient;
    }

    @Override
    public RealMatrix calculateHesseMatrixIn(RealVector point) {
        RealMatrix hessian = new Array2DRowRealMatrix(
                numberOfVariables(),
                numberOfVariables()
        );

        for (int i = 0; i < numberOfVariables(); i++) {
            for (int j = 0; j < numberOfVariables(); j++) {
                for (int k = 0; k < numberOfVariables(); k++) {
                    hessian.
                            setEntry(i, j,
                                    hessian.getEntry(i, j) +
                                    2 * coeficients.getEntry(k, i) *
                                            coeficients.getEntry(k, j));

                }
            }
        }
        return hessian;

    }
}
