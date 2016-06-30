package com.example.kedee.mistu.services;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {
    private String charset = "UTF-8";
    private HttpURLConnection conn;
    private DataOutputStream wr;
    private StringBuilder result;
    private URL urlObj;
    private JSONObject jObj = null;
    private StringBuilder sbParams;
    private String paramsString;

    //Constructor
    public JSONParser() {
    }

    public  JSONObject getJsonFromUrl(String url, String method, JSONObject params) {
        if (method.equals("POST")) { // request method is POST
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setFixedLengthStreamingMode(params.toString().getBytes().length);
                conn.connect();

                // paramsString = sbParams.toString();
                wr = new DataOutputStream(conn.getOutputStream());
                wr.write(params.toString().getBytes());
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("GET")){// request method is GET
            if (sbParams.length() != 0) {
                url += "?" + sbParams.toString();
            }
            try {
                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept-Charset", charset);
                conn.setConnectTimeout(15000);
                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try { //Receive the response from the server

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            if (result.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            conn.disconnect();

            Log.d("JSON Parser", "result: " + result.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }



        // try parse the string to a JSON object
        try {
            Log.i("tagConvertStr", "[" + result + "]");
            Log.d("JSON Parser", "after disconnecting input stream and before parsing results");

            String temp=result.toString();
            Log.e("TEMP", temp);

            if(temp.indexOf("{")==0){
                jObj = new JSONObject(result.toString());
            }else {
                jObj=new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            }

            Log.d("JSON Parser", "after disconnecting input stream and parsing results");
            Log.d("tagConvertStr",jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jObj;
    }
}
