package itg8.com.nowzonedesigndemo.utility.load_cell_algo;

import android.util.Log;

public class AlgoLoadCellBreathing {

    private static final String TAG = "AlgoLoadCellBreathing";
    public static final String LINE_BREAK = "\n";

    private int pCount = 0;
    private int tCount = 0;

    private double lastPeak;
    private double lastTrough;
    private boolean isPeakFound;
    private double lastValue;

    private StringBuilder stringBuilder;

    public AlgoLoadCellBreathing() {
        stringBuilder=new StringBuilder();
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
        return pCount > 20 && tCount > 20;
    }


}
