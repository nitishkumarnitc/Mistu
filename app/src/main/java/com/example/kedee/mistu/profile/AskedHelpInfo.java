package com.example.kedee.mistu.profile;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.Contact;
import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ahr.AHRFragmentListAdapter;
import com.example.kedee.mistu.ahr.AHRFragmentListItem;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AskedHelpInfo extends AppCompatActivity {
    private  int helpId;
    private CheckBox done;
    private int currentUserId;

    private int getCurrentUserId(){
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        return (Integer.parseInt(db.getUserAttr("userId")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title=getIntent().getStringExtra("TITLE");
        helpId=getIntent().getIntExtra("HELPID",0);
        setContentView(R.layout.activity_asked_help_info);
        currentUserId=getCurrentUserId();

        TextView textView=(TextView)findViewById(R.id.asked_help_details_title);
        textView.setText(title);

        ImageView imgView=(ImageView)findViewById(R.id.asked_help_details_view);
        if(imgView!=null){
            imgView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //show details of help request
                }
            });
        }
        done=(CheckBox)findViewById(R.id.asked_help_details_checkBox);
        View helpDone=findViewById(R.id.asked_help_details_done_container);
        if(helpDone!=null){
            helpDone.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(done.isChecked()){
                        done.setChecked(false);
                    }else{
                        done.setChecked(true);
                    }
                }
            });
        }
        databaseTransaction();
    }

    private void databaseTransaction(){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","getHelpers");
            values.put("HELPID",helpId);
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
            helpURL = "http://mistu.org/get_help_notif/helpers_helpReq.php";
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
            final ArrayList<HelperDetailListItem> listItems=new ArrayList<>(); //array to store details of help Requests by users.
            HelperDetailListAdapter adapter;
            int count=0;

            Log.d("HELPERs",result.toString());

            JSONArray jsonArray;
            int size;
            jsonArray=result.getJSONArray("server_response");
            size=jsonArray.length();

            if(size==0){
                return;
            }
            for(int i=0;i<size;i++){
                JSONObject jo=jsonArray.getJSONObject(i);

                String fname=jo.getString("fname");
                String lname=jo.getString("lname");
                String stream=jo.getString("stream");
                String dept=jo.getString("department");
                int helperId=Integer.parseInt(jo.getString("helperId"));

                fname=fname.substring(0,1).toUpperCase() + fname.substring(1);
                lname=lname.substring(0,1).toUpperCase() + lname.substring(1);
                String name=fname+" "+lname;
                String branchStream=stream+" , "+dept;

                listItems.add(new HelperDetailListItem(helperId,branchStream,name));
            }

            adapter = new HelperDetailListAdapter(listItems,getApplicationContext());

            ListView listView=(ListView)findViewById(R.id.asked_help_details_helpers_list);
            if(listView==null){
                Log.e("EMPTY", "List view is empty");
            }else {
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //list item on click :)
                        Intent intent=new Intent(getApplicationContext(), Contact.class);
                        intent.putExtra("TYPE",1);
                        intent.putExtra("USERID",listItems.get(position).getHelperId());
                        intent.putExtra("HELPIE_ID",currentUserId);
                        intent.putExtra("HELP_ID",helpId);
                        startActivity(intent);
                    }
                });
            }


        }
    }


}
