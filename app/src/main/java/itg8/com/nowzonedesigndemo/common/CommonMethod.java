package itg8.com.nowzonedesigndemo.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import itg8.com.nowzonedesigndemo.alarm.model.AlarmDaysModel;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.utility.BreathState;

import static java.lang.Long.parseLong;

/**
 * Created by Android itg 8 on 4/20/2017.
 */

public class CommonMethod {

    public static final String LAST_SLEEP_ID = "LAST_SLEEP_ID";
    private static final String TAG = "CommonMethod";

    static final String TEMPTOKEN = "HcENDQvJAk3UG2qIWyXKTS6UYbxg4SxBE98He6Cr29hAA4GaI7ZZ1sf_FPCqfRL3Yvjie8J6Q6370IQm0z628xmcI7Gm_HjdAFinQktoLpDSl_ANma3kA_KNUZT5WJJD-2AQB-wltgbgHVXlOBQRIPVpHZr8ejdRq7QNlDTIY0iwnz10a9Gjkqpu5l0SMWbspcWl1p3w39kZ_6heDMP_0y5rMZ-fI6hd-VrbSiDI_8bMl3JDm7sA2wn9JyMksGkCGrMfzMfqdnIjN_E-I0SFyydsn1_8FBeHXEy87LQnsBFayuytZUNZmjSg_w7N5Xxkn3cp_x_5j2bV0WFGkj23T1nEZHmqaY2Amj7W9OaXeD_0Le3_uCsgR3-L20Lm5WbpjSW9ZEMTOhCFcy3awwEDWrGZWjMw-Doy2WS7mzz-R0pQOxmWYd7wmV9k-I--QRi9liJ3Dd5J3mSrM7As4y0AC2BfmyPUp0EkYQuEKuhpCcI";

    public static final String USER_CURRENT_AVG = "USER_CURRENT_AVG";

    public static final long SEC = 1000;
    public static final long MINUTE = SEC * 60;

    public static final String SAVEDAYS = "SAVEDAYS";
    public static final String USER_ID = "USER_ID";
    public static final String MODEL = "MODEL";


    public static final long CONST_30_MIN = 1800000;

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATE_FORMAT_WITH_TIME = "hh:mm a";
    public static final String DATE_FORMAT_SERVER = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_SERVER2 = "yyyy,MM,dd,HH,mm";
    public static final String SAVEALARMTIME = "SAVEALARMTIME";

    public static final String AVG_MILE_BY_HEIGHT = "avgKmHeight";
    public static final String STEP_COUNT = "stepCount";
    public static final String ENABLE_TO_CONNECT = "enableToConnect";
    public static final String GOAL = "stepGoal";
    public static final String START_ALARM_TIME = "START_ALARM_TIME";
    public static final String END_ALARM_TIME = "END_ALARM_TIME";
    public static final String ALARM_FROMTIMEPICKER = "ALARM_FROMTIMEPICKER";
    public static final String ACTION_ALARM_NOTIFICATION = "ACTION_ALARM_NOTIFICATION";
    public static final String SLEEP_STARTED = "sleepStarted";
    public static final String STEP_GOAL = "stepGoal";
    public static final String ALARM_AP = "ALARM_AP";
    public static final String ENABLE_TO_CONNECT_IN_TIME = "ENABLE_TO_CONNECT_IN_TIME";

    public static final String BREATH = "BREATH";

    public static final String ACTION_DATA_LONG = "ACTION_AVAIL_LONG_TSMP";
    public static final String ALARM_END = "ALARM_END";
    public static final String FROMSMARTALARM = "FROMSMARTALARM";
    public static final String FROMMEDITATION = "FROMMEDITATION";
    public static final String SAVE_DAYS_FOR_MEDITATION = "SAVEDAYSFORMEDITATION";
    public static final String BLUETOOTH_OFF = "BLUETOOTH_OFF";
    public static final String FROM_DEVICE = "FROM_DEVICE";
    public static final String FROM_STEP_GOAL = "FROM_STEP_GOAL";
    public static final String FROM_PROFILE = "FROM_PROFILE";
    public static final String FROM_ALARM_SETTING = "FROM_ALARM_SETTING";
    public static final String FROM_MEDITATION = "FROM_MEDITATION";
    public static final String FROM_DEVICE_HISTORY = "FROM_DEVICE_HISTORY";
    public static final String FROM_ALARM_HOME = "FROM_ALARM_HOME";
    public static final String SAVETIMEINMILI = "SAVETIMEINMILI";
    public static final String IP_ADDRESS = "IpAddress";
    public static final String SOCKET_STOP_CLICKED = "stopClicked";
    public static final String DEVICE_STATE = "device state";
    public static final String TOKEN = "MYTOKEN";
    static final String BASE_URL = "http://nowzone.steponetechnologies.in/";
    //    static final String BASE_URL = "http://192.168.1.13/";
//    public static final String BASE_URL = "http://192.168.1.58:8090";
    public static final String ACTION_MOVEMENT = "ACTION_MOVEMENT";
    public static final String ACTION_MOVEMENT_STOPPED = "ACTION_MOVEMENT_STOPPED";
    public static final String ISLOGIN = "loginComplete";
    public static final String ACTION_AXIS_ACCEL = "ACTION_AXIS_YZ";
    public static final String STAGE_HEARED = "heardStage";

