package com.xaqb.policescan.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;

    private List<String> mLabels;

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, List<String> mLabels) {
        super(fm);
        this.mFragments = mFragments;
        this.mLabels = mLabels;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);

    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mLabels.get(position);
    }


}
