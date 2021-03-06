package itg8.com.nowzonedesigndemo.connection;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.WakeupAlarmActivity;
import itg8.com.nowzonedesigndemo.breath_history.BreathsHistoryActivity;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.common.ProfileModel;
import itg8.com.nowzonedesigndemo.db.DbHelper;
import itg8.com.nowzonedesigndemo.db.tbl.TblAverage;
import itg8.com.nowzonedesigndemo.db.tbl.TblBreathCounter;
import itg8.com.nowzonedesigndemo.db.tbl.TblPostureSedentary;
import itg8.com.nowzonedesigndemo.db.tbl.TblSleep;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.home.HomeActivity;
import itg8.com.nowzonedesigndemo.sleep.model.SleepActivityModel;
import itg8.com.nowzonedesigndemo.tosort.RDataManager;
import itg8.com.nowzonedesigndemo.tosort.RDataManagerListener;
import itg8.com.nowzonedesigndemo.utility.BleConnectionManager;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.DeviceState;
import itg8.com.nowzonedesigndemo.utility.FileLogUtility;
import itg8.com.nowzonedesigndemo.utility.Helper;
import itg8.com.nowzonedesigndemo.utility.OnSensorDataModelAvailForStoreInDB;
import itg8.com.nowzonedesigndemo.utility.OnStateAvailableListener;
import itg8.com.nowzonedesigndemo.utility.RDataManagerImp;
import itg8.com.nowzonedesigndemo.utility.StateCheckImp;
import itg8.com.nowzonedesigndemo.utility.UserLog;
import itg8.com.nowzonedesigndemo.utility.sleep_algo.SleepCalculationImp;

import static itg8.com.nowzonedesigndemo.common.BaseActivity.TAG_CLASS_BASE;
import static itg8.com.nowzonedesigndemo.common.CommonMethod.STEP_COUNT;
import static itg8.com.nowzonedesigndemo.common.CommonMethod.getArragedData;
import static itg8.com.nowzonedesigndemo.db.tbl.TblPostureSedentary.FIELD_DATE;
import static itg8.com.nowzonedesigndemo.home.HomeActivity.COLOR_CALM_M;
import static itg8.com.nowzonedesigndemo.home.HomeActivity.COLOR_FOCUSED_M;
import static itg8.com.nowzonedesigndemo.home.HomeActivity.COLOR_STRESS_M;
import static itg8.com.nowzonedesigndemo.home.StepMovingActivity.AXIS_Y;
import static itg8.com.nowzonedesigndemo.home.StepMovingActivity.AXIS_Z;


public class BleService extends OrmLiteBaseService<DbHelper> implements ConnectionStateListener, RDataManagerListener, OnStateAvailableListener {
    private static final String TAG = BleService.class.getSimpleName();
    private static final String TAGWithFull = BleService.class.getCanonicalName();
    public static final String ACTION_DEVICE_CONNECTED = TAGWithFull + ".ACTION_DEVICE_CONNECTED";
    public static final String ACTION_DEVICE_DISCONNECTED = TAGWithFull + ".ACTION_DEVICE_DISCONNECTED";
    public static final String ACTION_COUNT_RESULT = TAGWithFull + ".ACTION_COUNT_RESULT";
    public static final String ACTION_STEP_COUNT = TAGWithFull + ".ACTION_STEP_COUNT";
    public static final String ACTION_STATE_ARRIVED = TAGWithFull + ".ACTION_STATE_AVAIL";
    public static final String ACTION_DEVICE = TAGWithFull + ".ACTION_DEVICE";
    public static final String ACTION_SOCKET_INERRUPTED = TAGWithFull + ".SOCKET_INTERRUPT";
    public static final String ACTION_SERFVER_IP_CHANGED = TAGWithFull + "changedInIP";
    public static final String ACTION_DEVICE_NOT_ATTACHED_TO_BODY = TAGWithFull + "NotAttached";
    public static final String ACTION_RESET_ALL = TAGWithFull + "ActionDateChanged";
    public static final String ACTION_CALIBRATE_POSTURE = TAG_CLASS_BASE + "ActionCalibratePosture";
    private static final String ACTION_FORGROUND = "itg8.com.nowzonedesigndemo.forgroundService";
    private static final String CALM = "State: COMPOSED";
    private static final String FOCUSED = "State: ATTENTIVE";
    private static final String STRESS = "State: STRESS";
    private static final String SEDENTORY = "State: SEDENTARY";
    private static final String DISCONNECTED = "Device Disconnected...";
    private static final int WAKE_UP = 999;
    private static final double PI_MIN = -8.02d;
    private static final double PI_MAX = 8.02d;
    private static final double MIN_PRESSURE = 1100;
    private static final double MAX_PRESSURE = 8100;
    private static final int STRESS_NOTIFICATION_ID = 201;
    private static final int CALM_NOTIFICATION_ID = 202;
    private static final int FOCUSED_NOTIFICATION_ID = 203;
    private static final int DISCONNECTED_NOTIFICATION_ID = 204;
    private static final int SEDENTORY_NOTIFICATION_ID = 205;
    private static final int POSTURE_NOTIFICATION_ID = 206;
    private static final String POSTURE = "POSTURE";
    private static final int NOTIFICATION_ID = 1072;
    private static final String NOTIFICATION_CHANNEL_ID = "itg8.com.nowzonedesigndemo.connection.BleService";
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "BleNowzoneService";
    private final IBinder mBinder = new LocalBinder();
    private final StateCheckImp stateManager;
    TblBreathCounter counter;
    Dao<TblBreathCounter, Integer> userDao = null;
    boolean autoConnect;
    private Dao<TblAverage, Integer> avgDao = null;
    private Dao<TblState, Integer> stateDao = null;
    private Dao<TblStepCount, Integer> stepDao = null;
    private Dao<TblSleep, Integer> sleepDao = null;
    private Dao<DataModelPressure, Integer> dataModelPressureDao = null;
    private Dao<SleepActivityModel, Integer> sleepResultDao = null;
    private Dao<TblPostureSedentary, Integer> postureSedentaryDao;

