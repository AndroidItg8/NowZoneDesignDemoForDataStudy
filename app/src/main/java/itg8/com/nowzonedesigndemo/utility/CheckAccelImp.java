package itg8.com.nowzonedesigndemo.utility;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;


class CheckAccelImp {

    private static final String TAG = CheckAccelImp.class.getSimpleName();
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    private static final int TOTAL_SIZE_OF_DATA_COLLECTION = 20;
    private static final double G = 0.244;
    private static final double M_PI = 3.1415;
    private static final long MIN_DEEP_SLEEP_DURATION = 3;
    //COUNT CALCULATED BY 60 SEC DEVICE DATA COUNT/6;
    private static final int TEN_SEC_COUNT = 199;
    private static final int MAX_POSSIBLE_STEPS = 25;
    private static final int MIN_POSSIBLE_STEPS = 12;
    private static int nStepCount = 0;
    private AccelVerifyListener listener;
    private Observer observer;
    private double x2, y2, z2;
    private double vector;
    //Digital sum filter variables
    private int[] pedi_fil_x = new int[4];
    private boolean calibarated = false;

    //TODO
    private int[] pedi_fil_y = new int[4];
    private int[] pedi_fil_z = new int[4];
    private int pedi_fil_index;
    //Steps parameter variables
    private int pedi_sampling_counter;
    private int[] pedi_max = new int[3];
    private int[] pedi_min = new int[3];
    private int[] pedi_axis_result = new int[3];
    private int pedi_result;
    private int[] pedi_p2p = new int[3];
    private int[] pedi_ths = new int[3];
    private int pedi_new_fixed, pedi_old_fixed, pedi_precision;
    private int pedi_threshold;
    private int pedi_refresh_frequency;
    //Time window variables
    private int pedi_bad_flag;
    private int pedi_interval = 0;
    private int pedi_step_counter;
    private int i;
    private int axisIndex;
    private int lastStepVal = 0;
    private long lastTMP = 0;
    private double degconvert = 57.29;
    private double pitch;
    private double roll;
    DataModelPressure[] models;
    private int modelCounter = 0;
    private int count = 0;

    Rolling rolling;
    Rolling rollingForStep;
    private double sleep_threshold = 0;
    private double sleep_thresholdX = 0;
    private double sleep_thresholdY = 0;
    private double sleep_thresholdZ = 0;
    private boolean isSleepThresholdCalculated = false;
    private long alarmStartTime;


    //TODO uncomment
    private int stepListener = 0;


    private File completeFileStructure;
    private String info;
    private static final float alpha = 0.2f;
    private double fXg = 0;
    private double fYg = 0;
    private double fZg = 0;
    private double mAccelLast = 0;
    private float mAccel = 0;
    private double roll2 = 0;
    private int countSleep = 0;
    private boolean sleepEnded;
    private int lastStepSent = 0;
    private long lastMovementTmp;
    private boolean isNotified = false;
    private double theta;
    private double something;
    StepModule stepModule;
    int todaysActualSteps = 0;
    private int is25Steps = 0;
    private int validStepCount = 0;
    private double newZ;
    private double newY;
    private double newX;
    private double tempX;
    private double tempY;
    private double tempZ;
    private List<Double> collectionForStep = new ArrayList<>();

