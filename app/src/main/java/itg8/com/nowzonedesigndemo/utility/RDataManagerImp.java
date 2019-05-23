package itg8.com.nowzonedesigndemo.utility;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;


import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.common.NetworkCallScheduler;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.db.PresureAccelerometerSateImp;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.tosort.RDataManager;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.activity_algo.ActivityDetection;
import itg8.com.nowzonedesigndemo.utility.load_cell_algo.RDataManagerImpV2;
import itg8.com.nowzonedesigndemo.utility.model.breath.BreathingModel;
import itg8.com.nowzonedesigndemo.utility.model.breath.Breathingmaster;
import itg8.com.nowzonedesigndemo.utility.model.step.Stepmaster;
import itg8.com.nowzonedesigndemo.utility.model.step.StepsModel;

import static itg8.com.nowzonedesigndemo.common.CommonMethod.calculate;
import static itg8.com.nowzonedesigndemo.home.HomeActivity.PI_MAX;
import static itg8.com.nowzonedesigndemo.home.HomeActivity.PI_MIN;
import static itg8.com.nowzonedesigndemo.utility.AlgoAsync.ONE_MINUTE;
import static itg8.com.nowzonedesigndemo.utility.load_cell_algo.RDataManagerImpV2.df2;

/**
 * This is responsible for calculation uploading and all.
 */

public class RDataManagerImp implements RDataManager, PAlgoCallback, AccelVerifyListener {

    /**
     * this veriable use to define rolling average window. W=30
     */
    private static final int ROLLING_AVG_SIZE = 20;
    private static final int ROLLING_AVG_SIZE2 = 20;