    private BleConnectionManager manager;
    private Calendar calendar;
    private boolean disconnectByUser;
    private WifiManager.WifiLock lock;
    private BluetoothManager mBluetoothManager;
    private RDataManager dataManager;


    /**
     * This receiver allow all details to be handle using breadcast. like
     * operations on bluetooth from another receiver
     * alarm, new day data reset
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(getResources().getString(R.string.action_device_disconnect))) {
                if (manager != null) {

                    if (intent.hasExtra(CommonMethod.BLUETOOTH_OFF)) {
                        manager.disconnect();
                        return;
                    }
                    disconnectByUser = true;
                    if (intent.hasExtra(CommonMethod.ENABLE_TO_CONNECT))
                        if (manager.disconnect()) {
                            Prefs.putString(CommonMethod.STATE,
                                    DeviceState.DISCONNECTED.name());
                            Intent i = new Intent(context.getResources().getString(R.string.action_data_avail));
                            i.putExtra(CommonMethod.ACTION_GATT_DISCONNECTED, "DISCONNECT");
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                        }


                }
            } else if (action.equalsIgnoreCase(getResources().getString(R.string.action_device_sleep_start))) {
                if (dataManager != null) {
                    dataManager.onSleepStarted(true);
                    Calendar c=Calendar.getInstance();
                    c.setTimeInMillis(intent.getLongExtra(CommonMethod.START_ALARM_TIME, 0));
                    dataManager.onStartAlarmTime(c.getTimeInMillis());
                    TblSleep sleepForward = new TblSleep();
                    sleepForward.setDate(CommonMethod.getDateFromTMP(c.getTimeInMillis()));
                    sleepForward.setTimeStart(Prefs.getLong(CommonMethod.START_ALARM_TIME, 0));
                    sleepForward.setSleepState(CommonMethod.SLEEP_STARTED);
                    try {
                        Prefs.putInt(CommonMethod.LAST_SLEEP_ID,sleepDao.create(sleepForward));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals(getResources().getString(R.string.action_device_sleep_end))) {
                if (dataManager != null) {
                    Calendar c=Calendar.getInstance();
                    c.setTimeInMillis(intent.getLongExtra(CommonMethod.ALARM_END,0));
                    try {
                        QueryBuilder<TblSleep, Integer> builder = sleepDao.queryBuilder();
                        builder.limit(1L);
                        builder.orderBy(TblSleep.FIELD_ID, false);  // true for ascending, false for descending
                        TblSleep sleepForward = sleepDao.queryForFirst(builder.prepare());
                        if(sleepForward!=null){
                            sleepForward.setTimeEnd(c.getTimeInMillis());
                            sleepForward.setTimestamp(Calendar.getInstance().getTimeInMillis());
                            sleepForward.setSleepState(CommonMethod.SLEEP_ENDED);
                            sleepDao.update(sleepForward);
                            SleepCalculationImp imp=new SleepCalculationImp(dataModelPressureDao,sleepForward.getId(),sleepResultDao);
                            imp.calculateSleep(sleepForward.getTimeStart(),sleepForward.getTimeEnd(),null);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    dataManager.onSleepStarted(false);
                }
            } else if (action.equals(ACTION_SERFVER_IP_CHANGED)) {
                if (intent.hasExtra(CommonMethod.IP_ADDRESS)) {
                    if (dataManager != null) {
                        lock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock("MyWifiLock");
                        dataManager.startSendingData(intent.getStringExtra(CommonMethod.IP_ADDRESS));
                    }
                } else if (intent.hasExtra(CommonMethod.SOCKET_STOP_CLICKED)) {
                    if (dataManager != null) {
                        dataManager.onSocketStopped();
                        if (lock != null && lock.isHeld())
                            lock.release();
                    }
                }
            } else if (action.equals(ACTION_RESET_ALL)) {
                if (dataManager != null) {
                    dataManager.resetCountAndAverage();
                }
            } else if (action.equalsIgnoreCase(ACTION_CALIBRATE_POSTURE)) {
                if (dataManager != null) {
                    dataManager.startCalibration();
                }
            }
        }
    };
    private TblState tblState;
    private ProfileModel profileModel;
    private NotificationManager notificationManager;
    private boolean isNotificationShown;

    private double dLast;
    private float a = 0.96f;
    private BreathState lastState;
    private int minutes;
    private BreathState actualLastState;
    private FusedLocationProviderClient mFusedLocationClient;
    private int lastSeconds = 0;
    private boolean lastPosture;
    private long lastTimestamp;
    private long diff;
    private UserLog lastAttachState;
    private UserLog lastMovement;
    private long actualLastTimestamp;
    private int actualLastCount;

    public BleService() {
        manager = new BleConnectionManager(this);
        stateManager = new StateCheckImp(this, this);

        actualLastState = BreathState.UNKNOWN;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BLE Service started");
        dataManager = new RDataManagerImp(this, getApplicationContext());
        dataManager.setCalibrated(Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION, 0));
        String ip = Prefs.getString(CommonMethod.IP_ADDRESS, null);
        if (ip != null)
            dataManager.setServerIp(ip);
        dataManager.onSleepStarted(Prefs.contains(CommonMethod.SLEEP_STARTED));
        ;
        dataManager.onStartAlarmTime(Prefs.getLong(CommonMethod.START_ALARM_TIME, 0));
        dataManager.onEndAalrmTime(Prefs.getLong(CommonMethod.END_ALARM_TIME, 0));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getResources().getString(R.string.action_device_disconnect));
        intentFilter.addAction(getResources().getString(R.string.action_device_sleep_start));
        intentFilter.addAction(getResources().getString(R.string.action_device_sleep_end));
        intentFilter.addAction(ACTION_SERFVER_IP_CHANGED);
        intentFilter.addAction(ACTION_RESET_ALL);
        intentFilter.addAction(ACTION_CALIBRATE_POSTURE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //TODO start forground service from here with all parameter and condition validation,
        onStartForeGroundService();
        FileLogUtility.newInstance().addLogToFile(UserLog.SERVICE_START.name());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        calendar = Calendar.getInstance();
        profileModel = ((AppApplication) getApplication()).getProfileModel();
        AppApplication.getInstance().restoreStepsAndBreath();
        if (intent != null && intent.hasExtra(CommonMethod.ENABLE_TO_CONNECT)) {
            if (manager != null) {
                return START_STICKY;
            }
        }
        try {
            userDao = getHelper().getCountDao();
            avgDao = getHelper().getAvgDao();
            stateDao = getHelper().getStateDao();
            stepDao = getHelper().getStepDao();
            sleepDao = getHelper().getSleepDao();
            dataModelPressureDao = getHelper().getDataPresureDao();
            sleepResultDao=getHelper().getSleepResultDao();
            postureSedentaryDao = getHelper().getPostureSedentaryDao();

        } catch (SQLException e) {
            e.printStackTrace();
        }




        String address = null, name = null;
        if (intent != null && intent.hasExtra(CommonMethod.DEVICE_ADDRESS)) {
            address = intent.getStringExtra(CommonMethod.DEVICE_ADDRESS);
            name = intent.getStringExtra(CommonMethod.DEVICE_NAME);
        }
        if (initialize() && address != null && name != null) {
            selectedDevice(address, name);
        } else {
            reconnectBleDevice();
        }

        return START_STICKY;
    }

    private void onStartForeGroundService() {
//                    .setContentTitle("NowZone Running")
//                            .setTicker("")


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent notificationIntent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(" DESCRIPTION ");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            startForeground(NOTIFICATION_ID, notification);

        }


    }


    public void selectedDevice(String adress, String name) {
        sendToConnect(adress, name);
    }


    private void reconnectBleDevice() {
        if (manager == null)
            manager = new BleConnectionManager(this);
        checkAvailableDeviceToConnect();
    }

    private void checkAvailableDeviceToConnect() {
        String deviceAddress = Prefs.getString(CommonMethod.DEVICE_ADDRESS, null);
        if (deviceAddress != null) {
            sendToConnect(deviceAddress, "name");
        }
    }

    private void sendToConnect(String address, String name) {
        if (manager != null)
            manager.selectedDevice(address, name);
    }


    @Override
    public void onDeviceConnected(String address) {
        Prefs.putString(CommonMethod.DEVICE_ADDRESS, address);
        Prefs.putBoolean(CommonMethod.CONNECTED, true);
        sendBroadcast(ACTION_DEVICE_CONNECTED);
    }

    @Override
    public void onDeviceDisConnected() {
        sendBroadcast(ACTION_DEVICE_DISCONNECTED);
    }

    private void sendBroadcast(String action) {
        Log.d(TAG, "SendBroadcast");
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /**
     * this method use to send broadcast for each key feature with action defined intent
     *
     * @param key  KEY FOR INTENT DATA
     * @param data ORIGINAL DATA
     */
    private void sendBroadcast(String key, Object data) {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        if (data instanceof Double) {
            intent.putExtra(key, (Double) data);
            intent.putExtra(CommonMethod.ACTION_DATA_LONG, System.currentTimeMillis());
        } else if (data instanceof Integer) {
            intent.putExtra(key, (int) data);
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public void onDeviceDiscovered() {

    }

    @Override
    public void onDeviceConnectionChanged() {

    }


    @Override
    public void onAccelerometer(DataModelPressure model) {

        /**
         * Add now
         */
        Intent intent = new Intent(getString(R.string.action_data_avail));
        intent.putExtra(CommonMethod.MODEL, model);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }


    @Override
    public void onDataAvail(byte[] data, byte[] acc) {

        //        SharePrefrancClass.getInstance(this).savePref(CommonMethod.TIMESTAMP, Calendar.getInstance().getTimeInMillis());

//        Observable.create(new ObservableOnSubscribe<DataModelPressure>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<DataModelPressure> e) throws Exception {
//                e.onNext(getArragedData(data));
//            }
//        }).subscribeOn(Schedulers.io()).subscribe(new Observer<DataModelPressure>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(DataModelPressure dataModel) {
//                    dataManager.onRawDataModel(dataModel, getApplicationContext());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
        dataManager.onRawDataModel(getArragedData(data, acc), getApplicationContext());

//        Observable.just(getArragedData(data)).subscribeOn(Schedulers.io()).subscribe(new Observer<DataModelPressure>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(DataModelPressure dataModel) {
////                dataManager.onRawDataModel(dataModel, getApplicationContext());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
////            @Override
////            public void accept(@NonNull DataModelPressure dataModel) throws Exception {
////            }
//        });

//        Log.d(TAG, "value : " + getArragedData(data).getPressure() + " , " + " timestamp: " + CommonMethod.getTimeFromTMP(getArragedData(data).getTimestamp()));

    }


    @Override
    public void onSocketInterrupted() {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        intent.putExtra(ACTION_SOCKET_INERRUPTED, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        if (lock != null && lock.isHeld())
            lock.release();
    }


    @Override
    public void onMovement(float mAccel) {
        Intent intent = new Intent(getString(R.string.action_data_avail));
        intent.putExtra(CommonMethod.ACTION_MOVEMENT, mAccel);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (lastMovement != UserLog.MOVEMENT) {
            FileLogUtility.newInstance().addLogToFile(UserLog.MOVEMENT.name());
            lastMovement = UserLog.MOVEMENT;
        }
    }

    @Override
    public void onNoMovement(float i) {
        Intent intent = new Intent(getString(R.string.action_data_avail));
        intent.putExtra(CommonMethod.ACTION_MOVEMENT_STOPPED, i);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (lastMovement != UserLog.NO_MOVEMENT) {
            FileLogUtility.newInstance().addLogToFile(UserLog.NO_MOVEMENT.name());
            lastMovement = UserLog.NO_MOVEMENT;
        }
    }

    @Override
    public void currentState(DeviceState state) {
        Intent intent1 = new Intent(getString(R.string.action_data_avail));
        intent1.putExtra(CommonMethod.DEVICE_STATE, state);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
        if (state == DeviceState.DISCONNECTED) {
            dataManager.onDisconnected();
            if (disconnectByUser) {
                disconnectByUser = false;
                manager.isDisconnectedByUser(true);

            } else {
                manager.isDisconnectedByUser(false);
            }
            Intent intent = new Intent(this, HomeActivity.class);
            if (!isNotificationShown) {
                createNotification(DISCONNECTED, COLOR_STRESS_M, getResources().getString(R.string.msg_device_disconnect), intent);
                isNotificationShown = true;
            }
        } else if (state == DeviceState.CONNECTED) {
            cancelDisconnectNotification();
            dataManager.onConnected();
            isNotificationShown = false;
        }
        Log.d(TAG, "state is: " + state.name());
        Prefs.putString(CommonMethod.STATE, state.name());
        CommonMethod.resetTmpstmp();
    }

    private void cancelDisconnectNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(getIdByState(DISCONNECTED));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        manager.setBluetoothAdapter(mBluetoothAdapter);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppApplication.getInstance().storeStepsAndBreath();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        manager.disconnect();
        FileLogUtility.newInstance().addLogToFile(UserLog.SERVICE_STOP.name());
        if (android.os.Build.VERSION_CODES.O < android.os.Build.VERSION.SDK_INT)
            startService(new Intent(this, BleService.class));
        Log.d(TAG, "On destroy  called");
    }

    /**
     * This helps to connect with ble device
     *
     * @param device
     * @param callback
     */
    @Override
    public void connectGatt(BluetoothDevice device, BluetoothGattCallback callback) {
        Prefs.putString(CommonMethod.STATE, DeviceState.DISCONNECTED.name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setBluetoothGatt(device.connectGatt(getApplicationContext(), false, callback, BluetoothDevice.TRANSPORT_LE));
        } else {
            manager.setBluetoothGatt(device.connectGatt(getApplicationContext(), false, callback));
        }
        autoConnect = true;
        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//
//                if (device != null) {
//
////                    manager.setBluetoothGatt( (new BleConnectionCompat(getApplicationContext()).connectGatt(device, true, callback)));
//
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                        manager.setBluetoothGatt(device.connectGatt(getApplicationContext(), true, callback,BluetoothDevice.TRANSPORT_LE));
////                    }else {
////                        manager.setBluetoothGatt(device.connectGatt(getApplicationContext(), false, callback));
////                    }
////                    mGatt = device.connectGatt(getApplicationContext(), true, mGattCallback);
////                    scanLeDevice(false);// will stop after first device detection
//                }
//            }
//        });


    }

    @Override
    public void onFail(DeviceState state, int status) {
        Log.d(TAG, "Failed:" + state.name() + " status code: " + status);
    }

    @Override
    public void onDataProcessed(double dataModel) {
        Log.d(TAG, "Pressure Value after changes " + dataModel);
        sendBroadcast(CommonMethod.ACTION_DATA_AVAILABLE, dataModel);
    }

    /**
     * Device count is less than or equals to 3 then send broadcast about device is not connected
     */
    @Override
    public void onDeviceNotAttached() {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        intent.putExtra(ACTION_DEVICE_NOT_ATTACHED_TO_BODY, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        if (lastAttachState != UserLog.DEVICE_DETACHED) {
            FileLogUtility.newInstance().addLogToFile(UserLog.DEVICE_DETACHED.name());
            lastAttachState = UserLog.DEVICE_DETACHED;
        }
    }


    @Override
    public void onAxisDataAvail(double y, double z) {
        Intent intent = new Intent(CommonMethod.ACTION_AXIS_ACCEL);
        intent.putExtra(AXIS_Y, (float) y);
        intent.putExtra(AXIS_Z, (float) z);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    /**
     * if device attached to body
     */
    @Override
    public void onDeviceAttached() {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        intent.putExtra(ACTION_DEVICE_NOT_ATTACHED_TO_BODY, false);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        if (lastAttachState != UserLog.DEVICE_ATTACHED) {
            FileLogUtility.newInstance().addLogToFile(UserLog.DEVICE_ATTACHED.name());
            lastAttachState = UserLog.DEVICE_ATTACHED;
        }
    }

    /**
     * @param count     breath count
     * @param timestamp last timestamp from list
     * @param allDiff
     */
    @Override
    public void onCountAvailable(int count, long timestamp, SparseArray<List<Integer>> allDiff) {
        sendCountBroadcast(count, timestamp);
        stateManager.calculateNewBreathAvgForState(timestamp, userDao);
        checkStateOfMind(count, timestamp, allDiff);
    }

    @Override
    public void onCountAvailable(int count, long timestamp) {
        sendCountBroadcast(count);
    }

    @Override
    public void onResetInititalBreathingState() {
        actualLastState = BreathState.UNKNOWN;
    }

    private void checkStateOfMind(int count, long timestamp, SparseArray<List<Integer>> allDiff) {
        BreathState currentState = BreathState.UNKNOWN;
        int avgCount = Prefs.getInt(CommonMethod.USER_CURRENT_AVG);
        if (count <= 0)
            return;
        if (avgCount <= 0)
            return;


//        List<TblBreathCounter> breathCounters = null;
//        try {
//            QueryBuilder<TblBreathCounter, Integer> builder = userDao.queryBuilder().limit(2L).orderBy(TblBreathCounter.FIELD_NAME_ID, false);
//            breathCounters = userDao.query(builder.prepare());
//            Log.d(TAG, "breathcounters table data:" + new Gson().toJson(breathCounters));
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        int mLastOneCount = 0;
//        int mSecondLastCount = 0;
//        if (breathCounters != null)
//            if (breathCounters.size() >= 2) {
//                mLastOneCount = breathCounters.get(0).getCount();
//                mSecondLastCount = breathCounters.get(1).getCount();
//            }
//
//
//        if (mLastOneCount <= 0 && mSecondLastCount <= 0) {
//            return;
//        }
//        int percent = percent(avgCount, 10);
//        int newCalmCheck = avgCount - percent;
//        int newStressCheck = avgCount + percent;
//
//        if (mLastOneCount <= newCalmCheck && mSecondLastCount <= newCalmCheck && count == newCalmCheck) {
//            sendBroadcastState(BreathState.CALM, count, timestamp);
//            currentState = BreathState.CALM;
//        } else if (mLastOneCount >= newStressCheck && mSecondLastCount >= newStressCheck && count >= newStressCheck) {
//            sendBroadcastState(BreathState.STRESS, count, timestamp);
//            currentState = BreathState.STRESS;
//        } else if (count > newCalmCheck
//                && count < newStressCheck &&
//                mLastOneCount > newCalmCheck &&
//                mLastOneCount < newStressCheck &&
//                mSecondLastCount > newCalmCheck &&
//                mSecondLastCount < newStressCheck
//                ) {
//            sendBroadcastState(BreathState.FOCUSED, count, timestamp);
//            currentState = BreathState.FOCUSED;
//        } else {
//            currentState = BreathState.UNKNOWN;
//        }

        /**
         * New Method to check peak and trough difference(<=10).
         * Conditions:
         * 1) If difference is less than 50 percent of breathing count then non irratic.
         * 2) If difference is greater than 50 percent of breathing count then irratic breathing
         * 3) If count less than 10% of average then calm.
         */

        int m50Percent = count / 2;
        int diffPeakCount = 0;
        int difftroughCount = 0;
        int percent = percent(avgCount, 10);
        int newCalmCheck = avgCount - percent;
        int newStressCheck = avgCount + percent;
        List<Integer> peaks = allDiff.get(0);
        List<Integer> trough = allDiff.get(1);

        Log.d(TAG, "AllVALUEPeaksValue: " + new Gson().toJson(peaks));
        Log.d(TAG, "AllVALUETroughValue: " + new Gson().toJson(trough));

        for (int i = 1; i < peaks.size(); i++) {
            if (Math.abs(peaks.get(i) - peaks.get(i - 1)) <= 10) {
//            if ((peaks.get(i) - peaks.get(i - 1)) <= 10) {
                diffPeakCount++;
            }
        }

        for (int i = 1; i < trough.size(); i++) {
            if (Math.abs(trough.get(i) - trough.get(i - 1)) <= 10) {
//            if ((trough.get(i) - trough.get(i - 1)) <= 10) {
                difftroughCount++;
            }
        }
//        for (int i :
//                peaks) {
//            if (i <= 10) {
//            }
//        }

//        for (int i : trough) {
//            if (i <= 10)
//                difftroughCount++;
//        }

        Log.d(TAG, "AllVALUE DifferencePeakCount: " + diffPeakCount + "     DifferenceTroughCount: " + difftroughCount + " 50Percent: " + m50Percent);
        if (diffPeakCount >= m50Percent && difftroughCount >= m50Percent) {
            if (count < newCalmCheck) {
                if (actualLastState == BreathState.CALM) {
                    dataManager.arrangeBreathingForServer(getString(R.string.url_breathing), actualLastCount, actualLastTimestamp, actualLastState, count, timestamp, BreathState.CALM);
                    sendBroadcastState(BreathState.CALM, count, timestamp);
                }
                currentState = BreathState.CALM;
            } else if (count >= newCalmCheck && count <= newStressCheck) {
                if (actualLastState == BreathState.FOCUSED) {
                    dataManager.arrangeBreathingForServer(getString(R.string.url_breathing), actualLastCount, actualLastTimestamp, actualLastState, count, timestamp, BreathState.FOCUSED);
                    sendBroadcastState(BreathState.FOCUSED, count, timestamp);
                }
                currentState = BreathState.FOCUSED;
            }
        } else {
            if (count > newStressCheck) {
                if (actualLastState == BreathState.STRESS) {
                    dataManager.arrangeBreathingForServer(getString(R.string.url_breathing), actualLastCount, actualLastTimestamp, actualLastState, count, timestamp, BreathState.STRESS);
                    sendBroadcastState(BreathState.STRESS, count, timestamp);
                }
                currentState = BreathState.STRESS;
            }
        }

        if (currentState == BreathState.UNKNOWN) {
            actualLastCount = 0;
            dataManager.arrangeBreathingForServer(getString(R.string.url_breathing), actualLastCount, actualLastTimestamp, actualLastState, count, timestamp, BreathState.UNKNOWN);
            sendBroadcastState(BreathState.UNKNOWN, count, timestamp);
        }

        actualLastState = currentState;
        actualLastTimestamp = timestamp;
        actualLastCount = count;
//        dataManager.arrangeBreathingForServer(getString(R.string.url_breathing), count, timestamp, currentState);
        dataManager.sendRemainingStepsToServer(getString(R.string.url_steps));
    }

    private int percent(int avgCount, int percent) {
        return Math.round(((percent / 100.0f) * avgCount));
    }

    private void sendBroadcastState(BreathState state, int count, long timestamp) {
        Intent intent = new Intent(getResources().getString(R.string.action_data_avail));
        intent.putExtra(ACTION_STATE_ARRIVED, state);
        int minutes = buildNotificationForState(state);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            saveStateToDb(state, count, timestamp, 0, 0);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        double latitude = 0;
                        double longitude = 0;
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                        saveStateToDb(state, count, timestamp, latitude, longitude);

                    }
                });
    }

    private int buildNotificationForState(BreathState state) {
        if (lastState != null && lastState == state) {
            minutes += 1;
        } else {
            minutes = 2;
        }
        Intent intent = new Intent(this, BreathsHistoryActivity.class);
        if (state == BreathState.CALM) {
            createNotification(CALM, COLOR_CALM_M, "You have composed for last " + minutes + " minutes", intent);
        } else if (state == BreathState.FOCUSED) {
            createNotification(FOCUSED, COLOR_FOCUSED_M, "You have attentive for last " + minutes + " minutes", intent);
        } else if (state == BreathState.STRESS) {
            createNotification(STRESS, COLOR_STRESS_M, "You have stress for last " + minutes + " minutes", intent);
        }
        lastState = state;
        return minutes;
    }


    private void createNotification(String state, String color, String message, Intent intent) {
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            noti = new Notification.Builder(this)
                    .setContentTitle(state)
                    .setContentText(message).setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setColor(Color.parseColor(color)).build();
        }
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        if (noti == null)
            return;
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(getIdByState(state), noti);

    }

