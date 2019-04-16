package itg8.com.nowzonedesigndemo.posture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.adapter.BreathsHistoryAdapter;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.sleep.widget_custom_progressbar.DonutProgress;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.FilterUtility;

public class PostureCalibrationActivity extends AppCompatActivity implements  BreathsHistoryAdapter.OnItemHistoryClickedListener, View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fab_calibrate)
    FloatingActionButton fabCalibrate;
    @BindView(R.id.cardView_bottom)
    CardView bottomSheet;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.img_icon)
    ImageView imgIcon;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_bottom_parent)
    RelativeLayout rlBottomParent;
    //    @BindView(R.id.title)
//    CustomFontTextView title;

    @BindView(R.id.img_posture)
    ImageView imgPosture;
    @BindView(R.id.circularProgressGoal)
    DonutProgress circularProgressGoal;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.txt_time)
    CustomFontTextView txtTime;
    @BindView(R.id.lbl_time_value)
    CustomFontTextView lblTimeValue;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.lbl_calibration)
    TextView lblCalibration;
    private List<TblState> listState= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture_calibration);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prepareData();
         init();
        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void init() {
        float px =pxFromDp(getApplicationContext(),180);

        View bottomSheet = coordinator.findViewById(R.id.rl_bottom_parent);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d("onStateChanged", "onStateChanged:" + newState);
               if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                   fabCalibrate.setVisibility(View.GONE);

                   Log.d("onStateChanged", "onStateChanged Expand:" + newState);


               } else {
                   fabCalibrate.setVisibility(View.VISIBLE);
                   Log.d("onStateChanged", "onStateChanged Collapsed:" + newState);

               }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
                Log.e("onSlide", "onSlide");
            }
        });

        circularProgressGoal.setUnfinishedStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.color_steps_half));
        circularProgressGoal.setFinishedStrokeColor((ContextCompat.getColor(getApplicationContext(), R.color.color_posture_half)));
        circularProgressGoal.setUnfinishedStrokeWidth(12.0f);
        circularProgressGoal.setFinishedStrokeWidth(15.0f);
        circularProgressGoal.setStartingDegree(270);
        circularProgressGoal.setMax(100);
        circularProgressGoal.setProgress(70);

            // cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_sleep));
            appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_posture_half));
            ImageView img = (ImageView) findViewById(R.id.img_icon);
            img.setTransitionName(getString(R.string.activity_image_trans));
            img.setBackground(ContextCompat.getDrawable(this, R.drawable.posture_icon));
//            toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorWhite));
        toolbarLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.color_posture_half));
        toolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this, R.color.color_posture_half));
        toolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM | Gravity.CENTER);
        toolbarLayout.setTitle(getString(R.string.posture));

     //   toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        fabCalibrate.setOnClickListener(this);
         appBar.addOnOffsetChangedListener(this);



    }

    private void setRecyclerView(List<TblState> filterList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new BreathsHistoryAdapter(getApplicationContext(), filterList, this));

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


        getListFromType(BreathState.POSTURE);
    }
    private static <T extends Enum<?>> T randomEnum(Class<T> tClass) {
        Random random = new Random();
        int x = random.nextInt(tClass.getEnumConstants().length);
        return tClass.getEnumConstants()[x];
    }
    private void getListFromType(BreathState type) {
        List<TblState> filterList = new FilterUtility.FilterBuilder().createBuilder(listState).setFilter(type).build().getFilteredList();
        setRecyclerView(filterList);

    }

    @Override
    public void onItemClicked(View view, int position, TblState listState) {
        if(listState.getState().equalsIgnoreCase(BreathState.POSTURE.name()))
        {

        }

    }

    @Override
    public void onStepItemClicked(View itemView, int adapterPosition, TblStepCount tblStepCount) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.fab_calibrate:
                openCalibrateActivty();
                break;
        }


    }

    private void openCalibrateActivty() {
         startActivity(new Intent(getApplicationContext(),PostureCalibrateSettingActivity.class));
         //startActivity(new Intent(getApplicationContext(),Main2Activity.class));




    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
      //  Log.d(TAG, "onStateChanged Collapsed:" + verticalOffset);

    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
