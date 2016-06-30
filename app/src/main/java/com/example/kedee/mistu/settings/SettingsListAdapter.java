package com.example.kedee.mistu.settings;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedee.mistu.R;

import java.util.ArrayList;

public class SettingsListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<SettingsListItem> listItems;

    public SettingsListAdapter(ArrayList<SettingsListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
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
            convertView = mInflater.inflate(R.layout.settings_list_item, null);
        }
        SettingsListItem item=listItems.get(position);
        TextView textView=(TextView)convertView.findViewById(R.id.settings_name);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.settings_pic);

        textView.setText(item.getItemName());
        imageView.setImageResource(item.getPicId());

        return  convertView;
    }
}
