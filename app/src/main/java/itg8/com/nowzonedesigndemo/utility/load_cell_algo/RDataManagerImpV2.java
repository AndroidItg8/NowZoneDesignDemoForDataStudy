package itg8.com.nowzonedesigndemo.utility.load_cell_algo;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.Rolling;
import itg8.com.nowzonedesigndemo.utility.load_cell_algo.AlgoLoadCellBreathing;

/**
 * This is responsible for calculation uploading and all.
 */

public class RDataManagerImpV2 {


    private static final String TAG = "RDataManagerImpV2";

    /**
     * this veriable use to define rolling average window. W=30
     */
    private static final int ROLLING_AVG_SIZE = 20;
    private static final int DATA_PER_SECOND = 20;

    private static final int DATA_GATHER_LIMIT = DATA_PER_SECOND * 5;

    public static DecimalFormat df2 = new DecimalFormat("#.####");


    private Double[] loadCellRaw;
    private double[] loadCellProcessed;

    private Rolling mMA1;
    private Rolling mMA2;
    private Rolling mMA3;
    private int isGatheringLimit = 0;
    private double tempMin = -1;
    private AlgoLoadCellBreathing algo;
    private double z;
    private long lastPeakTimeStamp = 0;
    private RDataManagerListener listener;
    double max;
    double min;

    public RDataManagerImpV2(RDataManagerListener listener) {
        this.listener = listener;
        mMA1 = new Rolling(ROLLING_AVG_SIZE);
        mMA2 = new Rolling(ROLLING_AVG_SIZE);
        mMA3 = new Rolling(ROLLING_AVG_SIZE);
        loadCellRaw = new Double[DATA_GATHER_LIMIT];
//        loadCellProcessed = new double[DATA_GATHER_LIMIT];
        algo = new AlgoLoadCellBreathing();
    }

    public void onLoadCellDataAvail(DataModelPressure model) {
        WeakReference<Double> loadCellVal1 = new WeakReference<Double>((double) model.getLoadCell1());
        if (isGatheringLimit < DATA_GATHER_LIMIT) {
            loadCellRaw[isGatheringLimit] = loadCellVal1.get();
            isGatheringLimit++;
            return;
        }
        shiftLeft(loadCellRaw);
        loadCellRaw[isGatheringLimit - 1] = loadCellVal1.get();


//        if(min>loadCellVal1.get() || min==0)
//            min=loadCellVal1.get();
//        if(max<loadCellVal1.get() || max==0)
//            max=loadCellVal1.get();


        Log.d(TAG, "onLoadCellDataAvail: LoadCellRaw:->" + Arrays.toString(loadCellRaw));
        z = getZScore(loadCellVal1.get(),loadCellRaw);
        mMA1.add(Double.parseDouble(df2.format(z)));
        mMA2.add(Double.parseDouble(df2.format(mMA1.getaverage())));

//        mMA3.add(Double.parseDouble(df2.format(mMA2.getaverage())));
//        Log.d(TAG, "onLoadCellDataAvail: MA1:-> " + mMA1.getaverage() + "  MA2:-> " + mMA2.getaverage() + " MA3:-> " + mMA3.getaverage());
        Log.d(TAG, "onLoadCellDataAvail: MA1:-> " + mMA1.getaverage() + "  MA2:-> " + mMA2.getaverage() );

        /*  this  algorithm check real time peak count  and extrapolate */
        if (algo.isPeakGot(Double.parseDouble(df2.format(mMA2.getaverage())))) {

            /*
            *store peaks todo newAlgo 17 may
             *  **/
            listener.onCountAvailable(algo.addPeakTime(model.getTimestamp()),model.getTimestamp());


        /*    if (lastPeakTimeStamp <= 0) {
                lastPeakTimeStamp = model.getTimestamp();
                return;
            }


            Log.d(TAG, "onLoadCellDataAvail: calculateBreathing:-> " + CommonMethod.checkBreathing(model.getTimestamp(), lastPeakTimeStamp));
            listener.onCountAvailable(Integer.parseInt(CommonMethod.checkBreathing(model.getTimestamp(),lastPeakTimeStamp)),model.getTimestamp());
            lastPeakTimeStamp = model.getTimestamp();*/
        }



       /* if(algo.checkIfPeakMinutesDataComplete(Double.parseDouble(df2.format(mMA3.getaverage())))){

        }*/
    }


    public  double getZScore(double loadCellVal1, Double[] arr) {
        WeakReference<Double> max = new WeakReference<>(max(arr));
        WeakReference<Double> min = new WeakReference<>(min(arr));
//        if ((max(arr) - min(arr)) > 0)
//            return ((loadCellVal1 - min(arr)) / (max(arr) - min(arr)));
//        else
//            return 0;
        Log.d(TAG, "isActivityStarted: max: " + max.get() + " min: " + min.get() + " value:" + loadCellVal1);
        if ((max.get() - min.get()) > 0)
            return ((loadCellVal1 - min.get()) / (max.get() - min.get()));
        else
            return 0;
    }

    public  double min(Double[] arr) {
//        return min;
        return Collections.min(Arrays.asList(arr));
//        return arr[0];
    }

    public  double max(Double[] arr) {
//        return max;
        return Collections.max(Arrays.asList(arr));
//        return arr[arr.length-1];
    }




    private void shiftLeft(Double[] values) {
        //n stores the length of array
        WeakReference<Double> temp = new WeakReference<Double>(values[0]);

        for (int i = 0; i < values.length - 1; i++) {
            values[i] = values[i + 1];
            if (values[i] < tempMin || tempMin == -1)
                tempMin = values[i];
        }
        values[values.length - 1] = temp.get();
    }


}
