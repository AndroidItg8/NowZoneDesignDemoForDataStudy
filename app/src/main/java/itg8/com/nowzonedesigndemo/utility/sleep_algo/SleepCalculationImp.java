package itg8.com.nowzonedesigndemo.utility.sleep_algo;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.sleep.model.SleepActivityModel;
import itg8.com.nowzonedesigndemo.sleep.model.SleepVectorModel;

public class SleepCalculationImp {

    private static final String TAG = "SleepCalculationImp";
    private static final long MIN_DEEP_SLEEP_DURATION = 1;

    private final Dao<DataModelPressure, Integer> dao;
    private long id;
    private Dao<SleepActivityModel, Integer> sleepResultDao;
    private Disposable d;

    public SleepCalculationImp(Dao<DataModelPressure, Integer> dao, long id, Dao<SleepActivityModel, Integer> sleepResultDao) {
        this.dao = dao;
        this.id = id;
        this.sleepResultDao = sleepResultDao;
    }

    public void calculateSleep(long startTimestamp, long endTimestamp, OnSleepDetailListner listner) throws SQLException {
        if (dao == null) {
            return;
        }
        d = Observable.create(new ObservableOnSubscribe<List<SleepActivityModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SleepActivityModel>> e) throws Exception {
                Calendar startTime = Calendar.getInstance();
                startTime.setTimeInMillis(startTimestamp);
                Calendar endTime = Calendar.getInstance();
                endTime.setTimeInMillis(endTimestamp);
                QueryBuilder<DataModelPressure, Integer> queryBuilder = dao.queryBuilder();
                queryBuilder.where().between(DataModelPressure.FIELD_TIMESTAMP_DATE, startTime.getTime(), endTime.getTime());
                List<DataModelPressure> pressuresData = dao.query(queryBuilder.prepare());
                List<SleepVectorModel> modelVectorList = new ArrayList<>();
                long lastTimestamp = 0;
                long diffMinutes;
                long diffLong;
                long line;
                double min = 0;
                double max = 0;
                Double[] d = new Double[pressuresData.size()];
                int i = 0;
                for (DataModelPressure p :
                        pressuresData) {
                    SleepVectorModel model = new SleepVectorModel();
                    model.setTimestamp(p.getTimestamp());
                    model.setVector(CommonMethod.getVector(p.getX(), p.getY(), p.getZ()));
                    d[i] = model.getVector();
                    i++;
                    if (min==0 || min > model.getVector()) {
                        min = model.getVector();
                    }
                    if (max < model.getVector()) {
                        max = model.getVector();
                    }
                    modelVectorList.add(model);
                }

                SleepActivityModel sleepActivityModel = null;
                List<SleepActivityModel> sleepActivityModelList = new ArrayList<>();

                for (SleepVectorModel v :
                        modelVectorList) {
                    v.setzScore(CommonMethod.getZScore(v.getVector(), d));
                    Log.d(TAG, "subscribe: "+v.getzScore());
                    if (v.getzScore() > 0.70) {
                        line = v.getTimestamp();
                        if (line != 0) {
                            if (lastTimestamp <= 0) {
                                sleepActivityModel = new SleepActivityModel();
                                sleepActivityModel.setTblSleepID(id);
                                sleepActivityModel.setStartTime(line);
                                sleepActivityModel.setEndTime(line);
                                lastTimestamp = line;
                                continue;
                            }
                            diffLong = line - lastTimestamp;
                            diffMinutes = diffLong / (60 * 1000) % 60;
                            if (diffMinutes > MIN_DEEP_SLEEP_DURATION) {
                                sleepActivityModel.setEndTime(lastTimestamp);
                                sleepActivityModelList.add(sleepActivityModel);
                                sleepActivityModel = new SleepActivityModel();
                                sleepActivityModel.setTblSleepID(id);
                                sleepActivityModel.setStartTime(line);
                            } else {
                                sleepActivityModel.setEndTime(line);
//                                sleepActivityModelList.add(sleepActivityModel);
//                                sleepActivityModel=new SleepActivityModel();
//                                sleepActivityModel.setTblSleepID(id);
//                                sleepActivityModel.setStartTime(line);
                            }
                            lastTimestamp = line;
                        }
                    }
                }
                if(sleepActivityModel!=null && sleepActivityModel.getEndTime()>=0 ){
                    sleepActivityModelList.add(sleepActivityModel);
                }

                e.onNext(sleepActivityModelList);
                e.onComplete();


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<SleepActivityModel>>() {
                    @Override
                    public void accept(List<SleepActivityModel> models) throws Exception {
                        if(listner!=null)
                            listner.onListOfDetailAvail(models);

                        storeModels(models);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: ",throwable);
                    }
                });


    }

    private void storeModels(List<SleepActivityModel> models) {
        for (SleepActivityModel m :
                models) {
            try {
                sleepResultDao.create(m);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e(TAG, "subscribe: ",e);
            }
        }
    }

    public interface OnSleepDetailListner {
        void onListOfDetailAvail(List<SleepActivityModel> models);
    }
}
