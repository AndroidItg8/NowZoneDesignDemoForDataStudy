package itg8.com.nowzonedesigndemo.pressure;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PressureProcessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PressureProcessFragment extends Fragment implements OnChartValueSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final float MAX_ENTRIES = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "PressureProcessFragment";
    private com.github.mikephil.charting.charts.LineChart  chart;
    private LineDataSet set;
    private boolean moveToLastEntry=true;





    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(getString(R.string.action_data_avail))) {
                    double processPressure = intent.getLongExtra(CommonMethod.ACTION_DATA_LONG, 0);
                    Log.d("BreathPresenterImp","data to presenter:"+processPressure);
                    //    BreathPresenterImp.this.model.dataStarted(true);
                    onPressureReceived( processPressure);

                }
            }
        }
    };

    public PressureProcessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PressureProcessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PressureProcessFragment newInstance(String param1, String param2) {
        PressureProcessFragment fragment = new PressureProcessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pressure_process, container, false);
        chart=view.findViewById(R.id.line_chart);
        registerReceiver();
        setLineChart();

        return view;
    }
    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(getActivity().getResources().getString(R.string.action_data_avail)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        leftAxis.setAxisMaximum(8000f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                moveToLastEntry = false;
            }


            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
            }
        });






    }

    private void onPressureReceived(double pressureRaw) {
        Log.d(TAG, "onPressureReceived: Pressure"+pressureRaw);
        Observable<Double> observable = Observable.just(pressureRaw);
        observable.flatMap(new Function<Double, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> apply(Double aFloat) throws Exception {
                Log.d(TAG, "onPressureReceived: Pressure Float"+aFloat);
                addEntry(aFloat);
                return Observable.just(true);
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

            }
        });

        updateChartAnimate();



    }
    public void updateChartAnimate(){
        Handler handler = new Handler();
        int delay = 30000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                chart.animateX(50000);
                NotifyAll();

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void NotifyAll() {
        if(set!=null && chart !=null )
            set.notifyDataSetChanged();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }
    private void addEntry(Double aFloat) {
        BigDecimal bigDecimal = new BigDecimal(aFloat);
        Log.d(TAG, "addEntry: bigDecimal"+bigDecimal.floatValue());

        LineData data = chart.getData();

        if (data != null ) {

//            ILineDataSet set = data.getDataSetByIndex(0);
//         LineDataSet   set = (LineDataSet) data.getDataSetByIndex(0);

            set = (LineDataSet) data.getDataSetByIndex(0);
//             set.addEntry(new Entry(set.getEntryCount(),aFloat)); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            Log.d(TAG, "addEntry: set.getEntryCount :"+bigDecimal.floatValue());
//            data.addEntry(new Entry(set.getEntryCount(), bigDecimal.floatValue()), 0);
            data.addEntry(new Entry(data.getXMax()+1, bigDecimal.floatValue()), 0);

            data.notifyDataChanged();
//            removeOutdatedEntries(set);

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries

            chart.invalidate();
            chart.setVisibleXRangeMaximum(MAX_ENTRIES);

//             chart.setVisibleYRange(30, YAxis.AxisDependency.LEFT);





//            if (moveToLastEntry) {
//                // move to the latest entry
//
//                chart.moveViewToX(data.getEntryCount());
//            }




            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, " Pressure Process Data ");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLUE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLUE);
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
}
