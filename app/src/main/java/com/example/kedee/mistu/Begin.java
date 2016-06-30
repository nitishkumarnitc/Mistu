package com.example.kedee.mistu;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kedee.mistu.home.Home;
import com.example.kedee.mistu.services.DatabaseHandler;
import java.util.ArrayList;
import java.util.List;

public class Begin extends AppCompatActivity {

    private int fragType=0;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_new);
        //setContentView(R.layout.activity_begin);

        /*if(savedInstanceState!=null){
            fragType=savedInstanceState.getInt("fragType");
        } */

        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        boolean isUserLogin=db.isUserLogin();

        if(isUserLogin){
            Intent intent=new Intent(this,Home.class);
            startActivity(intent);
            finish();
        }
        // fragmentTransaction();
        viewPager = (ViewPager) findViewById(R.id.begin_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.begin_tabs);
        if(tabLayout!=null) {
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment loginFragment=new LoginFragment();
        Fragment registerFragment=new RegisterFragment();

        adapter.addFragment(loginFragment, "Login");
        adapter.addFragment(registerFragment, "Register");
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
