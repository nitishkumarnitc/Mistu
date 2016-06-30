package com.example.kedee.mistu.profile;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedee.mistu.Contact;
import com.example.kedee.mistu.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HelperDetailListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<HelperDetailListItem> listItems=new ArrayList<>();
    private ArrayList<ImageView> picList=new ArrayList<>();

    public HelperDetailListAdapter(ArrayList<HelperDetailListItem> listItems, Context context) {
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
        if(convertView==null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.helper_details_list_item, null);
        }

        TextView name=(TextView)convertView.findViewById(R.id.helper_detail_name);
        TextView branch=(TextView)convertView.findViewById(R.id.helper_detail_branch);
        final ImageView contact=(ImageView)convertView.findViewById(R.id.helper_detail_contact);
        picList.add((ImageView)convertView.findViewById(R.id.helper_detail_pic));

        //tick is used in search activity, here we are not using it. hence it is hidden
        ImageView tick=(ImageView)convertView.findViewById(R.id.user_confirmed);
        tick.setVisibility(View.GONE);

        HelperDetailListItem item=listItems.get(position);

        new DownloadImage(item.getHelperId(),position).execute();
        name.setText(item.getName());
        branch.setText(item.getBranchStream());

        /*
        contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        }); */




        return convertView;
    }

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        Bitmap image=null;
        int user_id;
        int position;

        public DownloadImage(int user_id ,int pos) {
            this.user_id=user_id;
            this.position=pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url_pic="http://www.mistu.org/email/getimage.php?id="+user_id;
            URL url = null;
            try {
                url = new URL(url_pic);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            picList.get(position).setImageBitmap(b);
        }
    }
}