    /**
     * We will pass the listener and latest step count received before service destroyed.
     *
     * @param listener           Listener back for RdataManagerImp.
     * @param mPreviousStepCount reent step count
     */
    public CheckAccelImp(final AccelVerifyListener listener, int mPreviousStepCount) {
        this.listener = listener;
        lastMovementTmp = System.currentTimeMillis();
        initPedometer(mPreviousStepCount);
        nStepCount = mPreviousStepCount;
        todaysActualSteps = mPreviousStepCount;
        rolling = new Rolling(1200);
        rollingForStep = new Rolling(5);
        models = new DataModelPressure[TOTAL_SIZE_OF_DATA_COLLECTION];
        observer = new Observer<StepModule>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(StepModule model) {
//                if (model)
//                    listener.onMotionStarts();
//                else
//                    listener.onMotionEnds();
                /**
                 * THIS LOGIC DEFINES AS,
                 * First gather last 10 sec data and check whether there are steps available or not,
                 * if steps available then increase is25Steps step count.
                 * If 10 sec count complete then find out whether steps are in the defined range.
                 * If so add those to todays actual steps count
                 * else reset is25Steps to 0; and validStepCount to 0
                 */

                if (validStepCount <= TEN_SEC_COUNT) {

                    if (0 != model.step) {
                        is25Steps++;
                    }
                } else {
                    if (is25Steps <= MAX_POSSIBLE_STEPS && is25Steps > MIN_POSSIBLE_STEPS) {
                        todaysActualSteps = todaysActualSteps + is25Steps;
                        listener.onStep(todaysActualSteps);
                        //Log.d(TAG, "valid step:" + is25Steps + " todaysSteps:" + todaysActualSteps);
                        is25Steps = 0;
                    } else {
                        //Log.d(TAG, "valid not step:" + is25Steps);
                        is25Steps = 0;
                    }
                    validStepCount = 0;
                }

                validStepCount++;
//                lastStepSent = model.step;
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

//    private void sendToServer(int steps) {
//        NetworkUtility utility=new NetworkUtility.NetworkBuilder().setHeader().build();
//        utility.saveSteps();
//    }

    public void resetStepCount() {
        pedi_step_counter = 0;
        todaysActualSteps = 0;
    }

    private DecimalFormat formatter = new DecimalFormat("#0.00");
    String log;
    StringBuilder sb;

    /**
     * this is actual method to calculate steps and sleep. Here we took G(Sensitivity) as
     * 0.224 for removing acceleration due to gravity.
     * CheckMovementForView is to check whether accelerometer having large movement or not.
     * When there is large movement we notify about movement in HomeActivity
     * We have check angle of movement in accelerometer for gesture recognition.
     * checkForSteps() is an actual method to calculate steps.
     *
     * @param model
     * @return
     */
    private Observable<StepModule> checkMovement(DataModelPressure model) {

        return Observable.create(e -> {
//            double xG = (model.getX()>32768)?model.getX():model.getX()-65536;
//            double yG = (model.getY() >32768) ?model.getY():model.getY()-65536;
//            double zG = (model.getZ() >32768) ?model.getZ():model.getZ()-65536;
            double xG = model.getX();
            double yG = model.getY();
            double zG = model.getZ();
            sb = new StringBuilder();
            sb.append("RawXYZ: X: ").append(xG).append(" Y: ").append(yG).append(" Z: ").append(zG);
            //Log.d(TAG, sb.toString());

            checkMovementForView(xG, yG, zG);
            something = calculatePitchRoll(xG, yG, zG);
            listener.onAngleAvail(something);

            //Log.d(TAG, "SOMETHING : " + something);

            xG = (xG * G) / 1000;
            yG = (yG * G) / 1000;
            zG = (zG * G) / 1000;


//            mAccelLast = roll;

            roll = sqrt((xG * xG) + (yG * yG) + (zG * zG));

//            float delta = (float) (roll - mAccelLast);
//            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

//            //Log.d("gdata:","X:"+xG+" Y:"+yG+" Z:"+zG);
//              //Log.d("Rollng avg:",String.valueOf(roll));

//            log="X:"+formatter.format( (model.getX() * 0.224)/1000)+ " Y:"+formatter.format((model.getY() * 0.224)/1000)+" Z:"+formatter.format((model.getZ() * 0.224)/1000);
//            log="X:"+formatter.format( (model.getX() * 0.224)/1000)+ " Y:"+formatter.format((model.getY() * 0.224)/1000)+" Z:"+formatter.format((model.getZ() * 0.224)/1000);
//              //Log.d("gdata:",String.valueOf(log));
            /**
             * This is for creating file with roll
             */
//            Observable.create((ObservableOnSubscribe<String>) e1 -> {
//                //createFile(String.valueOf(roll));
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
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });

//                    print(" Y =   %fG  #####" % ((ACCy * 0.224)/1000)),
//                    print(" Z =  %fG  #####" % ((ACCz * 0.224)/1000))
            pitch = atan2(-model.getX(), sqrt((model.getY() * model.getY()) + (model.getZ() * model.getZ()))) * degconvert;

            // //Log.d(TAG, "value ---" + model.getX() + " " + model.getY() + " " + model.getZ() + " roll " + roll + " pitch " + pitch);
//            //Log.d(TAG, "value ---" + model.getX() + " " + model.getY() + " " + model.getZ() + " theta " + theta + " pie " + pie+" alpha "+alpha);
//                double vector = calculateVector(model.getX(), model.getY(), model.getZ());
//                //Log.d(TAG, "vector: " + vector);
            /**
             * This is new method for testing 22/06/2017
             * <TESTED
             */

//            if(modelCounter==TOTAL_SIZE_OF_DATA_COLLECTION) {
//                modelCounter=0;
//                int count=analyzeAccel(models, TOTAL_SIZE_OF_DATA_COLLECTION);
//                Logs.d("COUNT STEP:"+count);
//                e.onNext(new StepModule(count,System.currentTimeMillis()));
//                models[modelCounter]=model;
//            }else {
//                models[modelCounter]=model;
//                modelCounter++;
//            }

            /**
             * This is old method providing value on tilt in sitting position  12/06/2017
             */
//            e.onNext(updateStepParameter((int) model.getX(), (int) model.getY(), (int) model.getZ(), model.getTimestamp()));


            /**
             * My old method from step calculation in AbcApp
             */
            e.onNext(checkForSteps(roll, model.getTimestamp()));
            e.onComplete();
//                if (updateStepParameter((int) model.getX(), (int) model.getY(), (int) model.getZ()) != lastStepVal){
            lastStepVal = pedi_step_counter;
        });
    }

