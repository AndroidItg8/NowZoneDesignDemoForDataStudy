package itg8.com.nowzonedesigndemo.breath_history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.fragment.BreathsHistoryFragment;
import itg8.com.nowzonedesigndemo.breath_history.fragment.StatisticsDashboardFragment;
import itg8.com.nowzonedesigndemo.breath_history.mvp.StatisticMVP;
import itg8.com.nowzonedesigndemo.breath_history.mvp.StatisticModuleImp;
import itg8.com.nowzonedesigndemo.breath_history.mvp.StatisticPresenterImp;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.sleep.widget_custom_progressbar.ProgressItem;
import itg8.com.nowzonedesigndemo.steps.TodayStepsFragment;
import itg8.com.nowzonedesigndemo.utility.ActivityState;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.FilterUtility;
import itg8.com.nowzonedesigndemo.utility.Helper;
import itg8.com.nowzonedesigndemo.widget.AnimationUtils;


public class BreathsHistoryActivity extends AppCompatActivity implements View.OnClickListener,
        AppBarLayout.OnOffsetChangedListener,
        CommonMethod.OnFragmentSendToActivityListener,
        StatisticMVP.StatisticView {

    private static final String TAG = BreathsHistoryActivity.class.getSimpleName();
    static Random random = new Random();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.switch_notify)
    ToggleButton switchNotify;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    //    @BindView(R.id.lbl_breath)
