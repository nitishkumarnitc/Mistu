package com.example.kedee.mistu.ahr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AHRFragmentListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AHRFragmentListItem> fragListItems;
    private AHRFragmentListItem item;
    private int currentUserId;
    private Button acceptButton;
    private ArrayList<ImageView> hrPicList=new ArrayList<>();

    private int getCurrentUserId(){
        DatabaseHandler db=new DatabaseHandler(context);
        return (Integer.parseInt(db.getUserAttr("userId")));
    }

    public AHRFragmentListAdapter(Context context, ArrayList<AHRFragmentListItem> fragListItems) {
        this.context = context;
        this.fragListItems = fragListItems;
        this.currentUserId=getCurrentUserId();
    }


    @Override
    public int getCount() {
        return fragListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return fragListItems.get(position);
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
            convertView = mInflater.inflate(R.layout.ahr_fragment_list_item, null);
        }

        View layout=convertView.findViewById(R.id.ahr_frag_list_rel_lay);
        hrPicList.add((ImageView)convertView.findViewById(R.id.hr_pic));
        new DownloadImage(fragListItems.get(position).getHelpieId(),position).execute();
        TextView hrName=(TextView)convertView.findViewById(R.id.hr_name);
        TextView hrStreamBranch=(TextView)convertView.findViewById(R.id.hr_stream_branch);
        TextView hrTitle=(TextView)convertView.findViewById(R.id.hr_title);


        hrName.setText(fragListItems.get(position).getName());
        hrStreamBranch.setText(fragListItems.get(position).getStreamBranch());
        hrTitle.setText(fragListItems.get(position).getHrTitle());


        ImageView viewReq=(ImageView)convertView.findViewById(R.id.ahr_view_image);
        viewReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,HRDetailView.class);

                item= fragListItems.get(position);

                intent.putExtra("NAME",item.getName());
                intent.putExtra("BRANCH_STREAM",item.getStreamBranch());
                intent.putExtra("CAT",item.getCategory());
                intent.putExtra("TITLE",item.getHrTitle());
                intent.putExtra("DES",item.getHrDetails());
                intent.putExtra("HELPID",item.getHelpReqId());
                intent.putExtra("HELPIEID",item.getHelpieId());
                intent.putExtra("TAG1",item.getTag1());
                intent.putExtra("TAG2",item.getTag2());
                intent.putExtra("TAG3",item.getTag3());


                context.startActivity(intent);
            }
        });
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
            hrPicList.get(position).setImageBitmap(b);
            //Toast.makeText(getApplicationContext(),"Inside on Post Execute",Toast.LENGTH_LONG).show();
        }
    }

}

