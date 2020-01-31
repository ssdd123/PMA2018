package ftn.proj.sportcenters.activities;
//
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.fragments.MyInvitationsFragment;
import ftn.proj.sportcenters.fragments.MyReservationsFragment;

public class MyProfileActivity extends AppCompatActivity {

    MyProfiilePagerAdapter myProfiilePagerAdapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        myProfiilePagerAdapter = new MyProfiilePagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(myProfiilePagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


    }

    public class MyProfiilePagerAdapter extends FragmentStatePagerAdapter {
        public MyProfiilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if(i==0){
                return new MyReservationsFragment();
            }
            else{
                return new MyInvitationsFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Rezervacije";
            }
            else{
                return "Pozivi";
            }
        }
    }

}
