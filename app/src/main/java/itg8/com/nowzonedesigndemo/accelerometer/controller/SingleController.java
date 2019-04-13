package itg8.com.nowzonedesigndemo.accelerometer.controller;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import itg8.com.nowzonedesigndemo.accelerometer.AccelListener;
import itg8.com.nowzonedesigndemo.accelerometer.ColorController;
import itg8.com.nowzonedesigndemo.common.CommonMethod;

public class SingleController extends BaseController{

    public static final int X_AXIS = 0;
    private AccelListener listener;
    private LineData lineData;

    private LineDataSet dataSetGraphA;

    public SingleController(AccelListener listener, LineData lineData) {
        this.listener = listener;
        this.lineData = lineData;
    }

    public void setSingleData(float x){
        initDataSetForLines();
        createEntries(x);
    }

    private void createEntries(float x) {
        lineData.addEntry(new Entry(dataSetGraphA.getEntryCount(), x, dataSetGraphA),X_AXIS);
        lineData.notifyDataChanged();
        listener.invalidateChart();
    }

    private void initDataSetForLines() {
        dataSetGraphA = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(X_AXIS);
        checkIfNewDataSetGraph();

    }

    private void checkIfNewDataSetGraph() {
        if (dataSetGraphA == null) {
            dataSetGraphA=CommonMethod.getDataSetGraph("X Value", ColorController.COLOR_X);
            lineData.addDataSet(dataSetGraphA);
        }


    }
}

