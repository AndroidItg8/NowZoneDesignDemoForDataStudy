package itg8.com.nowzonedesigndemo.home.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.home.HomeActivity;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.widget.steps.CustomStepImage;
import itg8.com.nowzonedesigndemo.widget.wave.BreathwaveView;

//import itg8.com.nowzonedesigndemo.widget.bottomnavigation.BottomNavigationItem;
//import itg8.com.nowzonedesigndemo.widget.bottomnavigation.BottomNavigationView;
//import itg8.com.nowzonedesigndemo.widget.bottomnavigation.OnBottomNavigationItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, HomeActivity.HomeFragmentInteractor {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "HomeFragment";

    @BindView(R.id.breathview)
    BreathwaveView breathview;
    @BindView(R.id.frameLayout_home)
    FrameLayout frameLayoutHome;
    @BindView(R.id.image)
    CustomStepImage mStepImage;
    @BindView(R.id.txt_device_not_attached)
    CustomFontTextView txtDeviceNotAttached;
    @BindView(R.id.rl_wave)
    FrameLayout rlWave;
    @BindView(R.id.img_breath)
    ImageView imgBreath;
    @BindView(R.id.txt_breathRate)
    TextView txtBreathRate;
    @BindView(R.id.txt_statusValue)
    TextView txtStatusValue;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.breathValue)
    TextView breathValue;
    @BindView(R.id.txt_minute)
    TextView txtMinute;
    @BindView(R.id.rl_breath)
    RelativeLayout rlBreath;
    @BindView(R.id.txt_calm)
    TextView txtCalm;
    @BindView(R.id.txt_calm_value)
    TextView txtCalmValue;
    @BindView(R.id.txt_focus)
    CustomFontTextView txtFocus;
    @BindView(R.id.txt_focus_value)
    CustomFontTextView txtFocusValue;
    @BindView(R.id.txt_stress)
    TextView txtStress;
    @BindView(R.id.txt_stress_value)
    TextView txtStressValue;
    @BindView(R.id.main_FrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.rl_main_top)
    RelativeLayout rlMainTop;
    @BindView(R.id.txtCount)
    CustomFontTextView txtCount;
    @BindView(R.id.txtAverage)
    CustomFontTextView txtAverage;
    @BindView(R.id.txt_state_value)
    CustomFontTextView txtStateValue;
    @BindView(R.id.imgState)
    ImageView imgState;
    @BindView(R.id.txtConnectionstatus)
    TextView txtConnectionstatus;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentManager fm;
    private Unbinder unbinder;
    private String countString;
    private boolean isViewEnable=false;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        isViewEnable=true;
        breathview.setOnClickListener(this);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewEnable=false;
        if((HomeActivity)getActivity()!=null)
            ((HomeActivity)getActivity()).destroyHomeListener();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((HomeActivity) context).setHomeListener(this);
    }

    @Override
    public void onClick(View v) {
//        if(v.getId()==R.id.breathview)
//            //callShareWhatsapp();
//        else
            fm = getActivity().getSupportFragmentManager();
    }



    @Override
    public void onBreathingWaveValueAvail(double value) {
        Log.d(TAG, "onBreathingWaveValueAvail: "+value);
        if(breathview!=null && !TextUtils.isEmpty(String.valueOf(value)))
            breathview.addSample(SystemClock.elapsedRealtime(), value);

    }


    @Override
    public void onMovementStarted() {
        if(isViewEnable) {
            mStepImage.startSteps();
//            mStepImage.stopSteps();
            breathview.setVisibility(View.GONE);
            mStepImage.setVisibility(View.VISIBLE);
            txtCount.setVisibility(View.GONE);
        }
    }


    @Override
    public void onMovementStopped() {
        if(isViewEnable) {
            mStepImage.stopSteps();
            breathview.setVisibility(View.VISIBLE);
            mStepImage.setVisibility(View.GONE);
            txtCount.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDeviceNotAttachedToBody() {
        if(isViewEnable) {
            txtDeviceNotAttached.setVisibility(View.VISIBLE);
            txtConnectionstatus.setText("");
        }
    }

    @Override
    public void ondeviceAttached() {
        if(isViewEnable) {
            txtDeviceNotAttached.setVisibility(View.GONE);
            txtConnectionstatus.setText("");
        }

    }

    @Override
    public void onAvgValueAvailable(int count) {
//        txtAvgBreathValue.setVisibility(View.VISIBLE);
//        txtAvgBreathValue.setText(String.valueOf(mAvgCount));
    }

    @Override
    public void onConnectionStateAvail(String name) {
        if(isViewEnable) {
            if (txtConnectionstatus != null)
                txtConnectionstatus.setText(name);
        }
    }

    @Override
    public void onBatteryCountAvail(int batteryCount) {

    }

    @Override
    public void onBreathingStateAvail(String state, int icon) {
        if(isViewEnable) {
            if (icon != 0) {
                imgState.setImageResource(icon);
                imgState.setVisibility(View.VISIBLE);
            } else {
                imgState.setVisibility(View.GONE);
            }if (state != null) {
                txtStateValue.setText(state);
                txtStateValue.setVisibility(View.VISIBLE);
            } else {
                txtStateValue.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBreathingCountAvail(int count) {
        if(isViewEnable) {
            countString = count + " BPM";
            txtCount.setText(countString);
            if (Prefs.getInt(CommonMethod.USER_CURRENT_AVG) != 0) {
                String average = "Average " + Prefs.getInt(CommonMethod.USER_CURRENT_AVG) + " bpm";
                txtAverage.setText(average);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        if (breathview != null)
                breathview.reset();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
