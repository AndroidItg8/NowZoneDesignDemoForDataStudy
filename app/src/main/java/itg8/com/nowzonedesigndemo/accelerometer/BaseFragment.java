package itg8.com.nowzonedesigndemo.accelerometer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import itg8.com.nowzonedesigndemo.R;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.math.BigDecimal;

import itg8.com.nowzonedesigndemo.common.CommonMethod;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public abstract class BaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public int from;
    private BigDecimal bigDecimal, bigDecimal1, bigDecimal2;



    public BaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment BaseFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static BaseFragment newInstance(String param1, String param2) {
//        BaseFragment fragment = new BaseFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(getString(R.string.action_data_avail))) {
                    if (intent.hasExtra(CommonMethod.ACC_X)) {
                        double acc_X = intent.getDoubleExtra(CommonMethod.ACC_X, -10);
                        double acc_Y = intent.getDoubleExtra(CommonMethod.ACC_Y, -10);
                        double acc_Z = intent.getDoubleExtra(CommonMethod.ACC_Z, -10);

                        Log.d(TAG, "data to presenter:" + " acc_X :" + acc_X + " acc_Y :" + acc_Y + " acc_Z " + acc_Z);
                        //    BreathPresenterImp.this.model.dataStarted(true);
//                        if(whereFrom()==1)
//                           onAccelerometer(new TempAccelerpmeter(acc_X, acc_Y, acc_Z));

                        bigDecimal2 = new BigDecimal(acc_Z);
                        bigDecimal1 = new BigDecimal(acc_Y);
                        bigDecimal = new BigDecimal(acc_X);
                        if (whereFrom() == 1 ) {
                            manupulateData(bigDecimal.floatValue(), bigDecimal1.floatValue(), bigDecimal2.floatValue());
                        }
                    }
                    if (intent.hasExtra(CommonMethod.GYNO_X)) {
                        double acc_X = intent.getDoubleExtra(CommonMethod.GYNO_X, -10);
                        double acc_Y = intent.getDoubleExtra(CommonMethod.GYNO_Y, -10);
                        double acc_Z = intent.getDoubleExtra(CommonMethod.GYNO_Z, -10);

                        Log.d(TAG, "data to presenter:" + " acc_X :" + acc_X + " acc_Y :" + acc_Y + " acc_Z " + acc_Z);
                        //    BreathPresenterImp.this.model.dataStarted(true);
                        bigDecimal2 = new BigDecimal(acc_Z);
                        bigDecimal1 = new BigDecimal(acc_Y);
                        bigDecimal = new BigDecimal(acc_X);
                        if (whereFrom() == 2 ) {
                            manupulateData(bigDecimal.floatValue(), bigDecimal1.floatValue(), bigDecimal2.floatValue());
                        }
                    }
                    if (intent.hasExtra(CommonMethod.MAGNO_X)) {
                        double acc_X = intent.getDoubleExtra(CommonMethod.MAGNO_X, -10);
                        double acc_Y = intent.getDoubleExtra(CommonMethod.MAGNO_Y, -10);
                        double acc_Z = intent.getDoubleExtra(CommonMethod.MAGNO_Z, -10);

                        Log.d(TAG, "data to presenter:" + " acc_X :" + acc_X + " acc_Y :" + acc_Y + " acc_Z " + acc_Z);
                        //    BreathPresenterImp.this.model.dataStarted(true);


                        bigDecimal2 = new BigDecimal(acc_Z);
                        bigDecimal1 = new BigDecimal(acc_Y);
                        bigDecimal = new BigDecimal(acc_X);
                        if (whereFrom() == 3 ) {
                            manupulateData(bigDecimal.floatValue(), bigDecimal1.floatValue(), bigDecimal2.floatValue());
                        }


                    }
                    if (intent.hasExtra(CommonMethod.LOAD_CELL_1)) {
                        long acc_X = intent.getLongExtra(CommonMethod.LOAD_CELL_1, -10);
                        long acc_Y = intent.getLongExtra(CommonMethod.LOAD_CELL_2, -10);

                        Log.d(TAG, "data to presenter: LOAD_CELL_1" + " acc_X :" + acc_X + "LOAD_CELL_2 " + acc_Y);
                        //    BreathPresenterImp.this.model.dataStarted(true);
                        bigDecimal1 = new BigDecimal(acc_Y);
                        bigDecimal = new BigDecimal(acc_X);
                        if (whereFrom() == 4) {
                        manupulateData(bigDecimal.floatValue(), bigDecimal1.floatValue(), 0F);
                        }
                    }
                    if (intent.hasExtra(CommonMethod.MIC)) {
                        long acc_X = intent.getLongExtra(CommonMethod.MIC, -10);

                        Log.d(TAG, "data to presenter:" + " acc_X  MIC:" + acc_X);
                        //    BreathPresenterImp.this.model.dataStarted(true);
                        bigDecimal = new BigDecimal(acc_X);
                        if (whereFrom() == 5) {
                            manupulateData(bigDecimal.floatValue(), 0F, 0F);
                        }
                    }


                }
                if (intent.hasExtra(CommonMethod.OPTICAL)) {
                    long acc_X = intent.getLongExtra(CommonMethod.MIC, -10);

                    Log.d(TAG, "data to presenter:" + " acc_X OPTICAL :" + acc_X);
                    //    BreathPresenterImp.this.model.dataStarted(true);
                    bigDecimal = new BigDecimal(acc_X);
                    if (whereFrom() == 6) {
                        manupulateData(bigDecimal.floatValue(), 0F, 0F);
                    }
                }


            }
            if (intent.hasExtra(CommonMethod.TEMP)) {
                long acc_X = intent.getLongExtra(CommonMethod.TEMP, -10);

                Log.d(TAG, "data to presenter:" + " acc_X OPTICAL :" + acc_X);
                //    BreathPresenterImp.this.model.dataStarted(true);
                bigDecimal = new BigDecimal(acc_X);
                if (whereFrom() == 7) {
                    manupulateData(bigDecimal.floatValue(), 0F, 0F);
                }
            }


        }
    };

    private void manupulateData(float x, float y, float z) {
        if (x > 0 && y > 0 && z > 0) {
                AccGynoMagno(x, y, z, 3);
            } else if (x > 0 && y > 0) {
                AccGynoMagno(x, y, z, 2);
            } else if (x > 0) {
                AccGynoMagno(x, y, z, 1);
            }





    }

    private void AccGynoMagno(float x, float y, float z, int all) {
        if(lineData()!=null) {
            if (all == 3) {
                    LineDataSet dataSetGraphA;
                    LineDataSet dataSetGraphB;
                    LineDataSet dataSetGraphC;

                    dataSetGraphA = lineData().getDataSetCount() == 0 ? null : (LineDataSet) lineData().getDataSetByIndex(0);
                    dataSetGraphB = lineData().getDataSetCount() == 0 ? null : (LineDataSet) lineData().getDataSetByIndex(1);
                    dataSetGraphC = lineData().getDataSetCount() == 0 ? null : (LineDataSet) lineData().getDataSetByIndex(2);


                    if (dataSetGraphA == null) {
                        dataSetGraphA = createSet(0);
                        lineData().addDataSet(dataSetGraphA);
                    }
                    if (dataSetGraphB == null) {
                        dataSetGraphB = createSet(1);
                        lineData().addDataSet(dataSetGraphB);
                    }
                    if (dataSetGraphC == null) {
                        dataSetGraphC = createSet(2);
                        lineData().addDataSet(dataSetGraphC);
                    }

                    lineData().addEntry(new Entry(dataSetGraphA.getEntryCount(), x, dataSetGraphA), 0);
                    lineData().addEntry(new Entry(dataSetGraphB.getEntryCount(), y, dataSetGraphB), 1);
                    lineData().addEntry(new Entry(dataSetGraphC.getEntryCount(), z, dataSetGraphB), 2);
                    lineData().notifyDataChanged();

            } else if (all == 2) {
                    LineDataSet dataSetGraphA;
                    LineDataSet dataSetGraphB;

                    dataSetGraphA =  lineData().getDataSetCount() == 0 ? null : (LineDataSet)  lineData().getDataSetByIndex(0);
                    dataSetGraphB =  lineData().getDataSetCount() == 0 ? null : (LineDataSet)  lineData().getDataSetByIndex(1);


                    if (dataSetGraphA == null) {
                        dataSetGraphA = createSet(0);
                        lineData().addDataSet(dataSetGraphA);
                    }
                    if (dataSetGraphB == null) {
                        dataSetGraphB = createSet(1);
                        lineData().addDataSet(dataSetGraphB);
                    }


                lineData().addEntry(new Entry(dataSetGraphA.getEntryCount(), x, dataSetGraphA), 0);
                lineData().addEntry(new Entry(dataSetGraphB.getEntryCount(), y, dataSetGraphB), 1);
                lineData().notifyDataChanged();

            } else  if (all == 1) {
                    LineDataSet dataSetGraphA;

                    dataSetGraphA =  lineData().getDataSetCount() == 0 ? null : (LineDataSet)  lineData().getDataSetByIndex(0);

                    if (dataSetGraphA == null) {
                        dataSetGraphA = createSet(0);
                        lineData().addDataSet(dataSetGraphA);
                    }

                lineData().addEntry(new Entry(dataSetGraphA.getEntryCount(), x, dataSetGraphA), 0);
                lineData().notifyDataChanged();
                }
            }
            onChartInvalidate();


    }


    private LineDataSet createSet(int temp) {
        String s = null;
        if (temp == 0) {
            s = "X Value";
        } else if (temp == 1) {
            s = "Y Value";
        } else if (temp == 2) {
            s = "Z Value";
        }

        LineDataSet set = new LineDataSet(null, s);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor(new ColorController().setColorToGraph(temp, whereFrom())));
//        set.setCircleColor(Color.BLUE);
        set.setLineWidth(2f);
//        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.parseColor(new ColorController().setColorToGraph(temp, whereFrom())));
//        set.setHighLightColor(Color.rgb(244, 117, 117));
//        set.setValueTextColor(Color.BLUE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    public void setFrom() {
        this.from = whereFrom();

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        registerReceiver();
    }

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_base, container, false);
//    }
    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(getActivity().getResources().getString(R.string.action_data_avail)));
//        model.onInitStateTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
            bigDecimal=null;
            bigDecimal1=null;
            bigDecimal2=null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract int whereFrom();
    public  abstract LineData lineData();

    public abstract void onChartInvalidate();


}
