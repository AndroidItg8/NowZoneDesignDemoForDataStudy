package itg8.com.nowzonedesigndemo.tosort;

import android.content.Context;

import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.utility.BreathState;


/**
 * Created by itg_Android on 3/7/2017.
 */

public interface RDataManager {
    void onRawDataModel(DataModelPressure model, Context applicationContext);
    void onSleepStarted(boolean b);
    void onStartAlarmTime(long startAlarm);
    void onEndAalrmTime(long endAlarm);

    void setServerIp(String ip);
    void startSendingData(String stringExtra);

    void onSocketStopped();

    void arrangeBreathingForServer(String string, int count, long timestamp, BreathState currentState);

    void arrangeStepsForServer(String url, int step, TblStepCount countLast, boolean fi);

    void resetCountAndAverage();

    void resetSteps();

    void setCalibrated(float aFloat);

    void startCalibration();

    void onDisconnected();
    void onConnected();

    void sendRemainingStepsToServer(String url);

    void arrangeBreathingForServer(String url, int actualLastCount, long actualLastTimestamp, BreathState actualLastState, int count, long timestamp, BreathState currentState);
}
