package itg8.com.nowzonedesigndemo.accelerometer.controller;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import itg8.com.nowzonedesigndemo.accelerometer.AccelListener;
import itg8.com.nowzonedesigndemo.accelerometer.ColorController;
import itg8.com.nowzonedesigndemo.common.CommonMethod;

public class AccController extends BaseController {

    private static final int X_AXIS = 0;
    private static final int Y_AXIS = 1;
    private static final int Z_AXIS = 2;
    private AccelListener listener;
    private LineData lineData;

    private LineDataSet dataSetGraphA;
    private LineDataSet dataSetGraphB;
    private LineDataSet dataSetGraphC;

    public AccController(AccelListener listener, LineData lineData) {
        this.listener = listener;
        this.lineData = lineData;
    }

    public void setAccData(float x,float y, float z){
        initDataSetForLines();
        createEntries(x,y,z);
    }

    private void createEntries(float x, float y, float z) {
        lineData.addEntry(new Entry(dataSetGraphA.getEntryCount(), x, dataSetGraphA),X_AXIS);
        lineData.addEntry(new Entry(dataSetGraphB.getEntryCount(), y, dataSetGraphA),Y_AXIS);
        lineData.addEntry(new Entry(dataSetGraphC.getEntryCount(), z, dataSetGraphA),Z_AXIS);
        lineData.notifyDataChanged();
        listener.invalidateChart();
    }

    private void initDataSetForLines() {
        dataSetGraphA = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(X_AXIS);
        dataSetGraphB = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(Y_AXIS);
        dataSetGraphC = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(Z_AXIS);
        checkIfNewDataSetGraph();

    }

    private void checkIfNewDataSetGraph() {
        if (dataSetGraphA == null) {
            dataSetGraphA=CommonMethod.getDataSetGraph("X Value", ColorController.COLOR_X);
            lineData.addDataSet(dataSetGraphA);
        }
        if (dataSetGraphB == null) {
            dataSetGraphB=CommonMethod.getDataSetGraph("Y Value", ColorController.COLOR_Y);
            lineData.addDataSet(dataSetGraphB);
        }
        if (dataSetGraphC == null) {
            dataSetGraphC=CommonMethod.getDataSetGraph("Z Value", ColorController.COLOR_Z);
            lineData.addDataSet(dataSetGraphC);
        }
    }
}

