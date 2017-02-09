package hr.fer.zemris.optjava.dz3.functions;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;

/**
 * Created by fsaina-lenovo on 10/17/16.
 */
public class PrijenosnaCostFunction implements IFunction {

    private RealMatrix variables;
    private RealVector solutions;

    public PrijenosnaCostFunction(RealMatrix coeficients, RealVector solutions) {
        this.variables = coeficients;
        this.solutions = solutions;
    }


    @Override
    public double valueAt(double[] input) {

        double[] constants = input;
        RealVector gradient = new ArrayRealVector(variables.getRowDimension());

        for (int i = 0; i < variables.getRowDimension(); i++) {

            //here be dragons
            gradient.setEntry(i, constants[0] * variables.getEntry(i, 0) +
                    constants[1] * Math.pow(variables.getEntry(i, 0), 3) * variables.getEntry(i, 1) +
                    constants[3] * Math.exp(constants[4] * variables.getEntry(i, 2)) *
                            (1 + Math.cos(constants[5] * variables.getEntry(i, 3))) +
                    constants[4] * variables.getEntry(i, 3) * Math.pow(variables.getEntry(i, 4), 2));
        }

        RealVector tmp = gradient.subtract(solutions);
        return StatUtils.sum(tmp.ebeMultiply(tmp).toArray());
    }

    public RealVector calculateGradientValueIn(RealVector constantsVector) {

        double[] gradient = new double[variables.getColumnDimension()+1];

		double c = constantsVector.getEntry(2);
		double d = constantsVector.getEntry(3);
		double e = constantsVector.getEntry(4);

		for (int i = 0; i < variables.getRowDimension(); i++) {
            double x1 = variables.getEntry(i, 0);
            double x2 = variables.getEntry(i, 1);
            double x3 = variables.getEntry(i, 2);
            double x4 = variables.getEntry(i, 3);
            double x5 = variables.getEntry(i, 4);

			double cos = 1 + Math.cos(e * x4);
			double exp = Math.exp(d * x3);
            double valueInPoint = valueAt(constantsVector.toArray());


			gradient[0] += 2 * valueInPoint * x1;
			gradient[1] += 2 * valueInPoint * x1 * x1 * x1 * x2;
			gradient[2] += 2 * valueInPoint * exp * cos;
			gradient[3] += 2 * valueInPoint * c * exp * cos * x3;
			gradient[4] -= 2 * valueInPoint * c * exp * Math.sin(e * x4) * x4;
			gradient[5] += 2 * valueInPoint * x4 * x5 * x5;
		}

        return new ArrayRealVector(gradient);
    }

}