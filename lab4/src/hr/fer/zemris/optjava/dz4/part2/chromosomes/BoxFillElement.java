package hr.fer.zemris.optjava.dz4.part2.chromosomes;

import hr.fer.zemris.optjava.dz4.part2.functions.BoxFillFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Single chromosome(unit) in the genetic algorithm.
 *
 * It is made as a Matrix of columns (that can hold at most 20 size sticks),
 * and rows that are determined by the input
 */
public class BoxFillElement implements Comparable{

    private List<List<Integer>> solution;
    private double score;
    private int height;

    public BoxFillElement(int width, int height){
        solution = new ArrayList<>();

        for (int i = 0; i < width ; i++) {
            solution.add(new ArrayList<Integer>());
        }

        this.height = height;
    }

    public BoxFillElement(List<List<Integer>> solution){
        this.solution = solution;
    }

    public static int sumSize(List<Integer> box) {

        int sum = 0;

        for(int i =0; i < box.size(); i++){
            sum += box.get(i);
        }

        return sum;
    }

    public List<Integer> getRow(int i){
        return solution.get(i);
    }

    public void evaluate(BoxFillFunction function){
        score = function.calculateValueIn(this, height);
    }

    public double getScore() {
        return score;
    }

    public List<List<Integer>> getSolution() {
        return solution;
    }

    public void setRow(int rowIndex, List<Integer> values){
        solution.set(rowIndex, values);
    }

    @Override
    public int compareTo(Object o) {

        BoxFillElement elem2 = (BoxFillElement) o;

        if(getScore() > elem2.getScore()){
            return -1;
        } else if (getScore() < elem2.getScore()){
            return 1;
        } else {
            return 0;
        }

    }

    public void randomAppend(List<Integer> sticks_copy) {

        while(sticks_copy.size() != 0){

            for(List<Integer> col : solution){

                if(sumSize(col) < 20){

                    List<Integer> toRemove = new ArrayList<>();
                    for(Integer i :sticks_copy){

                        if(sumSize(col) + i <= 20){
                            col.add(i);

                            toRemove.add(i);
                        }

                    }
                    sticks_copy.removeAll(toRemove);
                }
            }
        }
    }

    public boolean sumTotalNotMet(int sumToMeet) {
        int sum =0;
        for(List<Integer> i : solution){
            for(Integer in : i){
                sum += in;
            }
        }

        return sum != sumToMeet;
    }
}
