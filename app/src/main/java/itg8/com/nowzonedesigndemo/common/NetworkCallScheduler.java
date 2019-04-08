package itg8.com.nowzonedesigndemo.common;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.DataStoreScheduleBroadcastReceiver;
import itg8.com.nowzonedesigndemo.db.PresureAccelerometerSateImp;
import okhttp3.ResponseBody;



public class NetworkCallScheduler extends JobIntentService {
    private static final String TAG = "NetworkCallScheduler";
    private static final String ACTION_MY_NEW_JOB = "MyNewActionJobSchedulerMadeBySwappy";
    private List<DataModelPressure> listItem;
    private Long[] bitID = new Long[2];



    public static void enqueueWork(Context context){
        Log.d(TAG, "enqueueWork: ");
        Intent intent=new Intent(context,NetworkCallScheduler.class);
        enqueueWork(context, NetworkCallScheduler.class,DataStoreScheduleBroadcastReceiver.MY_JOB_ID,intent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    public void onStartJob() {
        Log.d(TAG, "onStartJob: jobParamaters");


        Observable.create(new ObservableOnSubscribe<List<DataModelPressure>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DataModelPressure>> e) throws Exception {
                PresureAccelerometerSateImp preAccSateImp;
                preAccSateImp = new PresureAccelerometerSateImp(getApplicationContext());
                listItem = (List<DataModelPressure>) preAccSateImp.findAll();
               // Log.d(TAG, "subscribe: "+new Gson().toJson(listItem));
                e.onNext(listItem);
            }
        }).flatMap(new Function<List<DataModelPressure>, Observable<Long[]>>() {
            @Override
            public Observable<Long[]> apply(List<DataModelPressure> dataModelPressures) throws Exception {
                if (dataModelPressures.size() > 0) {
                    bitID[0] = dataModelPressures.get(0).getSerialNo();
                    bitID[1] = dataModelPressures.get(dataModelPressures.size()-1).getSerialNo();
                }
//                sendToServer
                if(dataModelPressures.size()>0)
                return sendToServer(dataModelPressures);
                else
                return     Observable.just(bitID);

            }
        }).flatMap(new Function<Long[], ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Long[] longs) throws Exception {

                PresureAccelerometerSateImp preAccSateImp;
                preAccSateImp = new PresureAccelerometerSateImp(getApplicationContext());
                preAccSateImp.deleteBetweenIDS(longs);
                return Observable.just(0);
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object object) {
                Log.d(TAG, "onNext: ");
                scheduleNextJob();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "onError: ");
                scheduleNextJob();

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
                scheduleNextJob();
            }
        });
    }

    private Observable<Long[]> sendToServer(final List<DataModelPressure> dataModelPressures) {

        return CommonMethod.getController().storePressure(dataModelPressures).concatMap(new Function<ResponseBody, Observable<Long[]>>() {
            @Override
            public Observable<Long[]> apply(ResponseBody responseBody) throws Exception {
                Long[] s=new Long[2];
                s[0]=0L;
                s[1]=0L;
                try {
                    String response=responseBody.string();
                    JSONObject jsonObject = null;
                    if(response!=null) {
                        jsonObject = new JSONObject(response);
                        if (jsonObject.has("flag") && jsonObject.getInt("flag") == 1) {
                            if (dataModelPressures.size() > 0) {
                                s[0] = dataModelPressures.get(0).getSerialNo();
                                s[1] = dataModelPressures.get(dataModelPressures.size() - 1).getSerialNo();
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }
                return Observable.just(s);

            }

        });
    }

    private void scheduleNextJob() {
//        Intent intent=new Intent(this, DataStoreScheduleBroadcastReceiver.class);
////        intent.setAction(ACTION_MY_NEW_JOB);
////        sendBroadcast(intent);
        Log.d(TAG, "scheduleNextJob: ");
        DataStoreScheduleBroadcastReceiver.setAlarm(false,NetworkCallScheduler.this);
        stopSelf();
    }




    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork: ");
        onStartJob();
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork: ");
        return super.onStopCurrentWork();
    }
}
