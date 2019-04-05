package itg8.com.nowzonedesigndemo.steps;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayStepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayStepsFragment extends Fragment implements StepFragmentCommunicator {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = TodayStepsFragment.class.getSimpleName();


    Unbinder unbinder;
    @BindView(R.id.txt_calories)
    CustomFontTextView txtCalories;
    @BindView(R.id.customProgressRectangle)
    ProgressBar customProgressRectangle;
    @BindView(R.id.rl_releative)
    RelativeLayout rlReleative;
    @BindView(R.id.card_avg_calories)
    CardView cardAvgCalories;
    @BindView(R.id.ll_burn)
    LinearLayout llBurn;
    @BindView(R.id.txt_caloriesText)
    CustomFontTextView txtCaloriesText;
    @BindView(R.id.txt)
    CustomFontTextView txtGoal;
    @BindView(R.id.ll_calories)
    LinearLayout llCalories;
    @BindView(R.id.card_calories)
    CardView cardCalories;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.customFontTextView)
    CustomFontTextView customFontTextView;
    @BindView(R.id.card_miles)
    CardView cardMiles;
    @BindView(R.id.card_total_calories)
    CardView cardTotalCalories;
    @BindView(R.id.rl_step_value)
    RelativeLayout rlStepValue;
    @BindView(R.id.txtStepComplete)
    CustomFontTextView txtStepComplete;
    @BindView(R.id.txtWeekTotal)
    CustomFontTextView txtWeekTotal;
    @BindView(R.id.customFontTextView2)
    CustomFontTextView customFontTextView2;
    @BindView(R.id.txt_daliy_avg)
    CustomFontTextView txtDaliyAvg;


    // TODO: Rename and change types of parameters
    private TblStepCount  mParam1;
    private String mParam2;
    private boolean mShowUnit = true;
    private Context mContext;


    public TodayStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
         this.mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        //((StepsActivity) getActivity()).removeTodaysListener();
        if(mContext!= null)
            mContext= null;
        super.onDetach();
    }

    // TODO: Rename and change types and number of parameters
    public static TodayStepsFragment newInstance(TblStepCount param1) {
        TodayStepsFragment fragment = new TodayStepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_steps, container, false);
        unbinder = ButterKnife.bind(this, view);
       // ((StepsActivity) getActivity()).setListener(this);
         prepareData();

//        setProgressbar((int) stepsToCover);
        rlStepValue.setVisibility(View.VISIBLE);
        return view;
    }

    private void prepareData() {
        txtGoal.setText(String.valueOf(mParam1.getGoal()));
        txtStepComplete.setText(String.valueOf(mParam1.getSteps()));
//        txtWeekTotal.setText(String.valueOf(weekTotal));
//        txtDaliyAvg.setText(String.valueOf(steps));
        String calBurnText="";
//                = new DecimalFormat("#.##").format(calBurn) + " Calories burned today";
        float stepsToCover=((float) ((float)mParam1.getSteps()/(float)mParam1.getGoal())*100.0f);
        setProgressbar((int)stepsToCover);
        txtCalories.setText(calBurnText);

    }

    private void setProgressbar(int steps) {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.custom_progressbar);
        customProgressRectangle.setProgress(steps);
        customProgressRectangle.setSecondaryProgress(steps);
        customProgressRectangle.setMax(100);
        customProgressRectangle.setProgressDrawable(drawable);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onTodaysDataReceived(int goal, int steps, int weekTotal, double calBurn) {
        txtGoal.setText(String.valueOf(goal));
        txtStepComplete.setText(String.valueOf(steps));
        txtWeekTotal.setText(String.valueOf(weekTotal));
        txtDaliyAvg.setText(String.valueOf(steps));
        String calBurnText="";
//                = new DecimalFormat("#.##").format(calBurn) + " Calories burned today";
        float stepsToCover=((float) ((float)mParam1.getSteps()/(float)mParam1.getGoal())*100.0f);
        setProgressbar((int)stepsToCover);
        txtCalories.setText(calBurnText);
    }


}
