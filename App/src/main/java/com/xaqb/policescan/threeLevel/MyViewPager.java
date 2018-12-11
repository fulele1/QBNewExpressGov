package com.xaqb.policescan.threeLevel;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by LaiYingtang on 2016/5/22.
 * 主页面左右滑动
 */
public class MyViewPager extends ViewPager {
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context) {
        super(context);
    }
    //判断menu在x,y的位置
    public void scrollTo(int x,int y){
        if(getAdapter()==null||x>getWidth()*(getAdapter().getCount()-2)){
            return;
        }
        super.scrollTo(x,y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {//禁止左右滑动
        return false;
    }
}