    /**
     * This new algorithm found is from observation.
     * <p>
     * First we manipulate x, y, z axis data as follow
     * val=ABS(X)+ABS(Y)+ABS(Z)
     * Then we will check whether collected data has pattern.
     * To check pattern we will first try to find if each peak and trough are in plus minus sign
     * or not. If so then we check whether difference between two trough diff and average diff
     * is not more. If condition satisfied then valid step.
     */
    private void addForStep() {
        collectionForStep.add(rollingForStep.getaverage() - 250);
    }

    private void checkIfStepsGatheringCompleted() {
        List<Map<Integer, Double>> result = CommonMethod.peak_detection(collectionForStep, 10.0);
        TreeMap<Integer, Double> all = new TreeMap<>();

        all.putAll(result.get(0));
        all.putAll(result.get(1));
        List<Integer> allIndex = new ArrayList<>(all.keySet());
        List<Double> allValues = new ArrayList<>(all.values());
        for (int i = 1; i < all.size() - 1; i++) {
            if (allValues.get(i) > allValues.get(i + 1) && allValues.get(i) > allValues.get(i - 1)) {

                if (checkSign(allValues.get(i)) >= 1 &&
                        checkSign(allValues.get(i - 1)) < 1 &&
                        checkSign(allValues.get(i + 1)) < 1
                        ) {
                    int diff = allIndex.get(i + 1) - allIndex.get(i - 1);
                    if (0 == getAverageStepsDiff()) {
                        addAverageSteps(diff);
                        return;
                    }


                }
            }
        }
    }

    private void addAverageSteps(int diff) {
        AppApplication.getInstance().addAverageStepsDiff(diff);
    }

    private double getAverageStepsDiff() {
        return AppApplication.getInstance().getAverageStepCountTimeDiff();
    }

    private int checkSign(double v) {
        return (int) (v / Math.abs(v));
    }

    private synchronized void checkMovementForView(double xG, double yG, double zG) {
        mAccelLast = roll2;
        roll2 = sqrt((xG * xG) + (yG * yG) + (zG * zG));
        //Log.d(TAG, "ROLLING VECTOR:" + roll2);

        float delta = (float) (roll2 - mAccelLast);
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        // //Log.d(TAG, "ACCEL:" + mAccel + " DELTA:" + delta);
        rollingForStep.add(roll2);

        detectPeak(mAccel);
//        listener.onMovement(mAccel);


    }

    private List<Double> accList = new ArrayList<>(20);

    private void detectPeak(float accel) {
        if (accList.size() >= 20) {
            detect();
        }
        accList.add((double) accel);
    }


    public void setStepListener() {
        this.todaysActualSteps = 0;
    }

