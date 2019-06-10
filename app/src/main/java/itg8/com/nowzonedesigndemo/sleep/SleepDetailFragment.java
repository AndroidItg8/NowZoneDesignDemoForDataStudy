package itg8.com.nowzonedesigndemo.sleep;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.DataModelPressure;
import itg8.com.nowzonedesigndemo.db.DBManager;
import itg8.com.nowzonedesigndemo.db.DbHelper;
import itg8.com.nowzonedesigndemo.db.tbl.TblSleep;
import itg8.com.nowzonedesigndemo.sleep.adapter.SleepDetailAdapter;
import itg8.com.nowzonedesigndemo.sleep.model.SleepActivityModel;
import itg8.com.nowzonedesigndemo.utility.sleep_algo.SleepCalculationImp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SleepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepDetailFragment extends Fragment implements SleepCalculationImp.OnSleepDetailListner {

    private static final String TAG = "SleepDetailFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.rvMovements)
    RecyclerView rvMovements;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private TblSleep mSleep;
    private String mParam2;
    private SleepDetailAdapter adapter;


    public SleepDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SleepDetailFragment newInstance(TblSleep param1, String param2) {
        SleepDetailFragment fragment = new SleepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSleep = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sleep_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(mSleep==null)
            return view;
        txtDate.setText(mSleep.getDate());
       // SleepCalculationImp imp = new SleepCalculationImp(generateDao(), sleepForward.getId(), sleepResultDao);

        initAdapter();
        try {
            downloadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void downloadData() throws SQLException {
        DBManager.init(getActivity());
        DbHelper helper=DBManager.getInstance().getHelper();
        Dao<SleepActivityModel, Integer> dbSleepResult = helper.getSleepResultDao();
        QueryBuilder<SleepActivityModel,Integer> queryBuilder=dbSleepResult.queryBuilder();
        queryBuilder.where().eq(SleepActivityModel.FIELD_SLEEP_ID,mSleep.getId());
        List<SleepActivityModel> modelList=dbSleepResult.query(queryBuilder.prepare());
        onListOfDetailAvail(modelList);
    }

    private void initAdapter() {
        adapter= new SleepDetailAdapter();
        rvMovements.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovements.setAdapter(adapter);
    }

    private Dao<DataModelPressure, Integer> generateDao() {
        DBManager.init(getActivity());
        DbHelper helper = DBManager.getInstance().getHelper();
        try {
            return helper.getDataPresureDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onListOfDetailAvail(List<SleepActivityModel> models) {
        Log.d(TAG, "onListOfDetailAvail: "+new Gson().toJson(models));
        adapter.addSleepDetailList(models);
    }
}
