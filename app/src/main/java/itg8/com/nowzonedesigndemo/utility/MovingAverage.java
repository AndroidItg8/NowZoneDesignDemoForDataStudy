package itg8.com.nowzonedesigndemo.utility;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by swapnilmeshram on 24/10/17.
 */

public class MovingAverage {
    private Queue<Double> mWindow =new LinkedList<>();
    private int mPeriod;
    private double mSum=0;

    public MovingAverage(int mPeriod){
        this.mPeriod=mPeriod;
    }

    public void add(Double value){
        mSum=mSum+value;
        mWindow.add(value);
        if(mWindow.size()>mPeriod){
            mSum=mSum-mWindow.remove();
        }
    }

    public double getAverage(){
        if(mWindow.isEmpty()){
            return 0;
        }
        return mSum/mWindow.size();
    }
}
