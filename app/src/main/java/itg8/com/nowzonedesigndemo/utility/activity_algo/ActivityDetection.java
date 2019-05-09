package itg8.com.nowzonedesigndemo.utility.activity_algo;

import java.lang.ref.WeakReference;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.Const;
import itg8.com.nowzonedesigndemo.utility.Rolling;

public class ActivityDetection {

    private static final double ACTIVITY_THRESHOLD = 0.7;
    private double[] vectorArr;
    private int index = 0;
    private double zScore;
    private RDataManagerListener listener;

    public ActivityDetection(RDataManagerListener listener) {
        this.listener = listener;
        vectorArr = new double[Const.SIZE_ACCEL_VECTOR];
    }


    public void isActivityStarted(float x, float y, float z) {
        WeakReference<Double> accelVector = new WeakReference<>(Math.sqrt((x * x + y * y + z * z)));
        vectorArr[index] = accelVector.get();
        if (++index == Const.SIZE_ACCEL_VECTOR) {
            index = 0;
        }
        zScore = CommonMethod.getZScore(accelVector.get(), vectorArr);
        if (zScore > ACTIVITY_THRESHOLD)
            listener.onMovement((float) zScore);
        else
            listener.onNoMovement((float) zScore);
    }
}