    public static final String DEEP_SLEEP = "1";
    public static final String LIGHT_SLEEP = "2";
    public static final String SLEEP_ENDED = "SLEEP_ENDED_NZ";
    public static final String COMPOSED_CLICK = "COMPOSED_CLICK";
    public static final String ATTENTIVE_CLICK = "ATTENTIVE_CLICK";
    public static final String STRESS_CLICK = "STRESS_CLICK";
    public static final String NORMAL_CLICK = "NORMAL_CLICK";
    public static final String COLOR = "COLOR";
    public static final String BREATH_LIST = "Breathinglist";
    public static final String RECEIVER = "locationAddressReceiver";
    public static final String LOCATION_DATA_EXTRA = "LocatioNAddressExtra";
    public static final int FAILURE_RESULT = 3232;
    public static final int SUCCESS_RESULT = 232;
    public static final String RESULT_DATA_KEY = "addressDataKey";
    public static final String STEPS = "StepsData";
    public static final String POSTURE_CALIBRATION = "calibratePosture";
    public static final String BATTERY_VALUE = "batteryVal";
    public static final String STORAGE_STEPS = "storageFromStepbackground";
    public static final String STORAGE_BREATHS = "storageFromBreathingBackground";
    public static final String POSTURE_PAUSE = "posturePaused";
    public static final String AVG_STEP_COUNT_TIME_DIFF = "avgSTimeDiff";
    public static final String COUNT_FOR_AVG = "avgCountS";


    private static Typeface typeface;
    public static String FROMWEEk = "from_week";
    public static final String SELECTED_DEVICE = "SELECTED_DEVICE";
    public static final String ACTION_GATT_CONNECTED = "DEVICE_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "DEVICE_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_DISCOVERED";
    public static final String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
    public final static UUID DATA_ENABLE = UUID.fromString("0000ffa3-0000-1000-8000-00805f9b34fb");
    public final static UUID DATA_ENABLE_A4 = UUID.fromString("0000ffa4-0000-1000-8000-00805f9b34fb");//{ DATA ENABLE A$-> Presure}
    public final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public final static UUID VIBRATOR_SERVICE_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    public final static UUID VIBRATOR_UUID = UUID.fromString("00002A06-0000-1000-8000-00805f9b34fb");
    public static final byte[] VIBRATOR_LOW = new byte[]{00};
    public static final byte[] VIBRATOR_MED = new byte[]{01};
    public static final byte[] VIBRATOR_HIGH = new byte[]{02};
    public static final int JOB_CONNECT_DEVICE = 1;

    public static final byte[] BYTE_ARRAY_ON = new byte[]{01};
    public static final byte[] BYTE_ARRAY_OFF = new byte[]{00};
    // DAQ Specific UUIDs
    public final static UUID TEMP_SERVICE_UUID = UUID.fromString("0000ffa0-0000-1000-8000-00805f9b34fb");
    // BLE UUIDs
    public final static UUID SENSOR_ON_OFF = UUID.fromString("0000ffa1-0000-1000-8000-00805f9b34fb");
    public static final String SHARED = "NZPREF";
    public static final String DEVICE_ADDRESS = "device_address";

    public static final String DEVICE_NAME = "device_name";
    public static final String CONNECTED = "connected";
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static final String TIMESTAMP = "time";
    public static final String STATE = "state";
    public static final String BPM_COUNT = "bpmCount";
    public static final String FINISHED_DATA = "finishedData";
    public static final String STORAGE_PERM = "storage_perm";
    public static final String STORAGE_PATH = "storage_path";
    public static final String ALARM_END_TIME = "AlarmEndTime";
    private static long currentMillies = 0;
    private static DataModelPressure model;

