package com.example.kedee.mistu.ask;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kedee.mistu.R;

public class ASKCat extends AppCompatActivity {
    private int index;
    private String[] catNames= {"Academic","Emergency","Technical","Examination","Stationary","Finance","Medical","Placements","Sports" ,
            "Extra-Curricular","Contacts","Hostel Issues","Mess Issues","Others" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askcat);
        ListView listView=(ListView)findViewById(R.id.ask_cat_listView);
        if(listView!=null) {
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, catNames));
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> listView, View v, int pos, long id) {
                    index = pos;
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Position", pos);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }
    }

}

