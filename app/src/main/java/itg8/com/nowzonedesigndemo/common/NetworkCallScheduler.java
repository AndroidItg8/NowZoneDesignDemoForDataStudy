package itg8.com.nowzonedesigndemo.common;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;


import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.DataStoreScheduleBroadcastReceiver;
import itg8.com.nowzonedesigndemo.db.PresureAccelerometerSateImp;
import itg8.com.nowzonedesigndemo.utility.NetworkUtility;
import okhttp3.ResponseBody;

import static itg8.com.nowzonedesigndemo.DataStoreScheduleBroadcastReceiver.MY_JOB_ID;


public class NetworkCallScheduler extends JobService {
    private static final String TAG = "NetworkCallScheduler";
    private static final String ACTION_MY_NEW_JOB = "MyNewActionJobSchedulerMadeBySwappy";
    private List<DataModelPressure> listItem;
    private Long[] bitID = new Long[2];





    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: jobParamaters");

        HandlerThread handlerThread = new HandlerThread("SomeOtherThread");
        handlerThread.start();


        Observable.create(new ObservableOnSubscribe<List<DataModelPressure>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DataModelPressure>> e) throws Exception {
                PresureAccelerometerSateImp preAccSateImp;
                preAccSateImp = new PresureAccelerometerSateImp(getApplicationContext());
                listItem = (List<DataModelPressure>) preAccSateImp.findAll();
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
                return sendToServer(dataModelPressures);

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
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                scheduleNextJob();

            }
        });
        return true;
    }

    private Observable<Long[]> sendToServer(final List<DataModelPressure> dataModelPressures) {

        return CommonMethod.getController().storePressure(dataModelPressures).concatMap(new Function<ResponseBody, Observable<Long[]>>() {
            @Override
            public Observable<Long[]> apply(ResponseBody responseBody) throws Exception {
                Long[] s=new Long[2];
                if(dataModelPressures.size()>0){
                    s[0]=dataModelPressures.get(0).getSerialNo();
                    s[1]=dataModelPressures.get(dataModelPressures.size()-1).getSerialNo();
                }
                return Observable.just(s);
            }
        });
    }

    private void scheduleNextJob() {
//        Intent intent=new Intent(this, DataStoreScheduleBroadcastReceiver.class);
////        intent.setAction(ACTION_MY_NEW_JOB);
////        sendBroadcast(intent);

        DataStoreScheduleBroadcastReceiver.setAlarm(false,NetworkCallScheduler.this);
    }



    @Override
    public boolean onStopJob(JobParameters jobParameters) {
//        isStoreDb = !isStoreDb;
        return true;
    }


}
