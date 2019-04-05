package itg8.com.nowzonedesigndemo.breath_history.fragment;


import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.adapter.BreathsHistoryAdapter;
import itg8.com.nowzonedesigndemo.db.tbl.TblState;
import itg8.com.nowzonedesigndemo.db.tbl.TblStepCount;
import itg8.com.nowzonedesigndemo.utility.ActivityState;
import itg8.com.nowzonedesigndemo.utility.BreathState;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BreathsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BreathsHistoryFragment extends Fragment implements BreathsHistoryAdapter.OnItemHistoryClickedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "ARG_PARAM3";
    private static final String ARG_PARAM4 = "ARG_PARAM4";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;


    // TODO: Rename and change types of parameters
    private BreathState type;
    private ActivityState typeStepState;
    private android.content.Context mContext;
    private List<TblState> listState;
    private List<TblStepCount> listStepState;



    public BreathsHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param listState

     * @return A new instance of fragment BreathsHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BreathsHistoryFragment newInstance(BreathState param1, List<TblState> listState ) {
        BreathsHistoryFragment fragment = new BreathsHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,(param1));
        //args.putSerializable(ARG_PARAM4,(typeStep));
        args.putParcelableArrayList(ARG_PARAM2, (ArrayList<? extends Parcelable>) listState);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (BreathState) getArguments().getSerializable(ARG_PARAM1);
            listState = getArguments().getParcelableArrayList(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_breaths_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        setBgColor();
        return view;
    }

    private void setBgColor() {
        int imgRes = 0;
        int cardBgRes = 0;
        for (int i = 0; i < listState.size(); i++) {
            type = BreathState.valueOf(listState.get(i).getState());

            switch (type) {
                case CALM:
                    cardBgRes = (R.drawable.back_gradient_compose);
                    imgRes = R.drawable.calm_streak_icon;
                    break;
                case FOCUSED:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_attentive_half));
                    cardBgRes = (R.drawable.back_gradient_attentive);
                    imgRes = R.drawable.focus_streak_icon;

                    break;
                case STRESS:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_stress_half));
                    cardBgRes = (R.drawable.back_gradient_stress);
                    imgRes = R.drawable.stress_streak_icon;

                    break;
                case SEDENTARY:
//                holder.cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_normal));
                    cardBgRes = (R.drawable.back_gradient_compose);
                    break;
                default:
                    imgRes = R.drawable.stress_streak_icon;
                    cardBgRes = (R.drawable.back_gradient_compose);
            }
            recyclerView.setBackgroundResource(cardBgRes);

        }


        setRecyclerView();


    }

    private void setRecyclerView() {

        BitmapDrawable draw = (BitmapDrawable)getResources().getDrawable(R.drawable.timeline);
        draw.setTileModeY(Shader.TileMode.REPEAT);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        BreathsHistoryAdapter Adapter= null;
        Adapter = new BreathsHistoryAdapter(mContext, listState, this);
        recyclerView.setNestedScrollingEnabled(false);
        //SimpleDivideSeprator dividerItemDecoration = new SimpleDivideSeprator(mContext);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(Adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mContext != null)
            mContext = null;
    }

    @Override
    public void onItemClicked(View view, int position, TblState listState) {

    }

    @Override
    public void onStepItemClicked(View itemView, int adapterPosition, TblStepCount tblStepCount) {

    }
}
