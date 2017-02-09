package hr.fer.zemris.optjav.dz4.part1.selector;

import hr.fer.zemris.optjav.dz4.part1.ChromosomeElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public class TournamentSelection implements ISelector {

    private int numberOfChromosomes;

    public TournamentSelection(int num){
        this.numberOfChromosomes = num;
    }

    @Override
    public List<ChromosomeElement> select(List<ChromosomeElement> population) {

        List<ChromosomeElement> parentList = new ArrayList<>();
        Random random = new Random();


        while(parentList.size() != 2){
            List<ChromosomeElement> tmp = new ArrayList<>();

            for(int i = 0 ; i< numberOfChromosomes ; i++) {
                int selection = random.nextInt(population.size());
                tmp.add(population.get(selection));
            }

            //sort
            Collections.sort(tmp);
            parentList.add(tmp.get(0));
        }

        return parentList;
    }
}
