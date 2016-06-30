package com.example.kedee.mistu.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kedee.mistu.R;

import java.util.ArrayList;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Settings");
        setList();
    }

    private void setList(){
        ArrayList<SettingsListItem> listItems=new ArrayList<>();
        SettingsListAdapter adapter;

        listItems.add(new SettingsListItem("Reset Password",R.drawable.pass_icon));
        listItems.add(new SettingsListItem("Change Email ID",R.drawable.email_icon5));
        listItems.add(new SettingsListItem("Change Contact Number",R.drawable.call_icon));
        listItems.add(new SettingsListItem("Change Name",R.drawable.name_icon));
        listItems.add(new SettingsListItem("Change Display Pic",R.drawable.cir_profile));

        adapter=new SettingsListAdapter(listItems,getApplicationContext());
        ListView listView=(ListView)findViewById(R.id.settings_list);
        if(listView!=null){
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //list item on click :)
                    Intent intent;
                    switch (position){
                        case 0:
                            intent=new Intent(getApplicationContext(), Reset.class);
                            intent.putExtra("pos",position);
                            intent.putExtra("itemName","Password");
                            startActivity(intent);
                            break;

                        case 1:
                            intent=new Intent(getApplicationContext(), Reset.class);
                            intent.putExtra("pos",position);
                            intent.putExtra("itemName","Email");
                            startActivity(intent);
                            break;

                        case 2:
                            intent=new Intent(getApplicationContext(), Reset.class);
                            intent.putExtra("pos",position);
                            intent.putExtra("itemName","Contact Number");
                            startActivity(intent);
                            break;

                        case 3:
                            intent=new Intent(getApplicationContext(), Reset.class);
                            intent.putExtra("pos",position);
                            intent.putExtra("itemName","Name");
                            startActivity(intent);
                            break;

                        case 4:
                            break;

                    }
                }
            });
        }

    }
}
