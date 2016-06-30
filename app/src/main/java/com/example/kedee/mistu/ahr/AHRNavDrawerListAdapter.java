package com.example.kedee.mistu.ahr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedee.mistu.R;

import java.util.ArrayList;


public class AHRNavDrawerListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AHRNavDrawerItem> navDrawerItems;

    public AHRNavDrawerListAdapter(Context context, ArrayList<AHRNavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.ahr_drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.ahr_nd_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.ahr_nd_title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.ahr_nd_counter);

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if (navDrawerItems.get(position).getCounterVisibility()) {
            txtCount.setText(navDrawerItems.get(position).getCount());
        } else {
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }
}
