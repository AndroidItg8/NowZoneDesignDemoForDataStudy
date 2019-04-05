package itg8.com.nowzonedesigndemo.breath_history.mvp;

import android.util.Log;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.BreathStateImpModel;
import itg8.com.nowzonedesigndemo.db.tbl.TblPostureSedentary;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.steps.mvp.StepMVP;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.FilterUtility;
import itg8.com.nowzonedesigndemo.utility.Helper;

import static itg8.com.nowzonedesigndemo.utility.BreathState.CALM;
import static itg8.com.nowzonedesigndemo.utility.BreathState.FOCUSED;
import static itg8.com.nowzonedesigndemo.utility.BreathState.STRESS;

public class StatisticModuleImp implements StatisticMVP.StatisticModule {

    BreathStateImpModel breathModel;
    private StatisticMVP.StatisticListener listener;
    private long last;
    private long diff;
    CompositeDisposable disposable=new CompositeDisposable();
    private DisposableObserver<StepsItem> steps;
    private DisposableObserver<BreathItem> breath;

    public StatisticModuleImp() {
        breathModel = AppApplication.getInstance().getBreathStateModel();
    }

    @Override
    public void setListener(StatisticMVP.StatisticListener listener) {

        this.listener = listener;
    }

    @Override
    public void onStartGettingData(Date date) {
        startGettingBreathingData(Helper.getDateFromMillies(date.getTime()));
        startGettingStepsData(date);
        startGettingSleepData();
        startGettingPostureData(date);
    }

    @Override
    public void getDistictDates() {
        Date[] dates = new Date[2];
        Observable.create(new ObservableOnSubscribe<Date[]>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Date[]> e) throws Exception {
                List<TblState> states = breathModel.getDistict(TblState.FIELD_DATE);
                Date startDate = Helper.getDateFromString(states.get(0).getDate());
                Date endDate = Helper.getDateFromString(states.get(states.size() - 1).getDate());
                dates[0] = startDate;
                dates[1] = endDate;
                e.onNext(dates);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateSubscription());


    }

