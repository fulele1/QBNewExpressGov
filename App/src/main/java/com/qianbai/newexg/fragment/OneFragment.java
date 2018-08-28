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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.qianbai.newexg.ComQueryActivity;
import com.qianbai.newexg.PerQueryActivity;
import com.qianbai.newexg.R;
import com.qianbai.newexg.utils.ARouterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2017/9/5.
 */

public class OneFragment extends BaseFragment implements View.OnClickListener{

    private Context instance;
    private View view;
    private ImageView iv_msg;
    private ConvenientBanner mCb;
    private List<Integer> mImageList;
    private TextView txt_com ;
    private TextView txt_per ;
    private TextView txt_map ;
    private TextView txt_clue ;
    private TextView txt_psw ;
    private TextView txt_exit ;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one,null);
        instance = this.getActivity();
        initView();
        initData();
        addEvent();
        return view;
    }

    public void initView() {
        mCb = (ConvenientBanner) view.findViewById(R.id.cb_main);
        txt_com = (TextView) view.findViewById(R.id.txt_com_one);
        txt_per = (TextView) view.findViewById(R.id.txt_per_one);
        txt_map = (TextView) view.findViewById(R.id.txt_map_one);
        txt_clue = (TextView) view.findViewById(R.id.txt_clue_one);
        txt_psw = (TextView) view.findViewById(R.id.txt_psw_one);
        txt_exit = (TextView) view.findViewById(R.id.txt_exit_one);
        iv_msg = (ImageView) view.findViewById(R.id.iv_msg_main);
    }

    public void initData() {
        mImageList = new ArrayList();
        mImageList.add(R.mipmap.index_banner_main);
        mImageList.add(R.mipmap.one_banner);
        cbSetPage();
        mCb.startTurning(2000);
        cbItemEvent();
    }

    public void addEvent() {
        txt_com.setOnClickListener(this);
        txt_per.setOnClickListener(this);
        txt_map.setOnClickListener(this);
        txt_clue.setOnClickListener(this);
        txt_psw.setOnClickListener(this);
        txt_exit.setOnClickListener(this);
        iv_msg.setOnClickListener(this);
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_com_one://企业信息
                ARouterUtil.intentNoPar("/qb/ComQueryActivity",view);
                break;
            case R.id.txt_per_one://从业人员
                ARouterUtil.intentNoPar("/qb/PerQueryActivity",view);
                break;
            case R.id.txt_map_one://地图导航
                ARouterUtil.intentNoPar("/qb/MapNavgActivity",view);

                break;
            case R.id.txt_clue_one://线索信息
                ARouterUtil.intentNoPar("/qb/ClueListActivity",view);

                break;
            case R.id.txt_psw_one://修改密码
                ARouterUtil.intentNoPar("/qb/ModifyPSWActivity",view);

                break;
            case R.id.txt_exit_one://退出系统
                showAdialog(instance,"提示","是否要退出系统?","确定","取消");
                break;
            case R.id.iv_msg_main://通知消息
                ARouterUtil.intentNoPar("/qb/MsgListActivity",view);

                break;
        }

    }


    /**
     * 轮播图holder
     */
    public class CbHolder implements Holder<Integer> {

        private ImageView pImg;
        @Override
        public View createView(Context context) {
            pImg = new ImageView(context);
            pImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return pImg;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            pImg.setImageResource(data);
        }
    }


    /**
     * 轮播图设置图片
     */
    public void cbSetPage(){
        mCb.setPages(new CBViewHolderCreator<CbHolder>() {
            @Override
            public CbHolder createHolder() {
                return new CbHolder();
            }
        },mImageList);
//         .setPageIndicator(new int[] {R.mipmap.pointn, R.mipmap.pointc})
//         .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT);
    }



    /**
     * 轮播图子条目的点击事件
     */
    public void cbItemEvent(){
        mCb.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(instance,"这是第"+position+"图片",Toast.LENGTH_LONG).show();
            }
        });
    }



}
