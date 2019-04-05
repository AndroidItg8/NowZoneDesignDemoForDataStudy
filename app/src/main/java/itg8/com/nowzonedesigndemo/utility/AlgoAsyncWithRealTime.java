package itg8.com.nowzonedesigndemo.utility;

import android.os.Handler;
import android.os.Message;

import java.io.File;


class AlgoAsyncWithRealTime extends Handler {


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }

    private static final double DELTA = 5.0;
    private static final String TAG = AlgoAsyncWithRealTime.class.getSimpleName();
    private static final long ONE_MINUTE = 60000;
    private PAlgoCallback callback;
    private int bpmInMinute;
    private File completeFileStructure;

    AlgoAsyncWithRealTime(PAlgoCallback callback) {
        this.callback = callback;
    }

}