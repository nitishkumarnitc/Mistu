package com.example.kedee.mistu;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kedee.mistu.Interests.Registered;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.UserFunction;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    private View rootView=null;
    private Context context=null;

    /**
     *  JSON Response node names.
     **/

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERID="userId";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_SEX="sex";
    private static String KEY_BRANCH="branch";
    private static String KEY_STREAM="stream";
    private static String KEY_ERROR = "error";

    /**
     * Defining layout items.
     **/

    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Button btnRegister;
    RadioGroup radioGroup;
    // TextView registerErrorMsg;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_register, container, false);
        context=inflater.getContext();

        /**
         * Defining all layout items
         **/
        inputFirstName = (EditText)rootView.findViewById(R.id.register_fname);
        inputLastName = (EditText)rootView.findViewById(R.id.register_lname);
        inputUsername = (EditText)rootView.findViewById(R.id.register_username);
        inputEmail = (EditText)rootView.findViewById(R.id.register_email);
        inputPassword = (EditText)rootView.findViewById(R.id.register_password);
        btnRegister = (Button)rootView.findViewById(R.id.register_frag_button);

        radioGroup=(RadioGroup)rootView.findViewById(R.id.register_sex);




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ( !inputUsername.getText().toString().equals(""))
                        && ( !inputPassword.getText().toString().equals(""))
                        && ( !inputFirstName.getText().toString().equals(""))
                        && ( radioGroup.getCheckedRadioButtonId()!= -1)
                        && ( !inputLastName.getText().toString().equals(""))
                        && ( !inputEmail.getText().toString().equals("")) )
                    {
                        if(inputUsername.getText().toString().length() == 9 && checkFormat(inputUsername.getText().toString()) ){
                            NetAsync(view);
                        }else{
                            Toast.makeText(context,"Incorrect NITC Reg. Id format !!! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                else {
                    Toast.makeText(context,"One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private boolean checkFormat(String regId){
        regId=regId.toUpperCase();
        if(regId.charAt(0)=='M'){
            if(regId.charAt(7)=='C'&& regId.charAt(8)=='A'){
                return true;
            }
        }

        if(regId.charAt(0)=='B'||regId.charAt(0)=='M'){
            if(regId.charAt(7)=='C'&& regId.charAt(8)=='S'){
                return true;
            }
            else if(regId.charAt(7)=='E'&& regId.charAt(8)=='C'){
                return true;
            }
            else if(regId.charAt(7)=='E'&& regId.charAt(8)=='E'){
                return true;
            }
            else if(regId.charAt(7)=='M'&& regId.charAt(8)=='E'){
                return true;
            }
            else if(regId.charAt(7)=='C'&& regId.charAt(8)=='E'){
                return true;
            }
            else if(regId.charAt(7)=='P'&& regId.charAt(8)=='E'){
                return true;
            }
            else if(regId.charAt(7)=='A'&& regId.charAt(8)=='R'){
                return true;
            }
            else if(regId.charAt(7)=='B'&& regId.charAt(8)=='T'){
                return true;
            }
            else if(regId.charAt(7)=='C'&& regId.charAt(8)=='H'){
                return true;
            }
            else if(regId.charAt(7)=='E'&& regId.charAt(8)=='P'){
                return true;
            }
        }
        else {
            return false;
        }
        return false;
    }

    /* Async task for checking internet*/
    private class NetCheck extends AsyncTask<String,ProgressDialog,Boolean> {

        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            nDialog=new ProgressDialog(getActivity());
            nDialog.setTitle("Checking Internet Connection");
            nDialog.setMessage("Loading, Please wait");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if(netInfo != null) {
                    if (netInfo.isConnected()) {
                        return netInfo.isConnected();
                    }
                }

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            nDialog.dismiss();
            if(aBoolean){
                new ProcessRegister().execute();
            }
            else {
                Toast.makeText(context,"There is an error in Network connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ProcessRegister extends AsyncTask <String,Void,JSONObject>{
        private static final String TAG="Process Register";

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;
        String email,password,fname,lname,uname,sex,branch,stream;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText)rootView.findViewById(R.id.register_username);
            inputPassword = (EditText)rootView.findViewById(R.id.register_password);
            fname = inputFirstName.getText().toString();
            lname = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            uname= inputUsername.getText().toString(); // NITC Id is used as username here
            password = inputPassword.getText().toString();
            int sexId=radioGroup.getCheckedRadioButtonId();
            if(sexId==R.id.register_female){
                sex="female";
            }else{
                sex="male";
            }

            setBranchStream(uname);

            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Contacting Server...");
            pDialog.setMessage("Inside the Registration process ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            Log.d(TAG, "Inside on PreExecute");
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunction userFunction = new UserFunction();
            JSONObject json = userFunction.registerUser(fname, lname, email, uname, password,sex,branch,stream); //pass branch and stream fields too
            Log.d(TAG,"Inside on doInBackground");
            return json;

        }


        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            Log.d(TAG,"Inside onPostExecute");
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    Log.d(TAG, "Inside onPostExecute try catch");
                    //registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);

                    String red = json.getString(KEY_ERROR);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        //registerErrorMsg.setText("Successfully Registered");
                        Toast.makeText(context,"Successfully Registered", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Inside onPostExecute, after successfully registered");

                        DatabaseHandler db = new DatabaseHandler(context);
                        JSONObject json_user = json.getJSONObject("user");

                        /**
                         * Removes all the previous data in the SQlite database
                         **/

                        UserFunction logout = new UserFunction();
                        logout.logOutUser(context);

                        /*
                        db.addUser(json_user.getString(KEY_FIRSTNAME), json_user.getString(KEY_LASTNAME), json_user.getString(KEY_EMAIL),
                                json_user.getString(KEY_USERNAME), json_user.getString(KEY_UID),
                                json_user.getString(KEY_USERID),json_user.getString(KEY_SEX),json_user.getString(KEY_BRANCH),
                                json_user.getString(KEY_STREAM),json_user.getString(KEY_CREATED_AT));
                                */



                        /**
                         * Stores registered data in SQlite Database
                         * Launch Registered screen
                        */


                        Intent registered = new Intent(context, Verification.class);
                        registered.putExtra("EMAIL",json_user.getString(KEY_EMAIL));
                        registered.putExtra("USERID",json_user.getString(KEY_USERID));
                        registered.putExtra("NAME",json_user.getString(KEY_FIRSTNAME));
                        registered.putExtra("BRANCH",json_user.getString(KEY_BRANCH));
                        registered.putExtra("STREAM",json_user.getString(KEY_STREAM));


                        Log.d(TAG, "REgister"+json_user.getString(KEY_USERID) );

                        /**
                         * Close all views before launching Registered screen
                         **/
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(registered);
                        getActivity().finish();
                    }

                    else if (Integer.parseInt(red)== 2){
                        pDialog.dismiss();
                        // registerErrorMsg.setText("User already exists");
                        Toast.makeText(context,"User Already Exists....", Toast.LENGTH_LONG).show();
                    }
                    else if (Integer.parseInt(red)== 3){
                        pDialog.dismiss();
                        // registerErrorMsg.setText("Invalid Email id");
                        Toast.makeText(context,"Invalid Email id....", Toast.LENGTH_LONG).show();
                    }

                }

                else{
                    pDialog.dismiss();
                    //registerErrorMsg.setText("Error occurred in registration");
                    Toast.makeText(context,"Error occurred in registration", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

        private void setBranchStream(String regId){
            regId=regId.toUpperCase();
            if(regId.charAt(0)=='M'){
                stream="M-TECH";
                if(regId.charAt(7)=='C'&& regId.charAt(8)=='A'){
                    branch="CSE";
                    stream="MCA";
                    return;
                }
            }
            else if(regId.charAt(0)=='B'){
                stream="B-TECH";
            }

            if(regId.charAt(7)=='C'&& regId.charAt(8)=='S'){
                branch="CSE";
            }
            else if(regId.charAt(7)=='E'&& regId.charAt(8)=='C'){
                branch="ECE";
            }
            else if(regId.charAt(7)=='E'&& regId.charAt(8)=='E'){
                branch="EEE";
            }
            else if(regId.charAt(7)=='M'&& regId.charAt(8)=='E'){
                branch="MECH";
            }
            else if(regId.charAt(7)=='C'&& regId.charAt(8)=='E'){
                branch="CIVIL";
            }
            else if(regId.charAt(7)=='P'&& regId.charAt(8)=='E'){
                branch="PRO";
            }
            else if(regId.charAt(7)=='A'&& regId.charAt(8)=='R'){
                branch="ARCH";
            }
            else if(regId.charAt(7)=='B'&& regId.charAt(8)=='T'){
                branch="BIO";
            }
            else if(regId.charAt(7)=='C'&& regId.charAt(8)=='H'){
                branch="CHEM";
            }
            else if(regId.charAt(7)=='E'&& regId.charAt(8)=='P'){
                branch="EP";
            }
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }

}
