package com.xaqb.policescan;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
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
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

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
    EditText et_phone_login,et_psw_login;
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
        et_phone_login = findViewById(R.id.et_phone_login);
        et_psw_login = findViewById(R.id.et_psw_login);
        bt_login = findViewById(R.id.bt_login_login);
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
               String et_phone = et_phone_login.getText().toString().trim();
               String et_psw = et_psw_login.getText().toString().trim();
               if (et_phone.equals("")){
                   Toast.makeText(instance, "请输入账号", Toast.LENGTH_SHORT).show();
                   return;
               }else if (et_psw.equals("")){
                   Toast.makeText(instance, "请密码", Toast.LENGTH_SHORT).show();
                   return;
               }else {
                   Log.e("fule",HttpUrlUtils.getHttpUrl().userLogin());
                   RestClient.builder()
                           .url(HttpUrlUtils.getHttpUrl().userLogin())
                           .params("user",et_phone)
                           .params("pwd",et_psw)
                           .success(new ISuccess() {
                               @Override
                               public void onSuccess(String response) {
                                   Log.e("fule",response);
                                   Map<String, Object> map1 = JSON.parseObject(response,new TypeReference<Map<String, Object>>(){});

                                   if (map1.get("state").toString().equals("0")){
                                       Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(),new TypeReference<Map<String, Object>>(){});
                                       SPUtils.put(instance,"access_token", NullUtil.getString(map2.get("access_token")));
                                       SPUtils.put(instance,"ou_securityorg", NullUtil.getString(map2.get("policeorg")));
                                       SPUtils.put(instance,"so_level", NullUtil.getString(map2.get("solevel")));
                                       SPUtils.put(instance,"org", NullUtil.getString(map2.get("soname")));

                                       ARouter.getInstance()
                                               .build("/qb/MainActivity2")
                                               .withOptionsCompat(ActivityOptionsCompat.
                                                       makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0))//动画效果
                                               .navigation();
                                       finish();
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
                           .post();
               }

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
