package com.example.kedee.mistu.search;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.profile.HelperDetailListItem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<SearchResultUser> listItems=new ArrayList<>();
    private ArrayList<ImageView> picList=new ArrayList<>();

    public SearchResultAdapter(Context context, ArrayList<SearchResultUser> listItems) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.helper_details_list_item, null);
        }
        ImageView tick=(ImageView)convertView.findViewById(R.id.user_confirmed);
        TextView name=(TextView)convertView.findViewById(R.id.helper_detail_name);
        TextView branch=(TextView)convertView.findViewById(R.id.helper_detail_branch);
        final ImageView contact=(ImageView)convertView.findViewById(R.id.helper_detail_contact);

        picList.add((ImageView)convertView.findViewById(R.id.helper_detail_pic));



        SearchResultUser item=listItems.get(position);

        new DownloadImage(item.getUserId(),position).execute();
        name.setText(item.getName());
        branch.setText(item.getBranchStream());
        if(!item.getHasUserConfirmed()){
            tick.setVisibility(View.GONE);
        }


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
