package com.example.allu.trackyourpal.UI.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.allu.trackyourpal.R;
import com.example.allu.trackyourpal.UI.Fragments.Fragment_Friends;
import com.example.allu.trackyourpal.UI.Fragments.Fragment_findFriends;
import com.example.allu.trackyourpal.UI.Fragments.Fragment_settings;
import com.example.allu.trackyourpal.UI.Fragments.YourTourFragment;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeTab extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public int int_items = 0 ;
    SharedPreferences sharedPreferences;

    String Tag="WeektabFragment";
    String[] Tabs = {"Your Tour","Friends","Settings"};

    ProgressDialog progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.fragment_home_tab,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        tabLayout.setMinimumWidth(40);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);


        Log.e("TweekinfoTabfragment","init");

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position) {
                case 0:
                    // Top Rated fragment activity
                    return new YourTourFragment();
                case 1:
                    // Games fragment activity
                    return new Fragment_Friends();
                case 2:
                    // Movies fragment activity
                    return new Fragment_findFriends();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {
            return Tabs[position];

        }
    }


}
