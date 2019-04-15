package itg8.com.nowzonedesigndemo.registration;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.common.ProfileModel;
import itg8.com.nowzonedesigndemo.home.HomeActivity;
import itg8.com.nowzonedesigndemo.steps.widget.CustomFontTextView;
import itg8.com.nowzonedesigndemo.utility.Helper;
import okhttp3.ResponseBody;

public class RegistrationNewActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {




//
//    @BindView(R.id.basic_detail)
//    CustomFontTextView basicDetail;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.edt_mobile)
    EditText edtMobile;
    @BindView(R.id.input_name)
    TextInputLayout inputName;

    @BindView(R.id.input_mobile)
    TextInputLayout inputMobile;
    @BindView(R.id.edt_height)
    EditText edtHeight;
    @BindView(R.id.input_height)
    TextInputLayout inputHeight;

    @BindView(R.id.edt_weight)
    EditText edtWeight;
    @BindView(R.id.input_weight)
    TextInputLayout inputWeight;


    @BindView(R.id.lbl_birth)
    TextView lblBirth;
    @BindView(R.id.edt_day)
    EditText edtDay;
    @BindView(R.id.input_day)
    TextInputLayout inputDay;
    @BindView(R.id.edt_month)
    EditText edtMonth;
    @BindView(R.id.input_month)
    TextInputLayout inputMonth;
    @BindView(R.id.edt_year)
    EditText edtYear;
    @BindView(R.id.input_year)
    TextInputLayout inputYear;
    @BindView(R.id.ll_age)
    LinearLayout llAge;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.fab)
    FloatingActionButton fab;
//    @BindView(R.id.edt_gender)
//    EditText edtGender;
//    @BindView(R.id.input_gender)
//    TextInputLayout inputGender;
    @BindView(R.id.edt_height_feet)
    EditText edtHeightFeet;
    @BindView(R.id.input_height_feet)
    TextInputLayout inputHeightFeet;
    @BindView(R.id.edt_height_inch)
    EditText edtHeightInch;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.input_height_inch)
    TextInputLayout inputHeightInch;

    @BindView(R.id.input_password)
    TextInputLayout inputPassword;
    @BindView(R.id.ll_height_feet)
    LinearLayout llHeightFeet;
    @BindView(R.id.frame_height)
    FrameLayout frameHeight;
    @BindView(R.id.rgb_gender)
    RadioGroup rgbGender;
    @BindView(R.id.rgb_height)
    RadioGroup rgbHeight;
    @BindView(R.id.rgb_female)
    RadioButton rgbFemale;
    @BindView(R.id.rgb_male)
    RadioButton rgbMale;

    @BindView(R.id.rgb_weight)
    RadioGroup rgbWeight;
    @BindView(R.id.rgb_kg)
    RadioButton rgbKG;
    @BindView(R.id.rgb_pounds)
    RadioButton rgbPounds ;
    @BindView(R.id.rgb_inch)
    RadioButton rgbInch;
    @BindView(R.id.rgb_cm)
    RadioButton rgbCm;

    private ProfileModel model;
    private float heightInFeet;
    private float weightInKg;
    private boolean isFromCm;
    private boolean isFromKG;

    private static final String TAG = "RegistrationNewActivity";
    private Calendar mcurrentDate = null;

    private static double poundsToKilos(double pounds) {
        return pounds * 0.454;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_new);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);

        init();

        //checkAlreadyFilled();

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        model = new ProfileModel();
        button.setOnClickListener(this);
        lblBirth.setOnClickListener(this);
        edtDay.setOnClickListener(this);
        edtMonth.setOnClickListener(this);
        edtYear.setOnClickListener(this);
//        edtGender.setOnClickListener(this);
//        edtHeight.setOnClickListener(this);
//        edtWeight.setOnClickListener(this);
        rgbGender.setOnCheckedChangeListener(this);
        rgbWeight.setOnCheckedChangeListener(this);
        rgbHeight.setOnCheckedChangeListener(this);
        edtHeight.setFocusable(true);
        edtHeight.setFocusableInTouchMode(true);
        edtHeightInch.setVisibility(View.GONE);
        edtHeightFeet.setVisibility(View.GONE);
        edtHeightFeet.setOnClickListener(this);
        edtHeightInch.setOnClickListener(this);


