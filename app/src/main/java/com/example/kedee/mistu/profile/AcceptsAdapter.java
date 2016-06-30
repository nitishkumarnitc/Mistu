package com.example.kedee.mistu.profile;

import android.app.Activity;
import android.content.Context;
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

import com.example.kedee.mistu.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class AcceptsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProfileListItem> listItems=new ArrayList<>();
    private ArrayList<ImageView> picList=new ArrayList<>();

    public AcceptsAdapter(Context context, ArrayList<ProfileListItem> listItems) {
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
            LayoutInflater mInflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=mInflater.inflate(R.layout.profile_accepts_list_item,null);
        }
        ProfileListItem item=listItems.get(position);
        TextView category=(TextView)convertView.findViewById(R.id.profile_accepts_item_category);
        TextView title=(TextView)convertView.findViewById(R.id.profile_accepts_item_title);
        TextView name=(TextView)convertView.findViewById(R.id.profile_accepts_item_helpie_name);

        picList.add((ImageView)convertView.findViewById(R.id.profile_accepts_item_pic));
        new DownloadImage(item.getUserId(),position).execute();

        category.setText(item.getCategory());
        title.setText(item.getTitle());
        name.setText(item.getFname());


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
