package com.example.kedee.mistu.services;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kedee.mistu.R;

public class Test extends AppCompatActivity {
    private String[] catNames= {"Akjvkj","Ekjvjdkvnkj","T","E","S","F","M","P","S","E","C","H","M","Others"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ListView listView=(ListView)findViewById(R.id.test_listView);
        if(listView!=null) {
            listView.setAdapter(new ArrayAdapter<String>(Test.this, android.R.layout.simple_list_item_1, catNames));
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
}
