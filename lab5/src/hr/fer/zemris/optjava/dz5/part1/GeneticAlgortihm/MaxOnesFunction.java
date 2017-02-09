package hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public class MaxOnesFunction {

    private int n;

    public MaxOnesFunction(int n) {
        this.n = n;
    }

    public double getFitness(Chromosome chromosome){

        int i = chromosome.numberOfOnes();

        if(i <= 0.8 * n){
            return i /(double) n;
        } else if ( i > 0.8 * n && i <= 0.9*n) {
            return 0.8;
        } else {
            return (2 * i/(double)n)-1;
        }
    }

    public boolean isOptimum(Chromosome chromosome){
        if(getFitness(chromosome) == 1){
            return true;
        }

        return false;
    }
}
