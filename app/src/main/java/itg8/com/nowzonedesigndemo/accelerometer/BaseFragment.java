package itg8.com.nowzonedesigndemo.accelerometer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import itg8.com.nowzonedesigndemo.R;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.Random;

import itg8.com.nowzonedesigndemo.accelerometer.controller.AccController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.LoadCellController;
import itg8.com.nowzonedesigndemo.accelerometer.controller.SingleController;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public abstract class BaseFragment<T> extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public int from;
    private static final String TAG = "BaseFragment";
    private BigDecimal AXIS_Z, AXIS_Y, AXIS_X;
    private DataModelPressure model;
    public String title;


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

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(getString(R.string.action_data_avail))) {
                    if (intent.hasExtra(CommonMethod.MODEL)) {
                        model = intent.getParcelableExtra(CommonMethod.MODEL);
                        Log.d(TAG, "onReceive: " + new Gson().toJson(model));
                        if (whereFrom() == 1) {

                            AXIS_X = new BigDecimal(model.getX());
                            AXIS_Y = new BigDecimal(model.getY());
                            AXIS_Z = new BigDecimal(model.getZ());

                            if(getController()!=null)
                                ((AccController)getController()).setAccData(AXIS_X.floatValue(),AXIS_Y.floatValue(),AXIS_Z.floatValue());

                            title = "Accelerometer graph";

                        }

                        if (whereFrom() == 2) {
                            if (model.getgX() > 0 && model.getgY() > 0 && model.getgZ() > 0) {
                                AXIS_X = new BigDecimal(model.getgX());
                                AXIS_Y = new BigDecimal(model.getgY());
                                AXIS_Z = new BigDecimal(model.getgZ());
                                if(getController()!=null)
                                    ((AccController)getController()).setAccData(AXIS_X.floatValue(),AXIS_Y.floatValue(),AXIS_Z.floatValue());

                            }
                            title = "Gyrometer graph";

                        }

                        if (whereFrom() == 3) {
                            if (model.getmX() > 0 && model.getmY() > 0 && model.getmZ() > 0) {
                                AXIS_X = new BigDecimal(model.getmX());
                                AXIS_Y = new BigDecimal(model.getmY());
                                AXIS_Z = new BigDecimal(model.getmZ());
                                if(getController()!=null)
                                    ((AccController)getController()).setAccData(AXIS_X.floatValue(),AXIS_Y.floatValue(),AXIS_Z.floatValue());

                            }
                            title = "Magnetometer graph";
                        }

                        if (whereFrom() == 4) {
                                AXIS_X = new BigDecimal(model.getLoadCell1());
                                AXIS_Y = new BigDecimal(model.getLoadCell2());
                                if(getController()!=null)
                                    ((LoadCellController)getController()).setLoadData(AXIS_X.floatValue(), AXIS_Y.floatValue());

                            title = "Load Cell graph";
                        }

                        if (whereFrom() == 5) {
                            if (model.getMic() > 0L) {
                                AXIS_X = new BigDecimal(model.getMic());
                                if(getController()!=null){
                                    ((SingleController)getController()).setSingleData(AXIS_X.floatValue());
                                }
                            }
                            title = "MIC graph";
                        }

                        if (whereFrom() == 6) {
                            if (model.getOptical() > 0L) {
                                AXIS_X = new BigDecimal(model.getOptical());
                                if(getController()!=null){
                                    ((SingleController)getController()).setSingleData(AXIS_X.floatValue());
                                }
                            }
                            title = "Optical graph";
                        }

                        if (whereFrom() == 7) {
                            if (model.getTemprature() > 0) {
                                AXIS_X = new BigDecimal(model.getTemprature());

                                if(getController()!=null){
                                    ((SingleController)getController()).setSingleData(AXIS_X.floatValue());
                                }
                            }
                            title = "Temperature graph";
                        }


                        setTitle(title);
                    }
                }
            }
        }

    };




    public void setFrom() {
        this.from = whereFrom();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_base, container, false);
//    }
    public void registerReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(getActivity().getResources().getString(R.string.action_data_avail)));
//        model.onInitStateTime();
    }



    public abstract int whereFrom();

    public abstract LineData lineData();

    public abstract void setTitle(String title);


    public abstract T getController();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
