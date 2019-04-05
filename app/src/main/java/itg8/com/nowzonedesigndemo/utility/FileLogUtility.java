package itg8.com.nowzonedesigndemo.utility;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by swapnilmeshram on 25/12/17.
 */

public class FileLogUtility {
    private static final java.lang.String TAG = FileLogUtility.class.getSimpleName();
    static FileLogUtility fileLogUtility;
    private static final String filePath = "/nowzone/logs";
    private static File file;
    private static boolean fileCreated;
    static StringBuilder builder;
    private File completeFileStructure;
    private String lastNoMovement;
    private long lastNoMovementTmstm;

    public FileLogUtility() {

    }

    public static FileLogUtility newInstance() {
        createFileIfNotExist();
        if (fileLogUtility == null)
            fileLogUtility = new FileLogUtility();
        return fileLogUtility;
    }

    private static void createFileIfNotExist() {
        file = new File(Environment.getExternalStorageDirectory() + filePath, Helper.getCurrentDate() + ".txt");

//        fileCreated = file.exists() || file.mkdirs();
//        if(fileCreated)
//            file=new File(file,);
//        else
//        if(builder==null)
//            builder=new StringBuilder();
    }

    public void addLogToFile(String log) {
//        if(!fileCreated)
//            Logs.d(TAG,"File Not Exist Yet.. Please check permission");
        builder = new StringBuilder();
        log = builder.append(Helper.getCurrentDateTimeWithServerFormat()).append(" ").append(log).append("\n").toString();
        if (log.equalsIgnoreCase(UserLog.NO_MOVEMENT.name()))
//            if (lastNoMovement == null)
        {
            if (lastNoMovement == null) {
                lastNoMovement = log;
                lastNoMovementTmstm = System.currentTimeMillis();
                return;
            } else {
                if (System.currentTimeMillis() - lastNoMovementTmstm < 5000) {
                    return;
                } else {
                    log = builder.append(Helper.convertTimestampToTime(lastNoMovementTmstm)).append(" ").append(log).append("\n").toString();
                    lastNoMovement = null;
                }
            }
        }
        if (lastNoMovement != null && log.equalsIgnoreCase(UserLog.MOVEMENT.name())) {
            return;
        }


        Observable.just(log)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                                createFile(s);
                            }
                        });
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void createFile(String log) {

        completeFileStructure = new File(Environment.getExternalStorageDirectory() + filePath, Helper.getCurrentDate() + ".txt");
        try {
            FileWriter fWriter;
            if (completeFileStructure.exists()) {
                fWriter = new FileWriter(completeFileStructure, true);
                fWriter.append(log);
                fWriter.flush();
                fWriter.close();
            } else {
                completeFileStructure.getParentFile().mkdirs();
                fWriter = new FileWriter(completeFileStructure, true);
                fWriter.write(log);
                fWriter.flush();
                fWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
