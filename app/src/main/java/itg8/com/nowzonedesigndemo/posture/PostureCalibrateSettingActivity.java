package itg8.com.nowzonedesigndemo.posture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

import static itg8.com.nowzonedesigndemo.common.CommonMethod.calculate;
import static itg8.com.nowzonedesigndemo.home.StepMovingActivity.AXIS_Y;

public class PostureCalibrateSettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_posture)
    ImageView imgPosture;
    @BindView(R.id.btn_calibrate)
    CustomFontTextView btnCalibrate;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.lbl_calibrate)
    CustomFontTextView lblCalibrate;
    @BindView(R.id.line1)
    CustomFontTextView line1;
    @BindView(R.id.line2)
    CustomFontTextView line2;
    @BindView(R.id.customFontTextView3)
    CustomFontTextView customFontTextView3;
    private float accelY;
    private String TAG=PostureCalibrateSettingActivity.class.getSimpleName();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            accelY = intent.getFloatExtra(AXIS_Y, 0);
            Log.d(TAG,"POSTURERESULT "+accelY);

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(CommonMethod.ACTION_AXIS_ACCEL));

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorToast() {
        Toast.makeText(this, "Fail to calibrate result", Toast.LENGTH_SHORT).show();
    }

    private void showToast() {
        Toast.makeText(this, "Calibrated Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture_calibrate_setting);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fabPause = findViewById(R.id.fabPause);
        fabPause.setOnClickListener(view -> Prefs.putBoolean(CommonMethod.POSTURE_PAUSE,true));

        FloatingActionButton fabPlay = findViewById(R.id.fabPlay);
        fabPlay.setOnClickListener(view -> Prefs.putBoolean(CommonMethod.POSTURE_PAUSE,false));

        if(Prefs.getBoolean(CommonMethod.POSTURE_PAUSE,false)){
            fabPause.hide();//.setVisibility(View.GONE);
            fabPlay.show();//.setVisibility(View.VISIBLE);
        }else {
            fabPause.show();//.setVisibility(View.VISIBLE);
            fabPlay.hide();//.setVisibility(View.GONE);
        }



        btnCalibrate.setOnClickListener(v -> startCalibration());
    }

    private void startCalibration() {
        float actualAccel=accelY;
        Prefs.putFloat(CommonMethod.POSTURE_CALIBRATION,Math.abs(calculate(actualAccel)));
        if(Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION,0)!=0){
            showToast();
        }else {
            showErrorToast();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
