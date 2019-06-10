package itg8.com.nowzonedesigndemo.sleep.adapter;

import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.custom_widget.AutoSizeTextView;
import itg8.com.nowzonedesigndemo.db.tbl.TblSleep;
import itg8.com.nowzonedesigndemo.sleep.widget_custom_progressbar.CustomProgressBar;
import itg8.com.nowzonedesigndemo.sleep.widget_custom_progressbar.DonutProgress;

public class SleepDataAdapter extends RecyclerView.Adapter<SleepDataAdapter.ViewHolder> {

    private List<TblSleep> sleepList;
    private SleepItemClickListener listener;

    public SleepDataAdapter(SleepItemClickListener listener) {
        this.listener = listener;
        sleepList = new ArrayList<>();
    }


    public void addSleep(List<TblSleep> newList) {
        sleepList.clear();
        sleepList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_sleep, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.txtSleepValue.setText(CommonMethod.getTimeFromTMPAmPm(sleepList.get(position).getTimeStart()));
        viewHolder.txtWakeupValue.setText(CommonMethod.getTimeFromTMPAmPm(sleepList.get(position).getTimeEnd()));
    }

    @Override
    public int getItemCount() {
        return sleepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.txt_durations)
        TextView txtDurations;
        @BindView(R.id.txt_sleepValue)
        TextView txtSleepValue;
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.ll_percent)
        LinearLayout llPercent;
        @BindView(R.id.txt_wakeupValue)
        TextView txtWakeupValue;
        @BindView(R.id.txt_wakeup)
        TextView txtWakeup;
        @BindView(R.id.ll_percent_durations)
        LinearLayout llPercentDurations;
        @BindView(R.id.txt_goals)
        TextView txtGoals;
        @BindView(R.id.circularProgressGoal)
        DonutProgress circularProgressGoal;
        @BindView(R.id.ll_goals)
        LinearLayout llGoals;
        @BindView(R.id.rl_main)
        RelativeLayout rlMain;
        @BindView(R.id.txt_break)
        AutoSizeTextView txtBreak;
        @BindView(R.id.custom_progressbar)
        CustomProgressBar customProgressbar;
        @BindView(R.id.txt_awake)
        TextView txtAwake;
        @BindView(R.id.txt_deep)
        TextView txtDeep;
        @BindView(R.id.txt_deep_time)
        TextView txtDeepTime;
        @BindView(R.id.txt_light)
        TextView txtLight;
        @BindView(R.id.txt_light_time)
        TextView txtLightTime;
        @BindView(R.id.rl_overView)
        RelativeLayout rlOverView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(sleepList.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }

    public interface SleepItemClickListener{
        void onItemClicked(TblSleep sleep,int position);
    }

}
