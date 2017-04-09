package com.example.allu.trackyourpal.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.allu.trackyourpal.UI.Fragments.Fragment_Friends;
import com.example.allu.trackyourpal.UI.Fragments.Fragment_settings;
import com.example.allu.trackyourpal.UI.Fragments.YourTourFragment;

/**
 * Created by allu on 4/9/17.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new YourTourFragment();
            case 1:
                // Games fragment activity
                return new Fragment_Friends();
            case 2:
                // Movies fragment activity
                return new Fragment_settings();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}