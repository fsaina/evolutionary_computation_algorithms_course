package hr.fer.zemris.optjava.dz4.part2.selectors;

import hr.fer.zemris.optjava.dz4.part2.chromosomes.BoxFillElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 11/7/16.
 */
public class NTurnamentSelector implements IBoxSelector {


    @Override
    public BoxFillElement select(List<BoxFillElement> population, int n, boolean best) {
        Random random = new Random();

        List<BoxFillElement> tmp = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int selection = random.nextInt(population.size());
            tmp.add(population.get(selection));
        }

        //sort
        Collections.sort(tmp);

        if(best){
            return tmp.get(0);
        } else {
            return tmp.get(tmp.size()-1);
        }
    }
}
