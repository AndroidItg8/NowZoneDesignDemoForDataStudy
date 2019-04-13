package itg8.com.nowzonedesigndemo.tosort;

import android.util.SparseArray;

import java.util.List;

import itg8.com.nowzonedesigndemo.common.DataModelPressure;

/**
 * Created by itg_Android on 3/7/2017.
 */

public interface RDataManagerListener {
    void onDataProcessed(double m2MAData);

    void onCountAvailable(int count, long timestamp, SparseArray<List<Integer>> allDiff);

    void onCountAvailable(int count,long timestamp);

    void onStepCountReceived(int step);

    void onSleepInterrupted(long timestamp);

    void onStartWakeupSevice();

    void onSocketInterrupted();

    void onMovement(float mAccel);

    void onNoMovement(float i);

    void onDeviceNotAttached();

    void onDeviceAttached();

    void onAxisDataAvail(double y, double z);

    void onDeepsleepGot(long nextTmstmp, long lastTmstmp, long diffMinutes);

    void onSleepEnded();

    void onNotifySittingFor30Min();

    void onPostureResult(Boolean good);

    void postureCalibrationFail();

    void postureCalibratedSuccess();

    void onBatteryAvail(int battery);

    void onResetInititalBreathingState();

    boolean onSensorDataAvailToStore(DataModelPressure pressure);

    void onRawData(double pressure);

    void onAccelerometer(DataModelPressure model);


}
