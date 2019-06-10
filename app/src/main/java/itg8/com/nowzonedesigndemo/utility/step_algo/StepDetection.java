package itg8.com.nowzonedesigndemo.utility.step_algo;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.Rolling;

import static itg8.com.nowzonedesigndemo.common.CommonMethod.STEP_COUNT;

public class StepDetection {

    private static final String TAG = "StepDetection";

    private static final int STEPS_MA_COUNT = 15;
    private static final int STEPS_DETECTION_ITEM_COUNT=3;
    private int stepCount=0;

    //For steps
    private double[] xArr;
    private List<Double> stepDetectionArr;
    private int indexForStep = 0;
    private boolean hasEnoughDataForCount=false;
    private Rolling mA1;
    private double zScore;
    private RDataManagerListener listener;

    public StepDetection(RDataManagerListener listener) {
        this.listener = listener;
        stepCount= Prefs.getInt(STEP_COUNT,0);
        mA1=new Rolling(STEPS_MA_COUNT);
        xArr=new double[STEPS_MA_COUNT];
        stepDetectionArr=new ArrayList<>();
    }

    public void checkSteps(float x) {
        xArr[indexForStep]=x;
        indexForStep++;
        if(indexForStep==STEPS_MA_COUNT){
            if(!hasEnoughDataForCount)
                hasEnoughDataForCount=true;
            indexForStep=0;
        }
        if(hasEnoughDataForCount){
            zScore = getZScore(x);
            mA1.add(zScore);
            Log.d(TAG, "checkSteps() called with:  mA1.add(zScore) = [" + mA1.getaverage() + "]");
            checkIfStep(mA1.getaverage());
        }
    }

    private void checkIfStep(double mA1Avg) {
        stepDetectionArr.add(mA1Avg);
        Log.d(TAG, "checkIfStep() called with: stepDetectionArr = [" + Arrays.toString(stepDetectionArr.toArray()) + "]");
        if(stepDetectionArr.size()>3){
            if(stepDetectionArr.get(1)>stepDetectionArr.get(0) && stepDetectionArr.get(1)>stepDetectionArr.get(2)){
                stepCount++;
                listener.onStepCountReceived(stepCount);
            }
            stepDetectionArr.remove(0);
        }
    }


    public double getZScore(double loadCellVal1) {
        WeakReference<Double> max = new WeakReference<>(max(Arrays.copyOf(xArr,xArr.length)));
        WeakReference<Double> min = new WeakReference<>(min(Arrays.copyOf(xArr,xArr.length)));
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
        Arrays.sort(arr);
//        return Collections.min(Arrays.asList(vectorArr));
        return arr[0];
    }

    public double max(double[] arr) {
        Arrays.sort(arr);
//        return Collections.max(Arrays.asList(vectorArr));
        return arr[arr.length-1];
    }
}
