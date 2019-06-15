package itg8.com.nowzonedesigndemo.utility.posture_algo;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;

public class PostureCalbration {

    private static final int Z_ARRAY_LENGTH = 40;
    private int zIndex = 0;

    private float[] arrZ;

    public PostureCalbration() {
        arrZ = new float[40];
    }

    public void calibratePosture(float z) {
        if (zIndex == Z_ARRAY_LENGTH) {
            calbrate();
        }
        arrZ[++zIndex] = z;

    }

    private void calbrate() {
        float refVal = 0;
        for (float v : arrZ) {
            refVal += v;
        }
        Prefs.putFloat(CommonMethod.POSTURE_CALIBRATION, refVal);
    }


}
