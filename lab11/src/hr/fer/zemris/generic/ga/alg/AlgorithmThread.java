package hr.fer.zemris.generic.ga.alg;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.img.ImageProvider;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class AlgorithmThread extends Thread implements ImageProvider, IRNGProvider {

	private IRNG rngImplementation = new RNGRandomImpl();
	private GrayScaleImage image;

	public AlgorithmThread(Runnable r, int w, int h) {
		super(r);
		image = new GrayScaleImage(w, h);
	}

	@Override
	public IRNG getRNG() {
		return rngImplementation;
	}

	@Override
	public GrayScaleImage getImage() {
		return image;
	}

}