//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction().replace(R.id.profileFrameLayout, new BasicProfileFragment(), getClass().getSimpleName()).addToBackStack(getClass().getSimpleName()).commit();


    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//        switch (checkedId) {
//            case R.id.rgb_height_cm:
//                inputHeightCm.setVisibility(View.VISIBLE);
//                inputHeight.setVisibility(View.GONE);
//                edtHeightCm.setHintTextColor(Color.WHITE);
//                edtHeightCm.setHint("Height[cm]");
//                break;
//            case R.id.rgb_height_feet:
//                inputHeightCm.setVisibility(View.VISIBLE);
//                inputHeight.setVisibility(View.VISIBLE);
//                edtHeightCm.setHintTextColor(Color.WHITE);
//                edtHeight.setHintTextColor(Color.WHITE);
//                edtHeightCm.setHint("Height[inch]");
//                edtHeight.setHint("Height[feet]");
//                break;
//            case R.id.fab:
//                break;
//            case R.id.rgb_weight_kg:
//                edtWeight.setHintTextColor(Color.WHITE);
//                edtWeight.setHint("Weight[kg]");
//                break;
//            case R.id.rgb_weight_pounds:
//                edtWeight.setHintTextColor(Color.WHITE);
//                edtWeight.setHint("Weight[pounds]");
//                break;
//        }
//
//    }

    private void openDateTimeDialogue() {

        // Get Current Time
        mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub

                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd/MMM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                String[] entireDate = sdf.format(mcurrentDate.getTime()).split("/");
                String day = entireDate[0];
                String month = entireDate[1];
                String year = entireDate[2];
                edtDay.setText(day);
                edtMonth.setText(month);
                edtYear.setText(year);
                Log.d(getClass().getSimpleName(), "DatePicker:" + mcurrentDate.getTime());


            }


        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }


    public static int getAge(Date date) {

        int age = 0;

        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        dob.setTime(date);
        if (dob.after(now)) {
            return 0;
        }
        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);
        age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }
        return age;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (validate()) {
                    model.setName(edtName.getText().toString());
                    model.setMobile(edtMobile.getText().toString());
                    model.setHeight(calculateHeightInFeet());
                    model.setWeight(calculateWeightInKg());
                    model.setUserGroupId("4");
//                    model.setGender(edtGender.getText().toString());
                    model.setPassword(edtPassword.getText().toString());
                    model.setAge(String.valueOf(getAge(mcurrentDate.getTime())));

                    sendDataToServer(model);
                }
                break;
            case R.id.lbl_birth:
                //  openDateTimeDialogue();
                break;

            case R.id.edt_day:
                openDateTimeDialogue();
                break;
            case R.id.edt_month:
                openDateTimeDialogue();
                break;
            case R.id.edt_year:
                openDateTimeDialogue();
                break;

