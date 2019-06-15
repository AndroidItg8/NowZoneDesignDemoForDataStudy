package itg8.com.nowzonedesigndemo.utility.activity_algo;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.Const;
import itg8.com.nowzonedesigndemo.utility.Rolling;
import itg8.com.nowzonedesigndemo.utility.posture_algo.PostureDetectionAlgo;
import itg8.com.nowzonedesigndemo.utility.step_algo.StepDetection;

public class ActivityDetection {

    private static final double ACTIVITY_THRESHOLD = 0.7;
    private static final int CONST_LIMIT_DIFF_THRESHOLD = 30000;
    private double[] vectorArr;

    private int index = 0;
    private double zScore;
    private RDataManagerListener listener;

    private static final String TAG = "ActivityDetection ";

    private StepDetection stepDetection;
    private double min=0;
    private double max=0;
    private float x2;
    private float y2;
    private float z2;
    private double valNew;
    private double valOld;

    private PostureDetectionAlgo postureDetection;

    public ActivityDetection(RDataManagerListener listener) {
        this.listener = listener;
        vectorArr = new double[Const.SIZE_ACCEL_VECTOR];
        stepDetection=new StepDetection(listener);
        postureDetection=new PostureDetectionAlgo();
    }


    public void isActivityStarted(float x, float y, float z) {

        checkSteps(x);

        checkPosture(z);

//        checkMovement(x, y, z);
        //  01 June 2019 by Akshay Zadgaokar
        checkMovementV2(x, y, z);
    }

    private void checkPosture(float z) {
        postureDetection.detectPosture(z);
    }

    /**
     * This algo having issue about if device is located on table in still position, this providing movement on that also
     * @param x
     * @param y
     * @param z
     */
    private void checkMovement(float x, float y, float z) {
        double accelVector = Math.sqrt(((x * x) + (y * y) + (z * z)));
        vectorArr[index] = accelVector;
        index++;
        if (index == Const.SIZE_ACCEL_VECTOR) {
            index = 0;
        }
        if(min> accelVector || min==0)
            min= accelVector;
        if(max< accelVector || max==0)
            max= accelVector;

        zScore = getZScore(accelVector);
        Log.d(TAG, "isActivityStarted: "+ zScore);
        if (zScore > ACTIVITY_THRESHOLD)
            listener.onMovement((float) zScore);
        else
            listener.onNoMovement((float) zScore);
    }


    /**
     * Date : 01 June 2019 by Akshay Zadgaokar
     *
     * this algorithm uses as below
     *
     *     valNew=SQRT((X2-X1)^2 +(Y2-Y1)^2 + (Z2-Z1)^2
     *
     *     IF
     *           |valNew-ValOld|>=30000
     *      THEN
     *            movement
     *       ELSE
     *            no_movement
     *
     * @param x
     * @param y
     * @param z
     */
    private void checkMovementV2(float x, float y, float z) {

        if(x2<=0 && y2<=0 && z2<=0)
        {
            x2=x;
            y2=y;
            z2=z;
            return;
        }

        valNew=Math
                .sqrt(
                        (Math.pow(x2-x,2)+Math.pow(y2-y,2)+Math.pow(z2-z,2))
                );

        if(valOld==0){
            valOld=valNew;
            return;
        }

        zScore=Math.abs(valNew-valOld);
        if (zScore> Prefs.getInt(CommonMethod.STEP_THRESHOLD,CONST_LIMIT_DIFF_THRESHOLD)) {
            listener.onMovement((float) zScore);
        }else
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
