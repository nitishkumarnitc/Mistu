package com.example.kedee.mistu.settings;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Reset extends AppCompatActivity {
    private int position;
    private String itemName;
    private EditText editText1,editText2;
    private HashMap<String,String> userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        position=getIntent().getIntExtra("pos",-1);
        itemName=getIntent().getStringExtra("itemName");
        getSupportActionBar().setTitle(itemName);
        setView();
        setUserDetails();
    }
    private void setUserDetails(){
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        userDetails=new HashMap<>();
        userDetails=db.getUserDetails();
    }

    private void setView(){
        editText1=(EditText)findViewById(R.id.reset_editText1);
        editText2=(EditText)findViewById(R.id.reset_editText2);
        switch (position){
            case 0:
                if(editText1!=null) editText1.setHint("Old Password");
                if(editText2!=null) editText2.setHint("New Password");
                break;

            case 1:
                editText1.setVisibility(View.GONE);
                if(editText2!=null) editText2.setHint("New Email ID");
                break;

            case 2:
                editText1.setVisibility(View.GONE);
                if(editText2!=null) editText2.setHint("New Contact Number");
                break;

            case 3:
                if(editText1!=null) editText1.setHint("New First Name");
                if(editText2!=null) editText2.setHint("New Last Name");
                break;

            case 4:
                break;
        }
    }

    public void onClickDone(View v) {
        String itemName=null;
        String string1=null,string2=null;
        int currentUserId=Integer.parseInt(userDetails.get("userId"));
        String email=userDetails.get("email");
        switch (position) {
            case 0:
                itemName="chgPassword";
                string1=editText1.getText().toString();
                string2=editText2.getText().toString();
                break;

            case 1:
                itemName="chgEmail";
                string2=editText2.getText().toString();
                break;

            case 2:
                itemName="chgContact";
                string2=editText2.getText().toString();
                break;

            case 3:
                itemName="chgName";
                string1=editText1.getText().toString();
                string2=editText2.getText().toString();
                break;

        }
        JSONObject values=new JSONObject();
        try {
            values.put("tag",itemName);
            values.put("userID",currentUserId);
            values.put("email",email);
            values.put("string1",string1);
            values.put("string2",string2);

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
    }

    private class BackgroundTask extends AsyncTask<JSONObject,Void,JSONObject> {
        private String helpURL;
        JSONParser jsonParser;
        @Override
        protected void onPreExecute() {
            helpURL="http://www.mistu.org/login/";
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
            /*
            JSONArray jsonArray;
            int size;
            jsonArray=result.getJSONArray("server_response");
            size=jsonArray.length();
            if(size<=0){
                return  false;
            }

            JSONObject jo=jsonArray.getJSONObject(0);
            jo.getString("interest");
            */

            Log.i("SettingResult",result.toString());

            return true;


        }
    }
}
