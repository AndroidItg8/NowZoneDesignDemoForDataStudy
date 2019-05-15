package itg8.com.nowzonedesigndemo.sleep;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.db.DBManager;
import itg8.com.nowzonedesigndemo.db.DbHelper;
import itg8.com.nowzonedesigndemo.db.tbl.TblSleep;
import itg8.com.nowzonedesigndemo.home.HomeActivity;
import itg8.com.nowzonedesigndemo.sleep.adapter.SleepDataAdapter;
import itg8.com.nowzonedesigndemo.sleep.widget_custom_progressbar.ProgressItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleepFragment extends Fragment implements View.OnClickListener, SleepDataAdapter.SleepItemClickListener {

    private static final String TAG = "SleepFragment";


    Unbinder unbinder;
    @BindView(R.id.imgArrowLeft)
    ImageView imgArrowLeft;
    @BindView(R.id.imgArrowRight)
    ImageView imgArrowRight;
    @BindView(R.id.rv_sleep_data)
    RecyclerView rvSleepData;
    @BindView(R.id.txtDate)
    TextView txtDate;


    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;
    private Timer timer;
    Dao<TblSleep, Integer> sleepDao;

    private String[] totalDates;
    private int index;
    private SleepDataAdapter adapter;
    private Disposable disposable;

    public SleepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);
        unbinder = ButterKnife.bind(this, view);
        DBManager.init(getActivity());
        DbHelper helper = DBManager.getInstance().getHelper();
        initRv();
        try {
            sleepDao = helper.getSleepDao();
            getDistinctDateList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setArrowListener();
        return view;
    }

    private void initRv() {
        adapter = new SleepDataAdapter(this);
        rvSleepData.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSleepData.setAdapter(adapter);
    }

    private void setArrowListener() {
        imgArrowLeft.setOnClickListener(this);
    }


    private void getDistinctDateList() throws SQLException {
        QueryBuilder<TblSleep, Integer> queryBuilder = sleepDao.queryBuilder();
        queryBuilder.distinct().selectColumns(TblSleep.FIELD_DATE);
        List<TblSleep> list = sleepDao.query(queryBuilder.prepare());
        totalDates = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            totalDates[i] = list.get(i).getDate();
        }
        index = list.size() - 1;
        setText();
        initDownload();
    }

    private void setText() {
        if (totalDates.length > 0 && totalDates.length > index)
            txtDate.setText(totalDates[index]);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == imgArrowLeft) {
            moveDateToLeft();
        } else if (v == imgArrowRight) {
            moveDateToRight();
        }
    }

    private void moveDateToLeft() {
        if (index > 0) {
            --index;
            setText();
            initDownload();
        }
    }

    private void initDownload() {
        showProgress();
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = Observable.create(new ObservableOnSubscribe<List<TblSleep>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TblSleep>> e) throws Exception {
                QueryBuilder<TblSleep, Integer> queryBuilder = sleepDao.queryBuilder();
                queryBuilder.where().eq(TblSleep.FIELD_DATE, totalDates[index]);
                List<TblSleep> list = sleepDao.query(queryBuilder.prepare());
                e.onNext(list);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<TblSleep>>() {
                    @Override
                    public void accept(List<TblSleep> tblSleeps) throws Exception {
                        adapter.addSleep(tblSleeps);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void showProgress() {

    }

    private void moveDateToRight() {
        if (index < totalDates.length) {
            ++index;
            setText();
            initDownload();
        }
    }

    @Override
    public void onItemClicked(TblSleep sleep, int position) {
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setFragment(SleepDetailFragment.newInstance(sleep, ""));
        }
    }
}