    private void detect() {

        List<Map<Integer, Double>> result = CommonMethod.peak_detection(accList, 50.0);
        if (result.size() > 0) {
            if (result.get(0) != null && result.get(1) != null) {
                //Log.i(TAG, "peaks in accel: " + result.get(0).size());
                if (result.get(0).size() >= 1
                        && result.get(0).size() <= 3
                        && result.get(1).size() >= 1
                        && result.get(1).size() <= 3) {

                    count += 1;
//                    btnAdd.setText("Movement");
                    lastMovementTmp = System.currentTimeMillis();
                    listener.onMovement(0);
                    addForStep();
                } else if (result.get(0).size() > 3) {
//                    feedMultiple();
//                    btnAdd.setText("Movement");
                    lastMovementTmp = System.currentTimeMillis();
                    listener.onMovement(0);
                    checkIfStepsGatheringCompleted();
                } else {
//                    shouldStart=false;
                    listener.onNoMovement(0);
                }
                if (System.currentTimeMillis() > lastMovementTmp + CommonMethod.MINUTE * 30 && !isNotified) {
                    listener.onSittingForLast30Min();
                    isNotified = true;
                }
            }
        }
//        if(accel>80){
//        }
        accList.clear();
    }


    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    private void createFile(String log) {
        completeFileStructure = new File(Environment.getExternalStorageDirectory() + File.separator + "nowzone", "StepDataWithGImp.txt");
        try {
            FileWriter fWriter;
            if (completeFileStructure.exists()) {
                fWriter = new FileWriter(completeFileStructure, true);
                fWriter.append(log).append("\n");
            } else {
                fWriter = new FileWriter(completeFileStructure, true);
                fWriter.write(log);
            }
            fWriter.flush();
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private double calculatePitchRoll(double x, double y, double z) {
//        //Log.v(TAG, "axis: x:" + x + " y:" + y + " z:" + z);

        //Low Pass Filter
        fXg = fXg * alpha + (x * (1.0 - alpha));
        fYg = fYg * alpha + (y * (1.0 - alpha));
        fZg = fZg * alpha + (z * (1.0 - alpha));


        //Roll & Pitch Equations
        newX = Math.toDegrees(Math.atan((Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) / (z)));
        //Log.d(TAG, "NewX: " + newX);
        newY = y - fYg;
        newZ = z - fZg;
//        roll2 = (atan2(-fYg, fZg) * 360.0) / M_PI;
        pitch = (atan2(fXg, sqrt(fYg * fYg + fZg * fZg)) * 360.0) / M_PI;
        //Log.d(TAG, "Roll: " + (atan2(fYg, fZg) * 180.0) / M_PI);


        //This is from <a href='https://www.biopac.com/wp-content/uploads/app273.pdf'>ss</a>


        theta = (Math.atan(Math.sqrt((x * x) + (y * y)) / z));

//        return atan2(x, y) * 180 / 2.14f;
        return (atan2(fYg, fZg) * 180.0) / M_PI;
    }

    void onModelAvail(DataModelPressure model) {
        checkMovement(model).subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void pedi_fil_init() {

        for (i = 0; i < 4; i++) {
            pedi_fil_x[i] = 0;
            pedi_fil_y[i] = 0;
            pedi_fil_z[i] = 0;
        }
        pedi_fil_index = 0;
    }

    private void pedi_fil_update(int x, int y, int z) {

        pedi_fil_x[pedi_fil_index] = x;
        pedi_fil_y[pedi_fil_index] = y;
        pedi_fil_z[pedi_fil_index] = z;

        pedi_axis_result[X] = 0;
        pedi_axis_result[Y] = 0;
        pedi_axis_result[Z] = 0;
        for (i = 0; i < 4; i++) {
            pedi_axis_result[X] += pedi_fil_x[i];
            pedi_axis_result[Y] += pedi_fil_y[i];
            pedi_axis_result[Z] += pedi_fil_z[i];
        }
        pedi_fil_index++;
        pedi_fil_index = pedi_fil_index >= 4 ? pedi_fil_index - 4 : pedi_fil_index;
    }


    private void updateTimeWindow(long timestamp) {
        if (timestamp - lastTMP > 300) {
            pedi_step_counter++;
            lastTMP = timestamp;
        }
    }

    private void initPedometer(int mPreviousStepCount) {
        //Steps parameter variables
        pedi_sampling_counter = 0;
        for (i = 0; i < 3; i++) {
            pedi_max[i] = 0;
            pedi_min[i] = 0;
            pedi_axis_result[i] = 0;
            pedi_p2p[i] = 0;
            pedi_ths[i] = 0;
        }
        pedi_result = 0;
        pedi_new_fixed = 0;
        pedi_old_fixed = 0;
        pedi_precision = 0;
        pedi_threshold = 0;
        pedi_refresh_frequency = 50;

        pedi_fil_init();

        //Time window variables
        pedi_bad_flag = 0;

        pedi_step_counter = mPreviousStepCount;
    }

//convert the 2 hex values into int for easier processing

    /**
     * this algorithm for step count is getting result even after large tilt in sitting position.
     * <RED>FAIL</RED>
     *
     * @param x
     * @param y
     * @param z
     * @param timestamp
     * @return
     */

    private int updateStepParameter(int x, int y, int z, long timestamp) {

	/*Implemented*/
        //Save the 3-axis samples
        pedi_fil_update(x, y, z);

        //Find 3-axis max and min
        for (i = 0; i < 3; i++) {
            pedi_max[i] = pedi_axis_result[i] > pedi_max[i] ? pedi_axis_result[i] : pedi_max[i];
            pedi_min[i] = pedi_axis_result[i] < pedi_min[i] ? pedi_axis_result[i] : pedi_min[i];
        }

        //Sampling counter inc
        pedi_sampling_counter++;

        if (pedi_sampling_counter >= pedi_refresh_frequency) {
            //Reset Sampling
            pedi_sampling_counter = 0;

            //compute peak to peak value and dc value (dynamic threshold) for each axis
            pedi_p2p[X] = pedi_max[X] - pedi_min[X];
            pedi_p2p[Y] = pedi_max[Y] - pedi_min[Y];
            pedi_p2p[Z] = pedi_max[Z] - pedi_min[Z];

            pedi_ths[X] = (pedi_max[X] + pedi_min[X]) / 2;
            pedi_ths[Y] = (pedi_max[Y] + pedi_min[Y]) / 2;
            pedi_ths[Z] = (pedi_max[Z] + pedi_min[Z]) / 2;
        }

        //Check if new result exceeds precision
        //TBI

        //Update left-shift register
        pedi_old_fixed = pedi_new_fixed;
        pedi_new_fixed = pedi_result;

        //Find axis who's acceleration is largest i.e. highest max (alt. use p2p)
        axisIndex = pedi_max[X] > pedi_max[Y] ? X : Y;
        axisIndex = pedi_max[Z] > pedi_max[axisIndex] ? Z : axisIndex;

        //Set Threshold value
        pedi_threshold = pedi_ths[axisIndex];

        //Decide if a step was taken
        if (pedi_old_fixed > pedi_threshold && pedi_threshold > pedi_new_fixed) {
            updateTimeWindow(timestamp);
        }

        //Save the result of the highest axis
        pedi_result = pedi_axis_result[axisIndex];
        return pedi_step_counter;
    /*Tested*/
    }

    private static int nLastDetectedTime = 0;
    int mSamplingInterval;
    int mTotalTime;

    public static int analyzeAccel(DataModelPressure[] objectArray, int samplingInterval) {

        if (objectArray == null || objectArray.length < 1) {
            return 0;
        }
        int mSamplingInterval = samplingInterval;
        int mTotalTime = 1000;


        // [kbjung]
//        ContentObject co1 = objectArray.get(0);

        int idx = objectArray.length;

        float[] nX = new float[idx];
        float[] nY = new float[idx];
        float[] nZ = new float[idx];
        float[] n3D = new float[idx];

        for (int i = 0; i < idx; ++i) {
//            nX[i] = (float)(co1.mAccelData[i*3]+32768)/65535.f;
            nX[i] = (float) (objectArray[i].getX() + 32768) / 65535.f;
            //Logs.d("#"+nX[i]);
//            nY[i] = (float)(co1.mAccelData[i*3+1]+32768)/65535.f;
            nY[i] = (float) (objectArray[i].getY() + 32768) / 65535.f;
            //Logs.d("#"+nY[i]);
//            nZ[i] = (float)(co1.mAccelData[i*3+2]+32768)/65535.f;
            nZ[i] = (float) (objectArray[i].getZ() + 32768) / 65535.f;
            //Logs.d("#"+nZ[i]);
            n3D[i] = (float) sqrt(nX[i] * nX[i] + nY[i] * nY[i] + nZ[i] * nZ[i]);
//            //Log.d("###","#"+n3D[i]);
        }
        // //Log.d(TAG, "n3D " + Arrays.toString(n3D));

        //PeakDetector peakDetect = new PeakDetector(nY);
        PeakDetector peakDetect = new PeakDetector(n3D);
        int[] res = peakDetect.process(3, 1.5f);

        float fStepTime = 0.f;
        float fStepVelocity = 0.f;
        float fAvgVelocity = 0.f;
        int nTotalSamplingData = 20;

        if (res.length <= 0) {
            //ar.mCalorie = 0;
            //return ar;
            return nStepCount;
        }

        fStepTime = (res[0] + (nTotalSamplingData - nLastDetectedTime)) * 0.05f;
        fStepVelocity = 0.5f / fStepTime;
        fAvgVelocity = fStepVelocity;

        for (int i = 1; i < res.length - 1; ++i) {
            fStepTime = (res[i + 1] - res[i]) * 0.05f;
            fStepVelocity = 0.5f / fStepTime;
            fAvgVelocity += fStepVelocity;
        }

        fAvgVelocity /= res.length;
        fAvgVelocity *= 3.6f; // convert m/s to km/h
        nLastDetectedTime = res[res.length - 1];

        nStepCount += res.length;
//        ar.mShakeActionCount = nStepCount;

        Logs.d("#");
        Logs.d("# of Xdata: " + nX.length + ", shake: " + nStepCount);

        double MET = 1.0;
        if (fAvgVelocity < 2.7) {
            MET = 2.3;
        } else if (fAvgVelocity < 4) {
            MET = 2.9;
        } else if (fAvgVelocity < 4.8) {
            MET = 3.3;
        } else if (fAvgVelocity < 5.5) {
            MET = 3.6;
        } else if (fAvgVelocity < 10) {
            MET = 3.8;
        } else if (fAvgVelocity < 16) {
            MET = 4.0;
        }

        // 70kg�� ����� 3.5 mph(1.5m/s)�� 30�� �ɾ��� ��: 139.65 kcal
//        ar.mCalorie = MET*mWeight*(1/3600.)*1000;
//        duCalorie += ar.mCalorie;
//        ar.mSumOfCalorie = duCalorie;

/*
        int nPrevX;
		int nPrevY;
		int nPrevZ;
		int nDiffX;
		int nDiffY;
		int nDiffZ;
		int nPrevDiffX;
		int nPrevDiffY;
		int nPrevDiffZ;

		int nDirection1X = 0;
		int nDirection1Y = 0;
		int nDirection1Z = 0;

		int nDirection2X = 0;
		int nDirection2Y = 0;
		int nDirection2Z = 0;

		int idx = co1.mAccelData.length/3;

		nPrevX = co1.mAccelData[idx];
		nPrevY = co1.mAccelData[idx+1];
		nPrevZ = co1.mAccelData[idx+2];

		nPrevDiffX = nPrevX - co1.mAccelData[idx-3];
		nPrevDiffY = nPrevY - co1.mAccelData[idx-2];
		nPrevDiffZ = nPrevZ - co1.mAccelData[idx-1];


		int nThreshold = 100;
		if(Math.abs(nPrevDiffX) > nThreshold)
			nDirection2X = 1;
		else if(Math.abs(nPrevDiffX) < -nThreshold)
			nDirection2X = -1;

		if(Math.abs(nPrevDiffY) > nThreshold)
			nDirection2Y = 1;
		else if(Math.abs(nPrevDiffY) < -nThreshold)
			nDirection2Y = -1;

		if(Math.abs(nPrevDiffZ) > nThreshold)
			nDirection2Z = 1;
		else if(Math.abs(nPrevDiffZ) < -nThreshold)
			nDirection2Z = -1;


		for(int j=1; j<objectArray.size(); j++) {
			ContentObject co = objectArray.get(j);
			if(j == 0)
				ar.mStartTime = co.mTimeInMilli;

			/**
			 * Make your own analyzing code here.
			 */
            /*
            if(co.mAccelData != null) {
				int last_x = 0;
				int last_y = 0;
				int last_z = 0;

				// [kbjung]
				nDiffX = co.mAccelData[0] - nPrevX;
				nDiffY = co.mAccelData[1] - nPrevY;
				nDiffZ = co.mAccelData[2] - nPrevZ;

				if(nDiffX > nThreshold)
					nDirection1X = 1;
				else if(nDiffX < -nThreshold)
					nDirection1X = -1;

				if(nDiffY > nThreshold)
					nDirection1Y = 1;
				else if(nDiffY < -nThreshold)
					nDirection1Y = -1;

				if(nDiffZ > nThreshold)
					nDirection1Z = 1;
				else if(nDiffZ < -nThreshold)
					nDirection1Z = -1;

				if(nDirection1Y != 0 && nDirection2Y != 0 && nDirection1Y != nDirection2Y)
					nStepCount++;
//					ar.mShakeActionCount++;

				nPrevDiffX = nDiffX;
				nPrevDiffY = nDiffY;
				nPrevDiffZ = nDiffZ;

				for(int i=3; i<co.mAccelData.length/3; i+=3) {
					int axis_x = co.mAccelData[i];
					int axis_y = co.mAccelData[i+1];
					int axis_z = co.mAccelData[i+2];

					// [kbjung]
					nDiffX = axis_x - nPrevX;
					nDiffY = axis_y - nPrevY;
					nDiffZ = axis_z - nPrevZ;

					if(nDiffX > nThreshold)
						nDirection1X = 1;
					else if(nDiffX < -nThreshold)
						nDirection1X = -1;

					if(nDiffY > nThreshold)
						nDirection1Y = 1;
					else if(nDiffY < -nThreshold)
						nDirection1Y = -1;

					if(nDiffZ > nThreshold)
						nDirection1Z = 1;
					else if(nDiffZ < -nThreshold)
						nDirection1Z = -1;

					if(nDirection1Y != 0 && nDirection2Y != 0 && nDirection1Y != nDirection2Y)
						nStepCount++;
					ar.mShakeActionCount = nStepCount;

					nPrevDiffX = nDiffX;
					nPrevDiffY = nDiffY;
					nPrevDiffZ = nDiffZ;

//					double difference = 0;
//
//					if(last_x == 0 && last_y == 0 && last_z == 0) {
//
//					} else {
//						difference = Math.abs(axis_x + axis_y + axis_z - last_x - last_y - last_z) / samplingInterval * 10000;
//						ar.mSumOfDifference += difference;
//						ar.mCount++;
//
//						if(difference > SHAKE_THRESHHOLD) {
//							// This is shake action
//							ar.mShakeActionCount++;
//						}
//					}
//					last_x = axis_x;
//					last_y = axis_y;
//					last_z = axis_z;

					/*
					if(axis_x == 0 && axis_y == 0 && axis_z == 0) {
						previousMagnitude = 0;
					} else {
						difference = Math.sqrt(Math.pow(axis_x, 2) + Math.pow(axis_y, 2) + Math.pow(axis_z, 2));
						if(previousMagnitude > 0) {
							ar.mSumOfDifference += Math.abs(previousMagnitude - difference);
							ar.mCount++;
						}
						previousMagnitude = difference;
					}
					*/
                    /*
				}	// End of for loop
			}

		}	// End of for loop
//
//		if(ar.mCount > 0)
//			ar.mAverageDifference = ar.mSumOfDifference / ar.mCount;
//		else
//			ar.mAverageDifference = 0;
		*/

        //ar.mCalorie = Analyzer.calculateCalorie(ar.mShakeActionCount);	// Calculate calorie!!

        return nStepCount;
    }


    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    //    private void checkAccelerometerDataV2(AccelerometerModel model){
//        String steps =checkForSteps(model);
//    }
    private float mYOffset;
    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
    private float mLastDiff[] = new float[3 * 2];
    private float mLimit = 0.02f;
    private int mLastMatch = -1;


    private StepModule checkForSteps(double v, long timestamp) {

        //public void onSensorChanged(int sensor, float[] values) {
        stepModule = new StepModule();
        stepListener = 0;
        //Log.d(TAG, String.valueOf(v));
        synchronized (this) {

            int j = 1;
//                    double[] event={model.getX(),model.getY(),model.getZ()};
            //                        float vSum = 0;
//                        for (int i=0 ; i<3 ; i++) {
//                            final float v = (float) (mYOffset + event[i] * mScale[j]);
//                            vSum += v;
//                        }
            int k = 0;
//                        float v = vSum / 3;

            float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
            if (direction == -mLastDirections[k]) {
                // Direction changed
                int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                mLastExtremes[extType][k] = mLastValues[k];
                float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                if (diff > mLimit) {

                    boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                    boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                    boolean isNotContra = (mLastMatch != 1 - extType);

                    if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                        //Log.i(TAG, "step");
                        stepListener++;
                        mLastMatch = extType;
                    } else {
                        mLastMatch = -1;
                    }
                }
                mLastDiff[k] = diff;
            }
            mLastDirections[k] = direction;
            mLastValues[k] = (float) v;

        }
        stepModule.step = stepListener;
        stepModule.timestamp = timestamp;
        return stepModule;
    }


    public void onSleepdataAvail(DataModelPressure model, long alarmStartTime, long alarmEndsTime) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            Calendar calendar;

            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                sb = new StringBuilder();
                info = sb.append("Timestamp: ").append(model.getTimestamp()).append(" X: ").append(model.getX()).append(" Y: ").append(model.getY()).append(" Z: ").append(model.getZ()).toString();

                calendar = Calendar.getInstance();
                if (!calibarated) {
                    if (!isSleepThresholdCalculated) {
                        if (countSleep < 1200) {
                            rolling.add(calculateVector(model));
                            countSleep++;
                        } else {
                            sleep_threshold = rolling.getaverage();
                            isSleepThresholdCalculated = true;
                            calibarated = true;
                            countSleep = 0;
                            createFileSleep(info);
                        }
                        e.onComplete();
                        return;
                    }
                }
                createFileSleep(info);
                //Log.d(TAG, "sleep started");
//                model.setX(model.getX() >= 0 ? model.getX() : 65535 + model.getX());
//                model.setY(model.getY() >= 0 ? model.getY() : 65535 + model.getY());
//                model.setZ(model.getZ() >= 0 ? model.getZ() : 65535 + model.getZ());
                double vector = calculateVector(model);
                boolean interrupted = false;
                if (vector < 10) {
                    e.onComplete();
                    return;
                }
                if (vector + 10 < sleep_threshold || vector - 10 > sleep_threshold) {
                    sleepInterrupted(model.getTimestamp());
                    interrupted = true;
                }
                if (checkTime(model.getTimestamp()) > checkTime(alarmStartTime)
                        && checkTime(model.getTimestamp()) < checkTime(alarmEndsTime) && interrupted) {
                    startWakeupService();
                    isSleepThresholdCalculated = false;
                    calibarated = false;
                } else if (checkTime(model.getTimestamp()) > checkTime(alarmEndsTime)) {
                    startWakeupService();
                    isSleepThresholdCalculated = false;
                    calibarated = false;
                }

                if (countSleep < 500) {
                    rolling.add(calculateVector(model));
                    countSleep++;
                } else {
                    sleep_threshold = rolling.getaverage();
                    countSleep = 0;
                }

                e.onComplete();
            }

        }).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.e(TAG, e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void createFileSleep(String timeInMillis) {
        completeFileStructure = new File(Environment.getExternalStorageDirectory() + File.separator + "nowzone", Helper.getCurrentDate() + "_sleep.txt");
        try {
            FileWriter fWriter;
            fWriter = new FileWriter(completeFileStructure, true);
            if (completeFileStructure.exists()) {
                fWriter.append(String.valueOf(timeInMillis)).append("\n");
            } else {
                fWriter.write(String.valueOf(timeInMillis));
            }
            fWriter.flush();
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long checkTime(long time) {
        return time % (24 * 60 * 60 * 1000L);
    }

    private void startWakeupService() {
        listener.startWakeupService();
    }

    private void sleepInterrupted(long timestamp) {
        listener.onSleepInterrupted(timestamp);
    }

    private double calculateVector(DataModelPressure model) {
        vector = 0;
        if (newX > 0 && newY > 0 && newZ > 0) {
            tempX = Math.abs(newX - model.getX());
            tempY = Math.abs(newY - model.getY());
            tempZ = Math.abs(newZ - model.getZ());
            vector = sqrt((tempX * tempX) + (tempY * tempY) + (tempZ * tempZ));
        }
        return vector;
    }

    public void setSleepsEnd() {
        sleepEnded = true;
        startCalculationForSleep();
    }

    public void setSleepNotEnded() {
        sleepEnded = false;
    }

    private void startCalculationForSleep() {
        File completeFileStructure = new File(Environment.getExternalStorageDirectory() + File.separator + "nowzone", "sleep_cal.txt");
        if (completeFileStructure.exists()) {
            try {
                FileInputStream is = new FileInputStream(completeFileStructure);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                long lastTimestamp = 0;
                long diffMinutes;
                long diffLong;
                while (line != null) {
                    // //Log.d("StackOverflow", line);
                    line = reader.readLine();
                    if (line != null) {
                        long timestamp = Long.parseLong(line);
                        if (timestamp > 0) {
                            if (lastTimestamp <= 0) {
                                lastTimestamp = timestamp;
                                continue;
                            }
                            diffLong = timestamp - lastTimestamp;
                            diffMinutes = diffLong / (60 * 1000) % 60;
                            if (diffMinutes >= MIN_DEEP_SLEEP_DURATION) {
                                listener.onDeepsleepGot(timestamp, lastTimestamp, diffMinutes);
                            }
                            lastTimestamp = timestamp;
                        }
                    }
                }
                listener.onSleepEnded();
                if (completeFileStructure.delete()) {

                    //Log.d(TAG, "Deleted");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void startCalibrationg() {
//        Prefs.putFloat(CommonMethod.POSTURE_CALIBRATION, (float) Math.abs(something));
//        //Log.d(TAG,"POSTURERESULT "+something);
//        if(Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION,0)!=0){
//            listener.onPostureClibrated();
//        }else {
//            listener.onFailToCalibratePosture();
//        }
    }


    public class StepModule {
        int step;
        long timestamp;

        public StepModule() {
        }

        public StepModule(int step, long timestamp) {
            this.step = step;
            this.timestamp = timestamp;
        }
    }
}
