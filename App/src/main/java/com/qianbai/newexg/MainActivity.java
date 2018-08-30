package com.qianbai.newexg;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.adapter.FragmentAdapter;
import com.qianbai.newexg.fragment.OneFragment;
import com.qianbai.newexg.fragment.ThreeFragment;
import com.qianbai.newexg.fragment.TwoFragment;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/qb/MainActivity")
public class MainActivity extends BaseActivity {

    private MainActivity instance;
    private ViewPager mVpg;
    private RadioGroup mRgp;
    private RadioButton mRbOne, mRbTwo, mRbThree;
    private List<Fragment> mFrags;
    private FragmentManager mFragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatuBarUtil.setBanner(this);
        instance = this;
        initView();
        initDate();
        addEvent();
    }

    private void addEvent() {
        mVpg.setAdapter(new FragmentAdapter(mFragmentManager, mFrags));
        mRgp.setOnCheckedChangeListener(new CheckedChange());
        mVpg.setOnPageChangeListener(new pageChange());
    }

    private void initDate() {
        mFrags = new ArrayList<>();
        mFrags.add(new OneFragment());
        mFrags.add(new TwoFragment());
        mFrags.add(new ThreeFragment());
        mFrags.add(new ThreeFragment());
    }

    private void initView() {
        mFragmentManager = this.getSupportFragmentManager();
        mVpg = findViewById(R.id.vpg_main);
        mRgp = findViewById(R.id.rgp_main);
        mRbOne = findViewById(R.id.rb_one_main);
        mRbTwo = findViewById(R.id.rb_two_main);
        mRbThree = findViewById(R.id.rb_three_main);
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
                    mVpg.setCurrentItem(0);
                    StatuBarUtil.setBanner(instance);

                    break;
                case R.id.rb_two_main:
                    mVpg.setCurrentItem(1);
                    StatuBarUtil.setStatuBarLightMode(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色

                    break;
                case R.id.rb_three_main:
                    mVpg.setCurrentItem(2);
                    StatuBarUtil.setStatuBarLightMode(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色

                    break;
            }
        }
    }


    /**
     * 页面滑动后设置当前为点击
     */
    class pageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mRbOne.setChecked(true);
                    mRbOne.setTextColor(instance.getResources().getColor(R.color.main));
                    mRbTwo.setTextColor(Color.BLACK);
                    mRbThree.setTextColor(Color.BLACK);
                    StatuBarUtil.setBanner(instance);
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
    }


}
