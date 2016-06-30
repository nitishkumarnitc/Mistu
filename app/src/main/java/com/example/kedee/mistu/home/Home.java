package com.example.kedee.mistu.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kedee.mistu.Begin;
import com.example.kedee.mistu.Interests.Registered;
import com.example.kedee.mistu.R;
import com.example.kedee.mistu.ahr.AHR;
import com.example.kedee.mistu.ask.ASKActivity;
import com.example.kedee.mistu.ask.ASKFragment;
import com.example.kedee.mistu.notification.Notification;
import com.example.kedee.mistu.profile.Profile;
import com.example.kedee.mistu.search.Search;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.UserFunction;
import com.example.kedee.mistu.settings.Setting;

import java.util.ArrayList;

@SuppressWarnings("ResourceType")
public class Home extends Activity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle; // nav drawer title
    private CharSequence mTitle; // used to store app title
    private int currentPosition=0;
    private int currentUserId;

    private int getCurrentUserId(){
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        return (Integer.parseInt(db.getUserAttr("userId")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_home_layout);
        currentUserId=getCurrentUserId();

        setNavigationDrawer();

        ImageView notiImg=(ImageView)findViewById(R.id.new_home_icon1);
        ImageView ahrImg=(ImageView)findViewById(R.id.new_home_icon2);
        ImageView askImg=(ImageView)findViewById(R.id.new_home_icon4);
        ImageView searchImg=(ImageView)findViewById(R.id.new_home_icon8);
        //ImageView notiImg=(ImageView)findViewById(R.id.new_home_icon3);
        //ImageView searchImg=(ImageView)findViewById(R.id.new_home_icon6);
        //ImageView profImg=(ImageView)findViewById(R.id.new_home_icon7);


        notiImg.setOnClickListener(this);
        ahrImg.setOnClickListener(this);
        askImg.setOnClickListener(this);
        searchImg.setOnClickListener(this);
       // notiImg.setOnClickListener(this);
        //searchImg.setOnClickListener(this);
       // profImg.setOnClickListener(this);

    }

    void setNavigationDrawer(){
        TypedArray navMenuIcons;     // array to store navigation list icons. (Hence TypedArray)
        String[] navMenuTitles;
        ImageView navDrawImage;
        HomeNavDrawAdapter adapter;

        navMenuTitles = getResources().getStringArray(R.array.home_nav_drawer_items); // load slide menu items
        navMenuIcons = getResources().obtainTypedArray(R.array.home_nav_drawer_icons); // nav drawer icons from resources

        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>(); //array to store navigation list titles.
        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1))); // Profile
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1))); // Search
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));  //Interests
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1))); //Settings
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1))); //Logout

        // Recycle the typed array must be done to avoid runtime exception.
        navMenuIcons.recycle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.new_home_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.new_home_slider);

        // setting the nav drawer list adapter
        adapter = new HomeNavDrawAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        //setting the listener to the navigation drawer
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        navDrawImage=(ImageView)findViewById(R.id.new_home_icon5);
        if(navDrawImage!=null) {
            navDrawImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            });
        }
    }

    public void onClick(View v) {
        Intent intent;
        Fragment fragment;
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.new_home_icon1:
                intent=new Intent(this, Notification.class);
                intent.putExtra("CURRENT_USER_ID",currentUserId);
                startActivity(intent);
                break;
            case R.id.new_home_icon2:
                intent=new Intent(this,AHR.class);
                intent.putExtra("CURRENT_USER_ID",currentUserId);
                startActivity(intent);
                break;
            case R.id.new_home_icon4:
                intent=new Intent(this, ASKActivity.class);
                intent.putExtra("CURRENT_USER_ID",currentUserId);
                startActivity(intent);
                break;
            case R.id.new_home_icon8:
                intent=new Intent(this, Search.class);
                startActivity(intent);
                break;
            /*
            case R.id.new_home_icon7:
                intent=new Intent(this, Profile.class);
                intent.putExtra("CURRENT_USER_ID",currentUserId);
                startActivity(intent);
                break; */

            default:
                break;
        }
    }


    private  void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.replace(R.id.home_container, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        Intent intent=null;
        switch (position) {
            case 0:
                intent=new Intent(this, Profile.class);
                intent.putExtra("CURRENT_USER_ID",currentUserId);
                startActivity(intent);
                break;
            case 1:
                intent=new Intent(this, Search.class);
                intent.putExtra("CURRENT_USER_ID",currentUserId);
                startActivity(intent);
                break;
            case 2:
                intent=new Intent(this, Registered.class);
                startActivity(intent);
                break;
            case 3:
                intent=new Intent(this, Setting.class);
                startActivity(intent);
                break;
            case 4:
                UserFunction logout=new UserFunction();
                logout.logOutUser(getApplicationContext());
                Intent login=new Intent(getApplicationContext(), Begin.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
                break;
            case 5:
                //fragment = new WhatsHotFragment();
                break;
            default:
                break;
        }

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        // mDrawerToggle.onConfigurationChanged(newConfig);
    }
}

