package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Cross;

import org.apache.commons.math3.linear.RealVector;

import java.util.Random;

/**
 * Created by fsaina-lenovo on 12/20/16.
 */
public class BinaryCross implements DEACrossFunction{

    private static final double CR = 0.5;

    @Override
    public RealVector apply(RealVector u, RealVector mutant) {
        RealVector probeVec = u.copy();

        Random random = new Random();

        for(int i =0 ; i< u.getDimension(); i++){

            if(random.nextDouble() < CR) probeVec.setEntry(i, u.getEntry(i));
            else probeVec.setEntry(i, mutant.getEntry(i));
        }

        return probeVec;
    }
}
