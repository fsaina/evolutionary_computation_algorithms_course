package hr.fer.zemris.optjava.dz8.Algorithm.Differentation.BaseVector;

import hr.fer.zemris.optjava.dz8.Algorithm.Unit;

import java.util.List;
import java.util.Random;

public class RandomBaseVector implements DEABaseVectorFunction {

    @Override
    public Unit apply(List<Unit> population) {
        Random random = new Random();
        int index = random.nextInt(population.size());
        return population.get(index);
    }
}
