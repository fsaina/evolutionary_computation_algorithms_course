package hr.fer.zemris.optjava.dz3.representations;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public abstract class SingleObjectiveSolution {
    private double fitness;
    private double value;


    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int compareTo(SingleObjectiveSolution sos){
        if(fitness < sos.fitness)  return -1;
        else if (fitness > sos.fitness) return 1;
        return 0;
    }
}
