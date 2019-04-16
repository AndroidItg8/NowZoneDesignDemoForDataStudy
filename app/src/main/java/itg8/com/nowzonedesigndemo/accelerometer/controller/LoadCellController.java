package itg8.com.nowzonedesigndemo.accelerometer.controller;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import itg8.com.nowzonedesigndemo.accelerometer.AccelListener;
import itg8.com.nowzonedesigndemo.accelerometer.AccelerometerFragment;
import itg8.com.nowzonedesigndemo.accelerometer.ColorController;
import itg8.com.nowzonedesigndemo.common.CommonMethod;

public class LoadCellController extends BaseController {

    private static final int X_AXIS = 0;
    private static final int Y_AXIS = 1;
    private AccelListener listener;
    private LineData lineData;

    private LineDataSet dataSetGraphA;
    private LineDataSet dataSetGraphB;

    public LoadCellController(AccelListener listener, LineData lineData) {
        this.listener = listener;
        this.lineData = lineData;
    }

    public void setLoadData(float x,float y){
        initDataSetForLines();
        createEntries(x,y);


    }

    private void createEntries(float x, float y) {
        lineData.addEntry(new Entry(dataSetGraphA.getEntryCount(), x, dataSetGraphA),X_AXIS);
        lineData.addEntry(new Entry(dataSetGraphB.getEntryCount(), y, dataSetGraphB),Y_AXIS);
        lineData.notifyDataChanged();
        listener.invalidateChart();


    }

    private void initDataSetForLines() {
        dataSetGraphA = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(X_AXIS);
        dataSetGraphB = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(Y_AXIS);
        checkIfNewDataSetGraph();

    }

    private void checkIfNewDataSetGraph() {
        if (dataSetGraphA == null) {
            dataSetGraphA=CommonMethod.getDataSetGraph("X Value(Load Call 1)", ColorController.COLOR_X);
            lineData.addDataSet(dataSetGraphA);
        }
        if (dataSetGraphB == null) {
            dataSetGraphB=CommonMethod.getDataSetGraph("Y Value (Load Cell 2)", ColorController.COLOR_Y);
            lineData.addDataSet(dataSetGraphB);
        }

    }
}

