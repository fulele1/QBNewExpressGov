package com.xaqb.policescan.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.xaqb.policescan.R;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by fl on 2017/9/5.
 */

public class OneFragment extends BaseFragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private Context instance;
    private View view;
    private ImageView iv_msg,img_scan_one;
    private ConvenientBanner mCb;
    private List<Integer> mImageList;
    private TextView txt_com ;
    private TextView txt_per ;
    private TextView txt_map ;
    private TextView txt_clue ;
    private TextView txt_psw ;
    private TextView txt_exit ;
    private LinearLayout layout_button ;



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
        img_scan_one = (ImageView) view.findViewById(R.id.img_scan_one);
        layout_button =  view.findViewById(R.id.layout_button);
        requestCodeQRCodePermissions();


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
        img_scan_one.setOnClickListener(this);
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
                showAdialog(instance,"确定要退出?","确定","取消");
                break;
            case R.id.iv_msg_main://通知消息
                ARouterUtil.intentNoPar("/qb/MsgListActivity",view);

                break;

                case R.id.img_scan_one://二维码扫描
                ARouterUtil.intentNoPar("/qb/ScanActivity",view);

                break;
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void okDialog() {
        SPUtils.put(instance, "userPsw", "");
        ARouterUtil.intentNoPar("/qb/loginActivity",view);
        OneFragment.this.getActivity().finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this.getActivity(), perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
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
