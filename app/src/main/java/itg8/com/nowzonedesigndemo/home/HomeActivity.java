package itg8.com.nowzonedesigndemo.home;

import android.Manifest;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import itg8.com.nowzonedesigndemo.csv.CSVConvertFragment;
import itg8.com.nowzonedesigndemo.DataStoreScheduleBroadcastReceiver;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.accelerometer.AccelerometerFragment;
import itg8.com.nowzonedesigndemo.breath.BreathHistoryActivity;
import itg8.com.nowzonedesigndemo.breath_history.BreathsHistoryActivity;
import itg8.com.nowzonedesigndemo.common.BaseActivity;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.connection.BleService;
import itg8.com.nowzonedesigndemo.home.fragment.HomeFragment;
import itg8.com.nowzonedesigndemo.home.mvp.BreathPresenter;
import itg8.com.nowzonedesigndemo.home.mvp.BreathPresenterImp;
import itg8.com.nowzonedesigndemo.home.mvp.BreathView;
import itg8.com.nowzonedesigndemo.home.mvp.StateTimeModel;
import itg8.com.nowzonedesigndemo.login.LoginActivity;
import itg8.com.nowzonedesigndemo.pressure.PressureRawFragment;
import itg8.com.nowzonedesigndemo.registration.RegistrationNewActivity;
import itg8.com.nowzonedesigndemo.sanning.ScanDeviceActivity;
import itg8.com.nowzonedesigndemo.setting.AlarmSettingActivity;
import itg8.com.nowzonedesigndemo.setting.SettingMainActivity;
import itg8.com.nowzonedesigndemo.setting.fragment.AlarmSettingFragment;
import itg8.com.nowzonedesigndemo.sleep.SleepActivity;
import itg8.com.nowzonedesigndemo.sleep.SleepFragment;
import itg8.com.nowzonedesigndemo.steps.StepsActivity;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.utility.DeviceState;
import itg8.com.nowzonedesigndemo.utility.Rolling;
import itg8.com.nowzonedesigndemo.widget.BatteryView;
import itg8.com.nowzonedesigndemo.widget.navigation.BottomBar;
import itg8.com.nowzonedesigndemo.widget.navigation.OnTabReselectListener;
import itg8.com.nowzonedesigndemo.widget.navigation.OnTabSelectListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, BreathView, EasyPermissions.PermissionCallbacks, ComponentCallbacks2 {


    public static final String COLOR_CALM_M = "#240CB700";
    public static final String COLOR_STRESS_M = "#24B70F00";
    public static final String COLOR_FOCUSED_M = "#240C00B7";
    public static final double CONST_1 = -8.02d;
    public static final double CONST_2 = 8.02d;

    /**
     * for drawer
     */

    private static final int MENU_USER_PROFILE = 0;
    private static final int MENU_DEVICE = 1;
    private static final int MENU_NOTIFICATION = 2;
    private static final int MENU_POSTURE = 5;
    private static final int MENU_CONTACT_US = 6;
    private static final int MENU_ALARM = 4;
    private static final int MENU_FAQS = 7;
    private static final int MENU_PROGRAMS = 3;
    private static final int MENU_TEMP = 8;
    private static final int MENU_LOGOUT = 9;
    private static final int MENU_DISCONNECT = 10;
    private static final int MENU_EXPORT = 11;
    private static final int MENU_SLEEP = 12;
    private static final int MENU_SLEEP_HISTORY = 13;
    private static final int MENU_PROCESS_RAW = 9;
    private static final int MENU_PROCESS_PRESSURE = 9;


    //    public static final String COLOR_CALM_M = "#240CB700";
//    public static final String COLOR_STRESS_M = "#24B70F00";
//    public static final String COLOR_FOCUSED_M = "#240C00B7";
//    public static final double CONST_1 = -8.02d;
//    public static final double CONST_2 = 8.02d;
    private static final int RC_STORAGE_PERM = 20;
    private static final String COLOR_NORMAL_M = "#24006bb7";
    private static final String COLOR_NORMAL_S = "#27BEFB";
    private static final String COLOR_CALM_S = "#FF35FB27";
    private static final String COLOR_STRESS_S = "#FFF92E27";
    private static final String COLOR_FOCUSED_S = "#FF4027FB";
    private static final int LAST_333 = 333;
    //    public static final double PI_MIN = -7.02d;
//    public static final double PI_MAX = 7.02d;
    public static final double PI_MIN = -4.02d;
    public static final double PI_MAX = 4.02d;
    public static final double MIN_PRESSURE = 400;
    public static final double MAX_PRESSURE = 8190;
    private static final float MAX_CIRCLE_SIZE = 100f;
    private static final float MIN_CIRCLE_SIZE = 1f;
    private static final String IP_ADRESS_LBL = "Ip Address : ";
    private static final String PORT = "8080";
    private static final double smoothing = 50;
    private static final int HOME = 0;
    private static final int ACTIVITY = 1;
    private static final int PROGRAM = 2;
    private static final int STAT = 3;
    private static final int PROGRAMS = 6;
    private static final int MAX_BATTERY_VALUE = 8191;
    public static final int FROM_ACCEL = 1;
    public static final int FROM_GYRO = 2;
    public static final int FROM_LOAD_CELL = 4;
    public static final int FROM_MAG = 3;
    public static final int FROM_OPTICAL = 6;
    public static final int FROM_MIC = 5;
    public static final int FROM_TEMPRETURE = 7;
    private final String TAG = this.getClass().getSimpleName();
    long n = 0;
    double mu = 0.0;
    double sq = 0.0;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    @BindView(R.id.list_slidermenu)
    ListView listSlidermenu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    Rolling rolling;
    @BindView(R.id.battery)
    BatteryView battery;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.titleToolbar)
    TextView titleToolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.img_breath)
    ImageView imgBreath;
    @BindView(R.id.txt_breathRate)
    TextView txtBreathRate;
    @BindView(R.id.txt_statusValue)
    TextView txtStatusValue;
    @BindView(R.id.txt_status)
    TextView txtStatus;
    @BindView(R.id.txtConnectionStatus)
    TextView txtConnectionStatus;
    @BindView(R.id.breathValue)
    TextView breathValue;
    @BindView(R.id.txt_minute)
    TextView txtMinute;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
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
    @BindView(R.id.txtIpAddress)
    EditText txtIpAddress;
    @BindView(R.id.txtStartSocket)
    Button txtStartSocket;
    @BindView(R.id.main_FrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private ActionBarDrawerToggle toggle;
    private double lastMax = 10;
    private double lastMin = -10;
    private int count = 1;
    private int lastCount = 1;
    private long lastUpdate = 0;
    private double smoothed = 0;
    private double dLast;
    private float a = 0.96f;
    private boolean socketStarted = false;
    private Double lastPressure;
    private Fragment fragment;
    private FragmentManager fm;
    private BreathPresenter presenter;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItemList;
    private NavDrawerListAdapter adapter;
    private boolean mSlideNavigation = false;
    private HomeFragmentInteractor homeListener;
    private int lastBatteryLevel = 0;
    private int calcBattery;
    private String title;



    private AdapterView.OnItemClickListener sliderClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/**
 * Comment Now
 */

//            if (navDrawerItemList.size() - 1 == position)
//                onDeviceDisconnected();


            if (position == MENU_PROGRAMS) {
//                    startActivity(new Intent(getApplicationContext(), StepMovingActivity.class));
                setFragment(AccelerometerFragment.newInstance(FROM_GYRO));
                title = "Gyrometer Graph";


            } else if (position == MENU_POSTURE) {
                /**
                 * Comment Now
                 */
                setFragment(AccelerometerFragment.newInstance(FROM_LOAD_CELL));
                title = "Load cell Graph ";


                //    startActivity(new Intent(HomeActivity.this, PostureCalibrateSettingActivity.class));
            } else if (position == MENU_ALARM) {
                /**
                 * Comment Now
                 */

//                    String export = Helper.exportDB();
//                    Toast.makeText(HomeActivity.this, export, Toast.LENGTH_SHORT).show();
//// callSettingActvity(CommonMethod.FROM_ALARM_HOME);//

                setFragment(AccelerometerFragment.newInstance(FROM_MAG));
                title = "Magnetometer Graph ";

            } else if (position == MENU_FAQS) {
                title = "Optical Graph";
                setFragment(AccelerometerFragment.newInstance(FROM_OPTICAL));


//                    logoutFromUser();
            } else if (position == MENU_USER_PROFILE) {
                title = "Pressure Raw Graph";
                setFragment(HomeFragment.newInstance("", ""));
            } else if (position == MENU_DEVICE) {
                title = "Pressure Process Graph";
                setFragment(PressureRawFragment.newInstance("", ""));
            } else if (position == MENU_NOTIFICATION) {
                title = "Accelerometer Graph";
                setFragment(AccelerometerFragment.newInstance(FROM_ACCEL));

            } else if (position == MENU_CONTACT_US) {
                title = " MIC Graph";
                setFragment(AccelerometerFragment.newInstance(FROM_MIC));
            } else if (position == MENU_TEMP) {
                title = " Temperature Graph";
                setFragment(AccelerometerFragment.newInstance(FROM_TEMPRETURE));
            } else if (position == MENU_SLEEP) {
                title = " Sleep";
//                startActivity(new Intent(getApplicationContext(), AlarmSettingActivity.class));
                setFragment(AlarmSettingFragment.newInstance("",""));
            } else if (position == MENU_LOGOUT) {
                logoutFromUser();
            } else if (position == MENU_DISCONNECT) {
                onDeviceDisconnected();
            } else if (position == MENU_EXPORT) {
                title = " Export CSV";
                startExportWalaItem();
            }else if(position == MENU_SLEEP_HISTORY){
                title="Sleep History";
                SleepFragment fragment=new SleepFragment();
                setFragment(fragment);
            }

            if(title!=null) {
            titleToolbar.setText(title);
            }
            openDrawer();
        }
    };

    private void startExportWalaItem() {

        setFragment(CSVConvertFragment.newInstance("", ""));
    }

