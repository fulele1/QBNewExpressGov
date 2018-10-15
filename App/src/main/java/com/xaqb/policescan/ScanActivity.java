package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.StatuBarUtil;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

@Route(path = "/qb/ScanActivity")
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate{

    private ZBarView zbarview;
    private TextView tv_title_child;
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        StatuBarUtil.setStatuBarLightModeClild(this, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色

        initView();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        zbarview = findViewById(R.id.zbarview);
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("扫描");
    }


    @Override
    protected void onStart() {
        zbarview.setDelegate(this);
        zbarview.startCamera();
        zbarview.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别

        super.onStart();
    }

    @Override
    protected void onStop() {
        zbarview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zbarview.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.e("fule", "result:" + result);
        Bundle bundle = new Bundle();
        bundle.putString("billcode",result);
        ARouterUtil.intentPar("/qb/ExpressActivity",zbarview,bundle);
        vibrate();


        zbarview.startSpot(); // 延迟0.5秒后开始识别
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("fule", "打开相机出错");
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
//            case R.id.choose_qrcde_from_gallery:
//
//                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
//                        .cameraFileDir(null)
//                        .maxChooseCount(1)
//                        .selectedPhotos(null)
//                        .pauseOnScroll(false)
//                        .build();
//                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
//                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        zbarview.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
            zbarview.decodeQRCode(picturePath);

            /*
            没有用到 QRCodeView 时可以调用 QRCodeDecoder 的 syncDecodeQRCode 方法

            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
            .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    if (TextUtils.isEmpty(result)) {
//                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }.execute();
        }
    }



}
