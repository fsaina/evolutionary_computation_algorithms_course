package hr.fer.zemris.optjava.dz11;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.Evaluator;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.Population;
import hr.fer.zemris.generic.ga.Solution;
import hr.fer.zemris.generic.ga.alg.GeneticAlgorithm1;
import hr.fer.zemris.generic.ga.alg.IAlgorithm;
import hr.fer.zemris.generic.ga.crossover.ICrossover;
import hr.fer.zemris.generic.ga.crossover.UniformCrossover;
import hr.fer.zemris.generic.ga.img.ImageProvider;
import hr.fer.zemris.generic.ga.img.ThreadLocalImageProvider;
import hr.fer.zemris.generic.ga.mutation.IMutation;
import hr.fer.zemris.generic.ga.mutation.NormalMutation;
import hr.fer.zemris.generic.ga.selection.ISelection;
import hr.fer.zemris.generic.ga.selection.Tournament;

public class Pokretac1 {

	private static final int N = 2;

	public static void main(String[] args) {

		if (args.length != 7) {
			System.out.println("Invalid number of arguments!");
			System.exit(0);
		}

		String origPath = args[0];
		int sqrNum = Integer.parseInt(args[1]);
		int popSize = Integer.parseInt(args[2]);
		int maxiter = Integer.parseInt(args[3]);
		double minFit = Double.parseDouble(args[4]);
		String optParPath = args[5];
		String genImgPath = args[6];

		File origImgFile = new File(origPath);
		GrayScaleImage template = null;

		try {
			template = GrayScaleImage.load(origImgFile);
		} catch (IOException e) {
			System.err.println("Error loading image");
			System.exit(-1);
		}

		int imgWidth = template.getWidth();
		int imgHeight = template.getHeight();
		ImageProvider imgProvider = new ThreadLocalImageProvider(imgWidth, imgHeight);
		Evaluator evaluator = new Evaluator(template, imgProvider);
		Population pop = initPop(popSize, sqrNum, imgWidth, imgHeight);
		ISelection selection = new Tournament(N);
		ICrossover crossover = new UniformCrossover();

		double sigma = 5;
		IMutation mutation = new NormalMutation(sigma, imgWidth, imgHeight);

		IAlgorithm alg = new GeneticAlgorithm1(maxiter, pop, minFit, popSize, imgWidth, imgHeight, selection, crossover, mutation,
				evaluator);
		GASolution<int[]> sol = alg.runAlg();

		File genImgFile = new File(genImgPath);
		GrayScaleImage genImg = evaluator.draw(sol, null);

		try {
			genImg.save(genImgFile);
			FileWriter fw = new FileWriter(optParPath);
			fw.write(sol.toString());
			fw.close();
		} catch (IOException e) {
			System.err.println("Error!");
			e.printStackTrace();
		}
	}

	private static Population initPop(int popSize, int sqrNum, int imgWidth, int imgHeight) {
		List<GASolution<int[]>> sols = new ArrayList<>(popSize);
		Random rand = new Random();
		for (int i = 0; i < popSize; i++) {
			int[] data = new int[1 + 5 * sqrNum];
			data[0] = rand.nextInt(256);
			for (int j = 0; j < sqrNum; j++) {
				data[5 * j + 1] = rand.nextInt(imgWidth - 2);
				data[5 * j + 2] = rand.nextInt(imgHeight - 2);
				data[5 * j + 3] = (int) (rand.nextInt(imgWidth - data[5 * j + 1]) * 0.1) + 1;
				data[5 * j + 4] = (int) (rand.nextInt(imgHeight - data[5 * j + 2]) * 0.1) + 1;
				data[5 * j + 5] = rand.nextInt(256);
			}
			sols.add(new Solution(data));
		}
		return new Population(sols);
	}
}
