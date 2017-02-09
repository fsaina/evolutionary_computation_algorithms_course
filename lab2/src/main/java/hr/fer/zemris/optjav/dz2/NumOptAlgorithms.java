package hr.fer.zemris.optjav.dz2;

import hr.fer.zemris.optjav.dz2.functions.IFunction;
import hr.fer.zemris.optjav.dz2.functions.IHFunction;
import hr.fer.zemris.optjav.dz2.functions.PrijenosnaCostFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fsaina-lenovo on 10/12/16.
 */
public class NumOptAlgorithms {

    private static final double LOCAL_OPTIMUM_TRESHOLD = 0.15;
    private static final double THETA_GRADIENT_TRESHOLD = 0.2;

    private static final double LAMBDA_LOWER_INITIAL_VALUE = 0.0;
    private static final int MAX_NUMBER_OF_STEPS_THETAGRADIENT = 100;

    private static List<RealVector> solutions = new ArrayList<>();

    public static RealVector gradientDescent(IFunction function, int maxNumberOfIterations,
                                             ArrayList<Double> initialPoints,
                                             boolean verbose) {

        if (initialPoints.isEmpty() || initialPoints == null) {
            System.err.println("Please provide valid initial points");
            System.exit(-1);
        }


        RealVector x = convertArrayListToVector(initialPoints);

        int iteration = 1;
        while (true) {
            if (iteration > maxNumberOfIterations) {
                System.out.println("Exceeded number of iterations. Current solution" +
                        " was obtained with cost/fitness value of : " +
                        function.calculateValueIn(x));
                break;
            }

            if (verbose) {
                System.out.println("Solution: " + x + ", iteration: " + iteration);
                solutions.add(x);
            }

            RealVector gradient = function.calculateGradientValueIn(x);

            if (isLocalOptimum(gradient)) {
                break;
            } else {

                RealVector d = gradient.mapMultiply(-1);
                double lambda = bisectionAlgorithm(x, function, d);
                x = x.add(d.mapMultiply(lambda));
            }

            iteration++;
        }

        if (verbose) {
            drawSolutionsOnScreen(solutions);
        }
        return x;
    }

    private static void drawSolutionsOnScreen(final List<RealVector> solutions) {


        Runnable r = new Runnable() {
            public void run() {
                DrawLines lineComponent = new DrawLines(400, 400);

                Point p = new Point();
                p.setLocation(solutions.get(0).getEntry(0),
                        solutions.get(0).getEntry(1));

                for (int i = 1; i < solutions.size(); i++) {

                    lineComponent.addLine(new Line2D.Double(p.getX(),
                            p.getY(),
                            solutions.get(i).getEntry(0),
                            solutions.get(i).getEntry(1)
                    ));

                    p.setLocation(solutions.get(i).getEntry(0),
                            solutions.get(i).getEntry(1));
                }
                JOptionPane.showMessageDialog(null, lineComponent);
            }
        };
        SwingUtilities.invokeLater(r);

    }

    public static RealVector newtonMethod(IHFunction function,
                                          int maxNumberOfIterations,
                                          ArrayList<Double> initialPoints,
                                          boolean verbose) {

        RealVector x = convertArrayListToVector(initialPoints);
        RealVector thao = convertArrayListToVector(initialPoints);

        int iteration = 1;
        while (true) {

            if (iteration > maxNumberOfIterations) {
                System.out.println("Maximum number of iterations exceeded!");
                break;
            }

            if (verbose) {
                System.out.println("Solution: " + x + ", iteration: " + iteration);
                solutions.add(x);
            }

            RealVector gradient = function.calculateGradientValueIn(x);
            RealMatrix hessianMatrix = function.calculateHesseMatrixIn(x);


            if (isLocalOptimum(gradient)) {
                break;
            } else {
                RealMatrix inverse = new LUDecomposition(hessianMatrix).getSolver().getInverse();
                inverse = inverse.scalarMultiply(-1);
                thao = inverse.operate(gradient);

                double lambda = bisectionAlgorithm(x, function, thao);
                x = x.add(thao.mapMultiply(lambda));
            }

            iteration++;
        }

        if (verbose) {
            drawSolutionsOnScreen(solutions);
        }

        return x;
    }

    private static RealVector convertArrayListToVector(ArrayList<Double> initialPoints) {
        RealVector x = new ArrayRealVector(initialPoints.size());

        for (int i = 0; i < initialPoints.size(); i++) {
            x.setEntry(i, initialPoints.get(i));
        }

        return x;
    }

    private static double bisectionAlgorithm(RealVector x, IFunction function, RealVector d) {

        double lambdaLower = LAMBDA_LOWER_INITIAL_VALUE;
        double lambdaUpper;

        int power = 0;
        double thetaGradientValue;
        do {
            lambdaUpper = Math.pow(2, power);
            power++;

            thetaGradientValue = calculateThetaGradientInPoint(x, function, d, lambdaUpper).dotProduct(d);
        } while (thetaGradientValue <= 0);

        int k = 0;
        double lambda;
        while (true) {

            lambda = (lambdaLower + lambdaUpper) / 2;

            RealVector thetaGradient = calculateThetaGradientInPoint(x, function, d, lambda);
            double slope = -1 * thetaGradient.dotProduct(x);

            if (Math.abs(slope) < THETA_GRADIENT_TRESHOLD) {
                //it fell into the local optima
                break;

            } else if (slope > 0) {
                lambdaUpper = lambda;
            } else {
                lambdaLower = lambda;
            }

            if (k > MAX_NUMBER_OF_STEPS_THETAGRADIENT) {
                break;
            }
            k++;
        }

        return lambda;
    }


    /**
     * Method calculates the theta gradient in the point x.
     *
     * @param x
     * @param function
     * @param d
     * @param lambda
     * @return
     */
    private static RealVector calculateThetaGradientInPoint(RealVector x, IFunction function, RealVector d, double lambda) {
        RealVector gradientPoint = x.add(d.mapMultiply(lambda));
        RealVector theta = function.calculateGradientValueIn(gradientPoint);
        if(function instanceof PrijenosnaCostFunction){
            return theta.ebeMultiply(d);
        }

        return theta;
    }


    private static boolean isLocalOptimum(RealVector gradient) {

        for (double d : gradient.toArray()) {
            if (Math.abs(d) > LOCAL_OPTIMUM_TRESHOLD)
                return false;
        }

        return true;
    }
}
