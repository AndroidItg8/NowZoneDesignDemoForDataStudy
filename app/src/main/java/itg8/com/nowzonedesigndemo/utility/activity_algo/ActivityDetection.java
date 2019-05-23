package itg8.com.nowzonedesigndemo.utility.activity_algo;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.Const;
import itg8.com.nowzonedesigndemo.utility.Rolling;
import itg8.com.nowzonedesigndemo.utility.step_algo.StepDetection;

public class ActivityDetection {

    private static final double ACTIVITY_THRESHOLD = 0.7;
    private double[] vectorArr;

    private int index = 0;
    private double zScore;
    private RDataManagerListener listener;

    private static final String TAG = "ActivityDetection";

    private StepDetection stepDetection;
    private double min=0;
    private double max=0;

    public ActivityDetection(RDataManagerListener listener) {
        this.listener = listener;
        vectorArr = new double[Const.SIZE_ACCEL_VECTOR];
        stepDetection=new StepDetection(listener);
    }

    private double accelVector;


    public void isActivityStarted(float x, float y, float z) {

        checkSteps(x);

        accelVector =Math.sqrt(((x * x) + (y * y) + (z * z)));
        vectorArr[index] = accelVector;
        index++;
        if (index == Const.SIZE_ACCEL_VECTOR) {
            index = 0;
        }
        if(min>accelVector || min==0)
            min=accelVector;
        if(max<accelVector || max==0)
            max=accelVector;

        zScore = getZScore(accelVector);
        Log.d(TAG, "isActivityStarted: "+ zScore);
        if (zScore > ACTIVITY_THRESHOLD)
            listener.onMovement((float) zScore);
        else
            listener.onNoMovement((float) zScore);
    }


    private void checkSteps(float x) {
        Observable.just(x).map(new Function<Float, Object>() {
            @Override
            public Object apply(Float x) throws Exception {
                Log.d(TAG, "apply() called with: x = [" + x + "]");
                 stepDetection.checkSteps(x);
                return x;
            }
        }).subscribeOn(Schedulers.computation()).subscribe();

    }


    public double getZScore(double loadCellVal1) {
        WeakReference<Double> max = new WeakReference<>(max(Arrays.copyOf(vectorArr,vectorArr.length)));
        WeakReference<Double> min = new WeakReference<>(min(Arrays.copyOf(vectorArr,vectorArr.length)));
//        if ((max(arr) - min(arr)) > 0)
//            return ((loadCellVal1 - min(arr)) / (max(arr) - min(arr)));
//        else
//            return 0;
//        Log.d(TAG, "isActivityStarted: max: " + max.get() + " min: " + min.get() + " value:" + loadCellVal1);
        if ((max.get() - min.get()) > 0)
            return ((loadCellVal1 - min.get()) / (max.get() - min.get()));
        else
            return 0;
    }

    public double min(double[] arr) {
//        Arrays.sort(arr);
//        return Collections.min(Arrays.asList(vectorArr));
        return min;
    }

    public double max(double[] arr) {
//        Arrays.sort(arr);
//        return Collections.max(Arrays.asList(vectorArr));
        return max;
    }
}
