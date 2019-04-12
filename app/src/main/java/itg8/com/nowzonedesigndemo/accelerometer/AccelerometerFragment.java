package itg8.com.nowzonedesigndemo.accelerometer;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;


import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import java.math.BigDecimal;


import itg8.com.nowzonedesigndemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccelerometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccelerometerFragment extends BaseFragment implements OnChartValueSelectedListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AccelerometerFragment";
    private static final int MAXIMUM_VALUE_ALL = 65535;
    private static final float MAX_ENTRIES = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LineChart chart;
    private boolean moveToLastEntry = true;
    private LineDataSet set;
    private CheckBox xValue, yValue, zValue;
    private boolean isYValueChecked = true;
    private boolean isXValueChecked = true;
    private boolean isZValueChecked = true;


//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null) {
//                String action = intent.getAction();
//                if (action.equalsIgnoreCase(getString(R.string.action_data_avail))) {
//                    double acc_X = intent.getDoubleExtra(CommonMethod.ACC_X, -10);
//                    double acc_Y = intent.getDoubleExtra(CommonMethod.ACC_Y, -10);
//                    double acc_Z = intent.getDoubleExtra(CommonMethod.ACC_Z, -10);
//                    Log.d(TAG, "data to presenter:" + " acc_X :" + acc_X + " acc_Y :" + acc_Y + " acc_Z " + acc_Z);
//                    //    BreathPresenterImp.this.model.dataStarted(true);
//
//                    onAccelerometer(new TempAccelerpmeter(acc_X, acc_Y, acc_Z));
//                }
//            }
//        }
//    };


    public void onAccelerometer(TempAccelerpmeter tempAccelerpmeter) {
        addEntry(tempAccelerpmeter);
//        Observable<TempAccelerpmeter> observable = Observable.just(tempAccelerpmeter);
//        observable.flatMap(new Function<TempAccelerpmeter, Observable<Boolean>>() {
//            @Override
//            public Observable<Boolean> apply(TempAccelerpmeter aFloat) throws Exception {
//                Log.d(TAG, "onPressureReceived: Pressure Float" + new Gson().toJson(aFloat));
//
//                return Observable.just(true);
//            }
//        }).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Boolean>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//
//        updateChartAnimate();

    }


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
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        chart = view.findViewById(R.id.barChat);

        xValue = view.findViewById(R.id.chk_XValue);
        yValue = view.findViewById(R.id.chk_YValue);
        zValue = view.findViewById(R.id.chk_ZValue);
        xValue.setOnCheckedChangeListener(this);
        yValue.setOnCheckedChangeListener(this);
        zValue.setOnCheckedChangeListener(this);
        setFrom();

