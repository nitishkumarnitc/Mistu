package com.example.kedee.mistu.notification;


import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kedee.mistu.Interests.Interest;
import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ahr.AHRFragmentListAdapter;
import com.example.kedee.mistu.ahr.AHRFragmentListItem;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotiACCFragment extends Fragment {
    private Context context;
    private View rootView;
    private int currentUserId;

    public NotiACCFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=inflater.getContext();
        rootView=inflater.inflate(R.layout.fragment_noti_acc, container, false);
        currentUserId=getArguments().getInt("CURRENT_USER_ID");
        startDBTransaction();
        return rootView;
    }


    private void startDBTransaction(){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","notiAcc");
            values.put("USER_ID",currentUserId);
            Log.i("NOTII+USERID",""+currentUserId);

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
            helpURL="http://mistu.org/get_help_notif/user_noti.php";
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
            if( parseJsonServerResponse(result)){

            }else{
                Toast.makeText(context,"Please Try Later !!!",Toast.LENGTH_SHORT).show();
            }
        }

        private boolean parseJsonServerResponse(JSONObject result){
            boolean res=false;
            try {
                if(displayNotiAccList(result))return true;
            }catch (JSONException ex){
                //
            }
            return res;
        }
    }

    private boolean displayNotiAccList(JSONObject result)throws JSONException{

        ArrayList<NotiACCListItem> notiACCListItems=new ArrayList<>(); //array to store details of help Requests by users.
        NotiACCListAdapter adapter;
        int count=0;

        JSONArray jsonArray;
        jsonArray=result.getJSONArray("server_response");
        int size=jsonArray.length();
        if(size==0){
            return false;
        }

        Log.i("JSON_NOTI",result.toString());



        for(int i=0;i<size;i++){
            JSONObject jo=jsonArray.getJSONObject(i);

            String name=jo.getString("fname");
            String branch=jo.getString("department");
            String stream=jo.getString("stream");
            String category=jo.getString("category");
            String description=jo.getString("description");
            String tag1=jo.getString("tag1");
            String tag2=jo.getString("tag2");
            String tag3=jo.getString("tag3");
            String title=jo.getString("title");

            int hasContacted=Integer.parseInt (jo.getString("is_conf_clickable"));
            int hasSkipped=Integer.parseInt (jo.getString("is_skip_clickable"));

            int helperId=Integer.parseInt (jo.getString("helper_id"));
            int helpId=Integer.parseInt (jo.getString("help_id"));
            int notiId=Integer.parseInt (jo.getString("notif_id"));
            int currentUserId= this.currentUserId;

            float rating=2.0f; // we will change it later



            name=name.substring(0,1).toUpperCase() + name.substring(1);
            title=title.substring(0,1).toUpperCase()+title.substring(1);
            description=description.substring(0,1).toUpperCase()+description.substring(1);
            String branchStream=branch+" , "+ stream;

            notiACCListItems.add(new NotiACCListItem(branchStream, category,currentUserId,
            description,  hasContacted,  hasSkipped,  helperId,
            helpId,  name, notiId,  rating,  tag1,  tag2, tag3,  title));

        }
        adapter = new NotiACCListAdapter(context, notiACCListItems);

        ListView listView=(ListView)rootView.findViewById(R.id.noti_acc_frag_listView);
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
