package itg8.com.nowzonedesigndemo.breath_history.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.SingleDetailActivity;
import itg8.com.nowzonedesigndemo.breath_history.adapter.BreathsHistoryAdapter;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.utility.BreathState;
import itg8.com.nowzonedesigndemo.widget.AnimationUtils;
import itg8.com.nowzonedesigndemo.widget.RevealAnimationSetting;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodaysStacDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysStacDetailsFragment extends Fragment implements AnimationUtils.AnimationFinishedListener, AnimationUtils.Dismissable, BreathsHistoryAdapter.OnItemHistoryClickedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "ARG_PARAM3";
    @BindView(R.id.fl_parent)
    RelativeLayout flParent;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private BreathState mParam2;
    private Context mContext;
    private CommonMethod.OnFragmentSendToActivityListener listenerAtcivity;
    private List<TblState> listState;
    private BreathState type;
    private List<TblStepCount> listSteps;


    public TodaysStacDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param type
     * @return A new instance of fragment TodaysStacDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodaysStacDetailsFragment newInstance(int param1, List<TblState> type) {
        TodaysStacDetailsFragment fragment = new TodaysStacDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putParcelableArrayList(ARG_PARAM3, (ArrayList<? extends Parcelable>) type);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename and change types and number of parameters
    public static TodaysStacDetailsFragment newInstance(int param1, BreathState param2, List<TblStepCount> type) {
        TodaysStacDetailsFragment fragment = new TodaysStacDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        args.putParcelableArrayList(ARG_PARAM3, (ArrayList<? extends Parcelable>) type);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1, 0);
            mParam2 = (BreathState) getArguments().getSerializable(ARG_PARAM2);
            if(mParam2==null)
                listState = getArguments().getParcelableArrayList(ARG_PARAM3);
            else
                listSteps=getArguments().getParcelableArrayList(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todays_stac_details, container, false);
        unbinder = ButterKnife.bind(this, view);
//        if (mParam1 != 0)
//           // flParent.setBackgroundColor(ContextCompat.getColor(mContext, mParam1));
        // AnimationUtils.registerCreateShareLinkCircularRevealAnimation(mContext, view, mParam1, mParam2, this);
        //setBgColor();
        setRecyclerView();
        listenerAtcivity.onShowToggle();
        return view;
    }

    private void setBgColor() {
        int imgRes = 0;
        int cardBgRes = 0;
        for (int i = 0; i < listState.size(); i++) {
            type = BreathState.valueOf(listState.get(i).getState());

            switch (type) {
                case CALM:
//                    cardBgRes = (R.drawable.back_gradient_compose);
                    imgRes = R.drawable.calm_streak_icon;
                    cardBgRes = R.color.color_composed_half;

                    break;
                case FOCUSED:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_attentive_half));
//                    cardBgRes = (R.drawable.back_gradient_attentive);
                    imgRes = R.drawable.focus_streak_icon;
                    cardBgRes = R.color.color_attentive_half;
                    break;
                case STRESS:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_stress_half));
//                    cardBgRes = (R.drawable.back_gradient_stress);
                    imgRes = R.drawable.stress_streak_icon;
                    cardBgRes = R.color.color_stress_half;
                    break;
                case SEDENTARY:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_normal));
//                    cardBgRes = (R.drawable.back_gradient_compose);
                    cardBgRes = R.color.color_normal_half;
                    break;
                case ACTIVITY:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_normal));
//                    cardBgRes = (R.drawable.back_gradient_compose);
                    cardBgRes = R.color.color_sleep_half;
                    break;

                default:
                    imgRes = R.drawable.stress_streak_icon;
                    // cardBgRes = (R.drawable.back_gradient_compose);
                    cardBgRes = R.color.color_normal_half;
            }
            recyclerView.setBackgroundResource(cardBgRes);

        }

        setRecyclerView();

    }

    private void setRecyclerView() {
//        BitmapDrawable draw = (BitmapDrawable)getResources().getDrawable(R.drawable.timeline);
//        draw.setTileModeY(Shader.TileMode.REPEAT);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setNestedScrollingEnabled(false);
        if(mParam2==null)
            recyclerView.setAdapter(new BreathsHistoryAdapter(mContext, listState, this));
        else if(mParam2==BreathState.ACTIVITY)
            recyclerView.setAdapter(new BreathsHistoryAdapter(mContext, listSteps, this));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.listenerAtcivity = (CommonMethod.OnFragmentSendToActivityListener) context;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mContext != null) {
            mContext = null;
        }
    }

    @Override
    public void onAnimationFinished() {

    }

    @Override
    public void dismiss(OnDismissedListener listener) {
//        AnimationUtils.startCreateShareLinkCircularRevealExitAnimation(getActivity(), getView(), mParam1, mParam2, new OnDismissedListener() {
//            @Override
//            public void onDismissed() {
//                listener.onDismissed();
//            }
//        });
    }


    @Override
    public void onItemClicked(View view, int position, TblState listState) {
        Intent intent = new Intent(mContext, SingleDetailActivity.class);
        TextView diff = (TextView) view.findViewById(R.id.lbl_hr_value);
        TextView time = (TextView) view.findViewById(R.id.lbl_time);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        BreathState type = BreathState.valueOf(listState.getState());

        diff.setTransitionName(getString(R.string.diff));
        imageView.setTransitionName(getString(R.string.img));
        time.setTransitionName(getString(R.string.time));


        Pair<View, String> pair1 = Pair.create(imageView, imageView.getTransitionName());
        Pair<View, String> pair2 = Pair.create(time, time.getTransitionName());
        Pair<View, String> pair3 = Pair.create(diff, diff.getTransitionName());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair1, pair2, pair3);
        listenerAtcivity.onSingleDetail(intent,listState,options.toBundle());

    }

    @Override
    public void onStepItemClicked(View itemView, int adapterPosition, TblStepCount tblStepCount) {

    }
}
