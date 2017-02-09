package hr.fer.zemris.optjava.dz5.part2;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public class QuadratticAssignment implements IFunction {

    private static final double PENALTY = 1000000000;
    private DataReader dataReader;

    public QuadratticAssignment(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    @Override
    public double getFitness(Chromosome chromosome) {
        return -getCost(chromosome);
    }

    private double getCost(Chromosome chromosome) {

        if (dataReader.getNumberOfVariables() < 3) {
            System.err.println("Too small number of variables");
            System.exit(-1);
        }

        double sum = 0.0;

        //those are indexes
        int previousFactoryNumber = 1;
        for (int factoryNumber = 2; factoryNumber <= dataReader.getNumberOfVariables(); factoryNumber++) {

            double material = dataReader.getCosts()[previousFactoryNumber - 1][factoryNumber - 1];
            double length = dataReader.getDistances()[chromosome.getSolution().get(previousFactoryNumber - 1) - 1]
                    [chromosome.getSolution().get(factoryNumber - 1) - 1];

            sum += material * length;

            previousFactoryNumber = factoryNumber;
        }

        if(!noDuplicates(chromosome)){
            sum += PENALTY;
        }

        return sum;
    }

    @Override
    public boolean isOptimum(Chromosome chromosome) {

        if(Math.abs(getCost(chromosome)) == 0.0){
            return true;
        }

        return false;
    }


    private boolean noDuplicates(Chromosome c) {
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < c.getSize(); i++) {

            if (set.contains(c.getSolution().get(i))) {
                return false;
            }

            set.add(c.getSolution().get(i));
        }
        return true;
}
}