//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        presenter = new BreathPresenterImp(this);
        presenter.passContext(HomeActivity.this);
        presenter.onCreate();

        navigationViewBasic();
        SetDrawerLayout();
        setItemClickedListener();



        Log.d(TAG, "HEADERTOKEN: " + Prefs.getString(CommonMethod.TOKEN));
        // setIds();
        setFragment();
        Timber.tag(TAG);
        rolling = new Rolling(10);
        checkUserId();
        checkStoragePermission();
        setType();
        initOtherView();
        bottomBarTabSelected();
        DataStoreScheduleBroadcastReceiver.setAlarm(true, this);




    }

    private void checkUserId() {
        if (TextUtils.isEmpty(Prefs.getString(CommonMethod.USER_ID))) {
            callRegistritrationActivity();
            this.finish();
        }
    }

    private void callRegistritrationActivity() {
        startActivity(new Intent(this, RegistrationNewActivity.class));
    }


    private void bottomBarTabSelected() {
        fab.setOnClickListener(this);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                Log.d(TAG, "OnTAB ReSelected:" + tabId);
            }
        });

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                View view = findViewById(tabId);
                Log.d(TAG, "OnTAB Selected:" + view.getId());

//                if (tabId == R.id.tab_favorites) {
//                    // The tab with id R.id.tab_favorites was selected,
//                    // change your content accordingly.
//                }


            }

            @Override
            public void onTabPostionSelect(int position) {
                switch (position) {
                    case HOME:
                        // startActivity(new Intent(getApplicationContext(),BreathsHistoryActivity.class));
                        break;
                    case PROGRAM:
                        // startActivity(new Intent(getApplicationContext(),BreathsHistoryActivity.class));
                        break;
                    case STAT:
                        //startActivity(new Intent(getApplicationContext(),BreathsHistoryActivity.class));
                        break;
                }
            }
        }, true);


    }

    private void navigationViewBasic() {
        imgSetting.setOnClickListener(this);
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItemList = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        for (int i = 0; i < navMenuIcons.length(); i++) {
            navDrawerItemList.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
                    .getResourceId(i, -1)));
        }
        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItemList);
      if(listSlidermenu!=null)
        listSlidermenu.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float newWidth = (slideOffset * drawerView.getWidth()) * +1;
                Log.d(TAG, "NewWidth:" + newWidth);
                coordinator.setTranslationX(newWidth);
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();

            }
        };

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        titleToolbar.setText("Home");
        battery.setmCharging(false);
        //toolbar.setContentInsetsAbsolute(200, toolbar.getContentInsetRight());

        // toolbar.setNavigationIcon(R.drawable.ic_settings_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

      //  getSupportActionBar().setTitle("Home");


    }

    private void SetDrawerLayout() {
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        drawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void logoutFromUser() {
        Prefs.remove(CommonMethod.ISLOGIN);
        Prefs.remove(CommonMethod.TOKEN);
        Prefs.remove(CommonMethod.USER_ID);
        Intent intent = new Intent(this, RegistrationNewActivity.class);
        startActivity(intent);
        onDeviceDisconnected();
        finish();

    }


    @AfterPermissionGranted(RC_STORAGE_PERM)
    private void checkStoragePermission() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            onPermissionGrantedForStorage();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage), RC_STORAGE_PERM, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void setFragment() {
        fragment = new HomeFragment();
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_FrameLayout, fragment).commit();
    }

    public void setFragment(Fragment fragment) {
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_FrameLayout, fragment).addToBackStack(fragment.getTag()).commitAllowingStateLoss();
    }

    private void onPermissionGrantedForStorage() {
        File extStorageDir = Environment.getExternalStorageDirectory();
        File newExternalStorageDir = new File(extStorageDir, getResources().getString(R.string.app_name));
        if (!newExternalStorageDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            newExternalStorageDir.mkdir();
        }
        if (newExternalStorageDir.exists()) {
            newExternalStorageDir = new File(newExternalStorageDir, getResources().getString(R.string.breath));
            if (!newExternalStorageDir.exists())
                //noinspection ResultOfMethodCallIgnored
                newExternalStorageDir.mkdir();
        }

        Prefs.putString(CommonMethod.STORAGE_PATH, newExternalStorageDir.getAbsolutePath());

    }


    private void initOtherView() {
        int mAvgCount = Prefs.getInt(CommonMethod.USER_CURRENT_AVG);
        if (mAvgCount > 0) {
            setAvgValue(mAvgCount);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void setType() {


        // Change Now
//        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
//
//        waveLoadingView.setAmplitudeRatio(20);
//        waveLoadingView.setProgressValue(10);
//        waveLoadingView.setBorderWidth(1f);


//        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
//        waveLoadingView.setAmplitudeRatio(20);
//        waveLoadingView.setProgressValue(50);

//        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
//        waveLoadingView.setTopTitle("Top Title");
//        waveLoadingView.setCenterTitleColor(Color.GRAY);
//        waveLoadingView.setBottomTitleSize(18);
//        waveLoadingView.setProgressValue(50);
//        waveLoadingView.setBorderWidth(10);
//        waveLoadingView.setAmplitudeRatio(60);
//        waveLoadingView.setWaveColor(Color.GREEN);
//        waveLoadingView
//        waveLoadingView.setBorderColor(Color.GRAY);
//        waveLoadingView.setTopTitleStrokeColor(Color.BLUE);
//        waveLoadingView.setTopTitleStrokeWidth(3);

    }

    /**
     * this is set by me
     */
    private void setAnimator() {

        //mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        // Sets the length of the animation, default is 1000.


//        waveLoadingView.setAnimDuration(3000);
//        waveLoadingView.startAnimation();


        //  waveLoadingView.cancelAnimation();
        // waveLoadingView.resumeAnimation();
        //                    waveLoadingView.pauseAnimation();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                callSettingActvity(CommonMethod.FROM_ALARM_HOME);

                // startActivity(new Intent(getApplicationContext(), AlarmSettingActivity.class));

                break;
            case R.id.action_profile:
                callSettingActvity(CommonMethod.FROM_PROFILE);
//                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.action_device:
                callSettingActvity(CommonMethod.FROM_DEVICE);
                break;
            case R.id.action_meditation:
                //startActivity(new Intent(getApplicationContext(),AudioActivity.class));
                callSettingActvity(CommonMethod.FROM_MEDITATION);

                break;
            case R.id.action_alram:
                break;
            case R.id.action_step_goal:
                callSettingActvity(CommonMethod.FROM_STEP_GOAL);
                //  startActivity(new Intent(getApplicationContext(), StepGoalActivity.class));
                break;
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), StepMovingActivity.class));
                break;
            case R.id.action_logout:
                onDeviceDisconnected();
                break;
            case R.id.action_device_history:
                callSettingActvity(CommonMethod.FROM_DEVICE_HISTORY);
                break;
