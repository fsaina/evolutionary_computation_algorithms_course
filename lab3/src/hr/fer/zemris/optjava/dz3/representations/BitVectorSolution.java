package hr.fer.zemris.optjava.dz3.representations;

import java.util.Random;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public class BitVectorSolution extends SingleObjectiveSolution {


    private String[] bits;
    private int size;
    private int[] sizes;


    public BitVectorSolution(int[] sizes) {
        this.size = sizes.length;
        this.sizes = sizes;
        this.bits = new String[size];

        for(int i =0 ; i < sizes.length ; i++){
            bits[i] = generateRandomBitString(sizes[i], new Random());
        }

    }

    public int getSize() {
        return size;
    }

    public BitVectorSolution newLikeThis() {
        return new BitVectorSolution(sizes);
    }

    public BitVectorSolution duplicate() {
        BitVectorSolution solition = newLikeThis();

        int index = 0;
        for (String b : bits) {
            solition.bits[index] = b;
            index++;
        }

        return solition;
    }

    public String[] getBits() {
        return bits;
    }

    public void setBits(String[] bits) {
        this.bits = bits;
    }

    private String generateRandomBitString(int size, Random randomGenerator){

        StringBuilder sb = new StringBuilder();

        for(int i =0 ; i < size ; i++){

            double random = randomGenerator.nextDouble();

            if(random < 0.5){
                sb.append('0');
            } else {
                sb.append('1');
            }
        }

        return sb.toString();
    }

    public BitVectorSolution randomize(Random randomGenerator) {


        for (int i = 0; i < bits.length; i++) {


            String binaryString = bits[i];
            char[] array = binaryString.toCharArray();
            // gaussian distributon to mostly change the least significant bits
            int indexToChange = array.length -1 - (int) Math.floor(Math.abs(randomGenerator.nextGaussian())
                    * array.length /2) % array.length ;

            if(array[indexToChange] == '0'){
                array[indexToChange] = '1';
            } else {
                array[indexToChange] = '0';
            }

            bits[i] = new String(array);
        }

        return this;
    }

}
