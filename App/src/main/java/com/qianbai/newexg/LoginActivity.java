package com.qianbai.newexg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qianbai.newexg.RLview.Clue;
import com.qianbai.newexg.net.RestClient;
import com.qianbai.newexg.net.callback.IError;
import com.qianbai.newexg.net.callback.IFailure;
import com.qianbai.newexg.net.callback.ISuccess;
import com.qianbai.newexg.utils.HttpUrlUtils;
import com.qianbai.newexg.utils.NullUtil;
import com.qianbai.newexg.utils.SPUtils;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

@Route(path="/qb/loginActivity")
public class LoginActivity extends BaseActivity {

    private LoginActivity instance;
    private Unbinder unbinder;
//    @BindView(R.id.txt_finished_login)
    TextView txt_finished;
//    @BindView(R.id.txt_register_login)
    TextView txt_back_pwd,bt_login;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        instance = this;
        unbinder= ButterKnife.bind(instance);
        StatuBarUtil.translucentStatusBar(this,true);
        initView();
    }

    private void initView() {
        txt_finished = findViewById(R.id.txt_finished_login);
        txt_finished.setOnClickListener(instance);

        txt_back_pwd = findViewById(R.id.txt_back_pwd_login);
        txt_back_pwd.setOnClickListener(instance);

        bt_login = findViewById(R.id.bt_login_login);
        bt_login.setOnClickListener(instance);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.txt_finished_login:
                showAdialog(instance,"提示","是否退出登录","确定",View.VISIBLE);
                break;
            case R.id.txt_back_pwd_login:
                ARouter.getInstance()
                        .build("/qb/BackPWDActivity")
                        .withOptionsCompat(ActivityOptionsCompat.
                                makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0))//动画效果
                        .navigation();
                break;
            case R.id.bt_login_login:
                Log.e("fule",HttpUrlUtils.getHttpUrl().userLogin());
                RestClient.builder()
                        .url(HttpUrlUtils.getHttpUrl().userLogin())
                .params("user","police")
                .params("pwd","123456")
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                Log.e("fule",response);

                                Map<String, Object> map1 = JSON.parseObject(response,new TypeReference<Map<String, Object>>(){});

                                if (map1.get("state").toString().equals("0")){
                                    Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(),new TypeReference<Map<String, Object>>(){});
                                    SPUtils.put(instance,"access_token", NullUtil.getString(map2.get("access_token")));


                                    ARouter.getInstance()
                                            .build("/qb/MainActivity2")
                                            .withOptionsCompat(ActivityOptionsCompat.
                                                    makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0))//动画效果
                                            .navigation();
                                    finish();



                                }


                            }
                        })
                        .failure(new IFailure() {
                            @Override
                            public void onFailure() {
                                Toast.makeText(instance, "失败", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .error(new IError() {
                            @Override
                            public void onError(int code, String msg) {
                                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build()
                        .post();
        }
    }

    @Override
    public void dialogOk() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