    private int getIdByState(String state) {
        if (state.equalsIgnoreCase(CALM))
            return CALM_NOTIFICATION_ID;
        else if (state.equalsIgnoreCase(FOCUSED)) {
            return FOCUSED_NOTIFICATION_ID;
        } else if (state.equalsIgnoreCase(STRESS)) {
            return STRESS_NOTIFICATION_ID;
        } else if (state.equalsIgnoreCase(DISCONNECTED)) {
            return DISCONNECTED_NOTIFICATION_ID;
        } else if (state.equalsIgnoreCase(SEDENTORY)) {
            return SEDENTORY_NOTIFICATION_ID;
        } else if (state.equalsIgnoreCase(POSTURE)) {
            return POSTURE_NOTIFICATION_ID;
        }
        return 0;
    }

    private void saveStateToDb(BreathState state, int count, long timestamp, double latitude, double longitude) {
        /**
         * We will uncomment it when user needs data to be group as per state.
         * TODO uncomment for grouping state from db
         */
//        QueryBuilder<TblState, Integer> builder = stateDao.queryBuilder().limit(1L).orderBy(TblState.FIELD_ID, false);
//        List<TblState> tblStates = null;
//        try {
//            tblStates = stateDao.query(builder.prepare());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        TblState tblState;
//        if (tblStates!=null && tblStates.size()>0) {
//            tblState = tblStates.get(0);
//            if(!state.name().equalsIgnoreCase(tblState.getState())){
//                tblState.setTimestampEnd(timestamp);
//                try {
//                    stateDao.update(tblState);
//                    tblState=new TblState();
//                    tblState.setTimestampStart(timestamp);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }else
//            {
//
//            }
//        }
//        else {
//            tblState=new TblState();
//            tblState.setTimestampStart(timestamp);
//        }
        tblState = new TblState();
        String currentDate = Helper.getCurrentDate();
        tblState.setTimestampEnd(timestamp);
        tblState.setCount(count);
        tblState.setDate(currentDate);
        tblState.setState(state.name());
        tblState.setLatitude(latitude);
        tblState.setLongitude(longitude);
        try {
            stateDao.create(tblState);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStepCountReceived(int step) {
        storeStepToDb(step);
        Log.d(TAG, "OnStoreToDbSteps: " + step);
        Prefs.putInt(STEP_COUNT, step);
        sendStepBroadcast(ACTION_STEP_COUNT, step);
//        dataManager.arrangeStepsForServer(getString(R.string.url_steps), Prefs.getInt(STEP_COUNT));
    }

    private void storeStepToDb(int step) {
        if (profileModel == null) {
            profileModel = ((AppApplication) getApplication()).getProfileModel();
        }

        Observable.create((ObservableOnSubscribe<Void>) e -> {
            calendar.setTimeInMillis(System.currentTimeMillis());
            List<TblStepCount> countList = stepDao.queryBuilder().where().eq(TblStepCount.FIELD_DATE, calendar.getTime()).query();
            Log.i(TAG, "StepsTbl " + new Gson().toJson(countList));
            TblStepCount count;
            TblStepCount countLast = null;
            int previousSteps = 0;
            boolean fi = false;
            if (countList.size() > 0) {

                count = countList.get(countList.size() - 1);
                countLast = countList.get(countList.size() - 1);
                if (System.currentTimeMillis() - count.getEndTimestamp() > (CommonMethod.MINUTE * 2)) {
                    if (count.getStartTimestamp() == count.getEndTimestamp()) {
                        AppApplication.getInstance().removeLastStep();
                        stepDao.delete(count);
                    }
                    count = new TblStepCount();
                    count.setStartTimestamp(System.currentTimeMillis());
                    count.setEndTimestamp(System.currentTimeMillis());
                    dataManager.resetSteps();
                    fi = false;
                } else {
                    count.setEndTimestamp(System.currentTimeMillis());
                    fi = true;
                }

                previousSteps = count.getSteps();
            } else {
                count = new TblStepCount();
                count.setStartTimestamp(System.currentTimeMillis());
                count.setEndTimestamp(System.currentTimeMillis());
            }
            previousSteps = previousSteps + step;
            count.setDate(calendar.getTime());
            count.setSteps(previousSteps);
            count.setCalBurn(Helper.calculateCalBurnByStepCount(step, profileModel));
            count.setGoal(Prefs.getInt(CommonMethod.GOAL, 10000));
//            if (!fi) {
//                stepDao.create(count);
//            } else {
//                stepDao.update(count);
//            }
            dataManager.arrangeStepsForServer(getString(R.string.url_steps), previousSteps, count, fi);
            e.onComplete();

        }).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Void aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

//    private void storeStepToDb(int step) {
//        if (profileModel == null) {
//            profileModel = ((AppApplication) getApplication()).getProfileModel();
//        }
//
////        diff=System.currentTimeMillis()-lastTimestamp;
////        lastTimestamp=System.currentTimeMillis();
////        if(diff<315 || diff>5000){
////            Log.d(TAG,"lasttimestamp diff is less "+ diff);
////            return;
////        }
//        Observable.create((ObservableOnSubscribe<Void>) e -> {
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            List<TblStepCount> countList = stepDao.queryBuilder().where().eq(TblStepCount.FIELD_DATE, calendar.getTime()).query();
//            TblStepCount count;
//            boolean fi = false;
//            if (countList.size() > 0) {
//                count = countList.get(countList.size() - 1);
//                fi = true;
//            } else
//                count = new TblStepCount();
//            count.setDate(calendar.getTime());
//            count.setSteps(step);
//            count.setCalBurn(Helper.calculateCalBurnByStepCount(step, profileModel));
//            count.setGoal(Prefs.getInt(CommonMethod.GOAL));
//            if (!fi)
//                stepDao.create(count);
//            else
//                stepDao.update(count);
//            e.onComplete();
//
//        }).subscribeOn(Schedulers.computation())
//                .subscribe(new Observer<Void>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Void aLong) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, e.getMessage(), e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }


    private void sendStepBroadcast(String action, int step) {
        sendBroadcast(action, step);
    }

    private void sendCountBroadcast(int count, long timestamp) {
//        intent=new Intent(ACTION_COUNT_RESULT);
//        intent.putExtra(CommonMethod.BPM_COUNT,count);

//        if(count>) {
        sendBroadcast(CommonMethod.BPM_COUNT, count);
        storeToDb(count, timestamp);
//        }
    }

    private void sendCountBroadcast(int count) {
        sendBroadcast(CommonMethod.BPM_COUNT, count);
    }

    /**
     * after getting count from <a href="onCountAvailable">onCountAvailable</a> and store to db
     *
     * @param count     breathccount to store in databse
     * @param timestamp timestamp in long
     */
    private void storeToDb(int count, long timestamp) {
        // Retrieve the first source with data
        counter = new TblBreathCounter();
        counter.setCount(count);
        counter.setTimestamp(timestamp);
        counter.setDate(calendar.getTime());
        Observable.create((ObservableOnSubscribe<TblBreathCounter>) e -> {
            e.onNext(counter);
            e.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Observer<TblBreathCounter>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(TblBreathCounter tblBreathCounter) {
                                try {
                                    userDao.create(tblBreathCounter);
                                } catch (SQLException e) {
                                    e.printStackTrace();
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

    }


    @Override
    public void onStateAvailable(TblAverage model) {
        if (avgDao != null && model != null) {
            Log.d(TAG, "AvgDao in save");
            Prefs.putInt(CommonMethod.USER_CURRENT_AVG, model.getAverage());
            try {
                int c = avgDao.create(model);
                Log.d(TAG, "AvgDao saved: " + c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //TODO Notification: create notification for average here


    }


    @Override
    public void onSleepInterrupted(long timestamp) {

    }

    @Override
    public void onSleepEnded() {
        List<TblSleep> sleeps = null;
        try {
            sleeps = sleepDao.query(sleepDao.queryBuilder().orderBy(TblSleep.FIELD_ID, false).limit(1L).prepare());

            if (sleeps != null && sleeps.size() > 0) {
                TblSleep lastSleepStete = sleeps.get(0);
                if (lastSleepStete.getSleepState() == null || !lastSleepStete.getSleepState().equalsIgnoreCase(CommonMethod.SLEEP_STARTED))
                    lastSleepStete.setSleepState(CommonMethod.SLEEP_ENDED);
                lastSleepStete.setMinutes(Helper.calculateMinuteDiffFromTimestamp(lastSleepStete.getTimeStart(), Prefs.getLong(CommonMethod.SLEEP_ENDED)));
                lastSleepStete.setTimeEnd(Prefs.getLong(CommonMethod.SLEEP_ENDED));
                sleepDao.update(lastSleepStete);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNotifySittingFor30Min() {
        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            noti = new Notification.Builder(this)
                    .setContentTitle("Sedentary from long time...")
                    .setContentText("You were sitting at one position from last 30 min").setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setColor(Color.parseColor("#747474")).build();
        }
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        if (noti == null)
            return;
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(getIdByState(SEDENTORY), noti);

    }

    @Override
    public void onStartWakeupSevice() {
//       TODO uncomment after done sleep
//        Log.d(TAG,"notification alarm start active");
//        SharePrefrancClass.getInstance(getApplicationContext()).clearPref(CommonMethod.SLEEP_STARTED);
//        createPendingIntentForWakehimUp();


        Log.d(TAG, "notification alarm start active");
        Prefs.remove(CommonMethod.SLEEP_STARTED);
        Prefs.remove(CommonMethod.START_ALARM_TIME);
        createPendingIntentForWakehimUp();
//        Intent intent=new Intent(getApplicationContext(),.class);
//        startActivity(intent);
    }

    private void createPendingIntentForWakehimUp() {
        Intent intent = new Intent(getApplicationContext(), WakeupAlarmActivity.class);
        intent.putExtra("WAKEUP", Calendar.getInstance().getTimeInMillis());
        PendingIntent pi = PendingIntent.getActivity(this, WAKE_UP, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pi.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        if (dataManager != null) {
            dataManager.onSleepStarted(false);
        }
    }


    @Override
    public void onDeepsleepGot(long nextTmstmp, long lastTmstmp, long diffMinutes) {
        try {
            TblSleep sleep = new TblSleep();
            TblSleep sleepForward = new TblSleep();
            if (sleepDao == null) {
                sleepDao = getHelper().getSleepDao();
            }
            List<TblSleep> sleeps = sleepDao.query(sleepDao.queryBuilder().orderBy(TblSleep.FIELD_ID, false).limit(1L).prepare());
            if (sleeps.size() > 0) {
                /**
                 * First we check if the sleep data is available in table or what.
                 * If available then we know that last data always started with light sleep
                 * so first of all we will check whether lastSleepState sleepstate is not sleep started.(When we are setting an alarm
                 * , we put entry to the database about sleep state as SLEEP_STARTED. This means user click on start alarm.
                 * if true then we calculate minutes using lastTimestamp(Which is having diff of 10 min. and lastSleepState timestamp.
                 * Sleep started---movement---movement ---------------movement-----------movement---------------------movement.
                 *          2m     4m         15m          12m
                 * 12:01 AM         12:07AM          12:22AM       12:34AM
                 * SLEEP_STARTED
                 * We can clearly see that sleep starts then movement gap of 10 min is on 12:07 to 12:22
                 *                                                   lastTmsmp   nextTmsmp
                 */
                TblSleep lastSleepStete = sleeps.get(0);
                if (lastSleepStete.getSleepState() != null && !lastSleepStete.getSleepState().equalsIgnoreCase(CommonMethod.SLEEP_STARTED))
                    lastSleepStete.setSleepState(CommonMethod.LIGHT_SLEEP);
                lastSleepStete.setMinutes(Helper.calculateMinuteDiffFromTimestamp(lastSleepStete.getTimestamp(), lastTmstmp));
                lastSleepStete.setTimeEnd(lastTmstmp);
                sleepDao.update(lastSleepStete);

                sleep.setDate(Helper.getDateFromMillies(Prefs.getLong(CommonMethod.SAVEALARMTIME, 0)));
                sleep.setTimeStart(lastTmstmp);
                sleep.setTimeEnd(nextTmstmp);
                sleep.setMinutes(Helper.calculateMinuteDiffFromTimestamp(lastTmstmp, nextTmstmp));
                sleep.setTimestamp(nextTmstmp);
                sleep.setSleepState(CommonMethod.DEEP_SLEEP);
                sleepDao.create(sleep);

                sleepForward.setDate(Helper.getDateFromMillies(Prefs.getLong(CommonMethod.SAVEALARMTIME, 0)));
                sleepForward.setTimeStart(nextTmstmp);
                sleepForward.setTimestamp(nextTmstmp);
                sleepForward.setSleepState(CommonMethod.LIGHT_SLEEP);
                sleepDao.create(sleepForward);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostureResult(Boolean good) {
        Log.d(TAG, "POSTURERESULT: " + good);
        if (good) {
            try {
                List<TblPostureSedentary> allPostures = postureSedentaryDao.queryBuilder().where().eq(FIELD_DATE, calendar.getTime()).query();
                TblPostureSedentary tblPostureSedentary = null;
                boolean isNew = false;
                if (allPostures.size() <= 0) {
                    tblPostureSedentary = new TblPostureSedentary();
                    tblPostureSedentary.setDate(calendar.getTime());
                    tblPostureSedentary.setStartTimestamp(System.currentTimeMillis());
                    tblPostureSedentary.setEndTimestamp(System.currentTimeMillis());
                    isNew = true;
                } else {
                    tblPostureSedentary = allPostures.get(allPostures.size() - 1);
                    if (System.currentTimeMillis() - tblPostureSedentary.getEndTimestamp() < 120000)
                        tblPostureSedentary.setEndTimestamp(System.currentTimeMillis());
                    else {
                        tblPostureSedentary = new TblPostureSedentary();
                        tblPostureSedentary.setDate(calendar.getTime());
                        tblPostureSedentary.setStartTimestamp(System.currentTimeMillis());
                        tblPostureSedentary.setEndTimestamp(System.currentTimeMillis());
                        isNew = true;
                    }
                }

                if (isNew) {
                    postureSedentaryDao.create(tblPostureSedentary);
                } else {
                    postureSedentaryDao.update(tblPostureSedentary);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (notificationManager != null)
                notificationManager.cancel(getIdByState(POSTURE));
        } else {
            if (lastPosture != good)
                sendNotificationAboutPosture(true);
            else
                sendNotificationAboutPosture(false);

        }

        lastPosture = good;
    }

    private void sendNotificationAboutPosture(boolean isNew) {

//        manager.onStartVibrationLow();
        if (isNew)
            lastSeconds = 15;
        else
            lastSeconds += 15;
        Intent intent = new Intent(this, BreathsHistoryActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        manager.onStartVibrationLow();
        // Build notification
        // Actions are just fake
        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!isNew) {
                noti = new Notification.Builder(this)
                        .setContentTitle("Please seat straight")
                        .setContentText("Wrong posture from last " + lastSeconds + " Seconds").setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(pIntent)
                        .setOnlyAlertOnce(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setColor(Color.parseColor("#747474")).build();
            } else {
                noti = new Notification.Builder(this)
                        .setContentTitle("Please seat straight")
                        .setContentText("Wrong posture from last " + lastSeconds + " Seconds").setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(pIntent)
                        .setColor(Color.parseColor("#747474")).build();
            }
        }
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        if (noti == null)
            return;
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(getIdByState(POSTURE), noti);

    }


    @Override
    public void postureCalibrationFail() {
        Intent intent = new Intent(getString(R.string.title_activity_posture_calibration));
        intent.putExtra(CommonMethod.POSTURE_CALIBRATION, false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void postureCalibratedSuccess() {
        Intent intent = new Intent(getString(R.string.title_activity_posture_calibration));
        intent.putExtra(CommonMethod.POSTURE_CALIBRATION, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onRawData(double pressure) {
        Intent intent = new Intent(getString(R.string.action_data_avail));
        intent.putExtra(CommonMethod.RAW_DATA_PRESSURE, pressure);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }

    @Override
    public void onBatteryAvail(int battery) {
        Log.d(TAG, "onBattefryAvail: "+battery);
        Intent intent = new Intent(getString(R.string.action_data_avail));
        intent.putExtra(CommonMethod.BATTERY_VALUE, battery);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public boolean onSensorDataAvailToStore(DataModelPressure pressure) {
        try {
            return getHelper().getDataPresureDao().create(pressure) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("bekar jhala");
        }
//        return false;
    }

    public class LocalBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }
}