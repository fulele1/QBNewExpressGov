package com.xaqb.policescan.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xaqb.policescan.LoginActivity;
import com.xaqb.policescan.R;
import com.xaqb.policescan.UpdateActivityNew;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by fule on 2018/7/26.
 */

public class UpdateUtil {
    private Activity mContext;
    private String mNewVersion;
    private String urlDown;
    private String mAid;
    private AlertDialog alertDialog;
    private UpdateUtil instance;

    public UpdateUtil(Activity context, String aid) {
        mContext = context;
        mAid = aid;
        instance = this;
        initViews();
        savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    }

    /**
     * 下载新版本
     */
    Handler FoHandler = new Handler() {
        int iPercent = 0;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: //获取版本号
                    int newVersion = Integer.parseInt(mNewVersion.replace(".", ""));
                    int nowVersion = Integer.parseInt(getVersionName().replace(".", ""));
                    LogUtils.e("newVersion------"+newVersion);
                    LogUtils.e("nowVersion------"+nowVersion);

                    if (newVersion <= nowVersion) {

//                        SPUtils.put(mContext,"late","true");
                        if (SPUtils.get(mContext,"isclickFragment","").equals("true")){
                            showAdialog(mContext,"提示", "已经是最新版本", "确定", "",View.GONE);
                            SPUtils.put(mContext,"isclickFragment","false");
                        }
                    } else if (newVersion > nowVersion) {
//                        savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
//                        File oFile = new File(savePath + "/" + mnewVersion);
//                        if (oFile.exists()) {//本地存在
//                            isFinished = true;
//                            SPUtils.put(mContext,"late","false");
//                            showAdialog(mContext,"发现新版本", SPUtils.get(mContext,"av_content","").toString(),
//                                    "立刻安装","" ,View.VISIBLE);
//
//                        }else {
                            SPUtils.put(mContext,"late","false");
                            showAdialog(mContext,"发现新版本", SPUtils.get(mContext,"av_content","").toString(),
                                    "立刻更新","" ,View.VISIBLE);
                        }
//                    }
                    break;

                case 1:
                    downVersion();
                    break;

                case 10://下载进度
                    iPercent = msg.arg1;
                    btOk.setText(iPercent+"%");
                    if (pbprogress_dialog != null) pbprogress_dialog.setProgress(iPercent);
                    break;

                case 11://下载完成
                    isFinished = true;
                    btOk.setText("点击安装");
                    btOk.setClickable(true);

                    btOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOk();
                        }
                    });
                    break;
            }

            super.handleMessage(msg);
        }
    };

    boolean isFinished = false;


    /**
     * 获取版本号
     *
     * @return
     */
    public String getVersionName() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);

            // 当前应用的版本名称
            return info.versionName;

        } catch (Exception e) {
            return "";
        }
    }




    //获取版本信息
    public void getVersion() {//{"state":0,"mess":"success","data":"1.1"}

        RestClient.builder()
                .url("http://update.qbchoice.com/api/newVersion/"+mAid)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule",response);
                        Map<String, Object> map1 = JSON.parseObject(response,new TypeReference<Map<String, Object>>(){});

                        if (map1.get("state").toString().equals("0")){
//                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(),new TypeReference<Map<String, Object>>(){});
                            mNewVersion = map1.get("data").toString();
                            FoHandler.sendMessage(M(1));
                        }
                    }


                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(mContext, msg, LENGTH_SHORT).show();
                    }
                })
                .build()
                .get();


    }


    protected Message M(int iWhat) {
        Message oMess = new Message();
        oMess.what = iWhat;
        return oMess;
    }


    protected void downVersion() {
        RestClient.builder()
                .url("http://update.qbchoice.com/api/version/"+mAid+"/"+ mNewVersion)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtils.e("fule-----"+response);
                        Map<String, Object> map1 = JSON.parseObject(response,new TypeReference<Map<String, Object>>(){});

                        if (map1.get("state").toString().equals("0")){
                            Map<String, Object> map2 = JSON.parseObject(map1.get("data").toString(),new TypeReference<Map<String, Object>>(){});
                            urlDown = map2.get("av_downurl").toString();
                            String content = map2.get("av_content").toString();
                            LogUtils.e("content--------"+content);
                            SPUtils.put(mContext,"urlDown",urlDown);
                            SPUtils.put(mContext,"newVersion",mNewVersion);
                            SPUtils.put(mContext,"av_content",content);
                            FoHandler.sendMessage(M(0));
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(mContext, msg, LENGTH_SHORT).show();
                    }
                })
                .build()
                .get();


    }

    /**
     *
     * @param context
     * @param title
     * @param message
     * @param ok
     * @param view
     * @return
     */
    ProgressBar pbprogress_dialog;
    Button btOk;
    public AlertDialog showAdialog(final Context context, String title, String message, String ok, String no,int view) {
        alertDialog = new AlertDialog.Builder(context,R.style.update_dialog).create();
        alertDialog.setCancelable(false);// 不可以用“返回键”取消
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.my_dialog);
        TextView tvTitle = (TextView) window.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(title);
        TextView tvMessage = (TextView) window.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);
        pbprogress_dialog = (ProgressBar) window.findViewById(R.id.pbprogress_dialog);
         btOk =  window.findViewById(R.id.btn_dia_ok);
        btOk.setVisibility(view);
        btOk.setText(ok);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOk();
            }
        });
        Button btNo = (Button) window.findViewById(R.id.btn_dia_no);

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNo();
                alertDialog.dismiss();
            }
        });
        if (view==View.GONE){
            isexit = false;
        }
        return alertDialog;
    }

    boolean isexit  = true;
    /**
     * 对话框单击确定按钮处理
     */
    protected void dialogOk() {
        if (isFinished){
            //安装app
            Intent oInt = new Intent(Intent.ACTION_VIEW);
            LogUtils.e(savePath + "/................." + mnewVersion);
            oInt.setDataAndType(Uri.fromFile(new File(savePath + "/" + mnewVersion)), "application/vnd.android.package-archive");
            //关键点：
            //安装完成后执行打开
            oInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(oInt);
            alertDialog.dismiss();

        }else{
            pbprogress_dialog.setVisibility(View.VISIBLE);

            start();
        }

    }

    /**
     * 对话框单击取消按钮处理
     */
    protected void dialogNo() {
        if (isexit){
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        }

    }



    private String downUrl;
    private String mnewVersion;
    public void initViews() {
        downUrl = SPUtils.get(mContext,"urlDown","").toString();
        mnewVersion = SPUtils.get(mContext,"newVersion","").toString()+".apk";
    }

    protected String savePath = "";
    protected DownFileThread FoThread;
