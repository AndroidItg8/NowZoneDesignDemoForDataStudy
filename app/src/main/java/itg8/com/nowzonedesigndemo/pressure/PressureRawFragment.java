package itg8.com.nowzonedesigndemo.pressure;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

import static itg8.com.nowzonedesigndemo.common.CommonMethod.RAW_DATA_PRESSURE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PressureRawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PressureRawFragment extends Fragment implements OnChartValueSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LineChart chart;
    private static final String TAG = "PressureRawFragment";

    private static final int MAX_ENTRIES = 100;
    private boolean moveToLastEntry=true;


    double PressureRaw;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(getString(R.string.action_data_avail))) {
                    if(intent.hasExtra(RAW_DATA_PRESSURE)) {
                         PressureRaw = intent.getDoubleExtra(RAW_DATA_PRESSURE, 0);
//                    Log.d("BreathPresenterImp","data to presenter:"+model+"  "+timestamp);
                        //    BreathPresenterImp.this.model.dataStarted(true);
                        onPressureReceived((float) PressureRaw);
                    }

                }
            }
        }
    };



    private void onPressureReceived(float pressureRaw) {
        Log.d(TAG, "onPressureReceived: Pressure"+pressureRaw);
        addEntry(pressureRaw);

//        Observable<Float> observable = Observable.just(pressureRaw);
//        observable.flatMap(new Function<Float, Observable<Boolean>>() {
//            @Override
//            public Observable<Boolean> apply(Float aFloat) throws Exception {
//                Log.d(TAG, "onPressureReceived: Pressure Float"+aFloat);
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

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    public PressureRawFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PressureRawFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PressureRawFragment newInstance(String param1, String param2) {
        PressureRawFragment fragment = new PressureRawFragment();
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
        View view = inflater.inflate(R.layout.fragment_pressure_raw, container, false);
        chart = view.findViewById(R.id.line_chart);
        registerReceiver();
        setLineChart();
        return view;
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(getActivity().getResources().getString(R.string.action_data_avail)));
//        model.onInitStateTime();
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
        l.setForm(LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(8000f);
        leftAxis.setAxisMinimum(-2000f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);







    }



//    private void setData() {
//        if (chart.getData() != null &&
//                chart.getData().getDataSetCount() > 0) {
//            if(set1!=null && values.size()>0) { ;
//
//                set1 = (LineDataSet) chart.getData().getDataSetByIndex(lastIndex);
//                set1.setValues(values);
//                NotifyAll();
//            }
//        } else {
//            set1 = new LineDataSet(values, "DataSet 1");
//            set1.setDrawIcons(false);
//            set1.setColor(Color.BLACK);
//            set1.setCircleColor(Color.GREEN);
//            set1.setLineWidth(1f);
//            set1.setCircleRadius(3f);
//            set1.setDrawCircleHole(false);
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);
//            set1.setValueTextSize(9f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
//            set1.setDrawFilled(true);
//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return chart.getAxisLeft().getAxisMinimum();
//                }
//            });
//
//            if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.stress);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }
//            lastIndex =set1.getEntryCount();
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1); // add the data sets
//
//            LineData data = new LineData(dataSets);
//            chart.setData(data);
//        }
//    }

    private void NotifyAll() {
        if( chart !=null )

        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
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
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEntry(Float aFloat) {
//        BigDecimal bigDecimal = new BigDecimal(aFloat);
//        Log.d(TAG, "addEntry: bigDecimal"+bigDecimal.floatValue());
        LineData data = chart.getData();

        if (data != null ) {

//            ILineDataSet set = data.getDataSetByIndex(0);
//         LineDataSet   set = (LineDataSet) data.getDataSetByIndex(0);

            ILineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
//             set.addEntry(new Entry(set.getEntryCount(),aFloat)); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            Log.d(TAG, "addEntry: set.getEntryCount +"+ set.getEntryCount());
            data.addEntry(new Entry(set.getEntryCount(),aFloat), 0);
//            data.addEntry(new Entry(data.getXMax()+1,bigDecimal.floatValue()), 0);

            data.notifyDataChanged();
//            set.notifyDataSetChanged();

//            removeOutdatedEntries(set);

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries

            chart.invalidate();
            chart.setVisibleXRangeMaximum(MAX_ENTRIES);

//             chart.setVisibleYRange(30, YAxis.AxisDependency.LEFT);





            if (moveToLastEntry) {
                // move to the latest entry

                chart.moveViewToX(data.getEntryCount());
            }




            // this automatically refreshes the chart (calls invalidate())
//             chart.moveViewTo(data.getXValCount()-7, 55f,
//             AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Pressure Raw Data ");
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


        // (more code, irrelevant for the issue...)
        public  void removeOutdatedEntries(LineDataSet... dataSets) {
            for (DataSet ds : dataSets) {
                while (ds.getEntryCount() > 5000) {
                    ds.removeFirst();
                }
            }

    }
}


