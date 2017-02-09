package hr.fer.zemris.optjava.dz7.Algorithm;

import hr.fer.zemris.optjava.dz7.Data.*;
import hr.fer.zemris.optjava.dz7.Data.Model.Particle;
import hr.fer.zemris.optjava.dz7.NeuralNetwork.FFANN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


public class PSOAlgorithm implements IAlgorithm {

    private final double maxError;
    private List<Particle> particles;
    private int maxIterations;
    private Data data;
    private FFANN evaluationFunction;

    private static final double INITIAL_WEIGHT = 0.9;
    public static final double VMIN = 0.1;
    public static final double VMAX = 1200;

    public PSOAlgorithm(int populationSize,
                        int neighboorhoodSize,
                        int maxIterations,
                        double maxError,
                        final FFANN evaluationFunction,
                        Data data) {

        this.evaluationFunction = evaluationFunction;
        this.particles = initializeParticelPopulation(
                populationSize,
                evaluationFunction.getWeightsCount(),
                VMIN,
                VMAX);

        this.maxIterations = maxIterations;
        this.data = data;
        this.maxError = maxError;
        declareNnearestNeighbourhood(particles, neighboorhoodSize);
    }

    private List<Particle> neighboorhoodList(List<Particle> particles){
        Particle[] array = new Particle[particles.size()*2];

        IntStream.range(0, particles.size()).forEach( i -> {
            array[i] = particles.get(i);
            array[i+particles.size()] = particles.get(i);
        });

        return Arrays.asList(array);
    }

    private void declareNnearestNeighbourhood(List<Particle> particles, int windowSize) {
        List<Particle> localPart = neighboorhoodList(particles);
        for(int i = 0; i < particles.size(); i++){
            List<Particle> localParticles = new ArrayList<>();

            for(int m = i; m < i+2*windowSize; m++){
                localParticles.add(localPart.get(m));
            }
            particles.get(i).setLocalNeighborhood(localPart);
        }
    }

    public void run() {

        double weigth = INITIAL_WEIGHT;
        double step = (-weigth) / maxIterations;
        int iteration = 1;
        Random random = new Random();

        while (iteration < maxIterations && Particle.globalBestScore > maxError) {
            final double weightF = weigth;

            particles.stream().forEach(p -> p.evaluate(data));
            particles.stream().forEach(p -> p.updateVelocityAndPosition(weightF, random));

            weigth += step;
            iteration++;

            System.out.println(String.format(" %4d / %4d , score ->  %2.3f",
                    iteration,
                    maxIterations,
                    Particle.globalBestScore));
        }
    }

    private List<Particle> initializeParticelPopulation(
            int populationSize, int dim, double vmin, double vmax) {

        List<Particle> particlelist = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            particlelist.add(new Particle(dim, dim, vmax, vmin, random, evaluationFunction));
        }

        return particlelist;
    }
}
