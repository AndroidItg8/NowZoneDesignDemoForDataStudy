package itg8.com.nowzonedesigndemo.utility;

/**
 * Created by itg_Android on 3/7/2017.
 */

public class RollingWithSD {

    private int size;
    private double total = 0d;
    private int index = 0;
    private double samples[];
    private double max=0;
    private double min=0;


    public RollingWithSD(int size) {
        this.size = size;
        samples = new double[size];
        for (int i = 0; i < size; i++) samples[i] = 0d;
    }

    public void add(double x) {
        if(max==0 || x>max)
            max=x;
        if(min==0 || x<min)
            min=x;
        total -= samples[index];
        samples[index] = x;
        total += x;
        if (++index == size) {
            index = 0; // cheaper than modulus
            max=0;
            min=0;
        }
    }



    public int getIndex(){
        return index;
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