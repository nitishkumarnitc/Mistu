package com.example.kedee.mistu.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ServerInteraction;
import com.example.kedee.mistu.ahr.HRDetailView;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NotiREQListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NotiREQListItem> listItems;
    private ArrayList<ImageView> picList=new ArrayList<>();

    NotiREQListAdapter(Context context,ArrayList<NotiREQListItem> listItems){
        this.context=context;
        this.listItems=listItems;
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
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.noti_request_list_item,null);
        }

        TextView reqInfo=(TextView)convertView.findViewById(R.id.noti_req_info);
        TextView title=(TextView)convertView.findViewById(R.id.noti_req_list_title);
        picList.add((ImageView)convertView.findViewById(R.id.noti_req_list_item_img));

        final NotiREQListItem item=listItems.get(position);
        new DownloadImage(item.getHelpieId(),position).execute();

        String name=item.getName();
        String helpInfo="Seems like you can help "+"<b>" + name + "</b>"+".";
        reqInfo.setText(Html.fromHtml(helpInfo));
        title.setText(item.getTitle());



        ImageView viewDetail=(ImageView)convertView.findViewById(R.id.noti_req_view_img);
        if(viewDetail!=null){
            viewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, HRDetailView.class);
                    intent.putExtra("NAME",item.getName());
                    intent.putExtra("BRANCH_STREAM",item.getBranchStream());
                    intent.putExtra("CAT",item.getCategory());
                    intent.putExtra("TITLE",item.getTitle());
                    intent.putExtra("DES",item.getDescription());
                    intent.putExtra("TAG1",item.getTag1());
                    intent.putExtra("TAG2",item.getTag2());
                    intent.putExtra("TAG3",item.getTag3());
                    intent.putExtra("HELPID",item.getHelpId());
                    intent.putExtra("HELPIEID",item.getHelpieId());
                    intent.putExtra("ACCEPT",item.getIsAccepted());
                    intent.putExtra("NOTIID",item.getNotiId());
                    context.startActivity(intent);

                }
            });
        }

        return convertView;
    }

    private void sendDataToServer(NotiREQListItem item){
        JSONObject values=new JSONObject();
        String helpURL="http://mistu.org/processNotiClicks.php/";
        try {
            values.put("CODE","notiReqAccept");
            values.put("USER_ID",item.getCurrentUserId());
            values.put("HELP_ID",item.getHelpId());
            values.put("NOTI_ID",item.getNotiId());
            values.put("HELPIE_ID",item.getHelpieId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerInteraction interaction=new ServerInteraction(context,helpURL);
        interaction.sendDataToServer(values);
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
