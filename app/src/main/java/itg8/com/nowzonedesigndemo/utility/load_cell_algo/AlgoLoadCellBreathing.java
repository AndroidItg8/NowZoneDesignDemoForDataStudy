package itg8.com.nowzonedesigndemo.utility.load_cell_algo;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlgoLoadCellBreathing {

    private static final String TAG = "AlgoLoadCellBreathing";
    public static final String LINE_BREAK = "\n";
    private static final int FRAME_COUNT = 1200;
    private static final int MAX_COUNT = 1200;
    private static final int LIMIT_DATA_POINT = 10;

    private int pCount = 0;
    private int tCount = 0;

    private double lastPeak;
    private double lastTrough;
    private boolean isPeakFound;
    private double lastValue;

    private Calendar calendar;

    private StringBuilder stringBuilder;
    private int checkSecond;

    public AlgoLoadCellBreathing() {
        stringBuilder=new StringBuilder();
        calendar=Calendar.getInstance();
    }

    public String getLog(){
        return stringBuilder.toString();
    }

    public boolean isPeakGot(double loadCellRaw) {
        stringBuilder=new StringBuilder();
        stringBuilder.append("Init---").append(LINE_BREAK);
        stringBuilder.append("RawValue: ").append(loadCellRaw).append(" LastValue: ").append(lastValue).append(LINE_BREAK);
        if (lastValue == 0) {
            stringBuilder.append("in lastValue=0 ").append(LINE_BREAK);
            lastValue = loadCellRaw;
            return false;
        } else {
            if (lastValue < loadCellRaw) {
                stringBuilder.append("lastValue(").append(lastValue).append(")").append("<").append("loadCellRaw(").append(loadCellRaw).append(")").append(LINE_BREAK);
                if (checkIfSatisfied()) {
                    stringBuilder.append("Condition Satisfied, reseting all").append(LINE_BREAK);
                    Log.d(TAG, "isPeakGot: " + lastPeak);
                    isPeakFound=true;
                    pCount = 0;
                    tCount = 0;
                    lastPeak = 0;
                    lastTrough = 0;
                }else {
                    isPeakFound = false;
                    stringBuilder.append("Condition Fail isPeakFound =false").append(LINE_BREAK);

                }
                if (lastTrough == 0) {
                    stringBuilder.append("lastTrough is 0").append(LINE_BREAK);
                    lastTrough = lastValue;
                    stringBuilder.append("New lastTrough :->").append(lastTrough).append(LINE_BREAK);
                }
//            if(checkIfSatisfied()){
//                pCount=0;
//                tCount=0;
//            }
                if(tCount>0) {
                    tCount = 0;
                    pCount=0;
                }
                pCount++;
                stringBuilder.append("pCount Increase : ").append(pCount).append(LINE_BREAK);
            } else {
                if (lastPeak == 0) {
                    stringBuilder.append("lastPeak is 0").append(LINE_BREAK);
                    lastPeak = lastValue;
                    stringBuilder.append("New lastPeak :->").append(lastPeak).append(LINE_BREAK);
                }
                isPeakFound=false;
                stringBuilder.append("isPeakFound :->").append(isPeakFound).append(LINE_BREAK);
                tCount++;
                stringBuilder.append("tCount Increase : ").append(tCount).append(LINE_BREAK);
            }
        }
        lastValue = loadCellRaw;
        Log.d(TAG, "isPeakGot: "+getLog());
        return isPeakFound;
    }

    public boolean isPeakGotV2(double loadCellRaw) {

        if(lastValue==0){
            lastValue=loadCellRaw;
            return false;
        }
        if(loadCellRaw>lastValue){
            pCount++;
            lastPeak=lastValue;
        }else {
            if(pCount<20){
                pCount=0;
                tCount=0;
                return false;
            }else {
                tCount++;
                if(tCount>=20){

                }
            }
        }

        return isPeakFound;
    }

    private boolean checkIfSatisfied() {
        return pCount > LIMIT_DATA_POINT && tCount > LIMIT_DATA_POINT;
    }

    private List<Long> peakTiming=new ArrayList<>();

    public int addPeakTime(long timestamp) {
        peakTiming.add(timestamp);

        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.MINUTE,-1);
        Long[] arr = new Long[peakTiming.size()];
        arr=peakTiming.toArray(arr);
        return checkPeak(calendar.getTimeInMillis(), arr);
//        checkSecond=calendar.get(Calendar.SECOND);
    }

    private int checkPeak(long timeInMillis, Long[] objects) {
        int minutePeakCount=0;
        for (Long o :
                objects) {
            if(o>timeInMillis){
                minutePeakCount+=1;
            }else {
                peakTiming.remove(o);
            }
        }
        Log.d(TAG, "checkPeak: "+minutePeakCount);

        return minutePeakCount;

    }
}
 