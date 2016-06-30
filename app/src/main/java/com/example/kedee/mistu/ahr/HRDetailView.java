package com.example.kedee.mistu.ahr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HRDetailView extends AppCompatActivity {

    private String name,branchStream,category,title,description,tag1,tag2,tag3;
    private int picId,helpId,helpieId;
    private int currentUserId;
    private Bitmap bitmap;
    private ImageView pic;
    private int isAcceptClickable;
    private int notiId; //if activity is called from notification.
    private Button accept;

    private int getCurrentUserId(){
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        return (Integer.parseInt(db.getUserAttr("userId")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrdetail_view);

        Intent intent=getIntent();
        currentUserId=getCurrentUserId();

        name=intent.getStringExtra("NAME");
        branchStream=intent.getStringExtra("BRANCH_STREAM");
        category=intent.getStringExtra("CAT");
        title=intent.getStringExtra("TITLE");
        description=intent.getStringExtra("DES");
        tag1=intent.getStringExtra("TAG1");
        tag2=intent.getStringExtra("TAG2");
        tag3=intent.getStringExtra("TAG3");
        helpId=intent.getIntExtra("HELPID",0);
        helpieId=intent.getIntExtra("HELPIEID",0);
        isAcceptClickable=intent.getIntExtra("ACCEPT",1);
        notiId=intent.getIntExtra("NOTIID",-1); //-1 if it is called from classes other than notification class

        setLayoutData();
    }

    private void setLayoutData(){
        TextView name=(TextView)findViewById(R.id.hr_details_helpie_name);
        TextView branch=(TextView)findViewById(R.id.hr_details_helpie_branch);
        TextView cat=(TextView)findViewById(R.id.hr_details_category);
        TextView title=(TextView)findViewById(R.id.hr_details_title);
        TextView des=(TextView)findViewById(R.id.hr_details_description);
        TextView tag1=(TextView)findViewById(R.id.hr_details_tag1);
        TextView tag2=(TextView)findViewById(R.id.hr_details_tag2);
        TextView tag3=(TextView)findViewById(R.id.hr_details_tag3);
        accept=(Button)findViewById(R.id.hr_details_accept);
        if(isAcceptClickable==0 || currentUserId==helpieId){
            if(accept!=null){
                accept.setClickable(false);
            }
        }

        pic=(ImageView)findViewById(R.id.hr_details_pic);
        new DownloadImage(helpieId).execute();

        name.setText(this.name);
        branch.setText(this.branchStream);
        cat.setText(this.category);
        title.setText(this.title);
        des.setText(this.description);

        if((this.tag1).equals("")){
            tag1.setVisibility(View.GONE);
        }
        else{
            tag1.setText(this.tag1);
        }

        if((this.tag2).equals("")){
            tag2.setVisibility(View.GONE);
        }
        else{
            tag2.setText(this.tag2);
        }

        if((this.tag3).equals("")){
            tag3.setVisibility(View.GONE);
        }
        else{
            tag3.setText(this.tag3);
        }


    }

    public void requestAccept(View v){
        if(accept!=null){
            accept.setClickable(false);
        }
        Toast.makeText(getApplicationContext(),"Thanks! we will notify the user about it!!!"
                ,Toast.LENGTH_LONG).show();
        sendToServer(helpId);
    }

    private void sendToServer(int helpId){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","accept");
            values.put("HELPERID",currentUserId);
            values.put("HELPID",helpId);
            values.put("NOTIID",notiId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BackgroundTask().execute(values);
        } else {
            Toast.makeText(getApplicationContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(context,"Working",Toast.LENGTH_SHORT).show();

    }

    private class BackgroundTask extends AsyncTask<JSONObject,Void,JSONObject> {
        private String helpURL;
        JSONParser jsonParser;
        @Override
        protected void onPreExecute() {
            helpURL="http://mistu.org/askforhelp/helpAccept.php/";
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
                Log.i("HELP_ACCEPT","successful");
            }else{
                Toast.makeText(getApplicationContext(),"Server Failed to process request, Please try again !!!",Toast.LENGTH_SHORT).show();
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
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
            return res;
        }
    }

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        Bitmap image=null;
        int user_id;

        public DownloadImage(int user_id ) {
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
            pic.setImageBitmap(b);
            bitmap=b;
            //Toast.makeText(getApplicationContext(),"Inside on Post Execute",Toast.LENGTH_LONG).show();
        }
    }
}
