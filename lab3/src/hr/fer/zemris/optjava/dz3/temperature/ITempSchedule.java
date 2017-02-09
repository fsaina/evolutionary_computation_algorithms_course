package hr.fer.zemris.optjava.dz3.temperature;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public interface ITempSchedule {

    public double getNextTemperature();
    public int getInnerLoopCounter();
    public int getOuterLoopCounter();

}
