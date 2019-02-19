package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

@Route(path = "/qb/BackPWDActivity")
public class BackPWDActivity extends BaseActivity {

    private BackPWDActivity instance;
    private TextView tv_title, tv_find_bpwd;
    private TimeCount time;
    private EditText et_phone_backpwd, edit_pwd_backpwd, edit_code_backpwd;
    private Button bt_finish_bpwd;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pwd);
        instance = this;
        StatuBarUtil.translucentStatusBar(this, true);
        initView();
        addEvent();

    }

    private void addEvent() {
        tv_find_bpwd.setOnClickListener(instance);
        bt_finish_bpwd.setOnClickListener(instance);
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        tv_title = findViewById(R.id.tv_title_tilte);
        tv_title.setText("找回密码");
        et_phone_backpwd = findViewById(R.id.et_phone_backpwd);
        edit_pwd_backpwd = findViewById(R.id.edit_pwd_backpwd);
        edit_code_backpwd = findViewById(R.id.edit_code_backpwd);
        tv_find_bpwd = findViewById(R.id.tv_find_bpwd);
        bt_finish_bpwd = findViewById(R.id.bt_finish_bpwd);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_find_bpwd:
                getVCode();
                break;
            case R.id.bt_finish_bpwd:
                connection();
                break;
        }
    }


    public void connection() {
        DialogLoadingUtil.getInstance(instance).show();
        String tel = et_phone_backpwd.getText().toString().trim();
        String checkcode = edit_code_backpwd.getText().toString().trim();
        String newpwd = edit_pwd_backpwd.getText().toString().trim();
        if (tel.equals("")) {
            Toast.makeText(instance, "请输入电话号码...", Toast.LENGTH_SHORT).show();
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (checkcode.equals("")) {
            Toast.makeText(instance, "请输入手机验证码...", Toast.LENGTH_SHORT).show();
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (newpwd.equals("")) {
            Toast.makeText(instance, "请输入新密码...", Toast.LENGTH_SHORT).show();
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else {
            RestClient.builder()
                    .url(HttpUrlUtils.getHttpUrl().back_pwd(instance))
                    .params("tel", tel)
                    .params("checkcode", checkcode)
                    .params("newpwd", newpwd)
                    .success(new ISuccess() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onSuccess(String response) {
                            DialogLoadingUtil.getInstance(instance).dismiss();
                            LogUtils.e(response);
                            Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                            });
//
                            if (map1.get("state").toString().equals("0")) {
                                ARouter.getInstance()
                                        .build("/qb/loginActivity")
                                        .navigation();
                                instance.finish();
                            } else {
                                Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure(String s) {
                            DialogLoadingUtil.getInstance(instance).dismiss();
                            Toast.makeText(instance, "修改密码失败", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .error(new IError() {
                        @Override
                        public void onError(int code, String msg) {
                            DialogLoadingUtil.getInstance(instance).dismiss();
                            Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build()
                    .post();
        }

        DialogLoadingUtil.getInstance(instance).dismiss();
    }

    String phone;

    /**
     * 获取手机验证码
     */
    public void getVCode() {
        DialogLoadingUtil.getInstance(instance).show();
        phone = et_phone_backpwd.getText().toString().trim();
        if (phone == null || phone.equals("")) {
            Toast.makeText(instance, "请输入手机号码", Toast.LENGTH_SHORT).show();
            DialogLoadingUtil.getInstance(instance).dismiss();
        } else {

            LogUtils.e(HttpUrlUtils.getHttpUrl().getSmsCode(instance) +
                    "?tel=" + phone);
            RestClient.builder()
                    .url(HttpUrlUtils.getHttpUrl().getSmsCode(instance) +
                            "?tel=" + phone)
                    .success(new ISuccess() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onSuccess(String response) {
                            DialogLoadingUtil.getInstance(instance).dismiss();
                            LogUtils.e(response);
                            Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                            });
                            LogUtils.e("state" + map1.get("state").toString());
                            if (map1.get("state").toString().equals("0")) {
                                time = new TimeCount(60000, 1000);//构造CountDownTimer对象
                                time.start();
                                Toast.makeText(instance, "已发送验证码至您的手机", Toast.LENGTH_SHORT).show();
                            } else if (map1.get("state").toString().equals("204")) {
                                Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure(String s) {
                            DialogLoadingUtil.getInstance(instance).dismiss();
                            Toast.makeText(instance, "获取验证码失败，请重新获取", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .error(new IError() {
                        @Override
                        public void onError(int code, String msg) {
                            DialogLoadingUtil.getInstance(instance).dismiss();
                            Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build()
                    .get();

        }
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tv_find_bpwd.setText("重新获取");
            tv_find_bpwd.setBackgroundResource(R.drawable.bg_button);
            tv_find_bpwd.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv_find_bpwd.setClickable(false);
            tv_find_bpwd.setText(millisUntilFinished / 1000 + "S" + "后重新获取");
            tv_find_bpwd.setBackgroundResource(R.drawable.bg_button);
        }
    }

}
