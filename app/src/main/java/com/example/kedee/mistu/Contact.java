package com.example.kedee.mistu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.notification.NotiACCListItem;
import com.example.kedee.mistu.profile.HelperDetailListAdapter;
import com.example.kedee.mistu.profile.HelperDetailListItem;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Contact extends AppCompatActivity {
    private String name;
    private String branchStream;
    private int userId,helpId,helpieId,notiId;
    private ImageView userPic;
    private TextView emailText,phoneText,nameText,branchText;
    private View emailContainer,phoneContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int type;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        type=getIntent().getIntExtra("TYPE",-1);
        if(type==0){
            //contact class called from notification fragment
            userId=getIntent().getIntExtra("USERID",0);
            helpId=getIntent().getIntExtra("HELP_ID",0);
            helpieId=getIntent().getIntExtra("HELPIE_ID",0);
            notiId=getIntent().getIntExtra("NOTI_ID",0);

            Log.i("NOTI_DONE","<------"+type+" "+ userId+" "+helpieId+" "+helpId +" "+notiId+" ----------->");


        }
        else if(type==1){
            //contact class called from profile Asked fragment
            userId=getIntent().getIntExtra("USERID",0);
            helpId=getIntent().getIntExtra("HELP_ID",0);
            helpieId=getIntent().getIntExtra("HELPIE_ID",0);
            notiId= -1; // to show the server to take appropriate action
            Log.i("PROF_DONE","<------"+type+" "+ userId+" "+helpieId+" "+helpId +notiId+" ----------->");

        }
        else{
            finish();
        }

        setValuesOfLayout();

    }

    private void setValuesOfLayout(){
        userPic=(ImageView)findViewById(R.id.contact_pic);
        new DownloadImage(userId).execute();

        nameText=(TextView)findViewById(R.id.contact_name);
        branchText=(TextView)findViewById(R.id.contact_branch);

        emailText=(TextView)findViewById(R.id.contact_emailID);
        phoneText=(TextView)findViewById(R.id.contact_phoneNum);

        emailContainer=findViewById(R.id.contact_email_container);
        phoneContainer=findViewById(R.id.contact_phone_num_container);
        databaseTransaction();

    }

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        Bitmap image=null;
        int user_id;

        public DownloadImage(int user_id) {
            this.user_id=user_id;
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
            userPic.setImageBitmap(b);
        }
    }


    private void databaseTransaction(){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","getContact");
            values.put("USERID",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BackgroundTask().execute(values);
        } else {
            Toast.makeText(this, "Network Connection Failed", Toast.LENGTH_LONG).show();
        }
    }


    private class BackgroundTask extends AsyncTask<JSONObject,Void,JSONObject> {
        private String helpURL;
        JSONParser jsonParser;

        @Override
        protected void onPreExecute() {
            helpURL = "http://mistu.org/getUserDetails.php";
            jsonParser = new JSONParser();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            try {
                return jsonParser.getJsonFromUrl(helpURL, "POST", params[0]);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try{
                displayAHRList(result);
            }catch (Exception e){

            }
        }

        private void displayAHRList(JSONObject result)throws JSONException{

            Log.d("ContactInfo",result.toString());

            JSONArray jsonArray;
            int size;
            jsonArray=result.getJSONArray("server_response");
            size=jsonArray.length();

            if(size==0){
                return;
            }

            JSONObject jo=jsonArray.getJSONObject(0);

            String fname=jo.getString("fname");
            String lname=jo.getString("lname");
            String stream=jo.getString("stream");
            String dept=jo.getString("dept");
            final String mobile=jo.getString("mobile");
            String city=jo.getString("city");
            final String emailId=jo.getString("emailId");

            fname=fname.substring(0,1).toUpperCase() + fname.substring(1);
            lname=lname.substring(0,1).toUpperCase() + lname.substring(1);
            String name=fname+" "+lname;
            String branchStream=stream+" , "+dept;

            nameText.setText(name);
            branchText.setText(branchStream);
            emailText.setText(emailId);
            phoneText.setText(mobile);

            emailContainer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String[] addresses={emailId};
                    String subject="Thanks for accepting my request on iHelp";
                    composeEmail(addresses,subject);
                    sendConfirmationToServer();
                }

                public void composeEmail(String[] addresses, String subject) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });

            phoneContainer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    dialPhoneNumber(mobile);
                    sendConfirmationToServer();

                }

                public void dialPhoneNumber(String phoneNumber) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
        }

        private void sendConfirmationToServer(){
            Log.i("SERVER_INTERACTION","YES");
            ServerInteraction verify = new ServerInteraction(getApplicationContext(),"http://mistu.org/get_help_notif/processNotiClicks.php");
            JSONObject values=new JSONObject();
            try {
                values.put("CODE","userContacted");
                values.put("USER_ID",helpieId); //who is contacting helper ? helpie :)
                values.put("HELP_ID",helpId);
                values.put("NOTI_ID",notiId);
                values.put("HELPIE_ID",helpieId);
                values.put("HELPER_ID",userId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            verify.sendDataToServer(values);
        }

    }
}
