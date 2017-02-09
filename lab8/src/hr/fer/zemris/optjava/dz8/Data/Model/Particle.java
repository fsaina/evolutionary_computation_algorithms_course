package hr.fer.zemris.optjava.dz8.Data.Model;

import hr.fer.zemris.optjava.dz8.Data.Data;
import hr.fer.zemris.optjava.dz8.NeuralNetwork.FFANN;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;
import java.util.Random;

public class Particle extends Solution {
    private RealVector velocity;

    private List<Particle> neighbourhood;
    private FFANN function;

    private RealVector personalBestPosition;
    private double personalBestScore;

    private static final double C1 = 2.05; // personal contribution
    private static final double C2 = 2.05; // local contribution

    public Particle(int positionVectorSize, int velocityVectorSize,
                    double vmax,
                    double vmin,
                    Random random,
                    FFANN function) {

        super(random, positionVectorSize);
        velocity = createVector(velocityVectorSize, vmax, vmin, random);
        this.function = function;

        personalBestScore = Double.MAX_VALUE;
        globalBestScore = Double.MAX_VALUE;
    }

    private RealVector createVector(int velocityVectorSize, double vmax, double vmin, Random random) {
        RealVector vector = new ArrayRealVector(velocityVectorSize);

        for (int i = 0; i < vector.getDimension(); i++) {
            vector.setEntry(i, random.nextDouble() * (vmax - vmin) + vmin);
        }

        return vector;
    }

    //search in local neighbourhood
    private RealVector findGlobalBestPosition() {
        double bestScore = getPersonalBestScore();
        RealVector bestVectorPosition = getPersonalBestPosition();

        for (Particle particle : neighbourhood) {
            if (particle.getPersonalBestScore() > bestScore) {
                bestScore = particle.getPersonalBestScore();
                bestVectorPosition = particle.getPersonalBestPosition();
            }
        }

        return bestVectorPosition;
    }


    public RealVector getPersonalBestPosition() {
        return personalBestPosition;
    }


    /**
     * Score for the personal best position found
     *
     * @return
     */
    public double getPersonalBestScore() {
        return personalBestScore;
    }

    public void evaluate(Data data) {
        double error = evaluate(data, function);
        //smaller is better
        if (error < personalBestScore) {
            personalBestPosition = getSolution();        //positions are weights
            personalBestScore = error;
        }
    }

    public void updateVelocityAndPosition(double intertionFactor, Random random) {
        RealVector globalBest = findGlobalBestPosition();

        RealVector pbv = personalBestPosition.subtract(getSolution());  //personal best vector
        RealVector lbv = globalBest.subtract(getSolution());     //local best vector

        RealVector inertionContribution = velocity.mapMultiply(intertionFactor);

        RealVector velocityVector = createVectorVelocity(
                inertionContribution,
                pbv,
                lbv,
                C1,
                C2,
                random);

        setSolution(getSolution().add(velocityVector));
        velocity = velocityVector;
    }

    private RealVector createVectorVelocity(
            RealVector intertion,
            RealVector personal,
            RealVector local,
            double C1,
            double C2,
            Random random) {

        RealVector personalVector;
        RealVector localVector;
        RealVector finVector;

        personalVector = generateRandomMatrix(random, personal.getDimension()).scalarMultiply(C1).operate(personal);
        localVector = generateRandomMatrix(random, local.getDimension()).scalarMultiply(C2).operate(local);
        finVector = intertion.add(personalVector).add(localVector);


        return finVector;
    }


    private RealMatrix generateRandomMatrix(Random random, int n) {

        RealMatrix matrix = new Array2DRowRealMatrix(n, n);

        for (int i = 0; i < n; i++) {
            matrix.setEntry(i, i, random.nextDouble());
        }

        return matrix;
    }

    public void setLocalNeighborhood(List<Particle> localNeighborhood) {
        this.neighbourhood = localNeighborhood;
    }
}
