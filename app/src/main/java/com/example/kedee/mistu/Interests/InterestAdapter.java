package com.example.kedee.mistu.Interests;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedee.mistu.R;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.MyViewHolder> {

    private List<Interest> interestList;
    private MyViewHolder holder;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView pic;
        CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            name= (TextView) view.findViewById(R.id.interest_name);
            pic = (ImageView) view.findViewById(R.id.interest_image);
            checkBox = (CheckBox) view.findViewById(R.id.interest_checkBox);
        }
    }

    public MyViewHolder getHolder(){
        return holder;
    }


    public InterestAdapter(List<Interest> interestList) {
        this.interestList = interestList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interest_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.holder=holder;
        Interest interest = interestList.get(position);
        holder.name.setText(interest.getName());
        holder.pic.setImageResource(interest.getPicId());
        if(interest.isChecked()){
            holder.checkBox.setChecked(true);
        }
        else{
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }


}
