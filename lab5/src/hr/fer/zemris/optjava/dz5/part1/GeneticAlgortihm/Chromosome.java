package hr.fer.zemris.optjava.dz5.part1.GeneticAlgortihm;

import java.util.BitSet;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 11/14/16.
 */
public class Chromosome {

    private BitSet bitSet;
    private int size;

    private Chromosome(int size) {
        this.bitSet = new BitSet(size);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    public int numberOfOnes() {
        int ones = 0;
        for(int i =0 ; i< bitSet.size(); i++){
            if(bitSet.get(i) == true){
                ones++;
            }
        }

        return ones;
    }

    public static Chromosome generateRandom(int size, Random random) {
        Chromosome chromosome = new Chromosome(size);

        for(int i = 0; i< size; i++){

            double rand = random.nextDouble();
            if(rand < 0.5){
               chromosome.bitSet.set(i);
            } else {
               chromosome.bitSet.clear(i);
            }
        }

        return chromosome;
    }

    public Chromosome copy() {
        Chromosome copy = new Chromosome(size);

        for(int i = 0 ; i < size; i++){
            copy.getBitSet().set(i, bitSet.get(i));
        }

        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i =0 ; i< size; i++){
            if(bitSet.get(i)){
                sb.append("1");
            }else {
                sb.append("0");
            }
        }

        return sb.toString();
    }
}