    /**
     * this will check if packet receiving is completed.
     */
    private static final int PACKET_READY_TO_IMP = 1200;
    private static final String TAG = RDataManagerImp.class.getSimpleName();
    private final RDataManagerListener listener;
    private final Context mContext;
    private final Thread pressureThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!disconnected) {
                if (!pressureque.isEmpty() && mContext != null) {
                    processModelData(pressureque.poll(), mContext);
                }
            }
        }
    });
    private final ActivityDetection activityImp;
    private boolean disconnected = false;
    private Queue<DataModelPressure> pressureque = new ArrayDeque<>();
    private CheckAccelImp accelImp;
    private boolean isSleepStarted;
    /**
     * this will be use to send data from service to store in file.
     */
    DataModelPressure dataModel;
    private DataModelPressure[] dataStorage;
    private Rolling rolling, rolling2, rolling3;
    private DataModelPressure[] dataStorageRaw;
    private DataModelPressure modelTemp;
    private int indexDataStorage;
    private DataModelPressure[] tempHolder;
    private AlgoAsync async;
    private DataModelPressure[] tempHolderRaw;
    private long startAlarmTime, endAlarmTime;
    private String serverIp;
    private boolean connected;
    private PrintWriter out;
    private DataModelPressure pModel;
    private boolean changed = false;
    private Thread thread;
    private String toSendSocket;

    private StringBuilder sb;
    private NetworkUtility utility;
    private int lastStep;
    private double mSmallestPressure;
    private int mPressureCount;
    private int mPressureFilterCount;
    private boolean isStillSmallest = false;
    private boolean isNotSmallest = true;
    private boolean attached;
    private boolean movement = false;
    private int stepsTemp = 0;
    private float calibrate = 0;
    private float[] accelGatheredData = new float[300];
    private int countPosture = 0;
    private boolean hasMovementInGathering;
    private long c = 0;
    private boolean counted;

    double[] tempLineChartData = new double[1200];

    //TODO DUMEeeeeeeeeeeeDUMEeeeeeeeeeeeDUMEeeeeeeeeeeeDUMEeeeeeeeeeeeDUMEeeeeeeeeeeeDUMEeeeeeeeeeeeDUMEeeeeeeeeeeeDUMEeeeeeeeeeee
    private double lastMax = 4.02;
    private double lastMin = -4.02;
    //    private float a = 0.94f;
    private float a = 0.94f;
    private double dLast;
    private double actMin = 0;
    private double actMax = 0;
    private double newPressure;
    private double tempPressure;
    private double alpha = 0;
    private double dLastnew = 0;
    private double lowPassFilterValue;
    private double gf;
    private Rolling rollingGf;
    private Rolling rollingG;
    private Rolling rollingGf2;
    private double sumGf;
    private int coIndex = 0;
    private double newData;
    private boolean startDecreasing = false;
    private int countActualData = 0;
    private int startPos;
    private int endPos;
    private long newCount;
    private long lastCount = 14;
    private boolean rollingGIndexCompleted;
    private boolean isPreviousBreathingChecked = false;
    private long lastServerTimestamp;
    private boolean lastMaxDecreased;
    private boolean lastMinIncreased;
    private DescriptiveStatistics stats;
    private PresureAccelerometerSateImp preAccSateImp;


    private RDataManagerImpV2 loadCellImp;


    private void secondPref(double pressure) {

        rollingG.add(pressure);

//        if(((lastMin+lastMax)/2)<rollingGf.getaverage()){
//            lastMax--;
//            lastMin++;
//        }

        /**
         * Test 2: we will try to check if lastMax and lastMin average is what.
         * if last 10 pressures average is at which position
         * example
         *              Case 1 : p_avg<lastMaxMinAvg
         *
         *                          then lastMax--
         *              Case 2 : p_avg>lastMaxMinAvg
         *                          then lastMin++
         *
         */

//        if(rollingGf.getaverage()<lastMaxMinAvg(lastMax,lastMin))
//        {
//            lastMax--;
//            if(pressure<=lastMin){
//                lastMin=pressure;
//            }
//        }else{
//            lastMin++;
//            if(pressure>=lastMax){
//                lastMax=pressure;
//            }
//        }
        if (!rollingGIndexCompleted) {
            if (rollingG.getIndex() >= 39) {
                rollingGIndexCompleted = true;
                lastMax = rollingG.getMax() + 100;
                lastMin = rollingG.getMax() - 100;
            }
        } else {
//            if (lastMax == 0 || lastMin == 0) {
//                lastMax = pressure + 200;
//                lastMin = pressure - 200;
//                return;
//            }
            if (pressure + 100 > lastMax) {
                lastMax++;
                lastMin++;
            } else if (pressure - 100 < lastMin) {
                lastMin--;
                lastMax--;
            }
            //Log.d(TAG, "LastMax: " + lastMax + " lastMIn:" + lastMin);
        }
//        }

        /*
         * test 3
         *
         *
        lastMaxDecreased = false;
        lastMinIncreased = false;
        if (!rollingGIndexCompleted) {
            if (rollingG.getIndex() >= 39) {
                rollingGIndexCompleted = true;
                lastMax = rollingG.getMax();
                lastMin = rollingG.getMax() - 200;
            }
        } else {
            boolean increaseTillLastMax=false;
            boolean decreaseTillLastMin=false;
            if(rollingG.getIndex()==0){
                double getMax=rollingG.getMax();
                double getMin=rollingG.getMin();
                if(getMax>lastMax){
                    increaseTillLastMax=true;
                }
                if(getMin<lastMin){
                    decreaseTillLastMin=true;
                }
            }
            if(increaseTillLastMax){
                lastMax++;
            }
            if(decreaseTillLastMin){
                lastMin--;
            }
            if(increaseTillLastMax || decreaseTillLastMin){
                return;
            }
            //Test 3
            if (pressure < lastMax) {
                lastMax--;
                lastMaxDecreased = true;
            } else {
                lastMax = pressure;
//                lastMin++;
                return;
            }
            if (pressure > lastMin) {
                if(!lastMinIncreased)
                    lastMin++;
            } else {
                lastMin = pressure;
                return;
            }

            if (!lastMaxDecreased && !lastMinIncreased) {
                lastMin--;
                lastMax++;
                //Log.d(TAG, "LastMinLastMax booliean: MAX " + lastMax + " MIN " + " " + lastMin);

                return;
            }

            if (lastMax - lastMin > 30) {
                lastMax--;
                lastMin++;
            } else {
                lastMax++;
                lastMin--;
            }
            //Log.d(TAG, "LastMinLastMax: MAX " + lastMax + " MIN " + " " + lastMin);

             */


        /**  Test 2 (current)
         if(rollingG.isDiffGreaterThan(200)){
         if(lastMax-lastMin>200) {
         lastMin++;
         lastMax--;
         if (pressure > lastMax) {
         lastMax = pressure;
         }else if(pressure<lastMin){
         lastMin=pressure;
         }
         //Log.d(TAG,"LastMinLastMax: increaseDecrease"+rollingG.getMax()+" MIN "+rollingG.getMin()+" "+lastMax+" "+lastMin);
         return;
         }else {
         //                    lastMin--;
         //                    lastMax++;
         lastMin++;
         lastMax--;
         if (pressure > lastMax) {
         lastMax = pressure;
         }else if(pressure<lastMin){
         lastMin=pressure;
         }
         //Log.d(TAG,"LastMinLastMax: increaseDecrease... Else "+rollingG.getMax()+" MIN "+rollingG.getMin()+" "+lastMax+" "+lastMin);
         }
         }else{
         if(lastMax<8191){
         lastMax++;
         }
         if(lastMin>400){
         lastMin--;
         }
         }
         */


//            if (rollingG.getaverage() > lastMax)
//                if (pressure > lastMax) {
//                    lastMax = pressure;
//                } else
//                    lastMax = rollingG.getaverage() + 300;
//            else if (rollingG.getaverage() < lastMin)
//                if(pressure<lastMin){
//                    lastMin=pressure;
//                }else {
//                    lastMin = rollingG.getaverage() - 300;
//                }
//            return;
//        }


//        if (pressure >= lastMax) {
//            lastMax = pressure;
//            lastMin = lastMax - 650;
////            lastMin = lastMax - 250;
//        } else if (pressure <= lastMin) {
//            lastMin = pressure;
//            lastMax = lastMin + 650;
////            lastMax = lastMin + 250;
//        }
//        changeLastMaxLastMin(pressure, rollingGf.getaverage());


//        else if(pressure>(((lastMax+lastMax)/2)+50)){
//         lastMax++;
//         lastMin++;
//                        //Log.d(TAG,"MAXMINVALUE: Lastmin++    "+lastMax);
//
//        }else if( pressure<(((lastMax+lastMax)/2)-50)){
//            lastMax--;
//            lastMin--;
//        }

//        else if(((lastMax+lastMin)/2)>rollingGf.getaverage()){
//            lastMin++;
//            lastMax--;
//            //Log.d(TAG,"MAXMINVALUE: Lastmin++");
//        }else if(((lastMax+lastMin)/2)<rollingGf.getaverage()){
//            lastMax--;
//            lastMin++;
//            //Log.d(TAG,"MAXMINVALUE: LastMax        --");
//        }

//        if(Math.abs(rollingGf.getaverage()-pressure)<10){
//            lastMax=lastMax-10;
//            lastMin=lastMin+10;
//        }
//        else if(Math.abs(rollingGf.getaverage()-pressure)>500){
//            lastMax=lastMax+10;
//            lastMin=lastMin-10;
//            //Log.d(TAG, "500 Pressure: " + pressure + " lastMax: " + lastMax + " average: " + rollingGf.getaverage() + "  lastMIN: " + lastMin);
//        }
//
//        rollingG.add(lastMax);
//        if ((int) rollingG.getaverage() == (int) lastMax) {
//            lastMax--;
//        }
//        rollingGf2.add(lastMin);
//        if ((int) rollingGf2.getaverage() == (int) lastMin) {
//            lastMin++;
//        }

//        //Log.d(TAG, "100 Pressure: " + pressure + " lastMax: " + lastMax + " average: " + rollingGf.getaverage() + "  lastMIN: " + lastMin);


//        //Log.d(TAG,);

    }

    private double calculateProportionBySTD(double pressure) {
        rollingG.add(pressure);
        if (!rollingGIndexCompleted) {
            if (rollingG.getIndex() >= 39) {
                rollingGIndexCompleted = true;
//                lastMax = rollingG.getMax()+100;
//                lastMin = rollingG.getMax() - 100;
            }
            return 1;
        } else {
            //Log.d(TAG, "STDEV:" + rollingG.getStdev());
            Log.d(TAG, "calculateProportionBySTD: " + rollingG.getaverage() + "  --- " + rollingG.getStdev());
            if (rollingG.getStdev() != 0)
                return (pressure - rollingG.getaverage()) / rollingG.getStdev();
            else
                return 1;
        }
    }


    private void changeLastMaxLastMin(double pressure, double avg) {
        if (Math.abs(lastMax - lastMin) < 250 && (avg < lastMax - 250)) {
            lastMax--;
            lastMin++;
        }
    }

    private double lastMaxMinAvg(double lastMax, double lastMin) {
        return ((lastMax + lastMin / 2));
    }

    private double getLowPassFilterValue(double pressure, double a) {
        dLast = a * pressure + (1 - a) * dLast;
        return dLast;
    }

    private double calculateProportion(double pressure, double a) {
//        return (-0.02+(1.02*((pressure-(lastMax-500))/(lastMax-(lastMax-500)))));
//        double d=(double) Math.round((CONST_1+(CONST_2*((pressure-(lastMin))/(lastMax-lastMin)))) * 1000000000000000000d) / 1000000000000000000d;
//        s(i)=a*y(i)+(1-a)*s(i-1)

//        dLastnew=dLastnew + a * (pressure - dLastnew);
        dLastnew = a * pressure + ((1 - a) * dLastnew);

//        double d=pressure;
//        rolling.add(pressure);
//        d=rolling.getaverage();
//        dLast = d;
        // //Log.d(TAG,"ds:"+d);
//        return (PI_MIN + ((PI_MAX - PI_MIN) * ((dLastnew - (MIN_PRESSURE)) / (MAX_PRESSURE - MIN_PRESSURE))));
        return (PI_MIN + ((PI_MAX - PI_MIN) * (dLastnew - (lastMin)) / (lastMax - lastMin)));

//        update(pressure);
//        //Log.d(TAG, String.valueOf(var()));

//        return (MIN_CIRCLE_SIZE + ((MAX_CIRCLE_SIZE- MIN_CIRCLE_SIZE) * ((d - (lastMin)) / (lastMax - lastMin))));
//        return (lastMin + ((lastMax- lastMin) * ((d - (MIN_PRESSURE)) / (MAX_PRESSURE - MIN_PRESSURE))));
    }

    private double calculateRange(double value, double toMin, double toMax, double actMin, double actMax) {
        return (toMin + ((toMax - toMin) * ((value - (actMin)) / (actMax - actMin))));
    }

    private double getAlphaFilter(double pressure) {
        if (actMax == 0 || actMax < pressure)
            actMax = pressure;
        if (actMin == 0)
            actMin = actMax - 1;
        if (actMin > pressure)
            actMin = pressure;

        newPressure = (100 - calculateRange(pressure, 1, 100, actMin, actMax)) / 100;
        if (mPressureFilterCount > 20) {
            actMax = 0;
            actMin = 0;
        }
        return newPressure;
    }

    public RDataManagerImp(RDataManagerListener listener, Context mContext) {
        this.listener = listener;
        sb = new StringBuilder();
        isSleepStarted = false;
        startAlarmTime = 0;
        endAlarmTime = 0;
        this.mContext = mContext;
        rolling = new Rolling(ROLLING_AVG_SIZE);
        rolling2 = new Rolling(ROLLING_AVG_SIZE2);
        rollingGf = new Rolling(20);
        rollingG = new Rolling(40);
        rollingGf2 = new Rolling(20);
        rolling3 = new Rolling(10);
        dataStorage = new DataModelPressure[PACKET_READY_TO_IMP];
        tempHolder = new DataModelPressure[PACKET_READY_TO_IMP];
        indexDataStorage = 0;
//        pressureThread.start();
        dataStorageRaw = new DataModelPressure[PACKET_READY_TO_IMP];
        tempHolderRaw = new DataModelPressure[PACKET_READY_TO_IMP];
        accelImp = new CheckAccelImp(this, Prefs.getInt(CommonMethod.STEP_COUNT));
        loadCellImp = new RDataManagerImpV2(this.listener);
        activityImp = new ActivityDetection(this.listener);
    }


    @Override
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setSleepStarted(boolean sleepStarted) {
        isSleepStarted = sleepStarted;
        if (!isSleepStarted) {
            accelImp.setSleepsEnd();
        } else {
            accelImp.setSleepNotEnded();
        }
    }

    @Override
    public void onDeepsleepGot(long nextTmstmp, long lastTmstmp, long diffMinutes) {
        listener.onDeepsleepGot(nextTmstmp, lastTmstmp, diffMinutes);
    }

    @Override
    public void onSleepEnded() {
        listener.onSleepEnded();
    }

    private void setStartAlarmTime(long startAlarmTime) {
        this.startAlarmTime = startAlarmTime;
    }

    private void setEndAlarmTime(long endAlarmTime) {
        this.endAlarmTime = endAlarmTime;
    }

    @Override
    public void onRawDataModel(final DataModelPressure model, Context context) {
        if (model != null) {
            //Log.d(TAG, "onRawDataModel: " + model.toString());
            Log.d(TAG, "onRawDataModel: " + model.getBattery());
            if (loadCellImp != null) loadCellImp.onLoadCellDataAvail(model);
            if (activityImp != null)
                activityImp.isActivityStarted(model.getX(), model.getY(), model.getZ());
            sb.setLength(0);
//            if (!isSleepStarted) {
            listener.onBatteryAvail(model.getBattery());
            /**
             * Add now for Raw Process Data line chart
             */
            listener.onRawData(model.getPressure());
            listener.onAccelerometer(model);

            //Log.d(TAG, "onRawDataModel: "+new Gson().toJson(model));
            processModelData(model, mContext);

//            Observable.create(new ObservableOnSubscribe<String>() {
//                @Override
//                public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                    if (isSleepStarted) {
//                        pushToSleep(model, startAlarmTime,
//                                endAlarmTime);
//                        return;
//                    }
//
//                    processForStepCounting(model);
////                    processModelData(model, context);
//                }
//            }).subscribeOn(Schedulers.computation())
//                    .subscribe(new Observer<String>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(String s) {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
            /**
             * Currently we are working on pressure
             */


        } else {
            //Log.d(RDataManagerImp.class.getSimpleName(), "data received: model is null");
        }
    }


    /**
     * New implementation using cooefficient.
     *
     * @param
     */
    double max = Double.MIN_VALUE;
    double min = Double.MAX_VALUE;

    private void initMaxMin() {
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }

    private void gettingCooefficient(double pressure) {


        rolling.add(pressure);

//        rolling2.add(rolling.getaverage());
        pressure = rolling.getaverage();
        pressure = getLowPassFilterValue(pressure, 0.96);
        if (!startDecreasing) {
            if (pressure > max) {
                max = pressure;
//            min=max-1000;
            }
            if (pressure < min) {
                min = pressure;
//            max=min+1000;
            }
        } else {
            if (max > pressure) {
                max--;
            }
            if (min < pressure)
                min++;
//            min++;
        }

        newData = (pressure - min) / (max - min);
        listener.onDataProcessed(newData);
        //Log.d(TAG, "someItemWithCoeff : " + newData + " max: " + max + " min: " + min);
        double k = (-20 / 2) + (coIndex * (20 / (20 - 1)));
        gf = Math.exp(-(Math.pow(k, 2)) / (2 * (Math.pow(0.98, 2))));
        sumGf += gf;
        gf = gf / sumGf;
        coIndex++;
        if (newData > lastMax)
            lastMax = newData;
        if (newData < lastMin) {
            lastMin = newData;
        }
        if (coIndex >= 20) {
            coIndex = 0;
            if (lastMax - lastMin < 0.25) {
                startDecreasing = true;

            } else
                startDecreasing = false;

            lastMin = 1;
            lastMax = 0;
//            max=0;
//            min=0;
        }

    }


    @Override
    public void onSleepStarted(boolean b) {
        setSleepStarted(b);
    }

    @Override
    public void onStartAlarmTime(long startAlarm) {
        setStartAlarmTime(startAlarm);
    }

    @Override
    public void onEndAalrmTime(long endAlarm) {
        setEndAlarmTime(endAlarm);
    }

    private void pushToSleep(DataModelPressure model, long startTime, long endTime) {
        accelImp.onSleepdataAvail(model, startTime, endTime);
    }

    private DataModelPressure copy(DataModelPressure model) {
        modelTemp = new DataModelPressure();
        modelTemp.setTimestamp(model.getTimestamp());
        modelTemp.setTimestampDate(model.getTimestampDate());
        modelTemp.setPressureProcessed(model.getPressureProcessed());
        modelTemp.setBattery(model.getBattery());
        modelTemp.setPressure(model.getPressure());
        modelTemp.setTemprature(model.getTemprature());
        modelTemp.setX(model.getX());
        modelTemp.setY(model.getY());
        modelTemp.setZ(model.getZ());
        modelTemp.setCharging(model.getCharging());
        modelTemp.setDate(model.getDate());
        modelTemp.setTime(model.getTime());
        modelTemp.setmX(model.getmX());
        modelTemp.setmY(model.getmY());
        modelTemp.setmZ(model.getmZ());
        modelTemp.setgX(model.getgX());
        modelTemp.setgY(model.getgY());
        modelTemp.setgZ(model.getgZ());
        modelTemp.setLoadCell1(model.getLoadCell1());
        modelTemp.setLoadCell2(model.getLoadCell2());
        modelTemp.setDatetime(model.getDatetime());
        modelTemp.setUserId(model.getUserId());
        modelTemp.setMic(model.getMic());
        modelTemp.setOptical(model.getOptical());
        modelTemp.setSerialNo(model.getSerialNo());
        modelTemp.setUnused(model.getUnused());
        return modelTemp;
    }

    private void processForStepCounting(DataModelPressure model) {
        //TODO We need to check step and activity in this method
        accelImp.onModelAvail(model);
    }

    /**
     * Here we are trying to create file with both raw and process data of a pressure value.
     * Also passing processed pressure data with moving average1 to the activity to show the graph
     * Then we try to find whether device is attached to body or not.
     * For that process first we are checking if pressure value is smallest or not.
     * If the pressure value is very less then making that pressure value to be smallest.
     * Then we check for 100 of data point the smallest number is still small or not.
     * If continuous smallest no is there then device is not attached. else device attached.
     *
     * @param model
     * @param context
     */
    private void processModelData(DataModelPressure model, Context context) {

        rolling3.add(model.getPressure());
        alpha = getAlphaFilter(rolling3.getaverage());
        model.setPressureProcessed(rolling3.getaverage());
//        alpha=getAlphaFilter(model.getPressure());
//        lowPassFilterValue=getLowPassFilterValue(rolling3.getaverage(),alpha);
//        lowPassFilterValue=getLowPassFilterValue(model.getPressure(),alpha);
//        listener.onDataProcessed(calculateProportion(model.getPressure()));
//        tempPressure= calculateProportion(lowPassFilterValue,alpha);

        dataModel = copy(model);
        dLastnew = a * calculateProportionBySTD(model.getPressure()) + ((1 - a) * Double.parseDouble(df2.format(dLastnew)));
        Log.d(TAG, "processModelData: " + dataModel.getPressure() + " Processed Done: " + dLastnew);

//        dLastnew = calculateProportionBySTD(model.getPressure());
        tempPressure = (PI_MIN + ((PI_MAX - PI_MIN) * (dLastnew - (lastMin)) / (lastMax - lastMin)));
        rollingGf.add(tempPressure);
        rollingGf2.add(rollingGf.getaverage());
        tempPressure = rollingGf2.getaverage();
        listener.onDataProcessed(tempPressure);

//        String tempPressure = "";
        rolling.add(model.getPressure());
        model.setPressure(rolling.getaverage());
        rolling2.add(rolling.getaverage());
        model.setPressure(rolling2.getaverage());
//        model.setPressure(getLowPassFilterValue(dataModel.getPressure(),0.96d));
//        secondPref(model.getPressure());

//        tempPressure = calculateProportion(model.getPressure(), 0.96d);

        //Log.d(TAG, "count : " + model.getPressure());
        c++;
        if (movement) {
            hasMovementInGathering = true;
        }

        //tryToFindPeakAndTrough(model.getPressure(), model.getTimestamp(), 2.0d);

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(furtherImplementationOnPressureData(model, context));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


//        executor.execute(new SocketWorker(dataModel,model,serverIp));

//        new Thread(new SocketWorker(dataModel,model,serverIp)).start();

//        rolling3.add(rolling2.getaverage());
//        dataModel=new DataModelPressure();
//        dataModel.setPressure(rolling3.getaverage());
    }

    double maximum = 0;
    double minimum = 0;
    int maximumPos = 0;
    int minimumPos = 0;
    double value = 0;
    int maxPos = 0;
    int minPos = 0;
    boolean lookForMax = true;
    boolean peakDetected = false;
    boolean peak2Detected = false;
    boolean troughDetected = false;
    boolean trough2Detected = false;
    long startTimestamp = 0;
    long endTimestamp = 0;

    private synchronized void tryToFindPeakAndTrough(double pressure, long timestamp, double delta) {
//        Observable.create(new ObservableOnSubscribe<Boolean>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
        if (startTimestamp == 0) {
            startTimestamp = timestamp;
        }
        value = pressure;
        if (maximum == 0 || value > maximum) {
            maximum = value;
            maximumPos = maxPos;
        }

        if (minimum == 0 || value < minimum) {
            minimum = value;
            minimumPos = minPos;
        }

        if (lookForMax) {
            if (value < maximum - delta) {
                if (peakDetected) {
                    //Log.d(TAG, "Deteced: " + "peak2Detected  222222");
                    peak2Detected = true;
                    endPos = minPos;
                } else {
                    //Log.d(TAG, "Deteced: " + "peakDetected");
                    if (!peakDetected) {
                        peakDetected = true;
//                        startPos = minPos;
                    }
//                    peakDetected = true;
//                    startPos = minPos;
                }
                minimum = value;
                minimumPos = minPos;
                lookForMax = false;
            }
        } else {
            if (value > minimum + delta) {
                if (troughDetected) {
                    //Log.d(TAG, "Deteced: " + "trough2Detected  222222");
                    trough2Detected = true;
                    endPos = maxPos;
                } else {
                    //Log.d(TAG, "Deteced: " + "troughDetected");
                    if (!troughDetected) {
                        troughDetected = true;
//                        startPos = maxPos;
                    }
                }
                maximum = value;
                maximumPos = maxPos;
                lookForMax = true;
            }
        }


        if ((peakDetected && troughDetected && peak2Detected)) {
//            peakDetected = false;
            troughDetected = false;
            peak2Detected = false;
            trough2Detected = false;

            endTimestamp = timestamp;
//            counted = calculateTimeTaken(startPos, endPos);
//            counted = calculateTimeTaken(startPos, endPos);
            peakDetected = true;
            startPos = endPos;
//            maxPos = 0;
//            minPos = 0;
        } else if (troughDetected && peakDetected && trough2Detected) {
            peakDetected = false;
//            troughDetected = false;
            peak2Detected = false;
            trough2Detected = false;
            endTimestamp = timestamp;
//            counted = calculateTimeTaken(startPos, endPos);
            troughDetected = true;
            startPos = endPos;
        }
        maxPos++;
        minPos++;
//                e.onNext(counted);
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Observer<Boolean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    private boolean calculateTimeTaken(int minPos, int maxPos) {
        int totalTime = maxPos - minPos;
        long totalMillies = totalTime * 50;
        if (totalMillies <= 1000) {
            startTimestamp = 0;
            endTimestamp = 0;
            return false;
        }
//        if (endTimestamp - startTimestamp > 0)
//        //Log.d(TAG, "Deteced = TimeTaken" + (ONE_MINUTE / (endTimestamp - startTimestamp)) + " " + endTimestamp + " " + startTimestamp);
        newCount = (ONE_MINUTE / totalMillies);
//        //Log.d(TAG, "Deteced = TimeTaken" + (ONE_MINUTE / totalMillies) + " " + minPos + " " + maxPos + " " + ONE_MINUTE / (endTimestamp - startTimestamp));
        try {
            if (newCount < 35) {
                if (lastCount > newCount + 1)
                    lastCount -= 1;
                else if (lastCount < newCount - 1) {
                    lastCount += 1;
                } else {
                    lastCount = newCount;
                }
                //Log.d(TAG, "Deteced Count= NewCount" + newCount + " LastCount : " + lastCount);
                listener.onCountAvailable((int) lastCount, System.currentTimeMillis());

//            lastCount=newCount;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        startTimestamp = 0;
        endTimestamp = 0;
        return true;
    }

    private String calculatePerMinute(long l) {
        return String.valueOf(((ONE_MINUTE) / l));
    }

    private int furtherImplementationOnPressureData(DataModelPressure model, Context context) {
        checkIfDataGatheringCompleted(model, context, dataModel);
        pModel = model;
        changed = true;
        //Log.d(TAG, "SmallestPressure:" + mSmallestPressure);
        if (mSmallestPressure <= 500 || (mSmallestPressure > model.getPressure()))
            mSmallestPressure = model.getPressure();
        if (mPressureCount > 100) {
            if (isStillSmallest && !isNotSmallest) {
                attached = false;
                listener.onDeviceNotAttached();
            } else {
                attached = true;
                listener.onDeviceAttached();
            }
            mPressureCount = 0;
            isStillSmallest = false;
            isNotSmallest = false;
//            mSmallestPressure=model.getPressure();
        }
        if (Math.abs(model.getPressure() - mSmallestPressure) < 40 && mSmallestPressure < 1000) {
            isStillSmallest = true;
        } else {
            isNotSmallest = true;
        }

        mPressureCount += 1;
        return mPressureCount;
    }

    @Override
    public void startSendingData(String stringExtra) {
        startSocket(stringExtra);

    }

    @Override
    public void onSocketStopped() {
        connected = false;
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
    }

    Stack<Breathingmaster> breathStack = new Stack<>();

    @Override
    public void arrangeBreathingForServer(String url, int actualLastCount, long actualLastTimestamp, BreathState actualLastState, int count, long timestamp, BreathState currentState) {
        if (lastServerTimestamp != actualLastTimestamp && actualLastCount > 0) {
            Breathingmaster detailOld = new Breathingmaster();
            detailOld.setCount(actualLastCount);
            detailOld.setTimeOf(Helper.convertTimestampToTime(actualLastTimestamp));
            detailOld.setStateofmindmasterId(Helper.getIdByState(actualLastState));
            BreathingModel model = AppApplication.getInstance().addBreathing(detailOld);
            //Log.d(TAG, "OKL:" + "lastServerTimestamp!=actualLastTimestamp");
        }
        Breathingmaster detail = new Breathingmaster();
        detail.setCount(count);
        detail.setTimeOf(Helper.convertTimestampToTime(timestamp));
        detail.setStateofmindmasterId(Helper.getIdByState(currentState));
        BreathingModel model = AppApplication.getInstance().addBreathing(detail);
        lastServerTimestamp = timestamp;

        utility = new NetworkUtility.NetworkBuilder().setHeader().build();
        utility.saveBreath(url, model, new NetworkUtility.ResponseListener() {
            @Override
            public void onSuccess(Object message) {
//                TODO response
                AppApplication.getInstance().removeBreathing(detail);
            }

            @Override
            public void onFailure(Object err) {
//                TODO response
                ((Throwable) err).printStackTrace();
            }

            @Override
            public void onSomethingWrong(Object e) {
//                TODO response

            }
        });
    }

    @Override
    public void arrangeBreathingForServer(String url, int count, long timestamp, BreathState currentState) {
        Breathingmaster detail = new Breathingmaster();
        detail.setCount(count);
        detail.setTimeOf(Helper.convertTimestampToTime(timestamp));
//        detail.setCreated(Helper.convertTimestampToTimeCommaSeperated(timestamp));

        detail.setStateofmindmasterId(Helper.getIdByState(currentState));
        BreathingModel model = AppApplication.getInstance().addBreathing(detail);
//        if(timestamp-lastTmpStmp>65000){
//            lastBreathingState=BreathState.UNKNOWN;
//        }else {
//            if(lastBreathingState!=currentState && currentState!=BreathState.UNKNOWN){
//                change
//            }
//        }
//        lastTmpStmp=timestamp;

        if (!breathStack.empty()) {
            Breathingmaster temp = breathStack.pop();
            if (temp.getStateofmindmasterId() == Helper.getIdByState(currentState)) {
                breathStack.add(detail);
                isPreviousBreathingChecked = true;
                return;
            } else {
                if (!isPreviousBreathingChecked)
                    AppApplication.getInstance().changeLastBreathingState();

                isPreviousBreathingChecked = false;
            }
        }
        breathStack.add(detail);
        isPreviousBreathingChecked = false;

        utility = new NetworkUtility.NetworkBuilder().setHeader().build();
        utility.saveBreath(url, model, new NetworkUtility.ResponseListener() {
            @Override
            public void onSuccess(Object message) {
//                TODO response
                AppApplication.getInstance().removeBreathing(detail);
            }

            @Override
            public void onFailure(Object err) {
//                TODO response
                ((Throwable) err).printStackTrace();
            }

            @Override
            public void onSomethingWrong(Object e) {
//                TODO response
            }
        });
    }

    @Override
    public void arrangeStepsForServer(String url, int step, TblStepCount countLast, boolean fi) {
        if (lastStep == step)
            return;

        lastStep = step;


        Stepmaster model;
        if (!fi) {
            model = new Stepmaster();
            model.setCalriesburn(0);
            model.setFromtime(Helper.convertTimestampToTime(countLast.getStartTimestamp()));
            model.setTotime(Helper.convertTimestampToTime(countLast.getEndTimestamp()));
            model.setSteps(countLast.getSteps());
            model.setGoal(countLast.getGoal());
            AppApplication.getInstance().addSteps(model);
        } else {
            AppApplication.getInstance().updateSteps(countLast.getSteps(), 0, countLast.getEndTimestamp());
        }


//        List<itg8.com.nowzonedesigndemo.utility.model.step.ItemDetail> details = new ArrayList<>();
//        detail.setDate(Helper.convertTimestampToTime(System.currentTimeMillis()));
//        detail.setSteps(step);
//        details.add(detail);
//        model.setItemDetails(details);

    }

    @Override
    public void sendRemainingStepsToServer(String url) {
        StepsModel model = AppApplication.getInstance().getStepsToSendOnServer();
        StepsModel tempModel = new StepsModel();
        List<Stepmaster> stepmasters = new ArrayList<>();
        if (model.getStepmaster() == null || model.getStepmaster().size() <= 0) {
            return;
        } else {
            for (Stepmaster m :
                    model.getStepmaster()) {
                if (!m.getFromtime().equalsIgnoreCase(m.getTotime())) {
                    stepmasters.add(m);
                }
            }
            tempModel.setStepmaster(stepmasters);
        }
        if (utility == null)
            utility = new NetworkUtility.NetworkBuilder().setHeader().build();
        utility.saveSteps(url, tempModel, new NetworkUtility.ResponseListener() {
            @Override
            public void onSuccess(Object message) {
                //TODO do something
                AppApplication.getInstance().resetSteps();
            }

            @Override
            public void onFailure(Object err) {
                ((Throwable) err).printStackTrace();
            }

            @Override
            public void onSomethingWrong(Object e) {

            }
        });
    }


    @Override
    public void resetCountAndAverage() {
        if (accelImp != null) {
            accelImp.resetStepCount();
        }
    }

    @Override
    public void resetSteps() {
        accelImp.setStepListener();
    }

    private void startSocket(String ipAddress) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ipAddress);
                    //Log.d("ClientActivity", "C: Connecting...");
                    Socket socket = new Socket(serverAddr, 8080);
                    connected = true;
                    while (connected) {
                        try {
                            if (changed) {
                                //Log.d("ClientActivity", "C: Sending command.");
                                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                        .getOutputStream())), true);
                                // WHERE YOU ISSUE THE COMMANDS


//                                out.println
//                                        ("Raw: " + dataModel.getTimestamp() + " " + dataModel.getPressure() + " "
//                                        + dataModel.getX()
//                                        + " "
//                                        + dataModel.getY()
//                                        + " "
//                                        + dataModel.getZ()
//                                        + " Processed: "
//                                        + pModel.getPressure() + " "
//                                        + pModel.getX() + " "
//                                        + pModel.getY() + " "
//                                        + pModel.getZ());

//                                out.println
//                                        ("Raw: " +dataModel.getRawString()
//                                        + " Processed: "
//                                        +pModel.getAllInHex());

                                if (toSendSocket != null)
                                    out.println
                                            (toSendSocket);


                                //Log.d("ClientActivity", "C: Sent.");
                                changed = false;
                            }
                        } catch (Exception e) {
                            //Log.e("ClientActivity", "S: Error", e);
                        }
                    }
                    socket.close();
                    //Log.d("ClientActivity", "C: Closed.");
                } catch (Exception e) {
                    //Log.e("ClientActivity", "C: Error", e);
                    listener.onSocketInterrupted();
                    connected = false;
                }
            }
        });
        thread.start();

    }

    private synchronized void checkIfDataGatheringCompleted(DataModelPressure model, Context context, DataModelPressure rawData) {
        dataStorage[indexDataStorage] = model;
        //TODO this is only to create file with both filters. this will help to understand to noise problem.
        dataStorageRaw[indexDataStorage] = rawData;
        // //Log.d(TAG,"Size of dataStorage "+dataStorage.size());
        tempLineChartData[indexDataStorage] = tempPressure;
        if (indexDataStorage == PACKET_READY_TO_IMP - 1) {
            //Log.d(TAG, "datas  is greater");
            implementStorageProcess(context, dataStorage);
            indexDataStorage = 0;
        } else {
            indexDataStorage++;
        }
    }


    private void implementStorageProcess(Context context, DataModelPressure[] dataStorage) {
        //
        tempHolder = new DataModelPressure[ROLLING_AVG_SIZE];
        tempHolder = dataStorage.clone();

        tempHolderRaw = new DataModelPressure[ROLLING_AVG_SIZE];
        tempHolderRaw = this.dataStorageRaw.clone();
//        resetDataStorage(this.dataStorage);
//        resetDataStorage(this.dataStorageRaw);
        double[] tempLineChartRaw = tempLineChartData.clone();
        //We will do that after SAAS TODO SAAS
        passForFIleStorage(tempHolderRaw, context);

        //passForCalculation(tempHolder, tempLineChartRaw);
    }

    private void passForFIleStorage(DataModelPressure[] dataStorage, Context context) {
        /**
         * Save Data into database ;
         */
        storeDataInDatabase(dataStorage);


//        FileAsync async = new FileAsync(Prefs.getString(CommonMethod.STORAGE_PATH, ""));
//        async.execute(this.tempHolderRaw);


    }

    private void storeDataInDatabase(DataModelPressure[] dataStorage) {
        Observable.fromArray(dataStorage).flatMap(new Function<DataModelPressure, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> apply(DataModelPressure t) throws Exception {
//                preAccSateImp = new PresureAccelerometerSateImp(context);
//                boolean isSave = storeDataInDatabase(t, context);

                return Observable.just(listener.onSensorDataAvailToStore(t));

            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "onError: ",e );
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private boolean storeDataInDatabase(DataModelPressure dataStorage, Context context) {
        int insert = -1;

        if (dataStorage != null) {
            //Log.d(TAG, "storeDataInDatabase: b4 data store ");

            insert = preAccSateImp.create(dataStorage);
            //Log.d(TAG, "storeDataInDatabase: after  data store ");

        }
        if (insert > -1) {

            return true;
        } else
            return false;


    }


    private void scheduleJob(Context context) {
        //Log.d(TAG, "scheduleJob: ");

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(
                Context.JOB_SCHEDULER_SERVICE);

        ComponentName name = new ComponentName(context, NetworkCallScheduler.class);
        int result = jobScheduler.schedule(getJobInfo(123, 1, name));

        if (result == JobScheduler.RESULT_SUCCESS) {
            //Log.d(TAG, "Scheduled job successfully!");

        } else {
            //Log.d(TAG, "scheduleJob:  not connect ");
        }

    }

    private JobInfo getJobInfo(final int id, final long minute, ComponentName name) {
        final long interval = TimeUnit.SECONDS.toMillis(minute); // run every minute
        //Log.d(TAG, "getJobInfo: interval " + interval);
        final JobInfo jobInfo;
//        String value = new Gson().toJson(dataStorage);
//        PersistableBundle bundle = new PersistableBundle();
//        bundle.putString("data", value);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(interval)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
        }

        return jobInfo;
    }


    private String[] getListOfFile(String path) {
        return new String[0];
    }

    private void passForCalculation(DataModelPressure[] dataStorage, double[] tempLineChartRaw) {
        //Log.d(TAG, "came for calculation: " + dataStorage.length);
        if (!movement) {
            async = new AlgoAsync(this, tempLineChartRaw);
            async.execute(dataStorage);
        }
    }

    private void resetDataStorage(List<DataModelPressure> dataStorage) {
        dataStorage.clear();
    }

    @Override
    public void onCountResultAvailable(int count, long timestamp, SparseArray<List<Integer>> allDiff) {
        //Log.d(TAG, "bpmCount = " + count);
        if (!attached) {
            listener.onDeviceNotAttached();
            return;
        }
        if (!hasMovementInGathering)
            listener.onCountAvailable(count, timestamp, allDiff);
        else
            listener.onResetInititalBreathingState();
        hasMovementInGathering = false;

    }

    @Override
    public void onCountFail() {

    }

    @Override
    public void onMotionStarts() {
        //Log.d(TAG, "motion: started");
    }

    @Override
    public void onMotionEnds() {
        //Log.d(TAG, "motion: finish");
    }

    @Override
    public void onStep(int step) {
        if (attached) {
            //Log.d(TAG, "valid step attached: " + step);
            listener.onStepCountReceived(step);
        } else {
            //Log.d(TAG, "valid not attached: " + step);
        }
    }

    @Override
    public void onSleepInterrupted(long timestamp) {

        listener.onSleepInterrupted(timestamp);
    }

    @Override
    public void startWakeupService() {
        listener.onStartWakeupSevice();
    }

    @Override
    public void onMovement(float mAccel) {
        listener.onMovement(mAccel);
        movement = true;
    }

    @Override
    public void onNoMovement(float i) {
        listener.onNoMovement(i);
        movement = false;
    }

    @Override
    public void onSittingForLast30Min() {
        listener.onNotifySittingFor30Min();
    }

    @Override
    public void onPostureClibrated() {
        listener.postureCalibratedSuccess();
    }

    @Override
    public void onFailToCalibratePosture() {
        listener.postureCalibrationFail();
    }

    @Override
    public void onAngleAvail(double something) {
//        something=calculate((float) something);
        listener.onAxisDataAvail(something, 0);
        checkPosture(calculate((float) something)).subscribeOn(Schedulers.io()).subscribe();
    }

    private Observable checkPosture(float something) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(check5SecPosture(something));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }


    private int check5SecPosture(float accelY) {
        //Log.d(TAG, "TIMESTAMPFOR20DATA " + accelY);

        if (Prefs.getBoolean(CommonMethod.POSTURE_PAUSE, false)) {
            return 0;
        }
        if (movement || !attached)
            return 0;
        calibrate = Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION, 0);
        accelY = Math.abs(accelY);
        if (calibrate != 0) {
            if (countPosture < 300) {
                accelGatheredData[countPosture] = accelY;
                countPosture++;
            } else {
                float sum = 0;
                for (float f :
                        accelGatheredData) {
                    sum += f;
                }

                sum = sum / 300;

                countPosture = 0;
                //Log.d(TAG, "POSTURERAW: " + accelY + " " + calibrate);
                Arrays.fill(accelGatheredData, 0);
                if (sum > calibrate + 100 || sum < calibrate - 100) {
                    listener.onPostureResult(false);

                } else {
                    listener.onPostureResult(true);

                }
            }
        }
        return 0;
    }

    @Override
    public void setCalibrated(float aFloat) {
        calibrate = aFloat;
    }

    @Override
    public void startCalibration() {
        accelImp.startCalibrationg();
    }

    @Override
    public void onDisconnected() {
        disconnected = true;
    }

    @Override
    public void onConnected() {
        disconnected = false;
    }
}