//            case R.id.action_pressure_raw:
//                setFragment(PressureRawFragment.newInstance("", ""));
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callSettingActvity(String fromDevice) {
        Intent intent = new Intent(getApplicationContext(), SettingMainActivity.class);
        intent.putExtra(CommonMethod.BREATH, fromDevice);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onPressureDataAvail(double pressure, long ts) {
//            firstPreference(pressure);
        //Second Preference
        setToWave(pressure);

//        Observable.create(new ObservableOnSubscribe<Double>() {
//            @Override
//            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Double> e) throws Exception {
//                e.onNext(calculateForBreathing(pressure));
//                e.onComplete();
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Double>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Double aDouble) {
////                        setToWave(aDouble);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

    }

    private void setToWave(Double aDouble) {
        if (count > 30) {
//            Log.d(TAG, "Presssure: "+pressure+" value after smoothing: " + smoothedValue(pressure) + " proportion:" + calculateProportion(smoothedValue(pressure)));
//            secondPref(pressure);
//            breathview.addSample(SystemClock.elapsedRealtime(),calculateProportion(smoothedValue(pressure)));
//                            Log.d(TAG,"Progress wave: "+aDouble.intValue());
//
//                                waveLoadingView.setProgressValue(aDouble.intValue());
//                            }
//                            lastPressure=aDouble;
            if (homeListener != null)
                homeListener.onBreathingWaveValueAvail(aDouble);
        } else {
            count++;
        }
    }

    private <T> double calculateForBreathing(double pressure) {
        secondPref(pressure);
        return (calculateProportion(pressure));
    }
//        breathview.addSample(SystemClock.elapsedRealtime(), calculateProportion(pressure));

    double smoothedValue(double pressure) {
//        long now = Calendar.getInstance().getTimeInMillis();
//        long elapsedTime = now - lastUpdate;
        smoothed += 33 * ((pressure - smoothed) / smoothing);
//        lastUpdate = now;
        return smoothed;
    }

    private void secondPref(double pressure) {
        Log.d(TAG, "Pressure : " + pressure);
        rolling.add(pressure);
//

//        if(pressure<lastMin-40){
//            lastMin=pressure;
//        }
//        if(pressure>lastMax+40){
//            lastMax=pressure;
//        }

        if (pressure > lastMax) {
            lastMax = pressure + 100;
            lastMin = lastMax - 400;
        } else if (pressure < lastMin) {
            lastMin = pressure - 100;
            lastMax = lastMin + 400;
        }

    }

    private void firstPreference(double pressure) {
//        if(lastMax<pressure || lastMax-2000>pressure) {
        if (lastMax < pressure) {
            lastMax = pressure;
        } else {
            lastMax = lastMax - 1;
        }
        if (count > 30) {
            if (lastMin == 0)
                lastMin = pressure;


//            if(lastMin-pressure>lastMax-1000)
//                lastMin=lastMax-1000;
//            if(lastMin)
//            if(count%LAST_333==0)
//                lastMin=lastMax-500;
//            else

//
//                if(lastMin>lastMax-1000)
//                    lastMax=lastMin+1000;

//            if(lastMax-lastMin>2000) {
//                lastMin = lastMax - 2000;
//            }
//            else
//                lastMax=lastMin+2000;
            if (lastMin > pressure)
                lastMin = pressure;
            else
                lastMin = lastMin + 1;
//
////            if (lastMin > pressure || lastMin == 0){
////                lastMin = pressure;
////        }
        } else {
            count++;
        }


    }

    private double calculateProportion(double pressure) {
//        return (-0.02+(1.02*((pressure-(lastMax-500))/(lastMax-(lastMax-500)))));
//        double d=(double) Math.round((CONST_1+(CONST_2*((pressure-(lastMin))/(lastMax-lastMin)))) * 1000000000000000000d) / 1000000000000000000d;
//        s(i)=a*y(i)+(1-a)*s(i-1)


        double d = a * pressure + ((1 - a) * dLast);
//        double d=pressure;
//        rolling.add(pressure);
//        d=rolling.getaverage();
        dLast = d;
        // Log.d(TAG,"ds:"+d);
//        return (PI_MIN + ((PI_MAX - PI_MIN) * ((d - (MIN_PRESSURE)) / (MAX_PRESSURE - MIN_PRESSURE))));
        return (PI_MIN + ((PI_MAX - PI_MIN) * ((d - (lastMin)) / (lastMax - lastMin))));

//        update(pressure);
//        Log.d(TAG, String.valueOf(var()));

//        return (MIN_CIRCLE_SIZE + ((MAX_CIRCLE_SIZE- MIN_CIRCLE_SIZE) * ((d - (lastMin)) / (lastMax - lastMin))));
//        return (lastMin + ((lastMax- lastMin) * ((d - (MIN_PRESSURE)) / (MAX_PRESSURE - MIN_PRESSURE))));
    }

    void update(double x) {
        ++n;
        double muNew = mu + (x - mu) / n;
        sq += (x - mu) * (x - muNew);
        mu = muNew;
    }

    double mean() {
        return mu;
    }

    double var() {
        return n > 1 ? sq / n : 0.0;
    }

    @Override
    public void onDeviceConnected() {
        Log.d(TAG, "Device connected");
    }

    @Override
    public void onDeviceDisconnected() {

        Log.d(TAG, "Device disconnected");
        Intent intent = new Intent("ACTION_NW_DEVICE_DISCONNECT");
        intent.putExtra(CommonMethod.ENABLE_TO_CONNECT, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//        Prefs.remove(CommonMethod.ISLOGIN);
//        Prefs.remove(CommonMethod.TOKEN);
    }

    @Override
    public void onDeviceDisconnectedInTime() {
        Log.d(TAG, "Device disconnected");

        Intent intent = new Intent("ACTION_NW_DEVICE_DISCONNECT");
        intent.putExtra(CommonMethod.ENABLE_TO_CONNECT_IN_TIME, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public void setSocketClosed() {
        Log.i(TAG, "Socket Closed");


        socketStarted = false;
    }


    @Override
    public void onMovementStarted() {
        if (homeListener != null)
            homeListener.onMovementStarted();
    }

    @Override
    public void onMovementStopped() {
        if (homeListener != null)
            homeListener.onMovementStopped();
    }

    @Override
    public void onDeviceNotAttachedToBody() {
        if (homeListener != null)
            homeListener.onDeviceNotAttachedToBody();
    }

    @Override
    public void onDeviceAttached() {
        if (homeListener != null)
            homeListener.ondeviceAttached();
    }

    @Override
    public void onBatteryCountAvail(int intExtra) {
        Log.d(TAG, "Battery Level: " + intExtra);
//        calcBattery=(intExtra/MAX_BATTERY_VALUE)*100;
//        calcBattery = (((100) * (intExtra - (4000)) / (8191 - 4000)));
        ;
        calcBattery = intExtra;
        if (lastBatteryLevel != calcBattery) {
//            battery.setmLevel((intExtra/MAX_BATTERY_VALUE)*100);
            battery.setmLevel(calcBattery);
        }
        lastBatteryLevel = calcBattery;
//        homeListener.onBatteryCountAvail(intExtra);
    }

    @Override
    public void onNotLoginYet() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeviceStateAvail(DeviceState deviceState) {
        if (deviceState.name() != null) {
            if (homeListener != null) {
                homeListener.onConnectionStateAvail(deviceState.name());
                if (deviceState == DeviceState.CONNECTED) {
                    hideSnackbar();
                    homeListener.onConnectionStateAvail("");
                } else if (deviceState == DeviceState.DISCONNECTED) {
                    checkDeviceConnection(coordinator);
                }
                if (deviceState == DeviceState.CHARACTERISTICS_WRITE
                        || deviceState == DeviceState.WRITE) {
                    homeListener.onConnectionStateAvail("");
                }

            }
        }
    }

    private void setItemClickedListener() {
        if (listSlidermenu != null)
            listSlidermenu.setOnItemClickListener(sliderClickListener);



    }

    @Override
    public void onBreathCountAvailable(int intExtra) {
        Log.d(TAG, "Breath count: " + intExtra);
        if(homeListener!=null)
            homeListener.onBreathingCountAvail(intExtra);
    }

    @Override
    public void onStepCountReceived(int intExtra) {
        if(homeListener!=null)
            homeListener.onStepCountAvail(intExtra);
//        if (txtConnectionStatus.getVisibility() != View.VISIBLE)
//            txtConnectionStatus.setVisibility(View.VISIBLE);
//        txtConnectionStatus.setText(String.valueOf(intExtra));

        Log.d(TAG, "Step count: " + intExtra);
    }

    @Override
    public void onStartDeviceScanActivity() {
        Timber.i("Start device activity");
//        startActivity(new Intent(this, ScanDeviceActivity.class));
//        finish();
        checkDeviceConnection(coordinator);
        if (homeListener != null)
            homeListener.ondeviceAttached();

    }


    @Override
    public void onBreathingStateAvailable(BreathState state) {
        initOtherView();
        setStateRelatedDetails(state);

        // presenter.onInitTimeHistory();
    }

    @Override
    public void onStateTimeHistoryReceived(StateTimeModel stateTimeModel) {
        if (stateTimeModel != null) {
            txtCalmValue.setText(stateTimeModel.getCalm());
            txtFocusValue.setText(stateTimeModel.getFocus());
            txtStressValue.setText(stateTimeModel.getStress());
        }
    }

    @Override
    public void onRemoveSnackbar() {
        hideSnackbar();
    }

    private void setStateRelatedDetails(BreathState state) {
        switch (state) {
            case CALM:
                reactCalmState();
                break;
            case FOCUSED:
                reactFocusedState();
                break;
            case STRESS:
                reactStressState();
                break;
            case UNKNOWN:
                reactUnkownState();
                break;
        }
    }

    private void reactUnkownState() {
        if(homeListener!=null)
            homeListener.onBreathingStateAvail(null, 0);
    }


    private void setState(String name, int m, int s) {
        txtStatusValue.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtStatusValue.setText(name);
            }
        }, 60);
//        waveLoadingView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                waveLoadingView.setWaveColor(m);
//            }
//        },30);
//        waveLoadingView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                waveLoadingView.setWaveBgColor(s);
//            }
//        },90);
    }

    private void reactStressState() {
        if(homeListener!=null)
            homeListener.onBreathingStateAvail("Stress\nState", R.drawable.stress_streak_icon);

//        setState(BreathState.STRESS.name(), Color.parseColor(COLOR_STRESS_M), Color.parseColor(COLOR_STRESS_S));
    }

    private void reactFocusedState() {
        if(homeListener!=null)
            homeListener.onBreathingStateAvail("Attentive\nState", R.drawable.focus_streak_icon);
//        setState(BreathState.FOCUSED.name(), Color.parseColor(COLOR_FOCUSED_M), Color.parseColor(COLOR_FOCUSED_S));
    }

    private void reactCalmState() {
        if(homeListener!=null)
            homeListener.onBreathingStateAvail("Composed\nState", R.drawable.calm_streak_icon);
//        setState(BreathState.CALM.name(), Color.parseColor(COLOR_CALM_M), Color.parseColor(COLOR_CALM_S));
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        onPermissionGrantedForStorage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


    @Override
    public void onSnackbarOkClicked() {
        onDeviceDisconnected();
        Prefs.remove(CommonMethod.CONNECTED);
        Prefs.remove(CommonMethod.DEVICE_ADDRESS);
        startActivity(new Intent(getBaseContext(), ScanDeviceActivity.class));
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlSteps:
                Intent intent = new Intent(getApplicationContext(), StepsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sleep_main:
                startActivity(new Intent(getApplicationContext(), SleepActivity.class));
                break;
            case R.id.ll_breath_avg:
                startActivity(new Intent(getApplicationContext(), BreathHistoryActivity.class));
                break;
            case R.id.img_setting:
                openDrawer();
                break;
            case R.id.fab:
//                onDeviceDisconnected();
                startActivity(new Intent(getApplicationContext(), BreathsHistoryActivity.class));
                break;


        }
    }

    private void openDrawer() {
        if (mSlideNavigation) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
        mSlideNavigation = !mSlideNavigation;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void setAvgValue(int mAvgCount) {
        if (homeListener != null)
            homeListener.onAvgValueAvailable(mAvgCount);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                openDrawer();
                break;

        }
        return false;
    }


    private void callBreathActivity(Intent intent) {
        startActivity(intent);
    }


    public void setHomeListener(HomeFragmentInteractor homeListener) {
        this.homeListener = homeListener;
    }

    public void destroyHomeListener() {
        this.homeListener = null;
    }

    public interface HomeFragmentInteractor {
        void onBreathingWaveValueAvail(double value);

        void onBreathingStateAvail(String state, int focus_streak_icon);

        void onBreathingCountAvail(int count);

        void onMovementStarted();

        void onMovementStopped();

        void onDeviceNotAttachedToBody();

        void ondeviceAttached();

        void onAvgValueAvailable(int count);

        void onConnectionStateAvail(String name);

        void onBatteryCountAvail(int batteryCount);

        void onStepCountAvail(int intExtra);

    }
}
