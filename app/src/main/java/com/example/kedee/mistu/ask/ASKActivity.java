package com.example.kedee.mistu.ask;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kedee.mistu.R;

public class ASKActivity extends AppCompatActivity {

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        currentUserId=getIntent().getIntExtra("CURRENT_USER_ID",0);

        Fragment fragment=new ASKFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ask_fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK) {
            // get the position from intent
            if(data.getExtras()!=null && data.hasExtra("Position")){
                int position = data.getExtras().getInt("Position");
                ((ASKFragment)getFragmentManager().findFragmentById(R.id.ask_fragment_container)).setPos(position);
            }
        }
        else if(requestCode == 44 && resultCode == RESULT_OK){
            if(data.getExtras()!=null){
                String tag1 = data.getExtras().getString("TAG1");
                String tag2 = data.getExtras().getString("TAG2");
                String tag3 = data.getExtras().getString("TAG3");
                ((ASKFragment)getFragmentManager().findFragmentById(R.id.ask_fragment_container)).setTags(tag1,tag2,tag3);
            }
        }
    }
    public void onBackPressed() {

        super.onBackPressed();
        this.finish();

    }
}
