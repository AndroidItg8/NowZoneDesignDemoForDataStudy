package itg8.com.nowzonedesigndemo.common;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;


import com.google.gson.Gson;

import java.util.List;

import itg8.com.nowzonedesigndemo.db.PresureAccelerometerSateImp;


public class NetworkCallScheduler extends JobService {
    private static final String TAG = "NetworkCallScheduler";
    private List<DataModelPressure> listItem;
    private boolean isStoreDb=true;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: jobParamaters");

        HandlerThread handlerThread = new HandlerThread("SomeOtherThread");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, " Scheduled Running!!!!!!!!!!!!!");
                if(isStoreDb) {
                    PresureAccelerometerSateImp preAccSateImp;
                    preAccSateImp = new PresureAccelerometerSateImp(getApplicationContext());
                    listItem = (List<DataModelPressure>) preAccSateImp.findAll();
                    Log.d(TAG, "run: "+new Gson().toJson(listItem));
                }else {
                    if(listItem!=null)
                         networkCallForSaveData(listItem);
                }

                jobFinished(jobParameters, true);
            }
        });

        return true;
    }

    private void networkCallForSaveData(List<DataModelPressure> listDataPressure) {
        Log.d(TAG, "networkCallForSaveData: lis"+new Gson().toJson(listDataPressure) +"isStoreDb :"+isStoreDb);

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        isStoreDb =!isStoreDb;
        return false;
    }
}
