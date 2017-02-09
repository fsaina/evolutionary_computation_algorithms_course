package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public class Chromosome {

    private List<Integer> solution;

    private Chromosome(){
        solution = new ArrayList<>();
    }


    public static Chromosome generateRandom(int size, Random random){
        //counting starts from 1

        Chromosome chromosome = new Chromosome();

        for(int i =1 ; i <= size; i++){

            if(chromosome.solution.size() == 0){
                chromosome.solution.add(i);
            }else {
                int indexRandom = random.nextInt(chromosome.solution.size());
                chromosome.solution.add(indexRandom, i);
            }
        }

        return chromosome;
    }

    public int getSize() {
        return solution.size();
    }

    public List<Integer> getSolution() {
        return solution;
    }

    public Chromosome copy() {

        Chromosome copy = new Chromosome();
        copy.solution = new ArrayList<>(solution);

        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Integer i : solution){
            sb.append(i +", ");
        }
        return sb.toString();
    }
}
