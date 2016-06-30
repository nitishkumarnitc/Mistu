package com.example.kedee.mistu.ahr;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.services.DatabaseHandler;

import java.util.ArrayList;

@SuppressWarnings("ResourceType")
public class AHR extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private int currentUserId;


    private CharSequence mDrawerTitle; // nav drawer title
    private CharSequence mTitle; // used to store app title

    // slide menu items
    private String[] navMenuTitles;
    private int currentPosition=0;

    private int getCurrentUserId(){
        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        return (Integer.parseInt(db.getUserAttr("userId")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahr);

        //currentUserId=getIntent().getIntExtra("CURRENT_USER_ID",0);
        currentUserId=getCurrentUserId();
        Log.i("AHR_USER_ID",""+currentUserId);

        TypedArray navMenuIcons;     // array to store navigation list icons. (Hence TypedArray)
        ArrayList<AHRNavDrawerItem> navDrawerItems; //array to store navigation list titles.
        AHRNavDrawerListAdapter adapter;

        mTitle = mDrawerTitle = getTitle();



        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems = new ArrayList<AHRNavDrawerItem>();

        // adding nav drawer items to array
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1))); // Academic
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1))); // Emergency
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));  // PStationary
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1), true, "50+"));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[12], navMenuIcons.getResourceId(12, -1)));
        navDrawerItems.add(new AHRNavDrawerItem(navMenuTitles[13], navMenuIcons.getResourceId(13, -1)));

        // Recycle the typed array must be done to avoid runtime exception.
        navMenuIcons.recycle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_sliderMenu);

        // setting the nav drawer list adapter
        adapter = new AHRNavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        //setting the listener
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }else{
            //write code when done with fragment views.
        }


        //creating actionbar drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,//nav menu toggle icon
                R.string.drawer_open, // nav drawer open - description for accessibility
                R.string.drawer_close // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);

                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    /********On Create Ends***********/

    /**
     * Slide menu item click listener
     * */
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
        // update the main content by replacing fragments

        Fragment fragment = new AHRFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("CAT",position);
        bundle.putInt("CURRENT_USER_ID",currentUserId);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ahr_container, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