    public static final String RAW_DATA_PRESSURE = "RAW_DATA_PRESSURE";


    public static Typeface setFontOpenSansSemiBold(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
        return typeface;
    }

    public static Typeface setFontRobotoLight(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/robotolight.ttf");
        return typeface;
    }

    public CommonMethod() {
    }

    public static DataModelPressure getArragedData(byte[] data, byte[] acc) {
        if (data != null && data.length > 0) {
//            64 01D5 8BF516 8BF367 0705 01 000D 007A 02BF
//
//            64 - Batery status or Batery level
//            01D5 - Pressure
//            8BF516 - load cell 1
//            8BF367 - load cell 2
//            0705  - mic
//            01 - charging bit
//            000D - optical sensor
//            007A - Temperature
//            02BF - Unsued bytes
            byte valueBattery1 = data[0];

            byte value1 = data[1]; //Convert to double [Higher bit of pressure]
            byte value2 = data[2]; //Convert to double [Lower bit of pressure]
//            createFile(String.valueOf(value1)+" "+String.valueOf(value2));
            byte lC1Value1 = data[3];
            byte lC1Value2 = data[4];
            byte lC1Value3 = data[5];

            byte lC2Value1 = data[6];
            byte lC2Value2 = data[7];
            byte lC2Value3 = data[8];

            byte micValue1 = data[9];
            byte micValue2 = data[10];

            byte cBitValue = data[11];

            byte optCenValue1 = data[12];
            byte optCenValue2 = data[13];
            byte tempValue1 = data[14];
            byte tempValue2 = data[15];
            byte tempUnused1=data[16];
            byte tempUnused2=data[17];


            byte xAccHValue = acc[0];
            byte xAccLValue = acc[1];
            byte yAccHValue = acc[2];
            byte yAccLValue = acc[3];
            byte zAccHValue = acc[4];
            byte zAccLValue = acc[5];

            byte xGyrHValue = acc[6];
            byte xGyrLValue = acc[7];
            byte yGyrHValue = acc[8];
            byte yGyrLValue = acc[9];
            byte zGyrHValue = acc[10];
            byte zGyrLValue = acc[11];

            byte xMagHValue = acc[12];
            byte xMagLValue = acc[13];
            byte yMagHValue = acc[14];
            byte yMagLValue = acc[15];
            byte zMagHValue = acc[16];
            byte zMagLValue = acc[17];


//            String batteryBytes = new byte[]{valueBattery1,valueBattery2};
//            String pressure= bytesToHex(new byte[]{value1,value2});
//            String valueForX=bytesToHex(new byte[]{xHValue,xLValue});
//            String valueForY=bytesToHex(new byte[]{yHValue,yLValue});
//            String valueForZ=bytesToHex(new byte[]{zHValue,zLValue});
            model = new DataModelPressure();
            try {
                //for pressure
                model.setPressure(bytesToHexPressure(new byte[]{value1, value2}));
                //for accelerometer
                model.setX(bytesToHex(new byte[]{xAccHValue, xAccLValue}));
                model.setY(bytesToHex(new byte[]{yAccHValue, yAccLValue}));
                model.setZ(bytesToHex(new byte[]{zAccHValue, zAccLValue}));
                //for gyrometer
                model.setgX(bytesToHex(new byte[]{xGyrHValue, xGyrLValue}));
                model.setgY(bytesToHex(new byte[]{yGyrHValue, yGyrLValue}));
                model.setgZ(bytesToHex(new byte[]{zGyrHValue, zGyrLValue}));
                //for Magnetometer
                model.setmX(bytesToHex(new byte[]{xMagHValue, xMagLValue}));
                model.setmY(bytesToHex(new byte[]{yMagHValue, yMagLValue}));
                model.setmZ(bytesToHex(new byte[]{zMagHValue, zMagLValue}));

                //load cell 1
                model.setLoadCell1(bytesToHex(new byte[]{lC1Value1, lC1Value2, lC1Value3}));
                //load cell 2
                model.setLoadCell2(bytesToHex(new byte[]{lC2Value1, lC2Value2, lC2Value3}));
                // mic
                model.setMic(bytesToHex(new byte[]{micValue1, micValue2}));
                //charging bit
                model.setCharging(bytesToHex(new byte[]{cBitValue}));
                //optical sensor
                model.setOptical(bytesToHex(new byte[]{optCenValue1, optCenValue2}));
                //temprature
                model.setTemprature((bytesToHexSign(new byte[]{tempValue1, tempValue2}) / 16) + 24);
                model.setUnused(String.valueOf(bytesToHex(new byte[]{tempUnused1,tempUnused2})));
//                Log.d(TAG, "getArragedData: "+bytesToHex(new byte[]{tempValue1,tempValue2}));
                Log.d(TAG, "getArragedData: " + model.getTemprature());
//                model.setTemprature(getTempreture(model.getTemprature()));
                // battery
                model.setBattery(bytesToHex(new byte[]{valueBattery1}));
                model.setUserId(Prefs.getString(CommonMethod.USER_ID));
                model.setTimestamp(System.currentTimeMillis());
                model.setTimestampDate(new Date());
                model.setDatetime(CommonMethod.getDateTimeFromTMP(System.currentTimeMillis()));
                model.setDate(CommonMethod.getDateFromTMP(System.currentTimeMillis()));
                model.setTime(CommonMethod.getTimeFromTMP(System.currentTimeMillis()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    private static float getTempreture(float temprature) {
        return ((temprature / 16) + 24);
    }

    private static double bytesToHexPressure(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return convertHexToIntPressure(new String(hexChars));
    }

    private static void createFile(String log) {
        File completeFileStructure = new File(Environment.getExternalStorageDirectory() + File.separator + "nowzone", "raw data1.txt");
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


    public static int bytesToHex(byte[] bytes) throws NumberFormatException {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return convertHexToInt(String.valueOf(hexChars));
    }

    public static int bytesToHexSign(byte[] bytes) throws NumberFormatException {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return convertHexToIntSigned(String.valueOf(hexChars));
    }

    private static int convertHexToInt(String s) {
        try {
            return Integer.parseInt(s, 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int convertHexToIntSigned(String s) {
        try {
            return (short) Integer.parseInt(s, 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static int convertHexToIntPressure(String s) {
        try {
            return Integer.parseInt(s, 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Map<Integer, Double>> countBPM(DataModelPressure[] models, double delta) {
        List<Double> data = new ArrayList<>();
        for (DataModelPressure model :
                models) {
            data.add(model.getPressure());
        }
        return peak_detection(data, delta);
    }

    public static List<Map<Integer, Double>> countBPMBreath(DataModelPressure[] models, double delta) {
        List<Double> data = new ArrayList<>();
        for (DataModelPressure model :
                models) {
            data.add(model.getPressure());
        }
        return peak_detectionBreath(data, delta);
    }

    public static <U> List<Map<U, Double>> peak_detectionBreath(List<Double> values, Double delta, List<U> indices) {
//        assert (indices != null);
//        assert (values.size() != indices.size());

        LinkedHashMap<U, Double> maxima = new LinkedHashMap<U, Double>();
        LinkedHashMap<U, Double> minima = new LinkedHashMap<U, Double>();
        List<Map<U, Double>> peaks = new ArrayList<Map<U, Double>>();
        peaks.add(maxima);
        peaks.add(minima);

        Double maximum = null;
        Double minimum = null;
        U maximumPos = null;
        U minimumPos = null;

        boolean lookForMax = true;

        Integer pos = 0;
        for (Double value : values) {
            if (maximum == null || value > maximum) {
                maximum = value;
                maximumPos = indices.get(pos);
            }

            if (minimum == null || value < minimum) {
                minimum = value;
                minimumPos = indices.get(pos);
            }

            if (lookForMax) {
                if (value < maximum - delta) {
                    maxima.put(maximumPos, value);
                    minimum = value;
                    minimumPos = indices.get(pos);
                    lookForMax = false;
                }
            } else {
                if (value > minimum + delta) {
                    minima.put(minimumPos, value);
                    maximum = value;
                    maximumPos = indices.get(pos);
                    lookForMax = true;
                }
            }

            pos++;
        }

        return peaks;
    }

    public static <U> List<Map<U, Double>> peak_detection(List<Double> values, Double delta, List<U> indices) {
//        assert (indices != null);
//        assert (values.size() != indices.size());

        LinkedHashMap<U, Double> maxima = new LinkedHashMap<U, Double>();
        LinkedHashMap<U, Double> minima = new LinkedHashMap<U, Double>();
        List<Map<U, Double>> peaks = new ArrayList<Map<U, Double>>();
        peaks.add(maxima);
        peaks.add(minima);

        Double maximum = null;
        Double minimum = null;
        U maximumPos = null;
        U minimumPos = null;

        boolean lookForMax = true;

        Integer pos = 0;
        for (Double value : values) {
            if (maximum == null || value > maximum) {
                maximum = value;
                maximumPos = indices.get(pos);
            }

            if (minimum == null || value < minimum) {
                minimum = value;
                minimumPos = indices.get(pos);
            }

            if (lookForMax) {
                if (value < maximum - delta) {
                    maxima.put(maximumPos, value);
                    minimum = value;
                    minimumPos = indices.get(pos);
                    lookForMax = false;
                }
            } else {
                if (value > minimum + delta) {
                    minima.put(minimumPos, value);
                    maximum = value;
                    maximumPos = indices.get(pos);
                    lookForMax = true;
                }
            }

            pos++;
        }

        return peaks;
    }

    public static List<Map<Integer, Double>> peak_detection(List<Double> values, Double delta) {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < values.size(); i++) {
            indices.add(i);
        }

        return peak_detection(values, delta, indices);
    }

    public static List<Map<Integer, Double>> peak_detectionBreath(List<Double> values, Double delta) {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < values.size(); i++) {
            indices.add(i);
        }

        return peak_detectionBreath(values, delta, indices);
    }

    public static String getTimeFromTMP(long timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String ss = "";
        try {
            ss = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ss;
    }

    public static String getTimeFromTMPAmPm(long timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String ss = "";
        try {
            ss = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ss;
    }

    public static String getDateTimeFromTMP(long timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String ss = "";
        try {
            ss = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ss;
    }

    public static String getDateTimeForFileFromTMP(long timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  hh-mm-ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String ss = "";
        try {
            ss = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ss;
    }

    public static String getDateFromTMP(long timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        String ss = "";
        try {
            ss = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ss;
    }

    public static Calendar ConvertTime(Context mContext, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);

        return c;
    }

    public static void resetTmpstmp() {
        currentMillies = 0;
    }

    public static RetroController getController() {
        return AppApplication.getInstance().initNetworkCall();
    }

    public static LineDataSet getDataSetGraph(String s, String colorCode) {

        LineDataSet set = new LineDataSet(null, s);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor(colorCode));
//        set.setCircleColor(Color.BLUE);
        set.setLineWidth(2f);
//        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.parseColor(colorCode));
//        set.setHighLightColor(Color.rgb(244, 117, 117));
//        set.setValueTextColor(Color.BLUE);
        set.setValueTextSize(9f);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        return set;
    }

    public static final long ONE_MINUTE = 60000;

    public static String checkBreathing(long latestTimestamp, long lastTimeStamp) {
        long timeTaken = latestTimestamp - lastTimeStamp;
        return String.valueOf((int) ((ONE_MINUTE) / timeTaken));

    }

    public static double getVector(float x, float y, float z) {
        return Math.sqrt((x*x)+(y*y)+(z*z));
    }


    public interface alarmListener {
        void onAlarmListener(List<AlarmDaysModel> abc, String from);
    }

    public static CharSequence calculateHours(long startTime, long endTime) {
        String hourses;

        long diff = endTime - startTime;

        long seconds = diff / 1000; // seconds is milliseconds / 1000
        //   long milliseconds = (endTime - startTime) % 1000; // remainder is milliseconds that are not composing seconds.
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;

        long hr = 0;
        long min = 0;
        if (hours < 0) {
            hr = hours * 60;

        }
//        if(hours==0 && minutes <0)
//        {
//            minutes = (60+minutes) * -1;
//        }
        if (minutes < 0) {
            hr = hr + minutes;
        }

        hr = 1440 + hr;
        hours = hr / 60;
        minutes = hr % 60;
        if (hours < 0 || minutes < 0) {
//            hr = hours*60;
//            hr= hr+minutes;
//            hr+=1440;
//             min=  hr%60;
//           hr= hr/60;

            return hr + ":" + min;
        }
        hourses = hours + ":" + minutes;


        return hourses;
    }




    public interface OnFragmentSendToActivityListener {
        void onBackFragmentSendListener(Fragment fragment);

        void onShowToggle();

        void onHideToggle();

        void onChangeToolbarColor(Intent color, BreathState type, Bundle sharedView);

        void onSingleDetail(Intent intent, TblState tblState, Bundle sharedView);
    }

    public static interface ResultListener {
        void onResultAddress(String result);
    }

    private static final int ANGLE_MIN = 10;
    private static final int ANGLE_MAX = 640;
    private static final int MAX_BEND = 10;
    private static final int MIN_BEND = 180;

    public static float calculate(float accelY) {
        return (ANGLE_MIN + ((ANGLE_MAX - ANGLE_MIN) * ((accelY - (MIN_BEND)) / (MAX_BEND - MIN_BEND))));
//        return accelY;
    }

}