//    CustomFontTextView lblBreath;
//    @BindView(R.id.txt_breath_value)
//    CustomFontTextView txtBreathValue;
//    @BindView(R.id.ll_breath)
//    LinearLayout llBreath;
//    @BindView(R.id.lbl_activity)
//    CustomFontTextView lblActivity;
//    @BindView(R.id.custom_progressbar_activity)
//    CustomProgressBarHistory customProgressbarActivity;
//    @BindView(R.id.txt_activity_value)
//    CustomFontTextView txtActivityValue;
//    @BindView(R.id.ll_activity)
//    LinearLayout llActivity;
//    @BindView(R.id.lbl_posture)
//    CustomFontTextView lblPosture;
//    @BindView(R.id.custom_progressbar_posture)
//    CustomProgressBarHistory customProgressbarPosture;
//    @BindView(R.id.txt_posture_value)
//    CustomFontTextView txtPostureValue;
//    @BindView(R.id.custom_progressbar_breath)
//    CustomProgressBarHistory customProgressbarBreath;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;
//    @BindView(R.id.lbl_breath_time)
//    CustomFontTextView lblBreathTime;
//    @BindView(R.id.lbl_activity_time)
//    CustomFontTextView lblActivityTime;
//
//    @BindView(R.id.ll_posture)
//    LinearLayout llPosture;
//    @BindView(R.id.collapsing)
//    CollapsingToolbarLayout collapsing;
//    @BindView(R.id.lbl_compose_state)
//    CustomFontTextView lblComposeState;
//    @BindView(R.id.txt_compose_value)
//    CustomFontTextView txtComposeValue;
//    @BindView(R.id.lbl_compose_time)
//    CustomFontTextView lblComposeTime;
//    @BindView(R.id.card_composed)
//    CardView cardAvgCalories;
//    @BindView(R.id.lbl_stress_state)
//    CustomFontTextView lblStressState;
//    @BindView(R.id.txt_stress_value)
//    CustomFontTextView txtStressValue;
//    @BindView(R.id.lbl_stress_time)
//    CustomFontTextView lblStressTime;
//    @BindView(R.id.card)
//    CardView card;
//    @BindView(R.id.ll_main)
//    LinearLayout llMain;
//    @BindView(R.id.lbl_normal_state)
//    CustomFontTextView lblNormalState;
//    @BindView(R.id.txt_normal_value)
//    CustomFontTextView txtNormalValue;
//    @BindView(R.id.lbl_normal_state_time)
//    CustomFontTextView lblNormalStateTime;
//    @BindView(R.id.card_normal)
//    CardView cardNormal;
//    @BindView(R.id.lbl_focus_state)
//    CustomFontTextView lblFocusState;
//    @BindView(R.id.txt_focus_value)
//    CustomFontTextView txtFocusValue;
//    @BindView(R.id.lbl_focus_state_time)
//    CustomFontTextView lblFocusStateTime;
//    @BindView(R.id.card_focus)
//    CardView cardFocus;
//    @BindView(R.id.frameLayout)
//    FrameLayout frameLayout;
//    @BindView(R.id.rl_composed)
//    RelativeLayout rlComposed;
//    @BindView(R.id.rl_attentive)
//    RelativeLayout rlAttentive;
//    @BindView(R.id.rl_stress)
//    RelativeLayout rlStress;
//    @BindView(R.id.rl_normal)
//    RelativeLayout rlNormal;
//    @BindView(R.id.nestedScrollview)
//    NestedScrollView nestedScrollview;
//    @BindView(R.id.appbar)
//    AppBarLayout appbar;
//    @BindView(R.id.card_breath)
//    CardView cardBreath;
//    @BindView(R.id.card_activitys)
//    CardView cardActivitys;
//    @BindView(R.id.card_postures)
//    CardView cardPostures;
//    @BindView(R.id.rl_breath_details)
//    RelativeLayout rlBreathDetails;
//    @BindView(R.id.lbl_activity_state)
//    CustomFontTextView lblActivityState;
//    @BindView(R.id.rl_activity)
//    RelativeLayout rlActivity;
//    @BindView(R.id.lbl_target_state)
//    CustomFontTextView lblTargetState;
//    @BindView(R.id.txt_target_value)
//    CustomFontTextView txtTargetValue;
//    @BindView(R.id.lbl_target_time)
//    CustomFontTextView lblTargetTime;
//    @BindView(R.id.rl_target)
//    RelativeLayout rlTarget;
//    @BindView(R.id.card_target)
//    CardView cardTarget;
//    @BindView(R.id.rl_activity_details)
//    RelativeLayout rlActivityDetails;
//    @BindView(R.id.lbl_posture_state)
//    CustomFontTextView lblPostureState;
//    @BindView(R.id.lbl_posture_time)
//    CustomFontTextView lblPostureTime;
//    @BindView(R.id.rl_posture)
//    RelativeLayout rlPosture;
//    @BindView(R.id.lbl_bad_posture_state)
//    CustomFontTextView lblBadPostureState;
//    @BindView(R.id.txt_bad_posture_value)
//    CustomFontTextView txtBadPostureValue;
//    @BindView(R.id.lbl_bad_posture_time)
//    CustomFontTextView lblBadPostureTime;
//    @BindView(R.id.rl_bad_posture)
//    RelativeLayout rlBadPosture;
//    @BindView(R.id.card_bad_posture)
//    CardView cardBadPosture;
//    @BindView(R.id.rl_posture_details)
//    RelativeLayout rlPostureDetails;
//    @BindView(R.id.rl_main)
//    RelativeLayout rlMain;
    private Fragment fragment;
    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;
    private List<TblState> listState = new ArrayList();
    private List<TblStepCount> listActivityState = new ArrayList();
    private BreathState type;
    private int rainge;
    private ActivityState typeStep;
    private View alphaOne;
    private View alphaTwo;
    private FragmentManager fm;
    private boolean isChecked = false;
    private DashboardListener listener;
    private StatisticMVP.StatisticPresenter presenter;
    private HorizontalCalendar horizontalCalendar;

    private Date date;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (date != null)
                presenter.onStartGettingDataFromData(date);
        }
    };
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breaths_history);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new StatisticPresenterImp(this);
        handler = new Handler();

        prepareData();
//        initHorizontalCalander(null,null);


