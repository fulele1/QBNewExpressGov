package com.xaqb.policescan.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.xaqb.policescan.R;
import com.xaqb.policescan.RLview.Per;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.UpdateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by fl on 2017/9/5.
 */

public class OneFragment extends BaseFragment implements View.OnClickListener,
        EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private Context instance;
    private View view;
    private ImageView iv_msg,img_scan_one,iv_user_main;
    private ConvenientBanner convenientBanner;
    private List<Integer> mImageList;
    private TextView txt_com ;
    private TextView txt_per ;
    private TextView txt_map ;
    private TextView txt_clue ;
    private TextView txt_psw ;
    private TextView txt_exit,txt_update_one ;
    private EditText et_query_main ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one,null);
        instance = this.getActivity();
        initView();
        initData();
        addEvent();
        getConnection();//轮播图
        new UpdateUtil(getActivity(), "25").getVersion();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        et_query_main.setText("");
    }

    public void initView() {
        convenientBanner =  view.findViewById(R.id.cb_main);
        txt_com =  view.findViewById(R.id.txt_com_one);
        txt_per =  view.findViewById(R.id.txt_per_one);
        txt_map =  view.findViewById(R.id.txt_map_one);
        txt_clue =  view.findViewById(R.id.txt_clue_one);
        txt_psw =  view.findViewById(R.id.txt_psw_one);
        txt_exit =  view.findViewById(R.id.txt_exit_one);
        txt_update_one =  view.findViewById(R.id.txt_update_one);
        iv_msg =  view.findViewById(R.id.iv_msg_main);
        img_scan_one =  view.findViewById(R.id.img_scan_one);
        iv_user_main =  view.findViewById(R.id.iv_user_main);
        et_query_main =  view.findViewById(R.id.et_query_main);
        requestCodeQRCodePermissions();
    }

    public void initData() {
        mImageList = new ArrayList();
        mImageList.add(R.mipmap.index_banner_main);
        mImageList.add(R.mipmap.one_banner);
//        cbSetPage();
        convenientBanner.startTurning(2000);
//        cbItemEvent();
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
        iv_user_main.setOnClickListener(this);
        txt_update_one.setOnClickListener(this);

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
                if (et_query_main.getText().toString().equals("")){
                    ARouterUtil.intentNoPar("/qb/ScanActivity",view);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("billcode",et_query_main.getText().toString());
                    ARouterUtil.intentPar("/qb/ExpressActivity",v,bundle);
                }
                break;
            case R.id.iv_user_main://个人信息
                ARouterUtil.intentNoPar("/qb/UserActivity",view);

                break;
            case R.id.txt_update_one://检查更新
                LogUtils.e("检查更新");
                SPUtils.put(instance,"isclickFragment","true");
                new UpdateUtil(getActivity(), "25").getVersion();
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void okDialog() {
//        SPUtils.put(instance, "userPsw", "");//清除密码
        ARouterUtil.intentNoPar("/qb/loginActivity",view);
        OneFragment.this.getActivity().finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,
                grantResults, this);
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

    private String[] images;
    private String[] url;
    private List<String> bean;
    //轮播下面的小点（小圆点是本地的，自己导入的图片）
    private int[] indicator = {R.mipmap.point_gary, R.mipmap.point_red};

    public void getConnection() {


        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().banner_pic(instance)+"?access_token="+SPUtils.get(instance,"access_token",""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule","轮播"+response);
                        Map<String, Object> map1 = JSON.parseObject(response,new TypeReference<Map<String, Object>>(){});

                        if (map1.containsKey("table")){
                            if (map1.get("state").toString().equals("0")){
                                String table = map1.get("table").toString();
                                List<Map> list1 = JSON.parseArray(table, Map.class);
                                images = new String [list1.size()] ;
                                url = new String [list1.size()] ;
                                for (int j = 0; j < list1.size(); j++) {
                                    LogUtils.e("图片");
                                    images[j] = NullUtil.getString(list1.get(j).get("artimg"));
                                    url[j] = NullUtil.getString(list1.get(j).get("url"));
                                }
                                bean = Arrays.asList(images);
                                convenientBanner.setPointViewVisible(true)
                                        //设置小点
                                        .setPageIndicator(indicator);
                                //允许手动轮播
                                convenientBanner.setManualPageable(true);
                                //设置自动轮播的时间
                                convenientBanner.startTurning(2000);
                                //设置点击事件
                                //泛型为具体实现类ImageLoaderHolder
                                convenientBanner.setPages(new CBViewHolderCreator<NetImageLoadHolder>() {
                                    @Override
                                    public NetImageLoadHolder createHolder() {
                                        return new NetImageLoadHolder();
                                    }
                                }, bean);

                                //设置每个pager的点击事件
                                convenientBanner.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
//                                    if (url.length>0){
//                                        Uri uri = Uri.parse(url[convenientBanner.getCurrentItem()]);
//                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                        instance.startActivity(intent);
//                                    }
                                    }
                                });
                        }


                        }
                        if (map1.get("state").toString().equals("202")){
                            Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
                        Toast.makeText(instance, s, Toast.LENGTH_SHORT).show();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .get();
    }


    public class NetImageLoadHolder implements Holder<String> {
        private ImageView image_lv;

        //可以是一个布局也可以是一个Imageview
        @Override
        public ImageView createView(Context context) {
            image_lv = new ImageView(context);
            image_lv.setScaleType(ImageView.ScaleType.FIT_XY);

            return image_lv;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {

            Glide.with(context)
                    .load(data)
                    .into(image_lv);
        }

    }

}
