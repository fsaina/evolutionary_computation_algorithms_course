package hr.fer.zemris.generic.ga.alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.IGAEvaluator;
import hr.fer.zemris.generic.ga.Population;
import hr.fer.zemris.generic.ga.Solution;
import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.jobs.Job;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.optjava.rng.EVOThread;

public class GeneticAlgorithm1 implements IAlgorithm, Runnable {

	// poison solution
	public static final GASolution<int[]> POISON_PILL = new Solution(new int[1]);


	private boolean hasBestChanged;
	private double minimalFitness;
	private int maxnumberOfIterations;
	private int populationSize;
	private int imgWidth;
	private int imgHeight;
	private ISelection selection;
	private ICrossover crossover;
	private IMutation mutation;
	private Population population;
	private GASolution<int[]> globalBest = null;
	private IGAEvaluator<int[]> evaluator;

	public GeneticAlgorithm1(int maximumiteration,
							 Population population,
							 double minimalFitness,
							 int populationSize,
							 int imgWidth,
							 int imgHeight,
							 ISelection selection,
							 ICrossover crossover,
							 IMutation mutation,
							 IGAEvaluator<int[]> evaluator) {

		this.maxnumberOfIterations = maximumiteration;
		this.population = population;
		this.minimalFitness = minimalFitness;
		this.populationSize = populationSize;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.selection = selection;
		this.crossover = crossover;
		this.mutation = mutation;
		this.evaluator = evaluator;
	}

	@Override
	public GASolution<int[]> runAlg() {
		Thread thread = new AlgorithmThread(this, imgWidth, imgHeight);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return globalBest;
	}
	
	private GASolution<int[]> algorithmRun() {

		GASolution<int[]> best;

		System.out.println("Running algorithm: ");
		for (int iter = 0; iter < maxnumberOfIterations; iter++) {
			hasBestChanged = false;
			evaluate();

			best = Collections.min(population.getSols());
			if (globalBest == null || best.compareTo(globalBest) < 0) {
				globalBest = best;
				hasBestChanged = true;
			}

			if (Math.abs(globalBest.fitness) < minimalFitness) {
				return globalBest;
			}

			if (hasBestChanged) {
				System.out.println("Iteration: " + (iter + 1) + " ->  " + best.fitness);
			}

			List<GASolution<int[]>> newPop = new ArrayList<>(populationSize);

			for (int i = 0; i < populationSize; i++) {
				GASolution<int[]> parent1 = selection.select(population);
				GASolution<int[]> parent2 = selection.select(population);
				GASolution<int[]> child = crossover.cross(parent1, parent2);
				child = mutation.mutate(child);
				newPop.add(child);
			}

			population = new Population(newPop);
		}

		evaluate();

		best = Collections.min(population.getSols());

		if (best.compareTo(globalBest) < 0) {
			globalBest = best;
		}

		return globalBest;
	}

	private void evaluate() {

		int procNum = Runtime.getRuntime().availableProcessors();

		List<GASolution<int[]>> sols = population.getSols();
		Queue<GASolution<int[]>> r = new ConcurrentLinkedQueue<>(sols);
		Queue<GASolution<int[]>> q = new ConcurrentLinkedQueue<>();

		Thread[] threads = new Thread[procNum];

		for (int i = 0; i < procNum; i++) {
			r.add(POISON_PILL);
			threads[i] = new EVOThread(new Job(evaluator, r, q), imgWidth, imgHeight);
		}
		for (int i = 0; i < procNum; i++) {
			threads[i].start();
		}
		for (int i = 0; i < procNum; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
		}

	}

	@Override
	public void run() {
		algorithmRun();
	}

}
