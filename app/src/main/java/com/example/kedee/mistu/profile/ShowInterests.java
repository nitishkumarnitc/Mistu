package com.example.kedee.mistu.profile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ahr.AHR;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowInterests extends AppCompatActivity {
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_interests);
        currentUserId=getIntent().getIntExtra("USERID",0);
        sendToServer();
    }

    private void sendToServer(){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","userInterests");
            values.put("USER_ID",currentUserId);

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
            helpURL="http://mistu.org/get_help_notif/user_interest.php";
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
            try{
                if( parseJsonServerResponse(result)){
                    //print Log
                }else{
                    Toast.makeText(getApplicationContext(),"Server Failed to process request, Please try again !!!",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }


        }

        private boolean parseJsonServerResponse(JSONObject result)throws JSONException{

            ArrayList<ShowInterestsListItem> listItems=new ArrayList<>(); //array to store details of help Requests by users.
            ShowInterestsListAdapter adapter;
            int count=0;

            JSONArray jsonArray;
            int size;
            jsonArray=result.getJSONArray("server_response");
            size=jsonArray.length();
            if(size<=0){
                return  false;
            }
            for(int i=0;i<size;i++){
                JSONObject jo=jsonArray.getJSONObject(i);
                listItems.add(new ShowInterestsListItem(false,jo.getString("interest")));
            }
            adapter=new ShowInterestsListAdapter(getApplicationContext(),listItems);
            ListView listView=(ListView)findViewById(R.id.show_interests_list_view);
            if(listView==null){
                Log.e("EMPTY", "List view is empty");
            }else {
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //list item on click :)
                    }
                });
            }

            return true;

        }
    }
}