    private Observer<? super Date[]> dateSubscription() {
        return new Observer<Date[]>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Date[] dates) {
                listener.onDateRangeAvail(dates);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void startGettingPostureData(Date date) {
        Observable.create(new ObservableOnSubscribe<PostureItem>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<PostureItem> e) throws Exception {
                List<TblPostureSedentary> allList = breathModel.findAllPosture();
                PostureItem item = null;
                item = new PostureItem();
                if (allList != null) {
                    item.postureList = allList;

                }
                e.onNext(item);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).map(new Function<PostureItem, PostureItem>() {
            @Override
            public PostureItem apply(@NonNull PostureItem postureItem) throws Exception {
                return calculateMinutes(postureItem);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostureItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostureItem postureItem) {
                        listener.onPostureAvail(postureItem);
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


    private void startGettingSleepData() {

    }

    private void startGettingStepsData(Date date) {
        if(steps!=null)
           disposable.remove(steps);
        steps=Observable.create(new ObservableOnSubscribe<StepsItem>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<StepsItem> e) throws Exception {
                List<TblStepCount> stepCounts=(List<TblStepCount>) breathModel.findStepsBy(TblStepCount.FIELD_DATE,date);
                StepsItem module = new StepsItem();
                if(stepCounts.size()>0) {
                    module.stepCounts = stepCounts;
                    e.onNext(module);
                }else{
                    e.onNext(module);
                }

                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<StepsItem, StepsItem>() {
                    @Override
                    public StepsItem apply(@NonNull StepsItem stepsItem) throws Exception {
                        return calculateMinutes(stepsItem);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<StepsItem>() {

                    @Override
                    public void onNext(StepsItem stepsItem) {
                            listener.onStepsAvail(stepsItem);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposable.add(steps);
    }



    private void startGettingBreathingData(String date) {
        if(breath!=null)
            disposable.remove(breath);
        breath = Observable.create(new ObservableOnSubscribe<BreathItem>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<BreathItem> e) throws Exception {
                List<TblState> states = (List<TblState>) breathModel.findBreathBy(TblState.FIELD_DATE, date);
                BreathItem item = new BreathItem();
                item.state = CALM;
                item.states = new FilterUtility.FilterBuilder().createBuilder(states).setFilter(CALM).build().getFilteredList();
                item.minutes = String.valueOf(item.states.size());
                e.onNext(item);
                item = new BreathItem();
                item.state = FOCUSED;
                item.states = new FilterUtility.FilterBuilder().createBuilder(states).setFilter(FOCUSED).build().getFilteredList();
                item.minutes = String.valueOf(item.states.size());
                e.onNext(item);
                item = new BreathItem();
                item.state = STRESS;
                item.states = new FilterUtility.FilterBuilder().createBuilder(states).setFilter(STRESS).build().getFilteredList();
                item.minutes = String.valueOf(item.states.size());
                e.onNext(item);
                e.onComplete();
            }
        }).
                subscribeOn(Schedulers.io())
                .map(new Function<BreathItem, BreathItem>() {
                    @Override
                    public BreathItem apply(@NonNull BreathItem breathItem) throws Exception {
                        return calculateMinutes(breathItem);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BreathItem>() {


                    @Override
                    public void onNext(BreathItem breathStateListHashMap) {
                        if (breathStateListHashMap.state == CALM) {
                            listener.onComposedAvail(breathStateListHashMap);
                        } else if (breathStateListHashMap.state == FOCUSED) {
                            listener.onAttentiveAvail(breathStateListHashMap);
                        } else if (breathStateListHashMap.state == STRESS) {
                            listener.onStressAvail(breathStateListHashMap);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposable.add(breath);
    }

    private StepsItem calculateMinutes(StepsItem stepsItem) {
//        List<TblStepCount> stepps=new ArrayList<>();
//        TblStepCount count=null;
//        Calendar ca=Calendar.getInstance();
//        int min=0;
//        for (TblStepCount s :
//                stepsItem.stepCounts) {
//            min+= (int) ((s.getEndTimestamp()-s.getStartTimestamp())/ CommonMethod.MINUTE);
//        }
        int minutes=0;
        if(stepsItem.stepCounts!=null) {
            for (TblStepCount count :
                    stepsItem.stepCounts) {
                minutes += calculateStepsMinutes(count.getStartTimestamp(), count.getEndTimestamp());
            }

//            stepsItem.minutes = String.valueOf(stepsItem.stepCounts.get(stepsItem.stepCounts.size() - 1).getSteps());
            stepsItem.minutes = String.valueOf(minutes);
        }
        else
            stepsItem.minutes= String.valueOf(0);
        return stepsItem;

    }

    private long calculateStepsMinutes(long startTimestamp, long endTimestamp) {
        long diff=endTimestamp-startTimestamp;
        diff=diff/60000;
        return diff;
    }


    private PostureItem calculateMinutes(PostureItem postureItem) {
        int minutes=0;
        if(postureItem.postureList==null)
            return postureItem;
        for (TblPostureSedentary s :
                postureItem.postureList) {
            minutes+=(int)((s.getEndTimestamp()-s.getStartTimestamp())/CommonMethod.MINUTE);
        }
        postureItem.minutes= String.valueOf(minutes);
        return postureItem;
    }

    private BreathItem calculateMinutes(BreathItem breathItem) {
        List<TblState> states = new ArrayList<>();
        TblState rawState = null;
        Calendar ca = Calendar.getInstance();
        for (TblState state :
                breathItem.states) {
            if (rawState == null) {
                rawState = state;
                ca.setTimeInMillis(state.getTimestampEnd());
                ca.add(Calendar.MINUTE, -2);
                rawState.setTimestampStart(ca.getTimeInMillis());
                states.add(rawState);
            } else {
                diff = state.getTimestampEnd() - last;
                Log.d("Difference " + breathItem.state.name(), String.valueOf(diff));
                if (diff > 120000) {
                    rawState = state;
                    ca.setTimeInMillis(state.getTimestampEnd());
                    ca.add(Calendar.MINUTE, -2);
                    if (ca.getTimeInMillis() - states.get(states.size() - 1).getTimestampEnd() < 120000) {
                        states.get(states.size() - 1).setTimestampEnd(state.getTimestampEnd());
                    } else {
                        rawState.setTimestampStart(ca.getTimeInMillis());
                        states.add(rawState);
                    }
                } else {
                    states.get(states.size() - 1).setTimestampEnd(state.getTimestampEnd());
                }
            }
            last = state.getTimestampEnd();

        }
        last = 0;
        breathItem.states = states;
        int min = 0;
        for (TblState state :
                states) {
            long diff = state.getTimestampEnd() - state.getTimestampStart();
            min += (diff / 60000);
        }
        breathItem.minutes = String.valueOf(min);
        return breathItem;
    }

    @Override
    public void onDestrouyed() {
        disposable.clear();
    }


    public class BreathItem {
        public BreathState state;
        public List<TblState> states;
        public String minutes;
    }

    public class StepsItem{
        public List<TblStepCount> stepCounts;
        public String minutes;
    }

    public class PostureItem{
        public List<TblPostureSedentary> postureList;
        public String minutes;
    }

}