//            case R.id.edt_gender:
//                if (TextUtils.isEmpty(edtGender.getText())) {
//                    openBottomSheetDialogueForGender();
//                } else {
//                    // edtGender.setText(" ");
//                    openBottomSheetDialogueForGender();
//                }
//                break;
            case R.id.edt_height:
                if (TextUtils.isEmpty(edtHeight.getText())) {
                    openBottomSheetDialogueForHeight();
                } else {
                    //edtHeight.setText(" ");
                    edtHeight.setFocusableInTouchMode(false);
                    openBottomSheetDialogueForHeight();

                }
                break;
            case R.id.edt_height_feet:
                if (TextUtils.isEmpty(edtHeightFeet.getText())) {
                    openBottomSheetDialogueForHeight();
                } else {
                    // edtHeightFeet.setText(" ");
                    edtHeightFeet.setFocusableInTouchMode(false);
                    openBottomSheetDialogueForHeight();
                }

                break;
            case R.id.edt_height_inch:
                if (TextUtils.isEmpty(edtHeightInch.getText())) {
                    openBottomSheetDialogueForHeight();
                } else {
                    //edtHeightInch.setText(" ");
                    edtHeightInch.setFocusableInTouchMode(false);
                    openBottomSheetDialogueForHeight();
                }
                break;
            case R.id.edt_weight:
                if (TextUtils.isEmpty(edtWeight.getText())) {
                    OpenBottomSheetDialogueForWeight();
                } else {
                    // edtWeight.setText(" ");
                    edtWeight.setFocusableInTouchMode(false);
                    OpenBottomSheetDialogueForWeight();
                }
                break;


        }


    }

    private void sendDataToServer(ProfileModel model) {
showProgress();
        CommonMethod.getController().storeProfile(model.getMobile(), model.getName(), model.getAge(), model.getGender(), String.valueOf(model.getWeight()), model.getPassword(), model.getUserGroupId(), String.valueOf(model.getHeight())).flatMap(new Function<ResponseBody, Observable<String>>() {
            @Override
            public Observable<String> apply(ResponseBody responseBody) throws Exception {
                String userID = null;
                try {
//                    {"msg":"User Already exists","username":"9823778532"}
                    //{"msg":"User Successfully Registered","userid":"$lastid","flag":"1"}
                    String response = responseBody.string();
                    JSONObject jsonObject = null;
                    if (response != null) {

                        jsonObject = new JSONObject(response);
                        if (jsonObject.has("flag") && jsonObject.getInt("flag") == 1) {
                            if (jsonObject.has("userid")) {
                                userID = jsonObject.getString("userid");
                                Prefs.putString(CommonMethod.USER_ID, jsonObject.getString("userid"));
                            }

                        }else if(jsonObject.has("msg") && jsonObject.getString("msg").equalsIgnoreCase("User Already exists")){
                            if (jsonObject.has("userid")) {
                                userID = jsonObject.getString("userid");
                                Prefs.putString(CommonMethod.USER_ID, jsonObject.getString("userid"));
                            }
                            else
                                return Observable.error(new IllegalStateException("User Already exists"));



                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }
                return Observable.just(userID);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                hideProgress();
//                ((AppApplication) getApplication()).setProfileModel(model);
                callHomeActivity();
                // Snackbar.make(button,"User Profile created successfully, user id is"+s,Snackbar.LENGTH_LONG);

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                hideProgress();
                showSnackbar(e);
                e.printStackTrace();


            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void showSnackbar(Throwable e) {
        Snackbar.make(button,e.getMessage(),Snackbar.LENGTH_LONG).show();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }

    private void callHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
    }

    private void OpenBottomSheetDialogueForWeight() {

        View view = getLayoutInflater().inflate(R.layout.item_bottomsheet_weight, null);
        CustomFontTextView txtKg = (CustomFontTextView) view.findViewById(R.id.lbl_kg);
        CustomFontTextView txtPounds = (CustomFontTextView) view.findViewById(R.id.lbl_pounds);

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        final Dialog mBottomSheetDialog = new Dialog(RegistrationNewActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtWeight.setFocusable(true);

                mBottomSheetDialog.dismiss();


            }
        });
        txtKg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromKG = true;
                mBottomSheetDialog.dismiss();
                setDataFromBottomSheetForWieght();

            }
        });
        txtPounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromKG = false;
                mBottomSheetDialog.dismiss();
                setDataFromBottomSheetForWieght();
            }
        });


    }

    private void openBottomSheetDialogueForHeight() {

        View view = getLayoutInflater().inflate(R.layout.item_bottomsheet_height, null);
        CustomFontTextView txtCm = (CustomFontTextView) view.findViewById(R.id.lbl_cm);
        CustomFontTextView txtFeet = (CustomFontTextView) view.findViewById(R.id.lbl_feet);

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        final Dialog mBottomSheetDialog = new Dialog(RegistrationNewActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  isFromCm = true;
                setDataFromBottomSheetForHeight();
                mBottomSheetDialog.dismiss();


            }
        });
        txtFeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromCm = false;
                setDataFromBottomSheetForHeight();
                mBottomSheetDialog.dismiss();


            }
        });

    }

    private void setDataFromBottomSheetForHeight() {
        if (isFromCm) {
            edtHeight.setVisibility(View.VISIBLE);
            llHeightFeet.setVisibility(View.GONE);
            edtHeight.setFocusable(true);
            edtHeight.setFocusableInTouchMode(true);
            edtHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            edtHeight.setVisibility(View.GONE);
            llHeightFeet.setVisibility(View.VISIBLE);
            edtHeightFeet.setVisibility(View.VISIBLE);
            edtHeightInch.setVisibility(View.VISIBLE);
            llHeightFeet.setFocusableInTouchMode(true);
            edtHeightInch.setFocusableInTouchMode(true);
            edtHeightFeet.setFocusableInTouchMode(true);
            edtHeightInch.setFocusable(true);
            edtHeightInch.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtHeightFeet.setFocusable(true);
            edtHeightFeet.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        isFromCm = !isFromCm;
    }

    private void setDataFromBottomSheet(String gender) {
//        edtGender.setText(String.valueOf(gender.charAt(0)));
        Log.d(getClass().getSimpleName(), "BottomSheet:" + gender);

    }

    private void openBottomSheetDialogueForGender() {
        View view = getLayoutInflater().inflate(R.layout.fragment_basic_profile, null);
        CustomFontTextView txtFemale = (CustomFontTextView) view.findViewById(R.id.lbl_female);
        CustomFontTextView txtMale = (CustomFontTextView) view.findViewById(R.id.lbl_male);

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        final Dialog mBottomSheetDialog = new Dialog(RegistrationNewActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

            }
        });
        txtFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fe = txtFemale.getText().toString();
                setDataFromBottomSheet(fe);
                mBottomSheetDialog.dismiss();


            }
        });
        txtMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = txtMale.getText().toString();
                setDataFromBottomSheet(ma);
                mBottomSheetDialog.dismiss();

            }
        });


    }

    private void setDataFromBottomSheetForWieght() {
        edtWeight.setFocusableInTouchMode(true);
        edtWeight.setFocusable(true);
        edtWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private float calculateWeightInKg() {
        if (isFromKG)
            return (float) poundsToKilos(Double.parseDouble(edtWeight.getText().toString()));
        else
            return Float.parseFloat(edtWeight.getText().toString());
    }

    private float calculateHeightInFeet() {
        if (isFromCm)
            return Helper.centimeterToFeet(edtHeight.getText().toString());
        else
            return (float) (Float.parseFloat(edtHeight.getText().toString()) + (0.1 * Float.parseFloat(edtHeight.getText().toString())));
    }

    private boolean validate() {
        boolean valid = true;
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            inputName.setError("Please provide name...");
            setFocus(edtName);
            valid = false;
        }
        if (TextUtils.isEmpty(edtMobile.getText().toString())) {
            inputMobile.setError("Please provide mobile number...");
            setFocus(edtMobile);
            valid = false;
        }
//        if(isFromCm == true){
//        if (TextUtils.isEmpty(edtHeight.getText().toString())) {
//            inputHeight.setError("Please provide your height...");
//            setFocus(edtHeight);
//            valid = false;
//        }
//        }else{
//            if (TextUtils.isEmpty(edtHeightFeet.getText().toString())) {
//                inputHeightFeet.setError("Please provide your height...");
//                setFocus(edtHeightFeet);
//                valid = false;
//            }
//            if (TextUtils.isEmpty(edtHeightInch.getText().toString())) {
//                inputHeightInch.setError("Please provide your height...");
//                setFocus(edtHeightInch);
//                valid = false;
//            }
//
//        }
        if (TextUtils.isEmpty(edtPassword.getText().toString()) || edtPassword.getText().length() < 6) {
            inputPassword.setError("Please enter valid password (minimum 6 character)");
            setFocus(edtPassword);
            valid = false;
        }
        if (TextUtils.isEmpty(edtWeight.getText().toString())) {
            inputWeight.setError("Please provide your weight...");
            setFocus(edtWeight);
            valid = false;
        }

        return valid;
    }

    private void setFocus(EditText edtName) {
        edtName.requestFocus();
        edtName.setFocusableInTouchMode(true);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rgb_female:
                model.setGender("F");
                break;

            case R.id.rgb_male:
                model.setGender("M");
                break;

                case R.id.rgb_cm:
                    isFromCm=true;
                    setDataFromBottomSheetForHeight();
                break;

            case R.id.rgb_inch:
                isFromCm=false;
                setDataFromBottomSheetForHeight();
                break;

            case R.id.rgb_kg:
                edtWeight.setHintTextColor(Color.WHITE);
                edtWeight.setHint("Weight[kg]");
                isFromKG=true;
                break;
            case R.id.rgb_pounds:
                edtWeight.setHintTextColor(Color.WHITE);
                edtWeight.setHint("Weight[pounds]");
                isFromKG=false;
                break;
        }
    }
}
