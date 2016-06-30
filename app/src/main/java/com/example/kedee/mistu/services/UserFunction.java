package com.example.kedee.mistu.services;


import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

public class UserFunction {
    private JSONParser jsonParser;

    //URL of the PHP API
    private  static String url="http://www.mistu.org/login/";

    private static String loginURL = url;
    private static String registerURL = url;
    private static String forpassURL = url;
    private static String chgpassURL = url;

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";

    public UserFunction() {
        jsonParser=new JSONParser();
    }

    // Function to log in

    public JSONObject loginUser(String email, String password){
        JSONObject values=new JSONObject();

        try {
            values.put("tag",login_tag);
            values.put("email",email);
            values.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject=jsonParser.getJsonFromUrl(loginURL,"POST",values);
        return jsonObject;
    }

    // Function to change the password
    public JSONObject chgPass(String newpass, String email){
        JSONObject values=new JSONObject();

        try {
            values.put("email",email);
            values.put("tag",chgpass_tag);
            values.put("newpas",newpass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject=jsonParser.getJsonFromUrl(chgpassURL,"POST",values);
        return jsonObject;

    }

    // Function to reset the password
    public JSONObject forPass(String email){
        JSONObject params=new JSONObject();

        try {
            params.put("email", email);
            params.put("tag", forpass_tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject=jsonParser.getJsonFromUrl(forpassURL, "POST",params);
        return jsonObject;
    }

    //Function to register
    public JSONObject registerUser(String fname,String lname, String email, String uname, String password,String sex,
                                   String branch,String stream){
        JSONObject params=new JSONObject();

        try {
            params.put("tag", register_tag);
            params.put("fname", fname);
            params.put("lname", lname);
            params.put("email", email);
            params.put("uname", uname);
            params.put("password", password);
            params.put("sex",sex);
            params.put("branch",branch);
            params.put("stream",stream);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject=jsonParser.getJsonFromUrl(registerURL,"POST", params);
        return jsonObject;
    }

    //Function to logout user, Resets the temporary data stored in SQLite Database
    public boolean logOutUser(Context context){
        DatabaseHandler databaseHandler=new DatabaseHandler(context);
        databaseHandler.resetTables();
        return true;
    }
}
