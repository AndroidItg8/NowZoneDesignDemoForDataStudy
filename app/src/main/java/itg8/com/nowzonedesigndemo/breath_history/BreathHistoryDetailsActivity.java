package itg8.com.nowzonedesigndemo.breath_history;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.fragment.TodaysStacDetailsFragment;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.utility.BreathState;

public class BreathHistoryDetailsActivity extends AppCompatActivity implements CommonMethod.OnFragmentSendToActivityListener {

    private static final String TAG = BreathHistoryDetailsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @BindView(R.id.switch_notify)
    ToggleButton switchNotify;

    @BindView(R.id.img_breath)
    ImageView imgBreath;
    private BreathState type;
    private List<TblState> listState;
    private ArrayList<TblStepCount> listSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_history_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();


    }

    private void showToggle() {
        switchNotify.setVisibility(View.VISIBLE);

    }

    private void hideToggle() {
        switchNotify.setVisibility(View.GONE);
    }

    private void init() {
        String titile = null;

        View img = findViewById(R.id.img_breath);
        img.setTransitionName(getString(R.string.activity_image_trans));
        if (getIntent().hasExtra(CommonMethod.BREATH)) {
            listState = getIntent().getParcelableArrayListExtra(CommonMethod.BREATH);
            setFragment(CommonMethod.BREATH);
        }
        if(getIntent().hasExtra(CommonMethod.STEPS)){
            listSteps=getIntent().getParcelableArrayListExtra(CommonMethod.STEPS);
            setFragment(CommonMethod.STEPS);
        }
        if (getIntent().getSerializableExtra(CommonMethod.COLOR) != null) {
            type = (BreathState) getIntent().getSerializableExtra(CommonMethod.COLOR);
        }
        int bgColor = 0;
        switch (type) {
            case FOCUSED:
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_attentive_half));
                img.setBackground(ContextCompat.getDrawable(this, R.drawable.focus_streak_icon));
                titile = getString(R.string.focus_state);
                bgColor = R.color.color_attentive_half;

                break;
            case CALM:
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_composed_half));
                img.setBackground(ContextCompat.getDrawable(this, R.drawable.calm_streak_icon));
                titile = getString(R.string.compose_state);
                bgColor = R.color.color_composed_half;

                break;
            case STRESS:
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_stress_half));
                img.setBackground(ContextCompat.getDrawable(this, R.drawable.stress_streak_icon));
                bgColor = R.color.color_stress_half;

                titile = getString(R.string.stress_state);
                break;
            case SEDENTARY:
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_normal_half));
                img.setBackground(ContextCompat.getDrawable(this, R.drawable.sedentory_icon));
                titile = getString(R.string.silent_state);
                bgColor = R.color.color_normal_half;
                break;
            case ACTIVITY:
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_steps_half));
                img.setBackground(ContextCompat.getDrawable(this, R.drawable.activity_icon));
                titile = getString(R.string.activity);
                bgColor = R.color.color_steps_half;
                break;
        }
        Log.d(TAG, "Title:" + titile);
        if (titile != null) {
            toolbarLayout.setTitle(titile);
        }

        toolbarLayout.setContentScrimColor(ContextCompat.getColor(this,bgColor));
        toolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this,bgColor));

    }

    private void setFragment(String breath) {
        if(breath.equalsIgnoreCase(CommonMethod.BREATH))
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, TodaysStacDetailsFragment.newInstance(0, listState), TodaysStacDetailsFragment.class.getSimpleName()).commit();
        else if(breath.equalsIgnoreCase(CommonMethod.STEPS))
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, TodaysStacDetailsFragment.newInstance(0, BreathState.ACTIVITY, listSteps), TodaysStacDetailsFragment.class.getSimpleName()).commit();

    }


    @Override
    public void onBackFragmentSendListener(Fragment fragment) {

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





    }

    @Override
    public void onSingleDetail(Intent intent, TblState tblState, Bundle sharedView) {
       intent.putExtra(CommonMethod.STATE ,tblState);
        startActivity(intent,sharedView);
    }

    private void getListFromType(BreathState type) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                             onBackPressed();
                             break;
            default:
        }
        return super.onOptionsItemSelected(item);

    }
}
