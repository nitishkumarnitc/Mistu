package com.example.kedee.mistu.notification;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kedee.mistu.R;
import com.example.kedee.mistu.profile.AcceptsFragment;
import com.example.kedee.mistu.profile.AskedFragment;
import com.example.kedee.mistu.profile.HelpedFragment;
import com.example.kedee.mistu.profile.SharedFragment;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        currentUserId=getIntent().getIntExtra("CURRENT_USER_ID",0);

        viewPager = (ViewPager) findViewById(R.id.noti_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.noti_tabs);
        if(tabLayout!=null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle=new Bundle();
        bundle.putInt("CURRENT_USER_ID",currentUserId);

        Fragment notiACCFragment=new NotiACCFragment();
        notiACCFragment.setArguments(bundle);

        Fragment notiREQFragment=new NotiREQFragment();
        notiREQFragment.setArguments(bundle);

        adapter.addFragment(notiACCFragment, "Accepts");
        adapter.addFragment(notiREQFragment, "Requests");
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
}
