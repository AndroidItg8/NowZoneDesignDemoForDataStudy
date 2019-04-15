package itg8.com.nowzonedesigndemo.accelerometer;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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


import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.accelerometer.controller.AccController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.BaseController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.LoadCellController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.SingleController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccelerometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccelerometerFragment extends BaseFragment<BaseController> implements OnChartValueSelectedListener, CompoundButton.OnCheckedChangeListener {
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

        chart = view.findViewById(R.id.barChat);
        title = view.findViewById(R.id.title);

        xValue = view.findViewById(R.id.chk_XValue);
        yValue = view.findViewById(R.id.chk_YValue);
        zValue = view.findViewById(R.id.chk_ZValue);
        setFrom();
        setFilterVisible();
        setLineChart();
        if(whereFrom()==1 || whereFrom()==2 || whereFrom()==3)
            accController = new AccController(this::NotifyAll,lineData());
        else if(whereFrom()==4)
            accController = new LoadCellController(this::NotifyAll,lineData());
        else
            accController = new SingleController(this::NotifyAll,lineData());

        referenceQueue=new WeakReference<BaseController>(accController);
        return view;
    }

    private void setFilterVisible() {
        xValue.setVisibility(isToShowX()?View.VISIBLE:View.GONE);
        yValue.setVisibility(isToShowY()?View.VISIBLE:View.GONE);
        zValue.setVisibility(isToShowZ()?View.VISIBLE:View.GONE);
    }

    private boolean isToShowX() {
        return whereFrom()==1||whereFrom()==2||whereFrom()==3||whereFrom()==4;
    }
    private boolean isToShowY() {
        return whereFrom()==1||whereFrom()==2||whereFrom()==3||whereFrom()==4;
    }
    private boolean isToShowZ() {
        return whereFrom()==1||whereFrom()==2||whereFrom()==3;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        accController=null;
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
        chart.setOnChartValueSelectedListener(this);

        // enable description text
        chart.getDescription().setEnabled(true);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        chart.setData(data);

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
            leftAxis.setAxisMaximum(16777215);
            leftAxis.setAxisMinimum(-700000f);
        } else {
            leftAxis.setAxisMaximum(70000f);
            leftAxis.setAxisMinimum(-70000f);

        }
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);








    }
    private void NotifyAll() {
        if (chart != null) {
            chart.setVisibleXRangeMaximum(MAX_ENTRIES);
//            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
//            chart.invalidate();
//            chart.moveViewToX(lineData().getEntryCount());
            chart.invalidate();

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
                break;
            case R.id.chk_YValue:
                break;
            case R.id.chk_ZValue:
                break;
        }

    }


}
