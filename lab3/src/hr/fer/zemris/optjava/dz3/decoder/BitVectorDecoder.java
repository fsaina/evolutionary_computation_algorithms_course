package hr.fer.zemris.optjava.dz3.decoder;

/**
 * Created by fsaina-lenovo on 10/24/16.
 */
public abstract class BitVectorDecoder implements IDecoder {

    protected double[] mins;
    protected double[] maxs;
    protected int[] bits;
    private int n;
    private int totalBits;


    public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        this.mins = mins;
        this.maxs = maxs;
        this.bits = bits;
        this.n = n;
    }


    public int getTotalBits() {
        return totalBits;
    }

    public void setTotalBits(int totalBits) {
        this.totalBits = totalBits;
    }

    public int getDimension() {
        return n;
    }

    public void setDimension(int n) {
        this.n = n;
    }
}
