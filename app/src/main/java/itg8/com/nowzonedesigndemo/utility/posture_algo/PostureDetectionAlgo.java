package itg8.com.nowzonedesigndemo.utility.posture_algo;

import android.util.Log;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;

public class PostureDetectionAlgo {

    private static final String TAG = "PostureDetectionAlgo";

    private static final int MIN1 = 170;
    private static final int MAX1 = 16400;
    private static final int MIN2 = 48000;
    private static final int MAX2 = 65400;
    private static final int Z_ARRAY_LENGTH = 40;
    private int zIndex = -1;
    private  PostureCalbration postureCalbration;
    private int[] arrZ;

    public PostureDetectionAlgo() {
        arrZ = new int[Z_ARRAY_LENGTH];
        postureCalbration = new PostureCalbration();
    }

    public void detectPosture(float z) {

        if (zIndex == Z_ARRAY_LENGTH) {
            zIndex = -1;
            float batch = manipulatePostureData();
            if (Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION, 0) <= 0) {
                if (Prefs.getBoolean(CommonMethod.POSTURE_CALIBRATION_STARTED, false)) {
                    postureCalbration.calibratePosture(batch);
                }
                return;
            }

            if (batch < (getCalibration() - 7)) {
                Log.d(TAG, "detectPosture: POSTURE BEND AHEAD");
            } else if (batch > (getCalibration() + 7)) {
                Log.d(TAG, "detectPosture: POSTURE BEHIND");
            } else {
                Log.d(TAG, "detectPosture: PERFECT");
            }
        }

        arrZ[++zIndex] = (int) z;
    }

    private float getCalibration() {
        return Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION, 0);
    }


    private float manipulatePostureData() {
        float zTotal = 0;
        for (int i = 0; i < arrZ.length; i++) {
            if (arrZ[i] > MIN1 && arrZ[i] < MAX1)
                arrZ[i] = 90 * ((arrZ[i] - MIN1) / (MAX1 - MIN1));
            else
                arrZ[i] = -90 * ((arrZ[i] - MAX2) / (MIN2 - MAX2));

            zTotal += arrZ[i];
        }

        return zTotal / arrZ.length;
    }


}
