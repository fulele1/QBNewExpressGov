package com.xaqb.policescan.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xaqb.policescan.R;
import com.xaqb.policescan.adapter.MyFragmentPagerAdapter;
import com.xaqb.policescan.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fl on 2017/5/18.
 */

public class TwoFragment extends BaseFragment implements TabLayout.OnTabSelectedListener,
        ViewPager.OnPageChangeListener{

    private Context instance;
    private View view;


    private FrameLayout chartFragments;
    private TabLayout topTabs;
    private ViewPager viewpager;
    private Fragment twoOneFragment,twoTwoFragment;
    private FragmentTransaction fragmentTransaction;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, null);
        instance = TwoFragment.this.getActivity();


        chartFragments = view.findViewById(R.id.fragments);
        topTabs = view.findViewById(R.id.top_tabs);
        viewpager = view.findViewById(R.id.viewpager);

        initviewPage();


//        topTabs.setTabMode(TabLayout.MODE_FIXED);
//        topTabs.addTab(topTabs.newTab().setText("检查日志"),0);
//        topTabs.addTab(topTabs.newTab().setText("日常检查"),1);
//        topTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                FragmentManager manager=getChildFragmentManager();
//                fragmentTransaction = getChildFragmentManager().beginTransaction();
//                hideFragment(fragmentTransaction);
//                switch (tab.getPosition()){
//                    case 0:
//                        twoOneFragment = manager.findFragmentByTag("Tag0");
//                        if (twoOneFragment == null){
//                            twoOneFragment = new TwoBillFragment();
//                            fragmentTransaction.add(R.id.fragments,twoOneFragment,"Tag0");
//                        }else {
//                            fragmentTransaction.show(twoOneFragment);
//                        }
//                        break;
//                    case 1:
//                        twoTwoFragment = manager.findFragmentByTag("Tag1");
//                        if (twoTwoFragment == null){
//                            twoTwoFragment = new TwoComFragment();
//                            fragmentTransaction.add(R.id.fragments, twoTwoFragment,"Tag1");
//                        }else {
//                            fragmentTransaction.show(twoTwoFragment);
//                        }
//                        break;
//                }
//                fragmentTransaction.commit();//提交事务
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                fragmentTransaction.show(twoOneFragment);
//            }
//        });
//
//        topTabs.getTabAt(1).select();
//        topTabs.getTabAt(0).select();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
//        initviewPage();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mLabels.clear();
//        mFragments.clear();
    }


    public void initviewPage(){
         ArrayList<Fragment> mFragments = new ArrayList<>();
        List<String>  mLabels = new ArrayList<>();
//        if (mFragments.size() == 0&&mLabels.size() ==0) {
            mLabels.add("检查日志");
            mLabels.add("日常检查");
            topTabs.addOnTabSelectedListener(this);
            for (int i = 0; i < mLabels.size(); i ++) topTabs.addTab(topTabs.newTab().setText(mLabels.get(i)));

            mFragments.add(new TwoBillFragment());
            mFragments.add(new TwoComFragment());
            viewpager.setAdapter(new MyFragmentPagerAdapter(this.getChildFragmentManager(), mFragments, mLabels));
            viewpager.addOnPageChangeListener(this);

        LogUtils.e("mFragments-----------"+mFragments.size());
        LogUtils.e("mLabels------------"+mFragments.size());

//        }else {
//            viewpager.setAdapter(new MyFragmentPagerAdapter(this.getActivity().getSupportFragmentManager(), mFragments, mLabels));
//            viewpager.addOnPageChangeListener(this);
//        }

    }

    private void hideFragment(FragmentTransaction fragmentTransaction){

        if (twoOneFragment != null){
            fragmentTransaction.hide(twoOneFragment);
        }

        if (twoTwoFragment != null){
            fragmentTransaction.hide(twoTwoFragment);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        topTabs.setScrollPosition(position, 0, true);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }






}