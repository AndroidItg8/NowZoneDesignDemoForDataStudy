package itg8.com.nowzonedesigndemo.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath.timeline.ArraysHelper;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.utility.Rolling;
import itg8.com.nowzonedesigndemo.widget.CompassView;

public class StepMovingActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private static final String TAG = StepMovingActivity.class.getSimpleName();
    public static final String AXIS_Y = "AxisY";
    public static final String AXIS_Z = "AxisZ";
    private static final int ANGLE_MIN = 10;
    private static final int ANGLE_MAX = 360;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.btn_add)
    Button btnAdd;
    List<Integer> images;
    @BindView(R.id.image)
    CompassView mImage;
    @BindView(R.id.chart1)
    LineChart chart1;
    @BindView(R.id.chart2)
    LineChart chart2;
    @BindView(R.id.txtAngle)
    TextView txtAngle;
    private LineChart mChart;
    private int count = 0;
    private float g = 0;
    private static final int MAX_BEND = 150;
    private static final int MIN_BEND = 100;
    private float accelY;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            accelY = intent.getFloatExtra(AXIS_Y, 0);
            float accelZ = intent.getFloatExtra(AXIS_Z, 0);
//            addEntry(mChart, accelY);
//            addEntry(mChart2, accelZ);
            //TODO create compass
            if (mImage != null) {
                accelY = calculate(accelY);
                Log.d(TAG, "accelyAngle " + accelY + "Calibrated: "+Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION,0));
                mImage.setBearing((int) accelY);
                check5SecPosture(accelY);
            }


//            g = 0.9f * g + 0.1f * accel;
//            accel= (float) (accel-g);
//            Log.d(TAG,"GACC:-"+accel+" G:-"+g);

//            detectPeak(accel);

//            addEntry(mChart,accel);
//            double pressure=intent.getDoubleExtra(CommonMethod.BREATH,0);
//            addEntry(mChart2, (float) pressure);

        }
    };
    private float calibrate = 0;
    private float[] accelGatheredData = new float[300];
    private int countPosture = 0;

    private void check5SecPosture(float accelY) {
        accelY = Math.abs(accelY);
        if (calibrate != 0) {
            if (countPosture < 300) {
                accelGatheredData[countPosture] = accelY;
                countPosture++;
                Log.d(TAG, "TIMESTAMPFOR20DATA " + System.currentTimeMillis());
            } else {
                float sum = 0;
                for (float f :
                        accelGatheredData) {
                    sum += Math.abs(f);
                }

                sum = sum / 300;
                if (sum > calibrate + 500 || sum < calibrate - 500) {
                    txtAngle.setTextSize(24);
                    txtAngle.setTextColor(Color.RED);
                    txtAngle.setText("BAD POSTURE");
                } else {
                    txtAngle.setTextSize(24);
                    txtAngle.setTextColor(Color.GREEN);
                    txtAngle.setText("GOOD POSTURE");
                }
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);

                countPosture = 0;
                Arrays.fill(accelGatheredData, 0);
            }
        }
    }

    private float calculate(float accelY) {
        return (ANGLE_MIN + ((ANGLE_MAX - ANGLE_MIN) * ((accelY - (MIN_BEND)) / (MAX_BEND - MIN_BEND))));
    }

    private List<Double> accList = new ArrayList<>();
    private LineChart mChart2;
    private Rolling roll;
    private boolean shouldStart = true;

    private void detectPeak(float accel) {
        if (accList.size() >= 20) {
            detect();
        }
        accList.add((double) accel);
    }

    private boolean check(double avg) {
        return avg < 10 && Math.abs(avg) >= 0;
    }

    private void detect() {

        List<Map<Integer, Double>> result = CommonMethod.peak_detection(accList, 50.0);
        if (result.size() > 0) {
            if (result.get(0) != null && result.get(1) != null) {
                Log.i(TAG, "peaks in accel: " + result.get(0).size());
                if (result.get(0).size() >= 1 && result.get(0).size() <= 3 && result.get(1).size() >= 1 && result.get(1).size() <= 3) {

                    count += 1;
                    shouldStart = true;
//                    feedMultiple();
                    btnAdd.setText("Movement");
                } else if (result.get(0).size() > 3) {
                    shouldStart = true;
//                    feedMultiple();
                    btnAdd.setText("Movement");
                } else {
//                    shouldStart=false;
                    btnAdd.setText("No Movement");
                }
            }
        }
//        if(accel>80){
//        }
        accList.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_moving);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        images = new ArrayList<>();

        checkCalibration();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              image.addStep();
                //addValue();
                mImage.setCalibrated((int) accelY);
                Prefs.putFloat(CommonMethod.POSTURE_CALIBRATION, Math.abs(accelY));
                checkCalibration();
            }
        });

    }

    private void checkCalibration() {
        calibrate = Prefs.getFloat(CommonMethod.POSTURE_CALIBRATION, 0);
        if (calibrate <= 0) {
            txtAngle.setText("Please calibrate");
        } else {
            txtAngle.setText("STarted");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(CommonMethod.ACTION_AXIS_ACCEL));

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.actionAdd: {
//                break;
//            }
//            case R.id.actionClear: {
//                mChart.clearValues();
//                Toast.makeText(this, "Chart cleared!", Toast.LENGTH_SHORT).show();
//                break;
//            }
//            case R.id.actionFeedMultiple: {
//                feedMultiple();
//                break;
//            }
//        }
        return true;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}