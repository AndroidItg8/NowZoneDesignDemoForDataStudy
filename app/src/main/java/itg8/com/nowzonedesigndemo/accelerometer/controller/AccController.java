package itg8.com.nowzonedesigndemo.accelerometer.controller;

import android.graphics.Color;
import android.util.Log;


import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import itg8.com.nowzonedesigndemo.accelerometer.AccelListener;
import itg8.com.nowzonedesigndemo.accelerometer.ColorController;


public class AccController extends BaseController {

    private static final int X_AXIS = 0;
    private static final int Y_AXIS = 1;
    private static final int Z_AXIS = 2;
    private AccelListener listener;
    private LineData lineData;


    private static final String TAG = "AccController";
    private LineDataSet dataSetGraphA;
    private LineDataSet dataSetGraphB;
    private LineDataSet dataSetGraphC;


    public AccController(AccelListener listener, LineData lineData) {
        this.listener = listener;
        this.lineData = lineData;

//

    }

    public void setAccData(float x, float y, float z) {
        initDataSetForLines();
        createEntries(x, y, z);
    }

    private void createEntries(float x, float y, float z) {
            lineData.addEntry(new Entry(dataSetGraphA.getEntryCount(), x, dataSetGraphA), X_AXIS);
             lineData.addEntry(new Entry(dataSetGraphB.getEntryCount(), y, dataSetGraphB), Y_AXIS);
              lineData.addEntry(new Entry(dataSetGraphC.getEntryCount(), z, dataSetGraphC), Z_AXIS);

        //lineData.notifyDataChanged();
        listener.invalidateChart();


    }

    private void initDataSetForLines() {
        if (lineData != null) {
            dataSetGraphA = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(X_AXIS);
            dataSetGraphB = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(Y_AXIS);
            dataSetGraphC = lineData.getDataSetCount() == 0 ? null : (LineDataSet) lineData.getDataSetByIndex(Z_AXIS);
            checkIfNewDataSetGraph();

            dataSetGraphA.setVisible(listener.isXtoShow());
            dataSetGraphB.setVisible(listener.isYtoShow());
            dataSetGraphC.setVisible(listener.isZtoShow());
//            dataSetGraphA.setVisible(true);
//            dataSetGraphB.setVisible(true);
//            dataSetGraphC.setVisible(true);

        }

    }

    private void checkIfNewDataSetGraph() {
        if (dataSetGraphA == null) {
            dataSetGraphA = getDataSetGraph("X Value", ColorController.COLOR_X);
            lineData.addDataSet(dataSetGraphA);
        }

        if (dataSetGraphB == null) {
            Log.d(TAG, "onCheckedChanged: ");
            dataSetGraphB = getDataSetGraph("Y Value", ColorController.COLOR_Y);
            lineData.addDataSet(dataSetGraphB);
        }

        if (dataSetGraphC == null) {
            dataSetGraphC = getDataSetGraph("Z Value", ColorController.COLOR_Z);
            lineData.addDataSet(dataSetGraphC);
        }


    }


    public LineDataSet getDataSetGraph(String s, String colorCode) {

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


//        LineDataSet set = new LineDataSet(null, s);
//        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setColor(Color.parseColor(colorCode));
//        set.setCircleColor(Color.parseColor(colorCode));
//        set.setLineWidth(2f);
//        set.setCircleRadius(4f);
//        set.setFillAlpha(65);
//        set.setFillColor(Color.parseColor(colorCode));
//        set.setHighLightColor(Color.parseColor(colorCode));
//        set.setValueTextColor(Color.parseColor(colorCode));
//        set.setValueTextSize(9f);
//        set.setDrawValues(false);
        return set;
    }



}

