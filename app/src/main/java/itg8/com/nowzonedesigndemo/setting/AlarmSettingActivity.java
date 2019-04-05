package itg8.com.nowzonedesigndemo.setting;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_awakeAlarm)
    CustomFontTextView lblAwakeAlarm;
    @BindView(R.id.txt_alarm_time)
    CustomFontTextView txtAlarmTime;
    @BindView(R.id.btn_alarmStart)
    Button btnAlarmSetting;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.txtAmPm)
    CustomFontTextView txtAmPm;
    @BindView(R.id.releative)
    RelativeLayout releative;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.txt_alarm_status)
    CustomFontTextView txtAlarmStatus;
    @BindView(R.id.txt_am)
    CustomFontTextView txtAm;
    @BindView(R.id.btn_alarmCalibarating)
    Button btnAlarmCalibarating;
    @BindView(R.id.btn_alarmStarted)
    Button btnAlarmStarted;
    @BindView(R.id.btn_alarmFinished)
    Button btnAlarmFinished;
    private Animation zoomIn, zoomOut;
    private Thread thread;
    private Calendar c;
    SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm", Locale.getDefault());
    SimpleDateFormat formatDate2 = new SimpleDateFormat("a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        checkAlarmStartedOrNot();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.zoom_in_text);
        zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.zoomout_text);
        Log.d(getClass().getSimpleName(), "Calender:" + Calendar.getInstance().getTimeInMillis());
        Log.d(getClass().getSimpleName(), "Calender:" + System.currentTimeMillis());
        btnAlarmSetting.setOnClickListener(this);
        btnAlarmStarted.setOnClickListener(this);
        btnAlarmCalibarating.setOnClickListener(this);
        txtAlarmTime.setOnClickListener(this);
        long startAlarmTime = Prefs.getLong(CommonMethod.START_ALARM_TIME, 0);
        if (startAlarmTime > 0) {
            c = Calendar.getInstance();
            c.setTimeInMillis(startAlarmTime);
            txtAlarmTime.setText(formatDate.format(c.getTime()));
            txtAm.setText(formatDate2.format(c.getTime()));
            int hour = c.get(Calendar.HOUR);
            int minute = c.get(Calendar.MINUTE);
            Log.d(getClass().getSimpleName(), "ShareTime:" + hour + " " + minute);
            // txtAlarmTime.setText(hour+":"+ minute);

        }
    }

    private void checkAlarmStartedOrNot() {
        if (Prefs.contains(CommonMethod.SLEEP_STARTED)) {
            showFinishButton();
        }else {
            showAlarmStartButton();
        }
    }

    private void showFinishButton() {
        btnAlarmStarted.setVisibility(View.GONE);
        btnAlarmFinished.setVisibility(View.VISIBLE);
    }

    private void showAlarmStartButton() {
        btnAlarmStarted.setVisibility(View.VISIBLE);
        btnAlarmFinished.setVisibility(View.GONE);
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
        if (v.getId() == R.id.btn_alarmStart) {

        }
        if (v.getId() == R.id.txt_alarm_time) {
            OpenTimePickerDia();
        }
        if (v.getId() == R.id.btn_alarmStarted) {
        }


    }

    private void OpenTimePickerDia() {
        // Get Current Time
        c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        String endTime = (hourOfDay + ":" + minute);
                        Prefs.putString(CommonMethod.ALARM_AP, formatDate2.format(c.getTime()));
                        c = CommonMethod.ConvertTime(getApplicationContext(), hourOfDay, minute);
                        txtAm.setText(formatDate2.format(c.getTime()));
                        txtAlarmTime.setText(endTime);
                        endTime = formatDate.format(c.getTime());
                        txtAlarmTime.setText(endTime);
                        Log.d(getClass().getSimpleName(), "Time:" + endTime);
                        Prefs.putString(CommonMethod.SAVEALARMTIME, endTime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    private void sendBroadCast(boolean b) {
        Intent intent = new Intent(CommonMethod.ACTION_ALARM_NOTIFICATION);
        intent.putExtra(CommonMethod.ALARM_FROMTIMEPICKER, b);
        //  LocalBroadcastManager.getInstance(getApplicationContext()).
        sendBroadcast(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null)
            thread.interrupt();
    }
}





