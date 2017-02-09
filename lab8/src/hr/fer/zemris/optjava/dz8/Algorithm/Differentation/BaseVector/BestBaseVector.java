package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.BaseVector;

import hr.fer.zemris.optjava.dz8.Algorithm.Unit;

import java.util.List;

/**
 * Created by fsaina-lenovo on 12/20/16.
 */
public class BestBaseVector implements DEABaseVectorFunction {
    @Override
    public Unit apply(List<Unit> population) {
        return findBest(population);
    }

    public static Unit findBest(List<Unit> population){

        Unit best = population.get(0);
        double minError = Double.MAX_VALUE;

        for(Unit u : population){
            if(u.getCurrentError() <= minError){
                minError = u.getCurrentError();
                best = u;
            }
        }

        return best;
    }
}
