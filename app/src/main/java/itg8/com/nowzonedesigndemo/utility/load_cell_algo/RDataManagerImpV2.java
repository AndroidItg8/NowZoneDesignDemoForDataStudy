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

    public RDataManagerImpV2(RDataManagerListener listener) {
        this.listener = listener;
        mMA1 = new Rolling(ROLLING_AVG_SIZE);
        mMA2 = new Rolling(ROLLING_AVG_SIZE);
        mMA3 = new Rolling(ROLLING_AVG_SIZE);
        loadCellRaw = new Double[5];
        loadCellProcessed = new double[DATA_GATHER_LIMIT];
        algo = new AlgoLoadCellBreathing();
    }

    public void onLoadCellDataAvail(DataModelPressure model) {
        WeakReference<Double> loadCellVal1 = new WeakReference<Double>((double) model.getLoadCell1());
        if (isGatheringLimit < 5) {
            loadCellRaw[isGatheringLimit] = loadCellVal1.get();
            isGatheringLimit++;
            return;
        }
        shiftLeft(loadCellRaw);
        loadCellRaw[isGatheringLimit - 1] = loadCellVal1.get();
        Log.d(TAG, "onLoadCellDataAvail: LoadCellRaw:->" + Arrays.toString(loadCellRaw));
        z = CommonMethod.getZScore(loadCellVal1.get(),loadCellRaw);
        mMA1.add(Double.parseDouble(df2.format(z)));
        mMA2.add(Double.parseDouble(df2.format(mMA1.getaverage())));
        mMA3.add(Double.parseDouble(df2.format(mMA2.getaverage())));
        Log.d(TAG, "onLoadCellDataAvail: MA1:-> " + mMA1.getaverage() + "  MA2:-> " + mMA2.getaverage() + " MA3:-> " + mMA3.getaverage());
        if (algo.isPeakGot(Double.parseDouble(df2.format(mMA3.getaverage())))) {
            if (lastPeakTimeStamp <= 0) {
                lastPeakTimeStamp = model.getTimestamp();
                return;
            }

            Log.d(TAG, "onLoadCellDataAvail: calculateBreathing:-> " + CommonMethod.checkBreathing(model.getTimestamp(), lastPeakTimeStamp));
            listener.onCountAvailable(Integer.parseInt(CommonMethod.checkBreathing(model.getTimestamp(),lastPeakTimeStamp)),model.getTimestamp());
            lastPeakTimeStamp = model.getTimestamp();
        }
    }



    private Double[] shiftLeft(Double[] values) {
        //n stores the length of array
        WeakReference<Double> temp = new WeakReference<Double>(values[0]);

        for (int i = 0; i < values.length - 1; i++) {
            values[i] = values[i + 1];
            if (values[i] < tempMin || tempMin == -1)
                tempMin = values[i];
        }
        values[values.length - 1] = temp.get();
        return values;
    }


}
