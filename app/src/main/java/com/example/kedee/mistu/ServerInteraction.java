package com.example.kedee.mistu;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.kedee.mistu.notification.NotiREQListItem;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerInteraction {
    private final String url;
    private JSONObject values;
    private Context context;

    public ServerInteraction(Context context,String url) {
        this.context=context;
        this.url=url;
    }

    public void sendDataToServer(JSONObject values){
        this.values=values;

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
            helpURL=url;
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
            /*
            if( parseJsonServerResponse(result)==1){
                Toast.makeText(context,"Server Request Success",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"Server Failed to process request",Toast.LENGTH_SHORT).show();
            }
            */
        }

        private int parseJsonServerResponse(JSONObject result){
            int res=0;
            try {
                JSONArray jsonArray = result.getJSONArray("server_response");
                JSONObject jo=jsonArray.getJSONObject(0);
                String success=jo.getString("success");
                if(success.equals("yes")){
                    res=1;
                }
            }catch (JSONException ex){
                //
            }
            return res;
        }
    }
}