//        rlAttentive.setOnClickListener(this);
//        rlComposed.setOnClickListener(this);
//        rlNormal.setOnClickListener(this);
//        rlStress.setOnClickListener(this);
//        customProgressbarBreath.getThumb().mutate().setAlpha(0);
//        customProgressbarPosture.getThumb().mutate().setAlpha(0);
//        customProgressbarActivity.getThumb().mutate().setAlpha(0);
//        initDataToSeekbarBreath();
//        initDataToSeekbarActivity();
//        initDataToSeekbarPosture();
//        prepareData();
//        preparePostureData();
        setFragmentGridLayout();


    }

    private void showToggle() {
        switchNotify.setVisibility(View.VISIBLE);
    }

    private void hideToggle() {
        switchNotify.setVisibility(View.GONE);
    }

//

    private void setFragmentGridLayout() {
        fragment = StatisticsDashboardFragment.newInstance("", " ");
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }


    private void prepareActivityData() {
        Calendar calendar = Calendar.getInstance();
        TblStepCount tblStepCount = null;
        tblStepCount = new TblStepCount();
        tblStepCount.setSteps(500);
        tblStepCount.setGoal(1500);
        tblStepCount.setCalBurn(150);
        tblStepCount.setDate(calendar.getTime());
        Log.d(TAG, "Tblle Count:" + "Steps:" + tblStepCount.getSteps() + "Goal:" + tblStepCount.getGoal() + "Burn:" + tblStepCount.getCalBurn());
        calculateRemainStep(tblStepCount);


        fragment = TodayStepsFragment.newInstance(tblStepCount);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private void calculateRemainStep(TblStepCount tblStepCount) {
//         txtTargetValue.setText("1500");
//         int diff = tblStepCount.getGoal() - tblStepCount.getSteps();
//        txtActivityValue.setText("1000");

    }

    private int randomNumber() {
        Random rand = new Random();
        int minimum = 100;
        int maximum = 2000;
        int randomNum = minimum + rand.nextInt((maximum - minimum) + 100);
        return randomNum;

    }

    @Override
    public void onBackPressed() {
        //   fragment = fm.findFragmentByTag(TodaysStacDetailsFragment.class.getSimpleName());
        if (fragment == null) {
            Log.d(TAG, "OnBackPress Fragment Null:");
            return;
        }
        if (fragment instanceof AnimationUtils.Dismissable)
            ((AnimationUtils.Dismissable) fragment).dismiss(new AnimationUtils.Dismissable.OnDismissedListener() {
                @Override
                public void onDismissed() {
//                getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();

                    fragment = new StatisticsDashboardFragment();
                    onBackPressed();
                }
            });
        else
            super.onBackPressed();


    }

    private void initHorizontalCalander(Date startDate, Date endDateFrom) {
//        appbar.addOnOffsetChangedListener(this);
//        cardActivitys.setOnClickListener(this);
//        cardBreath.setOnClickListener(this);
//        cardPostures.setOnClickListener(this);
//        alphaOne = llActivity;
//        alphaTwo = llPosture;
//        /** end after 1 month from now */
//        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.MONTH, 1);
//
///** start before 1 month from now */
        Calendar endDate = Calendar.getInstance();
//        startDate.add(Calendar.MONTH, -1);
        if (startDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            startDate = calendar.getTime();
        }

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate)
                .endDate(endDate.getTime())
                .textColor(Color.LTGRAY, Color.BLACK)    // Text color for none selected Dates, Text color for selected Date.
                .selectedDateBackground(Color.TRANSPARENT)  // Background color of the selected date cell.
                .selectorColor(Color.BLACK)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Log.d(TAG, String.valueOf(position));
                BreathsHistoryActivity.this.date = date;
                handler.removeCallbacks(r);
                handler.post(r);
            }
        });
        // horizontalCalendar.goToday(true);

    }


    private void setFragment(List<TblState> filterList) {
        fragment = BreathsHistoryFragment.newInstance(type, filterList);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }


    private void initDataToSeekbarBreath() {
        progressItemList = new ArrayList<ProgressItem>();

        // green span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 20;
        mProgressItem.color = R.color.color_composed;
        progressItemList.add(mProgressItem);


        // red span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 20;
        Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
        mProgressItem.color = R.color.color_attentive;
        progressItemList.add(mProgressItem);


        // blue span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 30;
        mProgressItem.color = R.color.color_stress;
        progressItemList.add(mProgressItem);
//        customProgressbarBreath.initData(progressItemList);
//        customProgressbarBreath.invalidate();
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 40;
        mProgressItem.color = R.color.color_normal;
        progressItemList.add(mProgressItem);
//        customProgressbarBreath.initData(progressItemList);
//        customProgressbarBreath.invalidate();
    }

    private void initDataToSeekbarActivity() {
        progressItemList = new ArrayList<ProgressItem>();

        // green span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 70;
        mProgressItem.color = R.color.color_steps;
        progressItemList.add(mProgressItem);

        // red span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 30;
        Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
        mProgressItem.color = R.color.color_normal;
        progressItemList.add(mProgressItem);
//
//        customProgressbarActivity.initData(progressItemList);
//        customProgressbarActivity.invalidate();
    }

    private void initDataToSeekbarPosture() {
        progressItemList = new ArrayList<ProgressItem>();

        // green span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 30;
        mProgressItem.color = R.color.color_posture;
        progressItemList.add(mProgressItem);

        // red span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 70;
        Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
        mProgressItem.color = R.color.color_normal;
        progressItemList.add(mProgressItem);
//        customProgressbarPosture.initData(progressItemList);
//        customProgressbarPosture.invalidate();
    }

    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        TblState tblState = null;
        for (int i = 0; i < 20; i++) {
            tblState = new TblState();
            tblState.setState(randomEnum(BreathState.class).name());
            tblState.setTimestampStart(calendar.getTimeInMillis());
            calendar.add(Calendar.MINUTE, 10);
            tblState.setTimestampEnd(calendar.getTimeInMillis());
            listState.add(tblState);
        }
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> tClass) {
        Random random = new Random();
        int x = random.nextInt(tClass.getEnumConstants().length);
        return tClass.getEnumConstants()[x];
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_audio_player, menu);
        //MenuItem checkable = menu.findItem(R.id.action_toggle);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        List<TblState> filterList = null;

        switch (v.getId()) {
//            case R.id.rl_composed:
//                type = BreathState.CALM;
//                setFragment(filterList);
//                break;
//            case R.id.rl_attentive:
//                type = BreathState.FOCUSED;
//                filterList = new FilterUtility.FilterBuilder().createBuilder(listState).setFilter(type).build().getFilteredList();
//                setFragment(filterList);
//                break;
//            case R.id.rl_stress:
//                type = BreathState.STRESS;
//                filterList = new FilterUtility.FilterBuilder().createBuilder(listState).setFilter(type).build().getFilteredList();
//                setFragment(filterList);
//                break;
//            case R.id.rl_normal:
//                type = BreathState.UNKNOWN;
//                filterList = new FilterUtility.FilterBuilder().createBuilder(listState).setFilter(type).build().getFilteredList();
//                setFragment(filterList);
//                break;
//            case R.id.card_postures:
//                setPostureVisibility();
//                break;
//            case R.id.card_activitys:
//                setStepVisibility();
//                break;
//            case R.id.card_breath:
//                setBreathVisibility();
//                break;
//            case R.id.rl_activity:
//                 typeStep =ActivityState.STEP;
//                break;
//            case R.id.rl_target:
//                typeStep = ActivityState.GOAL;
//                break;
        }

    }

    private void setPostureVisibility() {
//        rlPostureDetails.setVisibility(View.VISIBLE);
//        rlActivityDetails.setVisibility(View.GONE);
//        rlBreathDetails.setVisibility(View.GONE);
//        alphaOne = llActivity;
//        alphaTwo = llBreath;

    }

    private void setBreathVisibility() {
//        rlActivityDetails.setVisibility(View.GONE);
//        rlPostureDetails.setVisibility(View.GONE);
//        rlBreathDetails.setVisibility(View.VISIBLE);
//        alphaOne = llActivity;
//        alphaTwo = llPosture;
//        setFragment(listState);

    }

    private void setStepVisibility() {
//        rlActivityDetails.setVisibility(View.VISIBLE);
//        rlPostureDetails.setVisibility(View.GONE);
//        rlBreathDetails.setVisibility(View.GONE);
//        alphaOne = llPosture;
//        alphaTwo = llBreath;
//        prepareActivityData();


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        OffsetChange(appBarLayout, verticalOffset);
    }

    private void OffsetChange(AppBarLayout appBarLayout, int verticalOffset) {
        rainge = appBarLayout.getHeight() - toolbar.getHeight();
        if (rainge > 0) {
            double alpha = (double) (verticalOffset) / -rainge;
            if (alpha > 0) {
                alphaOne.setAlpha(1 - (float) alpha);
                alphaTwo.setAlpha(1 - (float) alpha);

                if (alphaOne.getAlpha() <= 0.2) {
                    alphaOne.setVisibility(View.GONE);
                } else {
                    alphaOne.setVisibility(View.VISIBLE);
                }
                if (alphaTwo.getAlpha() <= 0.2) {
                    alphaTwo.setVisibility(View.GONE);
                } else {
                    alphaTwo.setVisibility(View.VISIBLE);
                }

            }
        }


    }

    @Override
    public void onBackFragmentSendListener(Fragment fragment) {
        this.fragment = fragment;
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
        fm.executePendingTransactions();
    }

    @Override
    public void onShowToggle() {
        showToggle();
    }

    @Override
    public void onHideToggle() {
        hideToggle();
    }

    @Override
    public void onChangeToolbarColor(Intent intent, BreathState type, Bundle sharedView) {
        intent.putExtra(CommonMethod.COLOR, type);
        startActivity(intent, sharedView);
    }

    @Override
    public void onSingleDetail(Intent intent, TblState tblState, Bundle sharedView) {

    }

    private List<? extends Parcelable> getListFromType(BreathState type) {
        List<TblState> filterList = new FilterUtility.FilterBuilder().createBuilder(listState).setFilter(type).build().getFilteredList();

        return filterList;
    }


    @Override
    public void onComposedMinutesAvailable(StatisticModuleImp.BreathItem minutes) {
        listener.onComposeMinutes(minutes);
    }

    @Override
    public void onStressMinutesAvailable(StatisticModuleImp.BreathItem minutes) {
        listener.onStressMinutes(minutes);
    }

    @Override
    public void onAttentiveMinutesAvailable(StatisticModuleImp.BreathItem minutes) {
        listener.onAttentiveMinutes(minutes);
    }

    @Override
    public void onDateRangeAvail(Date[] dates) {
        initHorizontalCalander(dates[0], dates[1]);
    }

    @Override
    public void onStepsDataAvail(StatisticModuleImp.StepsItem steps) {
        listener.onStepsMinutes(steps);
    }


    @Override
    public void onPostureDataAvail(StatisticModuleImp.PostureItem postureItem) {
        listener.onPostureDataAvail(postureItem);
    }

    public void setDashboardListener(DashboardListener listener) {
        presenter.onStartGettingDataFromData(Calendar.getInstance().getTime());
        this.listener = listener;
    }

    public interface DashboardListener {
        void onComposeMinutes(StatisticModuleImp.BreathItem item);

        void onStressMinutes(StatisticModuleImp.BreathItem item);

        void onAttentiveMinutes(StatisticModuleImp.BreathItem item);

        void onStepsMinutes(StatisticModuleImp.StepsItem steps);

        void onPostureDataAvail(StatisticModuleImp.PostureItem postureItem);
    }
}
