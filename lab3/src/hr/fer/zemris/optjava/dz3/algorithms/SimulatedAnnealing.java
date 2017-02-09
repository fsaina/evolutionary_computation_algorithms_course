package hr.fer.zemris.optjava.dz3.algorithms;

import hr.fer.zemris.optjava.dz3.decoder.IDecoder;
import hr.fer.zemris.optjava.dz3.functions.IFunction;
import hr.fer.zemris.optjava.dz3.neighbourhood.INeighbourhood;
import hr.fer.zemris.optjava.dz3.representations.SingleObjectiveSolution;
import hr.fer.zemris.optjava.dz3.temperature.ITempSchedule;

import java.util.Random;

/**
 * Simulated annealing algorithm implementation
 */
public class SimulatedAnnealing implements IOptAlgorithm {

    //value under which it will be considered the temperature reached zero
    private static final Double ZERO_VALUE = 0.0001;

    private IDecoder decoder;
    private INeighbourhood neighbourhood;
    private SingleObjectiveSolution startWith;
    private IFunction function;
    private boolean minimize;
    private Random random;
    private ITempSchedule iTempSchedule;
    private double initialTemp;

    public SimulatedAnnealing(IDecoder decoder, INeighbourhood neighbourhood, IFunction function, boolean minimize, ITempSchedule iTempSchedulem,
                              SingleObjectiveSolution initialValue) {
        this.decoder = decoder;
        this.neighbourhood = neighbourhood;
        this.function = function;
        this.minimize = minimize;
        this.startWith = initialValue;
        this.initialTemp = iTempSchedulem.getNextTemperature();
        this.iTempSchedule = iTempSchedulem;
        this.random = new Random();
    }

    @Override
    public SingleObjectiveSolution run() {

        SingleObjectiveSolution solution = startWith;

        int index = 0;
        outer:
        while (iTempSchedule.getOuterLoopCounter() != index) {

            double temperature = iTempSchedule.getNextTemperature();

            System.out.println(temperature);

            if (temperature < ZERO_VALUE) {
                break outer;
            }

            for (int i = 0; i < iTempSchedule.getInnerLoopCounter(); i++) {

                SingleObjectiveSolution neighbour = neighbourhood.randomNeighbour(solution);
                double delta = function.valueAt(decoder.decode(neighbour)) - function.valueAt(decoder.decode(solution));

                if (!minimize) {
                    //we are trying to maximize
                    delta *= -1;
                }

                if (delta <= 0) {
                    solution = neighbour;
                } else {
                    double dice = random.nextDouble();
                    if (dice <= Math.exp(-delta / temperature)) {
                        solution = neighbour;
                    }
                }

            }

            index++;
        }

        return solution;
    }
}
