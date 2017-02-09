package hr.fer.zemris.optjava.dz3.representations;

import java.util.Random;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public class DecimalSolution extends SingleObjectiveSolution {

    private double[] solutions;
    private Random random = new Random();
    private double max;
    private double min;


    public DecimalSolution(double max ,double min, int number){

        solutions = new double[number];
        random = new Random();
        this.max = max;
        this.min = min;

        for(int i =0 ;i < number ; i++){
            solutions[i] = random.nextDouble() * ( max - min) + min;
        }

    }

    public DecimalSolution copy(){
        return new DecimalSolution(this.max, this.min, solutions.clone());
    }

    public DecimalSolution(double max, double min , double[] solutions){
        this.solutions = solutions;
        this.max = max;
        this.min = min;
    }

    public void setSolutions(double[] solutions){
        this.solutions = solutions;
    }

    public double[] getSolutions(){
        return solutions;
    }

    public DecimalSolution getNeighbour(){


        DecimalSolution copy = copy();

        double[] array = copy.getSolutions();
        for(int i = 0 ; i < array.length ; i++){

            array[i] += random.nextGaussian();

        }
        copy.setSolutions(array);
        return copy;
    }
}
