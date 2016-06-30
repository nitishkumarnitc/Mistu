package com.example.kedee.mistu.profile;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpedFragment extends Fragment {
    private Context context;
    private View rootView;
    private int currentUserId;

    public HelpedFragment() {
        // Required empty public constructor
    }

    private int getCurrentUserId(){
        DatabaseHandler db=new DatabaseHandler(context);
        return (Integer.parseInt(db.getUserAttr("userId")));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=inflater.getContext();
        rootView= inflater.inflate(R.layout.fragment_helped, container, false);
        currentUserId=getCurrentUserId();
        databaseTransaction();
        return rootView;
    }

    private void databaseTransaction(){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","profHelped");
            values.put("USER_ID",currentUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BackgroundTask().execute(values);
        } else {
            Toast.makeText(context, "Network Connection Failed", Toast.LENGTH_LONG).show();
        }
    }


    private class BackgroundTask extends AsyncTask<JSONObject,Void,JSONObject> {
        private String helpURL;
        JSONParser jsonParser;

        @Override
        protected void onPreExecute() {
            helpURL = "http://mistu.org/get_help_notif/user_prof.php";
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
            ArrayList<ProfileListItem> listItems=new ArrayList<>(); //array to store details of help Requests by users.
            AcceptsAdapter adapter;
            int count=0;

            JSONArray jsonArray;
            int size;
            jsonArray=result.getJSONArray("server_response");
            size=jsonArray.length();

            //Log.d("Profile",result.toString());

            if(size==0){
                return;
            }


            for(int i=0;i<size;i++){
                JSONObject jo=jsonArray.getJSONObject(i);

                String fname=jo.getString("fname");
                String lname=jo.getString("lname");
                String sex=jo.getString("sex");
                String stream=jo.getString("stream");
                String dept=jo.getString("department");

                String cat=jo.getString("category");
                String title=jo.getString("title");
                String description=jo.getString("description");
                int helpId=Integer.parseInt(jo.getString("help_id"));
                int helperId=Integer.parseInt(jo.getString("user_id"));

                String tag1;
                String tag2;
                String tag3;

                if(jo.isNull("tag1")){
                    Log.i("TAG","tag 1");
                    tag1="";
                }else{
                    tag1=jo.getString("tag1");
                }

                if(jo.isNull("tag2")){
                    Log.i("TAG","tag 2");
                    tag2="";
                }else{
                    tag2=jo.getString("tag2");
                }

                if(jo.isNull("tag3")){
                    Log.i("TAG","tag 2");
                    tag3="";
                }else{
                    tag3=jo.getString("tag3");
                }

                fname=fname.substring(0,1).toUpperCase() + fname.substring(1);
                lname=lname.substring(0,1).toUpperCase() + lname.substring(1);
                title=title.substring(0,1).toUpperCase()+title.substring(1);
                description=description.substring(0,1).toUpperCase()+description.substring(1);

                listItems.add(new ProfileListItem(dept, cat, description, fname,
                        helpId, lname, stream, sex, tag1,tag2,tag3,title, helperId));


            }

            adapter = new AcceptsAdapter(context,listItems);

            ListView profAccListView=(ListView)rootView.findViewById(R.id.profile_helped_list_view);
            if(profAccListView==null){
                //Log.e("EMPTY", "List view is empty");
            }else {
                profAccListView.setAdapter(adapter);
                profAccListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //list item on click :)
                    }
                });
            }


        }
    }

}