//        registerReceiver();
        setLineChart();
        return view;
    }


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
        leftAxis.setAxisMaximum(70000f);
        leftAxis.setAxisMinimum(-70000f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

//        chart.setOnChartGestureListener(new OnChartGestureListener() {
//            @Override
//            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//                moveToLastEntry = false;
//            }
//
//
//            @Override
//            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//            }
//
//            @Override
//            public void onChartLongPressed(MotionEvent me) {
//            }
//
//            @Override
//            public void onChartDoubleTapped(MotionEvent me) {
////            }
//
//            @Override
//            public void onChartSingleTapped(MotionEvent me) {
//            }
//
//            @Override
//            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
//            }
//
//            @Override
//            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//            }
//
//            @Override
//            public void onChartTranslate(MotionEvent me, float dX, float dY) {
//            }
//        });


    }


    public void updateChartAnimate() {
        Handler handler = new Handler();
        int delay = 30000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                chart.animateX(50000);
                NotifyAll();

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void NotifyAll() {
        if (chart != null)


            chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }


//    private void addEntry(TempAccelerpmeter temp) {
//        Log.d(TAG, "addEntry: " + "AccX :" + temp.getAccY() + "AccY" + temp.getAccY() + "AccZ" + temp.getAccZ());
//        float xMin = 0;
//
//        LineData data = chart.getData();
//
//        if (data != null) {
//
////            ILineDataSet set = data.getDataSetByIndex(0);
////         LineDataSet   set = (LineDataSet) data.getDataSetByIndex(0);
//            LineDataSet dataSetGraphA = null, dataSetGraphB = null, dataSetGraphC = null;
//
//            if (isXValueChecked) {
//                dataSetGraphA = data.getDataSetCount()==0?null:(LineDataSet) data.getDataSetByIndex(0);
//            } else if (isYValueChecked) {
//                dataSetGraphB =  data.getDataSetCount()==0?null:(LineDataSet)data.getDataSetByIndex(1);
//            } else if (isZValueChecked) {
//                dataSetGraphC =  data.getDataSetCount()==0?null:(LineDataSet) data.getDataSetByIndex(2);
//            }
//            // LineDataSet set1= data.getDataSetByIndex(1);
//            // set.addEntry(...); // can be called as well
//            if (isXValueChecked) {
//                dataSetGraphA = createSet(0);
//                data.addDataSet(dataSetGraphA);
//                xMin = data.getXMin();
//            }
//            if (isYValueChecked) {
//                dataSetGraphB = createSet(1);
//                data.addDataSet(dataSetGraphB);
//                xMin = data.getXMin();
//
//            }
//            if (isZValueChecked) {
//                dataSetGraphC = createSet(2);
//                data.addDataSet(dataSetGraphC);
//                xMin = data.getXMin();
//
//            }
//
//
//            if (isXValueChecked) {
//                data.addEntry(new Entry(dataSetGraphA.getEntryCount(), new BigDecimal(temp.getAccX()).floatValue(), dataSetGraphA), 0);
//                dataSetGraphA.notifyDataSetChanged();
//
//            } else if (isYValueChecked) {
//                data.addEntry(new Entry(dataSetGraphB.getEntryCount(), new BigDecimal(temp.getAccY()).floatValue(), dataSetGraphB), 1);
//                dataSetGraphB.notifyDataSetChanged();
//
//            } else if (isZValueChecked) {
//                data.addEntry(new Entry(dataSetGraphC.getEntryCount(), new BigDecimal(temp.getAccZ()).floatValue(), dataSetGraphC), 2);
//                dataSetGraphC.notifyDataSetChanged();
//
//            }
//
//
//
//            data.notifyDataChanged();
////            removeOutdatedEntries(set);
//
//            chart.notifyDataSetChanged();
//            chart.invalidate();
//            chart.setVisibleXRangeMaximum(MAX_ENTRIES);
//
//
//        }
//    }


    private void addEntryForlong(long acc_x, long acc_y) {
        BigDecimal bigDecimal = new BigDecimal(acc_x);
        float valueX = bigDecimal.floatValue();
        float valueY = 0;
        if (acc_y > 0) {
            BigDecimal bigDecimal2 = new BigDecimal(acc_y);
            valueY = bigDecimal2.floatValue();
        }

        setChartValue(valueX, valueY);


    }

    private void setChartValue(float valueX, float valueY) {
        LineData data = chart.getData();


        if (data != null) {


            LineDataSet dataSetGraphA = null, dataSetGraphB = null;


            dataSetGraphA = data.getDataSetCount() == 0 ? null : (LineDataSet) data.getDataSetByIndex(0);
            if (valueY > 0)
                dataSetGraphB = data.getDataSetCount() == 0 ? null : (LineDataSet) data.getDataSetByIndex(1);

            // LineDataSet set1= data.getDataSetByIndex(1);
            // set.addEntry(...); // can be called as well
            if (dataSetGraphA == null && valueX > 0) {
                dataSetGraphA = createSet(0);
                data.addDataSet(dataSetGraphA);
            }
            if (dataSetGraphB == null && valueY > 0) {
                dataSetGraphB = createSet(1);
                data.addDataSet(dataSetGraphB);
            }

            data.addEntry(new Entry(dataSetGraphA.getEntryCount(), new BigDecimal(valueX).floatValue(), dataSetGraphA), 0);


        data.addEntry(new Entry(dataSetGraphB.getEntryCount(), new BigDecimal(valueY).floatValue(), dataSetGraphB), 1);


        data.notifyDataChanged();
//            removeOutdatedEntries(set);

        chart.notifyDataSetChanged();
        chart.invalidate();
        chart.setVisibleXRangeMaximum(MAX_ENTRIES);


    }

}

    private void addEntry(TempAccelerpmeter temp) {
        Log.d(TAG, "addEntry: " + "AccX :" + temp.getAccY() + "AccY" + temp.getAccY() + "AccZ" + temp.getAccZ());
        float xMin = 0;

      LineData  data = chart.getData();

        if (data != null) {

//            ILineDataSet set = data.getDataSetByIndex(0);
//         LineDataSet   set = (LineDataSet) data.getDataSetByIndex(0);
            LineDataSet dataSetGraphA = null, dataSetGraphB = null, dataSetGraphC = null;

            if (isXValueChecked) {
                dataSetGraphA = data.getDataSetCount() == 0 ? null : (LineDataSet) data.getDataSetByIndex(0);
            } else if (isYValueChecked) {
                dataSetGraphB = data.getDataSetCount() == 0 ? null : (LineDataSet) data.getDataSetByIndex(1);
            } else if (isZValueChecked) {
                dataSetGraphC = data.getDataSetCount() == 0 ? null : (LineDataSet) data.getDataSetByIndex(2);
            }
            // LineDataSet set1= data.getDataSetByIndex(1);
            // set.addEntry(...); // can be called as well
            if (isXValueChecked) {
                dataSetGraphA = createSet(0);
                data.addDataSet(dataSetGraphA);
                xMin = data.getXMin();
            }
            if (isYValueChecked) {
                dataSetGraphB = createSet(1);
                data.addDataSet(dataSetGraphB);
                xMin = data.getXMin();

            }
            if (isZValueChecked) {
                dataSetGraphC = createSet(2);
                data.addDataSet(dataSetGraphC);
                xMin = data.getXMin();

            }


            if (isXValueChecked) {
                data.addEntry(new Entry(dataSetGraphA.getEntryCount(), new BigDecimal(temp.getAccX()).floatValue(), dataSetGraphA), 0);
                dataSetGraphA.notifyDataSetChanged();

            } else if (isYValueChecked) {
                data.addEntry(new Entry(dataSetGraphB.getEntryCount(), new BigDecimal(temp.getAccY()).floatValue(), dataSetGraphB), 1);
                dataSetGraphB.notifyDataSetChanged();

            } else if (isZValueChecked) {
                data.addEntry(new Entry(dataSetGraphC.getEntryCount(), new BigDecimal(temp.getAccZ()).floatValue(), dataSetGraphC), 2);
                dataSetGraphC.notifyDataSetChanged();

            }


            data.notifyDataChanged();
//            removeOutdatedEntries(set);

            chart.notifyDataSetChanged();
            chart.invalidate();
            chart.setVisibleXRangeMaximum(MAX_ENTRIES);


        }
    }

    @Override
    public void onChartInvalidate() {
        chart.notifyDataSetChanged();
        chart.invalidate();
        chart.setVisibleXRangeMaximum(MAX_ENTRIES);
    }

    private LineDataSet createSet(int temp) {
        String c = null, s = null;
        if (temp == 0) {
            c = "#ed1f24";
            s = "X Value";
        } else if (temp == 1) {
            c = "#004bf6";
            s = "Y Value";
        } else if (temp == 2) {
            c = "#ffba00";
            s = "Z Value";
        }

        LineDataSet set = new LineDataSet(null, s);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor(c));
//        set.setCircleColor(Color.BLUE);
        set.setLineWidth(2f);
//        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.parseColor(c));
//        set.setHighLightColor(Color.rgb(244, 117, 117));
//        set.setValueTextColor(Color.BLUE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
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
                isXValueChecked = isChecked;
                break;
            case R.id.chk_YValue:
                isYValueChecked = isChecked;
                break;
            case R.id.chk_ZValue:
                isZValueChecked = isChecked;
                break;
        }

    }
//    private void registerReceiver() {
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(getActivity().getResources().getString(R.string.action_data_avail)));
////        model.onInitStateTime();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
