package itg8.com.nowzonedesigndemo.accelerometer;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;


import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;


import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.accelerometer.controller.AccController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.BaseController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.LoadCellController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.SingleController;
import itg8.com.nowzonedesigndemo.home.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccelerometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccelerometerFragment extends BaseFragment<BaseController> implements OnChartValueSelectedListener, CompoundButton.OnCheckedChangeListener, AccelListener, OnChartGestureListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AccelerometerFragment";
    private static final float MAX_ENTRIES = 100;

    // TODO: Rename and change types of parameters

    private LineChart chart;


    private TextView title;
    private BaseController accController;
    WeakReference<BaseController> referenceQueue;
    private CheckBox xValue;
    private CheckBox yValue;
    private CheckBox zValue;
    private boolean xVisible = true;
    private boolean yVisible = true;
    private boolean zVisible = true;
    private MPPointD topLeft;
    private MPPointD bottomLeft;


    public AccelerometerFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccelerometerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccelerometerFragment newInstance(int from) {
        AccelerometerFragment fragment = new AccelerometerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public int whereFrom() {
        return from;
    }


    @Override
    public LineData lineData() {
        return chart.getData();
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accelerometer, container, false);
        registerReceiver();
        chart = view.findViewById(R.id.barChat);
        title = view.findViewById(R.id.title);

        xValue = view.findViewById(R.id.chk_XValue);
        yValue = view.findViewById(R.id.chk_YValue);
        zValue = view.findViewById(R.id.chk_ZValue);

        xValue.setOnCheckedChangeListener(this::onCheckedChanged);
        yValue.setOnCheckedChangeListener(this::onCheckedChanged);
        zValue.setOnCheckedChangeListener(this::onCheckedChanged);

        setFrom();
        setFilterVisible();
        setLineChart();
        if (whereFrom() == 1 || whereFrom() == 2 || whereFrom() == 3)
            accController = new AccController(this, lineData());
        else if (whereFrom() == 4)
            accController = new LoadCellController(this, lineData());
        else
            accController = new SingleController(this, lineData());

        referenceQueue = new WeakReference<BaseController>(accController);

        accController.setVisibilityOfGraph(xVisible, yVisible, zVisible);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (accController != null)
//                    try {
//                        getActivity().runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                enableDummy();
//
//                            }
//                        });
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//            }
//        }).start();

        return view;
    }

    private void enableDummy() {
        if (getController() != null)
            ((AccController) getController()).setAccData(getRandom(62000, 63000), getRandom(5000, 6000), getRandom(35000, 36000));
    }

    Random r = new Random();

    private float getRandom(float i1, float i2) {
        return (float) r.nextInt((int) (i2 - i1)) + i1;
    }

    private void setFilterVisible() {
        xValue.setVisibility(isToShowX() ? View.VISIBLE : View.GONE);
        yValue.setVisibility(isToShowY() ? View.VISIBLE : View.GONE);
        zValue.setVisibility(isToShowZ() ? View.VISIBLE : View.GONE);
        yVisible = isToShowY();
        zVisible = isToShowZ();
    }

    private boolean isToShowX() {
        return whereFrom() == 1 || whereFrom() == 2 || whereFrom() == 3 || whereFrom() == 4;
    }

    private boolean isToShowY() {
        return whereFrom() == 1 || whereFrom() == 2 || whereFrom() == 3 || whereFrom() == 4;
    }

    private boolean isToShowZ() {
        return whereFrom() == 1 || whereFrom() == 2 || whereFrom() == 3;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        accController = null;
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BaseController getController() {
        if (referenceQueue.get() != null)
            return accController;

        return null;
    }

//    private void setLineChart() {
//      //  chart.setOnChartValueSelectedListener(this);
//
//        chart.getDescription().setEnabled(true);
//        chart.setTouchEnabled(true);
//
//        // enable scaling and dragging
//        chart.setDragEnabled(true);
//        chart.setScaleEnabled(true);
//        chart.setDrawGridBackground(false);
//        chart.setPinchZoom(false);
//        chart.setBackgroundColor(Color.WHITE);
//
//        LineData data = new LineData();
//        data.setValueTextColor(Color.BLACK);
//        chart.setData(data);
//        Legend l = chart.getLegend();
//        l.setForm(Legend.LegendForm.LINE);
//        l.setTextColor(Color.BLACK);
//        XAxis xl = chart.getXAxis();
//        xl.setTextColor(Color.BLACK);
//        xl.setDrawGridLines(false);
//        xl.setAvoidFirstLastClipping(true);
//        xl.setEnabled(true);
//
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTextColor(Color.BLACK);
//        if (from == 4) {
//            leftAxis.setAxisMaximum(16777215);
//            leftAxis.setAxisMinimum(-700000f);
//        } else {
//            leftAxis.setAxisMaximum(70000f);
//            leftAxis.setAxisMinimum(-70000f);
//
//        }
//        leftAxis.setDrawGridLines(true);
//
//        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setEnabled(false);
//
//
//    }

    private void setLineChart() {
//        chart.setOnChartValueSelectedListener(this);
        chart.setOnChartGestureListener(this);

        // enable description text
        chart.getDescription().setEnabled(true);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
//        chart.getAxisLeft().setStartAtZero(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        chart.setData(data);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleXEnabled(false);
//        chart.setDragDecelerationEnabled(true);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        if (from == 4) {
            leftAxis.setAxisMaximum(17777215);
            leftAxis.setAxisMinimum(-2000f);
        } else if(from== HomeActivity.FROM_ACCEL || from==HomeActivity.FROM_GYRO || from==HomeActivity.FROM_MAG || from == HomeActivity.FROM_OPTICAL) {
            leftAxis.setAxisMaximum(70000f);
            leftAxis.setAxisMinimum(0f);
        }else if(from== HomeActivity.FROM_TEMPRETURE){
            leftAxis.setAxisMaximum(85f);
            leftAxis.setAxisMinimum(-25f);
        }else if(from== HomeActivity.FROM_MIC){
            leftAxis.setAxisMaximum(9000f);
            leftAxis.setAxisMinimum(0f);
        }else {
            leftAxis.setAxisMaximum(70000f);
            leftAxis.setAxisMinimum(0f);
        }
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


    }



    private void NotifyAll() {
        if (chart != null) {
            removeOutdatedEntries(lineData().getDataSets());
            lineData().notifyDataChanged();
            chart.notifyDataSetChanged();
//            chart.invalidate();
            chart.setVisibleXRangeMaximum(MAX_ENTRIES);
            chart.moveViewToX(lineData().getEntryCount());
//            chart.moveViewTo(chart.getX(), (float) topLeft.y, YAxis.AxisDependency.LEFT);

//            YAxis yAxis=chart.getAxisLeft();
            if(topLeft!=null)
                chart.moveViewTo(lineData().getEntryCount(), (float) (topLeft.y+bottomLeft.y)/2, YAxis.AxisDependency.LEFT);


//            if(chart.getXChartMax()>MAX_ENTRIES){
//                chart.getLineData().getDataSetByIndex(0).removeEntry()
//            }
        }

    }

    int lastEntry;

    public void removeOutdatedEntries(List<ILineDataSet> dataSets) {
        for (IDataSet ds : dataSets) {
            lastEntry = ds.getEntryCount();
            while (ds.getEntryCount() > MAX_ENTRIES * 3 && lastEntry != ds.getEntryCount()) {
                ds.removeFirst();
            }
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(TAG, "onValueSelected: ");
    }

    @Override
    public void onNothingSelected() {
        Log.d(TAG, "onNothingSelected: ");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chk_XValue:
                xVisible = isChecked;
                break;
            case R.id.chk_YValue:
                yVisible = isChecked;
                break;
            case R.id.chk_ZValue:
                zVisible = isChecked;
                break;
        }

    }


    @Override
    public void invalidateChart() {
        NotifyAll();
    }

    @Override
    public boolean isXtoShow() {
        return xVisible;
    }

    @Override
    public boolean isYtoShow() {
        return yVisible;
    }

    @Override
    public boolean isZtoShow() {
        return zVisible;
    }

    /**
     * Callbacks when a touch-gesture has started on the chart (ACTION_DOWN)
     *
     * @param me
     * @param lastPerformedGesture
     */
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d(TAG, "onChartGestureStart: ");
    }

    /**
     * Callbacks when a touch-gesture has ended on the chart (ACTION_UP, ACTION_CANCEL)
     *
     * @param me
     * @param lastPerformedGesture
     */
    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d(TAG, "onChartGestureEnd: ");
        getYViewport();
        Log.d(TAG, "onChartScale: ---------->Y1: "+ topLeft.y+" Y2: "+ bottomLeft.y);

    }

    /**
     * Callbacks when the chart is longpressed.
     *
     * @param me
     */
    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    /**
     * Callbacks when the chart is double-tapped.
     *
     * @param me
     */
    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.d(TAG, "onChartDoubleTapped: ");
    }

    /**
     * Callbacks when the chart is single-tapped.
     *
     * @param me
     */
    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    /**
     * Callbacks then a fling gesture is made on the chart.
     *
     * @param me1
     * @param me2
     * @param velocityX
     * @param velocityY
     */
    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.d(TAG, "onChartFling: ");
    }

    /**
     * Callbacks when the chart is scaled / zoomed via pinch zoom gesture.
     *
     * @param me
     * @param scaleX scalefactor on the x-axis
     * @param scaleY scalefactor on the y-axis
     */
    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        ;

        getYViewport();
    }

    private void getYViewport() {
        topLeft = chart.getValuesByTouchPoint(chart.getViewPortHandler().contentLeft(), chart.getViewPortHandler().contentTop(), YAxis.AxisDependency.LEFT);
        bottomLeft = chart.getValuesByTouchPoint(chart.getViewPortHandler().contentLeft(), chart.getViewPortHandler().contentBottom(), YAxis.AxisDependency.LEFT);

    }

    /**
     * Callbacks when the chart is moved / translated via drag gesture.
     *
     * @param me
     * @param dX translation distance on the x-axis
     * @param dY translation distance on the y-axis
     */
    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}