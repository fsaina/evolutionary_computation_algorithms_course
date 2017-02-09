package hr.fer.zemris.generic.ga.img;

import hr.fer.zemris.art.GrayScaleImage;

public class ThreadLocalImageProvider implements ImageProvider {

	private ThreadLocal<GrayScaleImage> local;
	private int width;
	private int height;
	
	public ThreadLocalImageProvider(int w, int h) {
		super();
		local = new ThreadLocal<>();
		this.width = w;
		this.height = h;
	}

	@Override
	public GrayScaleImage getImage() {
		GrayScaleImage temp = local.get();
		if (temp != null) {
			return temp;
		}
		temp = new GrayScaleImage(width, height);
		local.set(temp);
		return temp;
	}

}
