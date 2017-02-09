package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.Cross;

import org.apache.commons.math3.linear.RealVector;

import java.util.Random;

/**
 * Created by fsaina-lenovo on 12/20/16.
 */
public class ExpCross implements DEACrossFunction {

    private static final double CR = 0.1;

    @Override
    public RealVector apply(RealVector u, RealVector mutant) {
        RealVector probeVec = u.copy();

        Random random = new Random();

        int start = random.nextInt(u.getDimension());

        for(int i =start ; i< u.getDimension(); i++){
            if(random.nextDouble() < CR) probeVec.setEntry(i, mutant.getEntry(i));
            else probeVec.setEntry(i, u.getEntry(i));
        }

        return probeVec;
    }
}
