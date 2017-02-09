package hr.fer.zemris.generic.ga.alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.IGAEvaluator;
import hr.fer.zemris.generic.ga.Population;
import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.jobs.CompleteJob;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.optjava.rng.EVOThread;

public class GeneticAlgorithm2 implements IAlgorithm, Runnable {

	private int maximumIteration;
	private int populationSize;
	private int imgWidth;
	private int imgHeight;
	private double minimumFitness;
	private boolean globalBestChanged;

	private Population population;
	private ICrossover crossover;
	private IMutation mutation;
	private ISelection selection;
	private IGAEvaluator<int[]> evaluator;
	private GASolution<int[]> globalBest = null;

	public GeneticAlgorithm2(int maximumIteration,
							 Population population,
							 double minimalFitness,
							 int populationSize,
							 int imgWidth,
							 int imgHeight,
							 ISelection selection,
							 ICrossover crossover,
							 IMutation mutation,
							 IGAEvaluator<int[]> evaluator) {

		this.maximumIteration = maximumIteration;
		this.population = population;
		this.minimumFitness = minimalFitness;
		this.populationSize = populationSize;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.selection = selection;
		this.crossover = crossover;
		this.mutation = mutation;
		this.evaluator = evaluator;
	}

	private GASolution<int[]> algorithmRun() {

		GASolution<int[]> best;
		System.out.println("Running algorithm:  ");

		for (int iter = 0; iter < maximumIteration; iter++) {
			globalBestChanged = false;

			population = makeChildren();

			best = Collections.min(population.getSols());
			if (globalBest == null || best.compareTo(globalBest) <= 0) {
				globalBest = best;
				globalBestChanged = true;
			}
			if (Math.abs(globalBest.fitness) < minimumFitness) {
				return globalBest;
			}

			if (globalBestChanged) {
				System.out.println("Iteration: " + (iter + 1) + " ->  " + best.fitness);
			}
		}
		best = Collections.min(population.getSols());
		if (best.compareTo(globalBest) < 0) {
			globalBest = best;
		}
		return globalBest;
	}

	private Population makeChildren() {

		int processorNumber = Runtime.getRuntime().availableProcessors();

		Queue<GASolution<int[]>> q = new ConcurrentLinkedQueue<>();
		Thread[] threads = new Thread[processorNumber];
		int generationNumber = populationSize / processorNumber;
		int n_module = populationSize % processorNumber;

		for (int i = 0; i < processorNumber; i++) {
			int childrenNum = generationNumber + (i < n_module ? 1 : 0);
			threads[i] = new EVOThread(
					new CompleteJob(
							childrenNum,
							population,
							q,
							selection,
							crossover,
							mutation,
							evaluator),
					imgWidth,
					imgHeight);
		}

		for (int i = 0; i < processorNumber; i++) {
			threads[i].start();
		}

		for (int i = 0; i < processorNumber; i++) {

			try {
				threads[i].join();
			} catch (InterruptedException e) {
				System.err.println("Error joining threads!");
				System.exit(-1);
			};
		}
		List<GASolution<int[]>> sols = new ArrayList<>(q);

		return new Population(sols);
	}

	@Override
	public void run() {
		algorithmRun();
	}

	@Override
	public GASolution<int[]> runAlg() {
		return algorithmRun();
	}
}
