package hr.fer.zemris.optjava.dz4.part2.functions;

import hr.fer.zemris.optjava.dz4.part2.chromosomes.BoxFillElement;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public class BoxFillFunction {

    private static final int PENALTY = -100;
    private int lengthSumToMeet;

    public BoxFillFunction(int lengthSumToMeet) {
        this.lengthSumToMeet = lengthSumToMeet;
    }

    // fitness function
    public double calculateValueIn(BoxFillElement box, int height) {

        int score = 0;
        for (int i =0 ; i < box.getSolution().size(); i++){
            int sizeForRow = box.sumSize(box.getSolution().get(i));

            if(sizeForRow == 0){
               score++;
            }

            if(sizeForRow > height){
                score += PENALTY;
            }

        }

        if(box.sumTotalNotMet(lengthSumToMeet)){
            score += PENALTY;
        }
        return score;
    }


}
