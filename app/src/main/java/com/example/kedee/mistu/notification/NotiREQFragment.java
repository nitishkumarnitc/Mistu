package com.example.kedee.mistu.notification;


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
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotiREQFragment extends Fragment {
    private Context context;
    private View rootView;
    private int currentUserId;

    public NotiREQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=inflater.getContext();
        rootView=inflater.inflate(R.layout.fragment_noti_req, container, false);
        currentUserId=getArguments().getInt("CURRENT_USER_ID");
        startDBTransaction();
        return rootView;
    }


    private void startDBTransaction(){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","notiReq");
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
            helpURL = "http://mistu.org/get_help_notif/user_noti.php";
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
            if (parseJsonServerResponse(result) == 1) {
                //print some info
            } else {
               //print some info
            }
        }

        private int parseJsonServerResponse(JSONObject result) {
            int res = 0;
            try {
                displayNotiReqList(result);
            } catch (JSONException ex) {
                //
            }
            return res;
        }
    }

    private void displayNotiReqList(JSONObject result)throws JSONException{

        ArrayList<NotiREQListItem> notiREQListItems=new ArrayList<>(); //array to store details of help Requests by users.
        NotiREQListAdapter adapter;
        int count=0;

        Log.i("NOTI_REQ",result.toString());

        JSONArray jsonArray;
        jsonArray=result.getJSONArray("server_response");
        int size=jsonArray.length();



        for(int i=0;i<size;i++){
            JSONObject jo=jsonArray.getJSONObject(i);

            Log.i("REQJO",jo.toString());

            /*
            "notif_id":"6","stream":"b xgqsxnx","sex":"xvhx","lname":"qxvcqgvxcgqsxc","help_id":"82",
            "department":"x xsx","is_conf_clickable":"1","helper_id":"2","is_skip_clickable":"0",
            "tag2":"bwxvw2bvx","category":"hvshwfshw","title":"vcfffffffffffffffffffnnnnnnnnnnnn",
            "tag3":null,"description":"ggggggggggggggggggggggggggggggggg",
            "is_acc_clickable":"1","tag1":"vxh2xv","fname":"qxvqbxvqbxvqbx"}

             */

            String name=jo.getString("fname");
            String branch=jo.getString("department");
            String stream=jo.getString("stream");
            String category=jo.getString("category");
            String description=jo.getString("description");
            String tag1=jo.getString("tag1");
            String tag2=jo.getString("tag2");
            String tag3=jo.getString("tag3");
            String title=jo.getString("title");

            int isAccepted=Integer.parseInt (jo.getString("is_acc_clickable"));
            int helpieId=Integer.parseInt (jo.getString("helpie_id"));
            int helpId=Integer.parseInt (jo.getString("help_id"));
            int notiId=Integer.parseInt (jo.getString("notif_id"));
            int currentUserId= this.currentUserId;

            name=name.substring(0,1).toUpperCase() + name.substring(1);
            title=title.substring(0,1).toUpperCase()+title.substring(1);
            description=description.substring(0,1).toUpperCase()+description.substring(1);
            String branchStream=stream+" , "+ branch;

            String temp=""+notiId+ name+ branchStream+ category + title+
                    description+ tag1+  tag2+  tag3+ helpId+ helpieId+ currentUserId+ isAccepted;
            Log.i("REQLISTITEM",temp);

            notiREQListItems.add(new NotiREQListItem(notiId, name, branchStream, category, title,
                            description,  tag1,  tag2,  tag3, helpId, helpieId, currentUserId, isAccepted));

        }


        adapter = new NotiREQListAdapter(context, notiREQListItems);

        ListView listView=(ListView)rootView.findViewById(R.id.noti_req_frag_listView);
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


    }

}


