package hr.fer.zemris.optjava.dz3.temperature;

import hr.fer.zemris.optjava.dz3.temperature.ITempSchedule;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public class GeometricTempSchedule implements ITempSchedule {

    private double alpha;
    private double currentTemperature;
    private double initialTemperature;
    private int innerLimit;
    private int outerLimit;

    int k = 0;

    public GeometricTempSchedule(double alpha, double initialTemperature, int innerLimit, int outerLimit) {

        if(innerLimit < 0 || outerLimit < 0){
            throw new IllegalArgumentException("Invalid number provided");
        }

        this.alpha = alpha;
        this.currentTemperature = initialTemperature;
        this.initialTemperature = initialTemperature;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;
    }

    @Override
    public double getNextTemperature() {
        //TODO check
        currentTemperature = Math.pow(alpha, k) * initialTemperature;
        innerLimit++;
        k++;
        return currentTemperature;
    }

    @Override
    public int getInnerLoopCounter() {
        return innerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return outerLimit;
    }

}
