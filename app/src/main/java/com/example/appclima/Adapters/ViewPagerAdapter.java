package com.example.appclima.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appclima.Fragments.DataFragment;
import com.example.appclima.Fragments.MapFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public ViewPagerAdapter(FragmentManager manager, int tabs)
    {
        super(manager);
        this.numberOfTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new DataFragment();

            case 1:
                return new MapFragment();

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
