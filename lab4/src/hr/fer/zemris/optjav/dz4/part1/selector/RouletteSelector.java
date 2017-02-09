package hr.fer.zemris.optjav.dz4.part1.selector;

import hr.fer.zemris.optjav.dz4.part1.ChromosomeElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public class RouletteSelector implements ISelector {

    @Override
    public List<ChromosomeElement> select(List<ChromosomeElement> population) {

        double fitnessSum = 0;
        double worstScore = Double.MAX_VALUE; //infact best
        double[] fitness = new double[population.size()];
        Random random = new Random();

        for(ChromosomeElement chromosomeElement : population){
            fitnessSum += Math.abs(chromosomeElement.getScore());
            if(chromosomeElement.getScore() < worstScore){
                worstScore = chromosomeElement.getScore();
            }
        }

        for(int i =0 ; i< population.size() ; i++){
            fitness[i] = (Math.abs(population.get(i).getScore()))/fitnessSum;
        }

        List<ChromosomeElement> choosen = new ArrayList<>();
        while (choosen.size() != 2) {
            double choose = random.nextDouble();

            double sum = 0;
            for(int m = 0 ; m < population.size(); m++){

                if(choose >= sum && choose <= sum+fitness[m]){
                    choosen.add(population.get(m));
                    break;
                }

                sum += fitness[m];
            }

            choosen.add(population.get(population.size()-1));
        }

        return choosen;
    }
}
