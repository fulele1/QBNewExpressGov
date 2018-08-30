package com.qianbai.newexg;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jaeger.library.StatusBarUtil;
import com.qianbai.newexg.fragment.OneFragment;
import com.qianbai.newexg.fragment.ThreeFragment;
import com.qianbai.newexg.fragment.TwoFragment;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.util.ArrayList;
import java.util.Random;

@Route(path = "/qb/MainActivity2")
public class Main2Activity extends BaseActivity {

    private Main2Activity instance;
    private ViewPager mVpHome;
//    private BottomNavigationBar mBottomNavigationBar;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private RadioGroup mRgp;
    private RadioButton mRbOne, mRbTwo, mRbThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        instance = this;
        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
        initView();
        setViewPage();
    }

    private void initView() {
        mVpHome = (ViewPager) findViewById(R.id.vp_home);
        mRgp = findViewById(R.id.rgp_main);
        mRbOne = findViewById(R.id.rb_one_main);
        mRbTwo = findViewById(R.id.rb_two_main);
        mRbThree = findViewById(R.id.rb_three_main);
        mRgp.setOnCheckedChangeListener(new CheckedChange());


    }


    /**
     * 点击改变页面的监听事件
     */
    class CheckedChange implements RadioGroup.OnCheckedChangeListener {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_one_main:
                    mVpHome.setCurrentItem(0);
                    break;
                case R.id.rb_two_main:
                    mVpHome.setCurrentItem(1);
                    break;
                case R.id.rb_three_main:
                    mVpHome.setCurrentItem(2);
                    break;
            }
        }
    }


    private void setViewPage() {

//        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
//        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.button_one, "首页"))
//                .addItem(new BottomNavigationItem(R.drawable.button_two, "检查日志"))
//                .addItem(new BottomNavigationItem(R.drawable.button_three, "数据分析"))
//                .initialise();
//
//        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(int position) {
//                mVpHome.setCurrentItem(position);
//            }
//
//            @Override
//            public void onTabUnselected(int position) {
//
//            }
//
//            @Override
//            public void onTabReselected(int position) {
//
//            }
//        });

        mFragmentList.add(new OneFragment());
        mFragmentList.add(new TwoFragment());
        mFragmentList.add(new ThreeFragment());

        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageSelected(int position) {
//                mBottomNavigationBar.selectTab(position);
                Log.e("fule",position+"");
                switch (position) {
                    case 0:
                        mRbOne.setChecked(true);
                        mRbOne.setTextColor(instance.getResources().getColor(R.color.main));
                        mRbTwo.setTextColor(Color.BLACK);
                        mRbThree.setTextColor(Color.BLACK);
                        StatusBarUtil.setTransparentForImageViewInFragment(instance, null);
                        break;
                        case 1:
                            mRbTwo.setChecked(true);
                            mRbTwo.setTextColor(instance.getResources().getColor(R.color.main));
                            mRbOne.setTextColor(Color.BLACK);
                            mRbThree.setTextColor(Color.BLACK);
                            StatuBarUtil.setStatuBarLightMode(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
                            break;
                        case 2:
                            mRbThree.setChecked(true);
                            mRbThree.setTextColor(instance.getResources().getColor(R.color.main));
                            mRbOne.setTextColor(Color.BLACK);
                            mRbTwo.setTextColor(Color.BLACK);
                            StatuBarUtil.setStatuBarLightMode(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpHome.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });


    }
}
