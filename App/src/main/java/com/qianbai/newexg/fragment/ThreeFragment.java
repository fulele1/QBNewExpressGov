package com.qianbai.newexg.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qianbai.newexg.R;
import com.qianbai.newexg.utils.StatuBarUtil;


/**
 * Created by fl on 2017/5/18.
 */

public class ThreeFragment extends BaseFragment implements View.OnClickListener{

    private Context instance;
    private View view;
    private Button btn_query;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = ThreeFragment.this.getActivity();
        view = inflater.inflate(R.layout.fragment_three,null);
        StatuBarUtil.setStatuBarLightMode(this.getActivity(), getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色

        btn_query = view.findViewById(R.id.bt_query_three);
        setEvent();
        return view;
    }

    private void setEvent() {
        btn_query.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_query_three:
                ARouter.getInstance()
                        .build("/qb/DataDelActivity")
                        .withOptionsCompat(ActivityOptionsCompat.
                                makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0))//动画效果
                        .navigation();
                break;
        }
    }
}
