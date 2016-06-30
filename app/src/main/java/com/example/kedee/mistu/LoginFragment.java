package com.example.kedee.mistu;

import com.example.kedee.mistu.home.Home;
import com.example.kedee.mistu.services.*;
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
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragment extends Fragment {
    private View rootView=null;
    private Context context=null;


    private Button btnLogin;

    private EditText inputEmail;
    private EditText inputPassword;

    private TextView loginErrorMsg;

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

    public LoginFragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_login, container, false);
        context=inflater.getContext();


        inputEmail = (EditText)rootView.findViewById(R.id.login_email);
        inputPassword = (EditText)rootView.findViewById(R.id.login_password);

        btnLogin = (Button)rootView.findViewById(R.id.login_button);
        TextView forgotPass=(TextView)rootView.findViewById(R.id.begin_forgot_password);


        /*
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PasswordReset.class);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        */


        //Toast is generated when the email and pwd field or any is empty

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!inputEmail.getText().toString().equals("")) && (!inputPassword.getText().toString().equals(""))) {
                    NetAsync(v);
                    // new ProcessLogin().execute();
                } else if ((!inputEmail.getText().toString().equals(""))) {
                    Toast.makeText(context, "Password can't be empty !!!", Toast.LENGTH_SHORT).show();
                } else if ((!inputPassword.getText().toString().equals(""))) {
                    Toast.makeText(context, "Email can't be empty !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Every field is mandatory to login !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    /* Async task for checking internet*/

    private class NetCheck extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog nDialog;
        private static  final String TAG="Login NetCheck";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "inside preExecute");
            nDialog = new ProgressDialog(getActivity());
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();

        }

        @Override
        protected Boolean doInBackground(String... args) {
            Log.d(TAG,"inside doInBackGround");

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if(netInfo != null) {
                    if (netInfo.isConnected()){
                        return netInfo.isConnected();
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            Log.d(TAG,"inside postExecute");
            nDialog.dismiss();
            if (th == true) {
                new ProcessLogin().execute();
            } else {
                Toast.makeText(context,"Check your Internet Connection !!!",Toast.LENGTH_LONG).show();
            }
        }
    }


    // A Async class for login into the system
    private class ProcessLogin extends AsyncTask<String,Void,JSONObject> {
        private static  final String TAG="Process Login";
        private ProgressDialog progressDialog;
        String email,password;

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "inside preExecute");
            super.onPreExecute();

            inputEmail= (EditText)rootView.findViewById(R.id.login_email);
            inputPassword= (EditText)rootView.findViewById(R.id.login_password);

            email=inputEmail.getText().toString();
            password=inputPassword.getText().toString();

            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Contacting server...");
            progressDialog.setMessage("Logging in...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }
        @Override
        protected JSONObject doInBackground(String... args) {
            Log.d(TAG,"inside DoInBackGround");
            UserFunction userFunction = new UserFunction();
            JSONObject json = userFunction.loginUser(email, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                Log.d("LOGIN","inside postExecute  "+json.toString());

                if(Integer.parseInt(json.getString("error"))==1){
                    Toast.makeText(context,"Incorrect Username/Password",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    inputEmail= (EditText)rootView.findViewById(R.id.login_email);
                    inputPassword= (EditText)rootView.findViewById(R.id.login_password);
                    inputEmail.setText("");
                    inputPassword.setText("");
                }

                else if (json.getString("success") != null) {
                    String res = json.getString("success");

                    if(Integer.parseInt(res) == 1){
                        progressDialog.setMessage("Loading User Space");
                        progressDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(context);
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQLite database.
                         **/
                        UserFunction logout = new UserFunction();
                        logout.logOutUser(context);

                        db.addUser(json_user.getString(KEY_FIRSTNAME), json_user.getString(KEY_LASTNAME), json_user.getString(KEY_EMAIL),
                                json_user.getString(KEY_USERNAME), json_user.getString(KEY_UID),
                                json_user.getString(KEY_USERID),json_user.getString(KEY_SEX),json_user.getString(KEY_BRANCH),
                                json_user.getString(KEY_STREAM),json_user.getString(KEY_CREATED_AT));
                        /**
                         *If JSON array details are stored in SQlite it launches the User Panel.
                         **/
                        Intent intent = new Intent(context, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        progressDialog.dismiss();
                        startActivity(intent);
                        /**
                         * Close Login Screen
                         **/

                        getActivity().finish();
                    }
                    else{
                        progressDialog.dismiss();
                        if(Integer.parseInt(res) == 2){
                            Toast.makeText(context,"Oops! You forgot to verify your Email Id.",Toast.LENGTH_LONG).show();
                        }
                        if(Integer.parseInt(res) == 0){ // verification not done .i.e. verification email not sent
                            JSONObject json_user = json.getJSONObject("user");
                            Toast.makeText(context,"Please fill up these required information again !",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, Verification.class);
                            intent.putExtra("EMAIL",json_user.getString(KEY_EMAIL));
                            intent.putExtra("USERID",json_user.getString(KEY_USERID));
                            intent.putExtra("NAME",json_user.getString(KEY_FIRSTNAME));
                            intent.putExtra("BRANCH",json_user.getString(KEY_BRANCH));
                            intent.putExtra("STREAM",json_user.getString(KEY_STREAM));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }
}
