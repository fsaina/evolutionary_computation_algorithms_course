package hr.fer.zemris.optjav.dz4.part1;

import hr.fer.zemris.optjav.dz4.part1.functions.IHFunction;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public class ChromosomeElement implements Comparable {

    private RealVector realVector;
    private double score;



    public ChromosomeElement(RealVector realVector) {
        this.realVector = realVector;
    }

    public void evaluate(IHFunction costFunction){
        score = costFunction.calculateValueIn(realVector);
    }

    public double getScore() {
        return score;
    }

    public RealVector getRealVector() {
        return realVector;
    }

    public void setRealVector(RealVector realVector) {
        this.realVector = realVector;
    }

    @Override
    public int compareTo(Object o) {

        ChromosomeElement element2 = (ChromosomeElement) o;

        if(this.score > element2.score){
            return 1;
        } else if (this.score < element2.score){
            return -1;
        } else {
            return 0;
        }

    }
}
