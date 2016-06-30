package com.example.kedee.mistu.ahr;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kedee.mistu.Interests.Interest;
import com.example.kedee.mistu.R;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


@SuppressWarnings("ResourceType")
public class AHRFragment extends Fragment {
    private ListView ahrList;
    private Context context;
    private View rootView;
    private int catPosition;
    private int currentUserId;
    private String[] navMenuTitles;
    private String category;
    private ArrayList<Integer> helpieIdArray=new ArrayList<>();

    public AHRFragment() {
        // Required empty public constructor
    }

    private int getCurrentUserId(){
        DatabaseHandler db=new DatabaseHandler(context);
        return (Integer.parseInt(db.getUserAttr("userId")));
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_ahr, container, false);
        context=inflater.getContext();

        catPosition=getArguments().getInt("CAT");
        currentUserId=getCurrentUserId();
        //currentUserId=getArguments().getInt("CURRENT_USER_ID");
        Log.i("AHR_FRAG_USER_ID",""+currentUserId);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        category=navMenuTitles[catPosition];

        databaseTransaction();
        return rootView;

    }

    private void databaseTransaction(){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","ahr");
            values.put("CATEGORY",category);
            values.put("USERID",currentUserId);
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
            helpURL = "http://mistu.org/ahr_new.php";
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
            TypedArray userPics=getResources().obtainTypedArray(R.array.nav_drawer_icons);     // array to store pics of users asking for help. (Hence TypedArray)
            ArrayList<AHRFragmentListItem> fragmentListItems=new ArrayList<AHRFragmentListItem>(); //array to store details of help Requests by users.
            AHRFragmentListAdapter adapter;
            int count=0;

            JSONArray jsonArray;
            int size;
            jsonArray=result.getJSONArray("server_response");
            size=jsonArray.length();



            for(int i=0;i<size;i++){
                JSONObject jo=jsonArray.getJSONObject(i);
                String name=jo.getString("name");
                String stream=jo.getString("stream");
                String dept=jo.getString("dept");
                String cat=jo.getString("category");
                String title=jo.getString("title");
                String description=jo.getString("description");
                String helpId=jo.getString("helpId");
                int helpieId=Integer.parseInt(jo.getString("helpieId"));
                helpieIdArray.add(helpieId);
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

                name=name.substring(0,1).toUpperCase() + name.substring(1);
                title=title.substring(0,1).toUpperCase()+title.substring(1);
                description=description.substring(0,1).toUpperCase()+description.substring(1);
                String branchStream=stream+" , "+dept;

                /*
                fragmentListItems.add(new AHRFragmentListItem(name,branchStream,cat,title,description,
                        Integer.parseInt(helpId),helpieId,
                        userPics.getResourceId(count++%10, -1),tag1,tag2,tag3)); */

                fragmentListItems.add(new AHRFragmentListItem(name,branchStream,cat,title,description,
                        Integer.parseInt(helpId),helpieId,tag1,tag2,tag3));
            }

            userPics.recycle(); // Recycle the typed array must be done to avoid runtime exception.
            adapter = new AHRFragmentListAdapter(context, fragmentListItems);
            Log.i("ILLAALOLA",""+currentUserId);

            ahrList=(ListView)rootView.findViewById(R.id.hr_frag_listView);
            if(ahrList==null){
                Log.e("EMPTY", "List view is empty");
            }else {
                ahrList.setAdapter(adapter);
                ahrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //list item on click :)
                    }
                });
            }


        }
    }
}

