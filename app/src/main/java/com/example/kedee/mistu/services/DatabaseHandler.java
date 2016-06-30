package com.example.kedee.mistu.services;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {
        // All Static variables
        // Database Version
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "iHelp"; // Database Name
        private static final String TABLE_LOGIN = "users"; // Login table name


        // Login Table Columns names
        private static final String KEY_ID = "_id";
        private static final String KEY_FIRSTNAME = "fname";
        private static final String KEY_LASTNAME = "lname";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_USERNAME = "uname";
        private static final String KEY_UID = "uid";
        private static final String KEY_USERID="userId";
        private static final String KEY_CREATED_AT = "created_at";
        private static final String KEY_SEX="sex";
        private static final String KEY_BRANCH="branch";
        private static final String KEY_STREAM="stream";

        public DatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_FIRSTNAME + " TEXT,"
                    + KEY_LASTNAME + " TEXT,"
                    + KEY_EMAIL + " TEXT UNIQUE,"
                    + KEY_USERNAME + " TEXT,"
                    + KEY_UID + " TEXT,"
                    + KEY_USERID + " TEXT,"
                    + KEY_SEX + " TEXT,"
                    + KEY_BRANCH + " TEXT,"
                    + KEY_STREAM + " TEXT,"
                    + KEY_CREATED_AT + " TEXT" + ")";
            db.execSQL(CREATE_LOGIN_TABLE);
            Log.d("ON_CREATE","inside on create");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST"+TABLE_LOGIN);
            onCreate(db);
        }

        /*Strring details of user in DB*/

        public void addUser(String fname,String lname,String email,String uname,String uid,String userId,String sex
                                        ,String branch,String stream,String created_at){
            SQLiteDatabase database=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(KEY_FIRSTNAME,fname);
            values.put(KEY_LASTNAME,lname);
            values.put(KEY_EMAIL,email);
            values.put(KEY_USERNAME,uname);
            values.put(KEY_UID,uid);
            values.put(KEY_USERID,userId);
            values.put(KEY_SEX,sex);
            values.put(KEY_BRANCH,branch);
            values.put(KEY_STREAM,stream);
            values.put(KEY_CREATED_AT,created_at);
            database.insert(TABLE_LOGIN, null, values);
            database.close();
        }

        /*Getting data of user*/

        public HashMap<String,String> getUserDetails(){
            HashMap<String,String> user=new HashMap<String,String>();
            String selectQuery="SELECT * FROM "+TABLE_LOGIN;
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor cursor=database.rawQuery(selectQuery,null);
            cursor.moveToFirst();
            if(cursor.getCount()>0){
                user.put("fname",cursor.getString(1));
                user.put("lname",cursor.getString(2));
                user.put("email",cursor.getString(3));
                user.put("uname",cursor.getString(4));
                user.put("uid", cursor.getString(5));
                user.put("userId",cursor.getString(6));
                user.put("sex",cursor.getString(7));
                user.put("branch",cursor.getString(8));
                user.put("stream",cursor.getString(9));
                user.put("created_at", cursor.getString(10));
            }
            cursor.close();
            database.close();
            return user;
        }

        //getting attribute value by name.

        public String getUserAttr(String attrName){
            HashMap<String,String> userDetails=getUserDetails();
            return userDetails.get(attrName);
        }

        /* checking user is login or not*/

        public boolean isUserLogin(){
            boolean isPresent=false;
            String selectQuery="SELECT * FROM "+TABLE_LOGIN;
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor cursor=database.rawQuery(selectQuery,null);
            cursor.moveToFirst();
            if (cursor.getCount()>0){
                isPresent=true;
                return isPresent;
            }
            else return isPresent;
        }



        //Getting user Login status, return true if rows are present

        public int getRowCount(){
            String countQuery="SELECT * FROM"+TABLE_LOGIN;
            SQLiteDatabase database=this.getReadableDatabase();
            Cursor cursor=database.rawQuery(countQuery, null);
            int rowCount=cursor.getCount();
            return rowCount;
        }

        //Re Create the table, Delete the tables and create them again

        public void resetTables(){
            SQLiteDatabase database=this.getWritableDatabase();
            database.delete(TABLE_LOGIN,null,null);
            database.close();
        }
}

