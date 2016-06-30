package com.example.kedee.mistu.ask;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ahr.AHR;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ASKFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private View rootView;
    private int pos;
    private String tag1,tag2,tag3;
    private TextView catText;
    private TextView tagText;
    private String category="";
    private String tag="";

    private Fragment fragment=this;

    public ASKFragment() {
        // Required empty public constructor
    }
    public void setPos(int pos){
        String[] catNames= {"Academic","Emergency","Technical","Examination","Stationary","Finance","Medical","Placements","Sports" ,
                "Extra-Curricular","Contacts","Hostel Issues","Mess Issues","Others" };
        this.pos=pos;
        category=catNames[pos];
        catText.setText(category);

    }
    public void setTags(String tag1,String tag2,String tag3){
        this.tag1=tag1;
        this.tag2=tag2;
        this.tag3=tag3;
        tag="";

        if(tag1.length()>0)tag+=tag1;
        else tag1="null";
        if(tag2.length()>0)tag=tag+" "+tag2;
        else tag2="null";
        if(tag3.length()>0)tag=tag+" "+tag3;
        else tag3="null";
        tagText.setText(tag);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_ask, container, false);
        context=inflater.getContext();
        setListeners();

        return  rootView;
    }

    private void setListeners(){
        catText=(TextView)rootView.findViewById(R.id.cat_name);
        ImageView catIcon=(ImageView)rootView.findViewById(R.id.cat_icon);
        catText.setOnClickListener(this);
        catIcon.setOnClickListener(this);

        tagText=(TextView)rootView.findViewById(R.id.tag_name);
        ImageView tagIcon=(ImageView)rootView.findViewById(R.id.tag_icon);
        tagText.setOnClickListener(this);
        tagIcon.setOnClickListener(this);

        Button submit=(Button)rootView.findViewById(R.id.ask_submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int vID=v.getId();
        if(vID==R.id.cat_name || vID==R.id.cat_icon){
            Intent intent=new Intent(getActivity(),ASKCat.class);
            getActivity().startActivityForResult(intent,22);
        }
        else if(vID==R.id.tag_name || vID==R.id.tag_icon){
            Intent intent=new Intent(getActivity(),TAG.class);
            getActivity().startActivityForResult(intent,44);
        }
        else if(vID==R.id.ask_submit){
            //Toast.makeText(context,"Submit",Toast.LENGTH_SHORT).show();


            EditText title=(EditText)rootView.findViewById(R.id.title_edit);
            EditText des=(EditText)rootView.findViewById(R.id.des_edit);

            String titleStr=title.getText().toString();
            String desStr=des.getText().toString();

            if(titleStr.length()==0 || desStr.length()==0 ||category=="" || tag==""){
                Toast.makeText(context,"All fields are mandatory !!! ",Toast.LENGTH_SHORT).show();
            }else{
                sendToServer(titleStr,desStr);
                Button submit=(Button)rootView.findViewById(R.id.ask_submit);
                submit.setClickable(false);
                //getActivity().finish();
            }

        }

    }

    private void sendToServer(String title,String des){
        JSONObject values=new JSONObject();
        try {
            values.put("CODE","askHelp");
            values.put("HELPIE_ID",getUserIdFromLocalDB());
            values.put("CATEGORY",category);
            values.put("TITLE",title);
            values.put("DESCRIPTION",des);
            values.put("TAG1",tag1);
            values.put("TAG2",tag2);
            values.put("TAG3",tag3);
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

        //Toast.makeText(context,"Working",Toast.LENGTH_SHORT).show();

    }

    private String getUserIdFromLocalDB() {
        String userID="";
        DatabaseHandler db=new DatabaseHandler(context);
        userID=db.getUserAttr("userId");
        return userID;
    }



    private class BackgroundTask extends AsyncTask<JSONObject,Void,JSONObject> {
        private String helpURL;
        JSONParser jsonParser;
        @Override
        protected void onPreExecute() {
            helpURL="http://mistu.org/askforhelp/askHelp.php/";
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

            Toast.makeText(context,"Your Request is under process, we will notify you as soon as we find someone to help you !!!"
                    ,Toast.LENGTH_LONG).show();
            //start AHR activity.
            Intent intent=new Intent(context,AHR.class);
            startActivity(intent);
            //finish this fragment
            getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();
            getActivity().finish();

        }
        /*
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
        */
    }


}

