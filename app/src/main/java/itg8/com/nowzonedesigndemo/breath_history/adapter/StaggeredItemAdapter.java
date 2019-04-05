package itg8.com.nowzonedesigndemo.breath_history.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.nowzonedesigndemo.R;
import itg8.com.nowzonedesigndemo.breath_history.model.TodayStaModel;

/**
 * Created by Android itg 8 on 9/22/2017.
 */

public class StaggeredItemAdapter extends RecyclerView.Adapter<StaggeredItemAdapter.ViewHolder> {

     private OnTodaysItemClickListener listener;
    private Context mContext;
    private List<TodayStaModel> itemList;
    private Random mRandom = new Random();

    public StaggeredItemAdapter(Context mContext, List<TodayStaModel> itemList, OnTodaysItemClickListener listener ) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.rv_item_today_stac, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.name.getLayoutParams().height = getRandomIntInRange(300, 100);
        holder.cardView.setCardBackgroundColor(itemList.get(position).getColor());
        holder.name.setText(itemList.get(position).getName());

    }

    private int getRandomIntInRange(int max, int min) {
        return mRandom.nextInt((max - min) + min) + min;

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.relative)
        RelativeLayout relative;
        @BindView(R.id.cardView)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(v, getAdapterPosition());
                }
            });
        }
    }
}
