package itg8.com.nowzonedesigndemo.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import itg8.com.nowzonedesigndemo.R;

/**
 * Created by Android itg 8 on 9/16/2017.
 */

public class NavDrawerListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
     LayoutInflater mInflater;
     private enum ItemList
     {
         ITEM_LIST,
         HEADER_ITEM
     }

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof  NavDrawerItem)
            return ItemList.ITEM_LIST.ordinal();
        else
            return ItemList.HEADER_ITEM.ordinal();


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        int rowType = getItemViewType(position);
        View View;
        if (convertView == null) {
            holder = new ViewHolder();

            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_navigation_view,null);
                 holder.imgView = (ImageView) convertView.findViewById(R.id.icon);
                    holder.textView = (TextView) convertView.findViewById(R.id.title);
                    holder.imgView .setImageResource(navDrawerItems.get(position).getIcon());
                    holder.textView.setText(navDrawerItems.get(position).getTitle());

                    break;
                case TYPE_HEADER:
                    convertView = mInflater.inflate(R.layout.item_navigation_header, null);
                    holder.imgView = (ImageView) convertView.findViewById(R.id.img_icon);
                    holder.textView = (TextView) convertView.findViewById(R.id.txt_title);

//                    holder.imgView .setImageResource(navDrawerItems.get(position).getHeaderList().getIcon());
//                    holder.textView.setText(navDrawerItems.get(position).getHeaderList().getTitle());

                    break;
            }
             convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;


//        if (convertView == null) {
//            LayoutInflater mInflater = (LayoutInflater)
//                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(R.layout.item_navigation_view, null);
////            convertView = mInflater.inflate(R.layout.item_navigation_header, null);
//        }
//
//        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
//        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
//
//        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
//        txtTitle.setText(navDrawerItems.get(position).getTitle());
//
//        return convertView;
    }
     public  static class ViewHolder
     {
         TextView textView;
         ImageView imgView;
     }
}
