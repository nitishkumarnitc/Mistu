package com.example.kedee.mistu.profile;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ahr.AHRFragmentListItem;

import java.util.ArrayList;

public class ShowInterestsListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ShowInterestsListItem> listItems;
    private ShowInterestsListItem item;
    private int currentUserId;

    public ShowInterestsListAdapter(Context context, ArrayList<ShowInterestsListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.show_interests_list_item, null);
        }
        item=listItems.get(position);
        TextView textView=(TextView)convertView.findViewById(R.id.show_interest_item_info);
        CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.show_interest_item_checkbox);

        textView.setText(item.getName());
        checkBox.setVisibility(View.GONE);

        return  convertView;
    }
}
