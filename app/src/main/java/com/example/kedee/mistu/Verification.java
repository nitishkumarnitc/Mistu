package com.example.kedee.mistu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kedee.mistu.Interests.Interest;
import com.example.kedee.mistu.Interests.Registered;
import com.example.kedee.mistu.services.UserFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Verification extends AppCompatActivity implements View.OnClickListener{

    private  boolean isInterestsSelected=false;
    private String userEmail,userName,userBranch,userStream;
    private int userId;
    private ImageView imageToUpload=null;
    private static final int RESULT_LOAD_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        if(savedInstanceState!=null){
            userEmail=savedInstanceState.getString("EMAIL");
            userName=savedInstanceState.getString("NAME");
            userBranch=savedInstanceState.getString("BRANCH");
            userStream=savedInstanceState.getString("STREAM");
            userId=savedInstanceState.getInt("USERID");
        }else{
            userEmail=getIntent().getStringExtra("EMAIL");
            userName=getIntent().getStringExtra("NAME");
            userBranch=getIntent().getStringExtra("BRANCH");
            userStream=getIntent().getStringExtra("STREAM");
            userId=Integer.parseInt(getIntent().getStringExtra("USERID"));
        }

        TextView textView=(TextView)findViewById(R.id.verification_user_interests_selection);
        textView.setOnClickListener(this);

        View proceed=findViewById(R.id.verification_done);
        proceed.setOnClickListener(this);

        TextView choosePic=(TextView)findViewById(R.id.verification_choose_pic);
        choosePic.setOnClickListener(this);

        imageToUpload=(ImageView)findViewById(R.id.verification_pic_user);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        Intent intent;
        switch (id){
            case R.id.verification_user_interests_selection:
                intent=new Intent(this, Registered.class);
                intent.putExtra("TYPE",0); // 0 refers that registered class is called from verification class.
                intent.putExtra("NAME",userName);
                intent.putExtra("BRANCH",userBranch);
                intent.putExtra("STREAM",userStream);
                intent.putExtra("USERID",userId);
                startActivityForResult(intent,99);
                break;

            case R.id.verification_choose_pic:
                Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                break;

            case R.id.verification_done:
                TextView phoneNumText=(TextView)findViewById(R.id.verification_phone_num);
                TextView cityText=(TextView)findViewById(R.id.verification_current_city);

                String phoneNum=phoneNumText.getText().toString();
                String city=cityText.getText().toString().toUpperCase();

                //city.replaceAll("\\s+","");

                Boolean isContactValid=isValidPhoneNum(phoneNum);


                if(isContactValid && !city.equals("")) {
                    if(isInterestsSelected){
                        //encoding image
                        Bitmap image=((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                        int nh = (int) ( image.getHeight() * (512.0 / image.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(image, 512, nh, true);

                        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                        scaled.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream);
                        String encodedImage= Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

                        //sending email for verification
                        ServerInteraction verify = new ServerInteraction(this,"http://mistu.org/email/sendverificationmail.php");
                        JSONObject values=new JSONObject();
                        try {
                            values.put("CODE","Verify");
                            values.put("EMAIL",userEmail);
                            values.put("USERID",userId);
                            values.put("MOBILE",phoneNum);
                            values.put("CITY",city);
                            values.put("ENCODED_IMAGE",encodedImage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        verify.sendDataToServer(values);

                        //logout user
                        UserFunction logout = new UserFunction();
                        logout.logOutUser(this);

                        //redirect to login
                        Toast.makeText(this,"Please verify your email id and login again. ",Toast.LENGTH_SHORT).show();
                        intent=new Intent(this,Begin.class);
                        startActivity(intent);
                        this.finish();
                    }
                    else{
                        Toast.makeText(this,"Oops! you forgot to tell us your Interests and Skills.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    if(!isContactValid){
                        Toast.makeText(this,"Oops! seems like your contact number is not valid.",Toast.LENGTH_SHORT).show();
                    }
                    else if(city.equals("")){
                        Toast.makeText(this,"Oops! you forgot to tell us your current city.",Toast.LENGTH_SHORT).show();
                    }
                }
                break;


        }
    }



    private boolean isValidPhoneNum(String phoneNum){
        if(phoneNum.length()==10){
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK &&data!=null){
            Uri selectImage=data.getData();
            imageToUpload.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            imageToUpload.setImageURI(null);
            imageToUpload.setImageURI(selectImage);

        }

        else if (requestCode == 99 && resultCode == RESULT_OK) {
            // get the position from intent
            if(data.getExtras()!=null && data.hasExtra("Selected")){
                isInterestsSelected = data.getExtras().getBoolean("Selected");
            }
        }

    }

    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("NAME",userName);
        bundle.putString("STREAM",userStream);
        bundle.putString("BRANCH",userBranch);
        bundle.putString("EMAIL",userEmail);
        bundle.putInt("USERID",userId);
    }
}