//    protected boolean FbRun = false;
    protected void start() {
        savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        File oFile1 = new File(savePath);
        if (!oFile1.exists()) oFile1.mkdir();

        File oFile = new File(savePath + "/" + mnewVersion);
        if (oFile.exists()) oFile.delete();
        FoThread = new DownFileThread();
//        FbRun = true;
        FoThread.start();
    }


    /**
     * 下载文件
     */
    class DownFileThread extends Thread {
        private int FiState = 0;

        @Override
        public void start() {
            FiState = 2;
            super.start();
        }

        public void over() {
            FiState = 0;
        }

        public void pause() {
            FiState = 1;
        }

        public void resum() {
            if (FiState == 1) FiState = 2;
        }

        protected void send(int iWhat, int iProgress, String sError) {
            Message oMess = Message.obtain();
            oMess.what = iWhat;
            oMess.arg1 = iProgress;
            oMess.obj = sError;
            FoHandler.sendMessage(oMess);
        }
        private static final int REQUEST_EXTERNAL_STORAGE = 1;
        private  String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE };

        public  void verifyStoragePermissions(Activity activity) {
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }
        }
        @Override
        public void run() {
            initViews();
            final File oFile = new File(savePath + "/" + mnewVersion);

            verifyStoragePermissions(mContext);

            try {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet oGet = new HttpGet(downUrl);//此处的URL为http://..../path?arg1=value&....argn=value
                LogUtils.e("下载地址"+downUrl);
                HttpResponse oResponse = client.execute(oGet); //模拟请求
                int iCode = oResponse.getStatusLine().getStatusCode();//返回响应码
                if (iCode == 200) {
                    long iAll = oResponse.getEntity().getContentLength();
                    if (iAll <= 1000) {
                        send(1, 0, "文件数据大小错误");
                        FiState = 0;
                        return;
                    }
                    FileOutputStream oStream = new FileOutputStream(oFile);
                    InputStream oInput = oResponse.getEntity().getContent();
                    try {
                        byte[] aBuffer = new byte[1024];
                        int iLen = -1;
                        long iCount = 0;
                        int iProgress = 0;
                        int iSend = 0;
                        int iTimeout = 0;
                        while (FiState > 0)//0:停止
                        {
                            if (FiState > 1)//1：暂停
                                 {
                                iLen = oInput.read(aBuffer);
                                if (iLen > 0) {
                                    oStream.write(aBuffer, 0, iLen);
                                    iCount += iLen;
                                    if (iAll > 0) {
                                        btOk.setClickable(false);
                                        iProgress = (int) (((float) iCount / iAll) * 100);
                                        if (iProgress > iSend) {
                                            send(10, iProgress, "");
                                            iSend = iProgress;
                                        }
                                    }
                                    iTimeout = 0;
                                    if (iCount >= iAll) {
                                        send(11, 0, "");
                                        FiState = 0;
                                        break;
                                    }
                                    //Thread.sleep(100);
                                } else {
                                    Thread.sleep(100);
                                    iTimeout += 1;
                                    if (iTimeout > 600) {
                                        send(1, 0, "网络传输超时");
                                        break;
                                    }
                                }
                            } else Thread.sleep(1000);
                        } //while
                    } finally {
                        oInput.close();
                        oStream.close();
                    }
                } else {
                    send(1, 0, oResponse.getStatusLine().getReasonPhrase());
                }
                FiState = 0;
            } catch (Exception E) {
                send(1, 0, E.getMessage());

            }
        }
    }

}