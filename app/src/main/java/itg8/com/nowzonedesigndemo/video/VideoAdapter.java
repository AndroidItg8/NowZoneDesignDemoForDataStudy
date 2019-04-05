package itg8.com.nowzonedesigndemo.video;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import itg8.com.nowzonedesigndemo.R;

/**
 * Created by swapnilmeshram on 01/12/17.
 */

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    @BindView(R.id.title_category)
    TextView titleCategory;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_main_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
