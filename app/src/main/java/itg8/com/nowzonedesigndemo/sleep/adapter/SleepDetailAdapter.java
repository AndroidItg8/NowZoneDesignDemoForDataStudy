package itg8.com.nowzonedesigndemo.sleep.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.sleep.model.SleepActivityModel;

public class SleepDetailAdapter extends RecyclerView.Adapter<SleepDetailAdapter.ViewHolder> {


    List<SleepActivityModel> list;

    public SleepDetailAdapter() {
        list=new ArrayList<>();
    }

    public void addSleepDetailList(List<SleepActivityModel> models){
        list.clear();
        list.addAll(models);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_sleep_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.txtStartTime.setText(list.get(position).getStartTimeLabel());
            viewHolder.txtEndTime.setText(list.get(position).getEndTimeLabel());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtStartTime)
        TextView txtStartTime;
        @BindView(R.id.view1)
        View view1;
        @BindView(R.id.txtEndTime)
        TextView txtEndTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
