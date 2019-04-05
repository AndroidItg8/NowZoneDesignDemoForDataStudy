package itg8.com.nowzonedesigndemo.utility;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;


class AlgoAsync extends AsyncTask<DataModelPressure[],Void,Integer> {


    private static final double DELTA = 2.0;
    private static final String TAG = AlgoAsync.class.getSimpleName();
    public static final long ONE_MINUTE = 60000;
    private PAlgoCallback callback;
    final private double[] tempLineChartRaw;
    private int bpmInMinute;
    private File completeFileStructure;
    private TreeMap<Integer, Double> all;
    int j=0;

    AlgoAsync(PAlgoCallback callback, double[] tempLineChartRaw) {
        this.callback = callback;
        this.tempLineChartRaw = tempLineChartRaw;
        j=0;
    }

    @SafeVarargs
    @Override
    protected final Integer doInBackground(DataModelPressure[]... lists) {
        List<Map<Integer, Double>> data = CommonMethod.countBPMBreath(lists[0], DELTA);
     //   Log.d(TAG,"size : "+data.size());
        if(data.size()>0) {
            int count= Math.max(data.get(0).size(),data.get(1).size());
            DataModelPressure[] listModel = lists[0];
            if (listModel.length > 0) {
                Log.d(TAG,"StartTime "+listModel[0].getTimestamp()+" EndTime "+ listModel[listModel.length - 1].getTimestamp() +" diff: "+(listModel[listModel.length - 1].getTimestamp()-listModel[0].getTimestamp())+" count:"+count);
                long timeTaken = listModel[listModel.length - 1].getTimestamp() - listModel[0].getTimestamp();
                if (timeTaken > 50000) {
                    bpmInMinute = (int) ((ONE_MINUTE * count) / timeTaken);
                    if(bpmInMinute<40) {
                        SparseArray<List<Integer>> allDiff = writeEverythingInFile(data, bpmInMinute, listModel[0].getTimestamp(), listModel[listModel.length - 1].getTimestamp(), listModel);
                        callback.onCountResultAvailable(bpmInMinute, listModel[listModel.length - 1].getTimestamp(), allDiff);
                    }
//                    callback.onCountResultAvailable(bpmInMinute, listModel.get(listModel.size() - 1).getTimestamp());
                }
                    /**
                     * as we cakculated that a count in one minute is 2000. so we will directly send count
                     */
//                Log.d(TAG,"hashmap : "+new Gson().toJson(data));
//                Log.d(TAG,"hashmap BPMInMinute: "+bpmInMinute);
//                Log.d(TAG,"hashmap timestamp : "+listModel[0].getTimestamp()+" "+listModel[listModel.length-1].getTimestamp());
             //                    callback.onCountResultAvailable(count, listModel.get(listModel.size() - 1).getTimestamp());
            }
        }
        return null;
    }

    private SparseArray<List<Integer>> writeEverythingInFile(List<Map<Integer, Double>> data, int bpmInMinute, long startTime, long endTime, DataModelPressure[] listModel) {
        StringBuilder content= new StringBuilder();
        int count=0;
        TreeMap<Integer,Double> peaks=new TreeMap<>(data.get(0));
        TreeMap<Integer,Double> trough=new TreeMap<>(data.get(1));
        List<Integer> indexPeak=new ArrayList<>();
        List<Integer> indexTrough=new ArrayList<>();
        List<Integer> diffPeak=new ArrayList<>();
        List<Integer> diffThrogh=new ArrayList<>();
        SparseArray<List<Integer>> allDifferenceAfterCalculation=new SparseArray<>();
        all=new TreeMap<>();

        all.putAll(peaks);
        all.putAll(trough);
        StringBuilder header= new StringBuilder("Count:" + bpmInMinute + " StartTime:" + Helper.convertTimestampToTime(startTime) + " EndTime:" + Helper.convertTimestampToTime(endTime) + "\n");
        for (Map.Entry<Integer, Double> map :
                all.entrySet()) {
            header.append(map.getKey()).append(" ").append(map.getValue()).append("\n");
            if(count%2==0){
                indexPeak.add(map.getKey());
            }else {
                indexTrough.add(map.getKey());
            }
            count++;
        }

        boolean hasFalsePeak=false;
        int falseTroughCount = 0;
        int falsePeakCount = 0;
        for(int i = 1; i<indexPeak.size(); i++){
            if((indexPeak.get(i)-indexPeak.get(i-1))>=20) {
                diffPeak.add((indexPeak.get(i) - indexPeak.get(i - 1)));
            }else {
                falsePeakCount++;
            }
        }
        for(int i=1; i<indexTrough.size();i++){
            if(indexTrough.get(i)-indexTrough.get(i-1)>=20) {
                diffThrogh.add((indexTrough.get(i) - indexTrough.get(i - 1)));
            }else{
                falseTroughCount++;
            }
        }


        int countToMinus = (falsePeakCount + falseTroughCount) / 2;
        AlgoAsync.this.bpmInMinute-=countToMinus;

        Log.d(TAG,"ctm: "+countToMinus +" count: "+bpmInMinute+" PeakDifference: "+new Gson().toJson(diffPeak));

//        for (Integer i :
//                diffPeak) {
//
//        }
        count=0;
        allDifferenceAfterCalculation.put(0,diffPeak);
        allDifferenceAfterCalculation.put(1,diffThrogh);
        content.append("Peaks:\n");
        for(int i=0; i<diffPeak.size(); i++){
            content.append(diffPeak.get(i)).append("\n");
        }
        content.append("Trough:\n");
        for(int i=0; i<diffThrogh.size(); i++){
            content.append(diffThrogh.get(i)).append("\n");
        }
        StringBuilder stringPressure = new StringBuilder();

        for (DataModelPressure m :
                listModel) {
            stringPressure.append(Helper.convertTimestampToTime(m.getTimestamp())).append(m.getPressure()).append(" ").append(m.getX()).append(" ").append(m.getY()).append(" ").append(m.getZ()).append(" ").append(tempLineChartRaw[j]).append("\n");
            j++;
        }
        createFile(stringPressure.toString() +header+content);
        return allDifferenceAfterCalculation;
    }

        private void createFile(String log) {
            completeFileStructure = new File(Environment.getExternalStorageDirectory() + File.separator + "nowzone", Helper.getCurrentDate()+"breathingPeakTrough.txt");
            try {
                FileWriter fWriter;
                if (completeFileStructure.exists()) {
                    fWriter = new FileWriter(completeFileStructure, true);
                    fWriter.append(log);
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

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}