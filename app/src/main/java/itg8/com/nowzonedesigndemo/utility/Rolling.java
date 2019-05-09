package itg8.com.nowzonedesigndemo.utility;

import android.util.Log;

import java.math.BigInteger;

/**
 * Created by itg_Android on 3/7/2017.
 */

public class Rolling {

    private int size;
    private double total = 0;
    private int index = 0;
    private double samples[];
    private double max=0;
    private double min=0;
    double sd=0;
    private double average;


    public Rolling(int size) {
        this.size = size;
        samples = new double[size];
        for (int i = 0; i < size; i++) samples[i] = 0d;
    }

    public double getTotal() {
        return total;
    }

    public void add(double x) {
        Log.d("Rolling", "rawadd: "+x);
        if(max==0 || x>max)
            max=x;
        if(min==0 || x<min)
            min=x;
        total -= samples[index];
//        total=total.subtract(BigInteger.valueOf((long) samples[index]));
        samples[index] = x;
        total += x;
//        total.add(x)
        if (++index == size) {
            index = 0; // cheaper than modulus
            max=0;
            min=0;
        }
    }

    public int getIndex(){
        return index;
    }

    public double getStdev(){
        synchronized (this) {
            sd = 0;
            average=getaverage();
            for (int i = 0; i < size; i++) {
//                sd += Math.pow(samples[i] - getaverage(), 2) / size;
                sd += Math.pow(samples[i] - getaverage(), 2);
            }

            return Math.sqrt(sd/size);
        }
    }

    public double getaverage() {
        return total / size;
    }

    public boolean isDiffGreaterThan(double diff){
        return max-min<diff && max!=min;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}