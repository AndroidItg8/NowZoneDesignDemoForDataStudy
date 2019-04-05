package itg8.com.nowzonedesigndemo.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by swapnilmeshram on 15/11/17.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder{

    private final OnRecyclerviewClickListener<T> listener;

    public BaseViewHolder(View itemView, OnRecyclerviewClickListener<T> listener) {
        super(itemView);
        this.listener = listener;
    }

    public void onItemClicked(int position,T t){
        if(listener!=null)
            listener.onClick(position,t);
    }
}
