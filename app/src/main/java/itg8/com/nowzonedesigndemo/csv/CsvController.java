package itg8.com.nowzonedesigndemo.csv;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.db.PresureAccelerometerSateImp;
import itg8.com.nowzonedesigndemo.utility.ExportToCsv;

import static itg8.com.nowzonedesigndemo.utility.ExportToCsv.getTitle;
import static itg8.com.nowzonedesigndemo.utility.ExportToCsv.stopWriter;

public class CsvController {
    private static final String TAG = "CsvController";

    public static int COUNT = 0;

    private File file;
    private PresureAccelerometerSateImp pressureController;
    private OnFileWriteListener listener;
    private StringBuilder builder;
    private String title;

    public interface OnFileWriteListener {
        void onFileWriteStart();

        void onStopped();

        void onError(Throwable e);

        void onProgress(int i);
    }


    public CsvController(PresureAccelerometerSateImp pressureController, OnFileWriteListener listener) {
        this.pressureController = pressureController;
        this.listener = listener;
        try {
            file = ExportToCsv.exportEmailInCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void exportData(Calendar start, Calendar end) {
        listener.onFileWriteStart();
        Observable.create(new ObservableOnSubscribe<DataModelPressure>() {
            @Override
            public void subscribe(ObservableEmitter<DataModelPressure> e) throws Exception {
                List<DataModelPressure> dataModelPressures = pressureController.getBetween(start.getTime(), end.getTime());
                Log.d(TAG, "subscribe: " + dataModelPressures.size());
                COUNT = dataModelPressures.size();
                for (DataModelPressure d :
                        dataModelPressures) {
                    e.onNext(d);
                }

                e.onComplete();
            }
        }).flatMap(new Function<DataModelPressure, Observable<String>>() {
            @Override
            public Observable<String> apply(DataModelPressure d) throws Exception {
                //  builder.append(dataModelPressure.get)

                builder = new StringBuilder();
                if (title == null) {
                    title = getTitle();
                    builder.append(title);
                }
                builder.append(d.getDatetime()).append(",");
                builder.append(d.getPressure()).append(",");
                builder.append(d.getX()).append(",");
                builder.append(d.getY()).append(",");
                builder.append(d.getZ()).append(",");
                builder.append(d.getgX()).append(",");
                builder.append(d.getgY()).append(",");
                builder.append(d.getgZ()).append(",");
                builder.append(d.getmX()).append(",");
                builder.append(d.getmY()).append(",");
                builder.append(d.getmZ()).append(",");
                builder.append(d.getTemprature()).append(",");
                builder.append(d.getBattery()).append(",");
                builder.append(d.getPressureProcessed()).append(",");
                builder.append(d.getLoadCell1()).append(",");
                builder.append(d.getLoadCell2()).append(",");
                builder.append(d.getMic()).append(",");
                builder.append(d.getCharging()).append(",");
                builder.append(d.getOptical()).append("\n");
                return Observable.just(builder.toString());
            }
        }).flatMap(new Function<String, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> apply(String s) throws Exception {
                Log.d(TAG, "apply: " + s);
                return Observable.just(ExportToCsv.writeToFile(s, file));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        Log.d(TAG, "onNext: "+s);
                        if (s)
                            listener.onProgress(--COUNT);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        stopWriter();
                        Log.d(TAG, "onComplete: ");
                        listener.onStopped();
                        title = null;
                    }
                });
    }
}
