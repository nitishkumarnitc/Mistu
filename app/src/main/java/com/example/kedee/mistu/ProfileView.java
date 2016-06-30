package com.example.kedee.mistu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.ahr.AHR;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileView extends AppCompatActivity {
    private int userId;
    private int picId;
    private String fname,lname;
    private String branchStream;
    private float rating;
    private ArrayList<String> interestList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        setClickListeners();
        setValuesInLayout();
        setListView();
    }

    private void setClickListeners(){
        TextView contact=(TextView)findViewById(R.id.noti_acc_profile_view_contact);
        TextView skip=(TextView)findViewById(R.id.noti_acc_profile_view_skip);

        if(contact!=null){
            contact.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                }
            });

        }
        if(skip!=null){
            skip.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                }
            });
        }
    }

    private void setValuesInLayout(){
        TextView fullName=(TextView)findViewById(R.id.noti_acc_profile_view_helper_full_name);
        TextView branchStr=(TextView)findViewById(R.id.noti_acc_profile_view_helper_branch);
        RatingBar ratingBar=(RatingBar)findViewById(R.id.noti_acc_profile_view_helper_rating);
        ImageView userPic=(ImageView)findViewById(R.id.noti_acc_profile_view_helper_pic);

        //set user pic too after fetching from database

        fullName.setText(fname+" "+lname);
        branchStr.setText(branchStream);
        ratingBar.setRating(rating);
    }

    private void setListView(){

    }

    private void sendToServer(String title,String des){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","userInterests");
            values.put("USERID",userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BackgroundTask().execute(values);
        } else {
            Toast.makeText(this, "Network Connection Failed", Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(context,"Working",Toast.LENGTH_SHORT).show();

    }

    private class BackgroundTask extends AsyncTask<JSONObject,Void,JSONObject> {
        private String helpURL;
        JSONParser jsonParser;
        @Override
        protected void onPreExecute() {
            helpURL="http://mistu.org/askforhelp/getUserInfo.php/";
            jsonParser=new JSONParser();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            try {
                return jsonParser.getJsonFromUrl(helpURL,"POST",params[0]);
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
            if( parseJsonServerResponse(result)==1){
                //get reference to listView
                //set Adapter to listview
                //display listview

            }else{
                Toast.makeText(getApplicationContext(),"Unable to fetch user interests and skills :(",Toast.LENGTH_SHORT).show();
            }

        }

        private int parseJsonServerResponse(JSONObject result){
            int res=0;
            Log.e("RESULT",result.toString());
            try {
                JSONArray jsonArray = result.getJSONArray("server_response");
                JSONObject jo=jsonArray.getJSONObject(0);
                String success=jo.getString("success");
                Log.e("SUCCESS",success);
                if(success.equals("yes")){
                    res=1;
                    //extract interest from json array
                    //put the interests into arrayList
                    //return

                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
            return res;
        }
    }
}
