package hr.fer.zemris.generic.ga.mutation;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.Solution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class NormalMutation implements IMutation {

	private int width;
	private int height;
	private double valueSigma;

	public NormalMutation(double sigma, int w, int h) {
		this.valueSigma = sigma;
		this.width = w;
		this.height = h;
	}

	@Override
	public GASolution<int[]> mutate(GASolution<int[]> chr) {
		int[] data = chr.getData().clone();
		int size = (data.length - 1) / 5;

		IRNG rand = RNG.getRNG();

		data[0] = get(rand, data[0], 0, 255);

		for (int i = 0; i < size; i++) {

			data[i * 5 + 1] = get(rand, data[i * 5 + 1], 0, width - 2);
			data[i * 5 + 2] = get(rand, data[i * 5 + 2], 0, height - 2);
			data[i * 5 + 3] = get(rand, data[i * 5 + 3], 1, width - 1 - data[i * 5 + 1]);
			data[i * 5 + 4] = get(rand, data[i * 5 + 4], 1, height - 1 - data[i * 5 + 2]);
			data[i * 5 + 5] = get(rand, data[i * 5 + 5], 0, 255);
		}
		return new Solution(data);
	}
	
	private int get(IRNG rand, int data, int min, int max) {
		int val = (int) (rand.nextGaussian() * valueSigma + data);
		if (val < min) {
			val = min;
		}
		if (val > max) {
			val = max;
		}
		return val;
	}
}
