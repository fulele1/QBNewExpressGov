package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
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
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.DateUtil;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.Map;

@Route(path = "/qb/ModifyPSWActivity")
public class ModifyPSWActivity extends BaseActivity {
    private ModifyPSWActivity instance;
    private TextView tv_title;
    private Button bt_finished_modify;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        instance= this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();

    }

    private EditText et_old_modify,et_new_modify,et_con_modify;
    @SuppressLint("ResourceAsColor")
    private void initView() {
        et_old_modify = findViewById(R.id.et_old_modify);
        et_new_modify = findViewById(R.id.et_new_modify);
        et_con_modify = findViewById(R.id.et_con_modify);
        bt_finished_modify = findViewById(R.id.bt_finished_modify);
        bt_finished_modify.setOnClickListener(instance);
        tv_title = findViewById(R.id.tv_title_child_tilte);
        tv_title.setText("修改密码");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_finished_modify:
                connection();
                break;
        }
    }


    public void connection(){
        DialogLoadingUtil.getInstance(instance).show();//显示加载框
        String oldpwd = et_old_modify.getText().toString().trim();
        String newpwd = et_new_modify.getText().toString().trim();
        String repwd = et_con_modify.getText().toString().trim();
        if (oldpwd.equals("")){
            Toast.makeText(instance, "请输入旧密码...", Toast.LENGTH_SHORT).show();
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        }else if (newpwd.equals("")){
            Toast.makeText(instance, "请输入新密码...", Toast.LENGTH_SHORT).show();
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        }else if (repwd.equals("")){
            Toast.makeText(instance, "请确认密码...", Toast.LENGTH_SHORT).show();
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        }else {
            RestClient.builder()
                    .url(HttpUrlUtils.getHttpUrl().modifypws()+"?access_token=" + SPUtils.get(instance, "access_token", ""))
                    .params("oldpwd",oldpwd)
                    .params("newpwd",newpwd)
                    .params("repwd",repwd)
                    .success(new ISuccess() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onSuccess(String response) {
                            LogUtils.e(response);
                            DialogLoadingUtil.getInstance(instance).dismiss();
                            Map<String, Object> map1 = JSON.parseObject(response,new TypeReference<Map<String, Object>>(){});
//
                            if (map1.get("state").toString().equals("0")){
                                        SPUtils.put(instance, "userPsw", "");//清除密码
                                ARouter.getInstance()
                                        .build("/qb/loginActivity")
                                        .navigation();
                                instance.finish();
                            }else if (map1.get("state").toString().equals("237")){
                                Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
                            }else if (map1.get("state").toString().equals("221")){
                                Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
                            }else if (NullUtil.getString(map1.get("state")).equals("10")) {
                            ARouterUtil.intentNoPar("/qb/loginActivity", bt_finished_modify);
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
//{"state":"237","mess":"两次密码不一致"}
        //{"state":"221","mess":"旧密码输入错误"}
        //{"state":0,"mess":"success"}
    }

}
