package com.example.kedee.mistu.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedee.mistu.Contact;
import com.example.kedee.mistu.Interests.Interest;
import com.example.kedee.mistu.ProfileView;
import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ServerInteraction;
import com.example.kedee.mistu.services.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NotiACCListAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<NotiACCListItem> listItems;
    private ArrayList<ImageView> helpersPicList=new ArrayList<>();
    private int currentUserId;

    NotiACCListAdapter(Context context,ArrayList<NotiACCListItem> listItems){
        this.context=context;
        this.listItems=listItems;
        DatabaseHandler db=new DatabaseHandler(context);
        currentUserId=Integer.parseInt(db.getUserAttr("userId"));
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
            convertView=inflater.inflate(R.layout.noti_accepts_list_item,null);
        }

        TextView acceptInfo=(TextView)convertView.findViewById(R.id.noti_acc_info);
        TextView shortTitle=(TextView)convertView.findViewById(R.id.noti_acc_list_help_title);
        helpersPicList.add((ImageView)convertView.findViewById(R.id.noti_acc_list_item_img));


        String name=listItems.get(position).getName();
        name= "<b>" + name + "</b> "+" "+"wants to help you !!!";
        String title=" " +listItems.get(position).getTitle();


        acceptInfo.setText(Html.fromHtml(name));
        shortTitle.setText(title);

        new DownloadImage(listItems.get(position).getHelperId(),position).execute();

        //assigning listeners to views

        ImageView helperPic=(ImageView)convertView.findViewById(R.id.noti_acc_list_item_img);
        ImageView contactPic=(ImageView)convertView.findViewById(R.id.noti_phone_logo);
        final NotiACCListItem item=listItems.get(position);


        helperPic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //show profile of the helper (using helper Id)
                Intent intent=new Intent(context , ProfileView.class);
                //fill putExtra to send values to new profile View activity
                context.startActivity(intent);
            }
        });

        contactPic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                NotiACCListItem item=listItems.get(position);
                Intent intent=new Intent(context, Contact.class);
                intent.putExtra("TYPE",0); //type zero means called from notification
                intent.putExtra("USERID",item.getHelperId()); //helperId is user id for contact class
                intent.putExtra("HELP_ID",item.getHelpId());
                intent.putExtra("NOTI_ID",item.getNotiId());
                intent.putExtra("HELPIE_ID",item.getCurrentUserId());
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
         //   Log.i("PICSET",b.toString());
            helpersPicList.get(position).setImageBitmap(b);
            //Toast.makeText(getApplicationContext(),"Inside on Post Execute",Toast.LENGTH_LONG).show();
        }
    }
}

