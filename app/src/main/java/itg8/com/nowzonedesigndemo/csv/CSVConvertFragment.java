package itg8.com.nowzonedesigndemo.csv;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.AppApplication;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.PresureAccelerometerSateImp;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CSVConvertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CSVConvertFragment extends Fragment implements CsvController.OnFileWriteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.lblStartDate)
    TextView lblStartDate;
    @BindView(R.id.lblDDMMYYY)
    TextView lblDDMMYYY;
    @BindView(R.id.lblStartTime)
    TextView lblStartTime;
    @BindView(R.id.lblHHMMSS)
    TextView lblHHMMSS;
    @BindView(R.id.lblEndDate)
    TextView lblEndDate;
    @BindView(R.id.lblEndDDMMYYY)
    TextView lblEndDDMMYYY;
    @BindView(R.id.lblEndTime)
    TextView lblEndTime;
    @BindView(R.id.lblEndHHMMSS)
    TextView lblEndHHMMSS;
    @BindView(R.id.ll_datetime)
    LinearLayout llDatetime;
    @BindView(R.id.btnExport)
    Button btnExport;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.txtProgressVal)
    TextView txtProgressVal;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;

    private Calendar startDateTime = Calendar.getInstance();
    private Calendar endDateTime = Calendar.getInstance();
    private boolean hasView;


    public CSVConvertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CSVConvertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CSVConvertFragment newInstance(String param1, String param2) {
        CSVConvertFragment fragment = new CSVConvertFragment();
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

    DatePickerDialog datePickerStartDialog;
    DatePickerDialog datePickerEndDialog;
    TimePickerDialog timePickerStartDialog;
    TimePickerDialog timePickerEndDialog;


    private void setDate(Calendar startDateTime, int dayOfMonth, int monthOfYear, int year) {
        startDateTime.set(Calendar.YEAR, year);
        startDateTime.set(Calendar.MONTH, monthOfYear);
        startDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void setTime(Calendar startDateTime, int hourOfDay, int minute) {
        startDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        startDateTime.set(Calendar.MINUTE, minute);
    }

    CsvController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_csvconvert, container, false);
        unbinder = ButterKnife.bind(this, view);
        hasView=true;
        initDateTimePicker();
        lblDDMMYYY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerStartDialog.show();
            }
        });

        lblEndDDMMYYY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerEndDialog.show();
            }
        });

        lblHHMMSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerStartDialog.show();
            }
        });

        lblEndHHMMSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerEndDialog.show();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetText();
            }
        });


        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller = new CsvController(new PresureAccelerometerSateImp(AppApplication.getInstance()), CSVConvertFragment.this);
                controller.exportData(startDateTime, endDateTime);
            }
        });


        return view;
    }

    private void resetText() {
        lblDDMMYYY.setText("dd-MM-yyyy");
        lblEndDDMMYYY.setText("dd-MM-yyyy");
        lblHHMMSS.setText("HH : MM : SS");
        lblEndHHMMSS.setText("HH : MM : SS");
        startDateTime=Calendar.getInstance();
        endDateTime=Calendar.getInstance();
    }

    private void initDateTimePicker() {

        datePickerStartDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        setDate(startDateTime, dayOfMonth, monthOfYear, year);
                        setDateValue(startDateTime, lblDDMMYYY);

                    }
                }, startDateTime.get(Calendar.YEAR), startDateTime.get(Calendar.MONTH), startDateTime.get(Calendar.DAY_OF_MONTH));


        datePickerEndDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        setDate(endDateTime, dayOfMonth, monthOfYear, year);
                        setDateValue(endDateTime, lblEndDDMMYYY);

                    }
                }, endDateTime.get(Calendar.YEAR), endDateTime.get(Calendar.MONTH), endDateTime.get(Calendar.DAY_OF_MONTH));

        timePickerStartDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        setTime(startDateTime, hourOfDay, minute);
                        setTimeValue(startDateTime, lblHHMMSS);
                    }
                }, startDateTime.get(Calendar.HOUR_OF_DAY), startDateTime.get(Calendar.MINUTE), false);


        timePickerEndDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        setTime(endDateTime, hourOfDay, minute);
                        setTimeValue(endDateTime, lblEndHHMMSS);

                    }
                }, endDateTime.get(Calendar.HOUR_OF_DAY), endDateTime.get(Calendar.MINUTE), false);

    }

    private void setDateValue(Calendar startDateTime, TextView lblDDMMYYY) {
        lblDDMMYYY.setText(CommonMethod.getDateFromTMP(startDateTime.getTimeInMillis()));
    }

    private void setTimeValue(Calendar startDateTime, TextView HHMMSS) {
        HHMMSS.setText(CommonMethod.getTimeFromTMPAmPm(startDateTime.getTimeInMillis()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasView=false;
        unbinder.unbind();
    }

    @Override
    public void onFileWriteStart() {
        if(hasView)
            progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopped() {
        if(hasView)
            progressbar.setVisibility(View.GONE);
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if(hasView)
            txtProgressVal.setText("Error: "+e.getMessage());
    }

    @Override
    public void onProgress(int i) {
        if (txtProgressVal != null)
            if (i == 0)
                txtProgressVal.setText("Completed... Go to Nowzone folder in your storage to get your file...");
            else
                txtProgressVal.setText(String.valueOf(i));
    }
}
