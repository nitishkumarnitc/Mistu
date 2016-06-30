package com.example.kedee.mistu.ask;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kedee.mistu.R;

import java.util.ArrayList;

public class TAG extends AppCompatActivity implements View.OnClickListener {

    private String[] autoComplete=null;
    private AutoCompleteTextView ed1,ed2,ed3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        ed1 = (AutoCompleteTextView) findViewById(R.id.tag_edit1);
        ed2 = (AutoCompleteTextView) findViewById(R.id.tag_edit2);
        ed3 = (AutoCompleteTextView) findViewById(R.id.tag_edit3);

        String[] cse=getResources().getStringArray(R.array.interests_cse);
        String[] eee=getResources().getStringArray(R.array.interests_eee);
        String[] ece=getResources().getStringArray(R.array.interests_ece);
        String[] mech=getResources().getStringArray(R.array.interests_mech);
        String[] civil=getResources().getStringArray(R.array.interests_civil);
        String[] chem=getResources().getStringArray(R.array.interests_chem);
        String[] pro=getResources().getStringArray(R.array.interests_pro);
        String[] bio=getResources().getStringArray(R.array.interests_bt);
        String[] arch=getResources().getStringArray(R.array.interests_arch);
        String[] ep=getResources().getStringArray(R.array.interests_ep);

        String[] sports=getResources().getStringArray(R.array.interests_sports);
        String[] arts=getResources().getStringArray(R.array.interests_arts);
        String[] technical=getResources().getStringArray(R.array.interests_technical);
        String[] others=getResources().getStringArray(R.array.interests_others);

        String[] interestsList=generalConcatAll(cse,eee,ece,mech,civil,chem,pro,bio,arch,ep,sports,arts,technical,others);

        //autoComplete= getResources().getStringArray(R.array.interests_cse);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, interestsList);
        ed1.setThreshold(2);
        ed1.setAdapter(adapter);
        ed2.setThreshold(2);
        ed2.setAdapter(adapter);
        ed3.setThreshold(2);
        ed3.setAdapter(adapter);


        Button done=(Button)findViewById(R.id.tag_done);
        done.setOnClickListener(this);
    }

    private String[] generalConcatAll(String[]... jobs) {
        int len = 0;
        for (final String[] job : jobs) {
            len += job.length;
        }

        final String[] result = new String[len];

        int currentPos = 0;
        for (final String[] job : jobs) {
            System.arraycopy(job, 0, result, currentPos, job.length);
            currentPos += job.length;
        }

        return result;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.tag_done) {
            String tag1 = ed1.getText().toString();
            String tag2 = ed2.getText().toString();
            String tag3 = ed3.getText().toString();


            if (tag1.length() == 0 && tag2.length() == 0 && tag3.length() == 0) {
                Toast.makeText(this, "At least on tag required", Toast.LENGTH_SHORT).show();
            } else {
                tag1=tag1.toLowerCase().replace(" ","_");
                tag2=tag2.toLowerCase().replace(" ","_");
                tag3=tag3.toLowerCase().replace(" ","_");

                Intent returnIntent = new Intent();
                Bundle extras = new Bundle();
                extras.putString("TAG1", tag1);
                extras.putString("TAG2", tag2);
                extras.putString("TAG3", tag3);

                returnIntent.putExtras(extras);
                setResult(Activity.RESULT_OK, returnIntent);

                this.finish();


            }
        }
    }
}

