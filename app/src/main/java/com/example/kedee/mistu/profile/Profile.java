package com.example.kedee.mistu.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.services.DatabaseHandler;
import com.example.kedee.mistu.services.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int currentUserId;
    private ImageView userPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUserId=getIntent().getIntExtra("CURRENT_USER_ID",0);
        setUserDetails();

        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.profile_tabs);
        if(tabLayout!=null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setUserDetails(){
        DatabaseHandler db=new DatabaseHandler(this);
        HashMap<String,String> user=new HashMap<>();
        user=db.getUserDetails();
        String fname=user.get("fname");
        String lname=user.get("lname");
        String branch=user.get("branch");
        String stream=user.get("stream");

        TextView name=(TextView)findViewById(R.id.profile_name);
        TextView branchStream=(TextView)findViewById(R.id.profile_branch_stream);

        name.setText(fname+" "+lname);
        branchStream.setText(stream+","+branch);

        userPic=(ImageView)findViewById(R.id.profile_pic);
        new DownloadImage(currentUserId).execute();


        TextView interests=(TextView)findViewById(R.id.profile_interests);
        if(interests!=null) {
            interests.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),ShowInterests.class);
                    intent.putExtra("USERID",currentUserId);
                    startActivity(intent);
                }
            });
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AcceptsFragment(), "Accepts");
        adapter.addFragment(new AskedFragment(), "Asked");
        adapter.addFragment(new HelpedFragment(), "Helped");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        Bitmap image=null;
        int user_id;

        public DownloadImage(int user_id ) {
            this.user_id=user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url_pic="http://www.mistu.org/email/getimage.php?id="+user_id;
            URL url = null;
            try {
                url = new URL(url_pic);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            userPic.setImageBitmap(b);
            //Toast.makeText(getApplicationContext(),"Inside on Post Execute",Toast.LENGTH_LONG).show();
        }
    }


}
