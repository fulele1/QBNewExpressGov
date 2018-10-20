package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.DateUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.Map;

@Route(path = "/qb/JointDelActivity")
public class JointDelActivity extends BaseActivity {

    private JointDelActivity instance;
    private TextView tv_title_child;
    private String id;
    private TextView txt_comname_joint_del,txt_ucorgname_joint_del,txt_ucorgdate_joint_del,txt_ucorguser_joint_del,
            txt_ucorgthing_joint_del,txt_uccontent_joint_del,txt_ucresult_joint_del,txt_ucuser_joint_del,txt_ucdate_joint_del;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_del);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
        internet();
    }

    private void internet() {

        Log.e("fule", HttpUrlUtils.getHttpUrl().joint_list() +"/"+ id + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().joint_list() +"/"+ id + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            txt_comname_joint_del.setText(NullUtil.getString(map2.get("comname")));
                            txt_ucorgname_joint_del.setText(NullUtil.getString(map2.get("ucorgname")));
                            txt_ucorgdate_joint_del.setText(DateUtil.getDate(NullUtil.getString(map2.get("ucorgdate"))));
                            txt_ucorguser_joint_del.setText(NullUtil.getString(map2.get("ucorguser")));
                            txt_ucorgthing_joint_del.setText(NullUtil.getString(map2.get("ucorgthing")));
                            txt_uccontent_joint_del.setText(NullUtil.getString(map2.get("uccontent")));
                            txt_ucresult_joint_del.setText(NullUtil.getString(map2.get("ucresult")));
                            txt_ucuser_joint_del.setText(NullUtil.getString(map2.get("ucuser")));
                            txt_ucdate_joint_del.setText(DateUtil.getDate(NullUtil.getString(map2.get("ucdate"))));
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
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
                .get();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        id = bundle.getString("id");
    }



    @SuppressLint("WrongViewCast")
    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("日志详情");
        txt_comname_joint_del = findViewById(R.id.txt_comname_joint_del);
        txt_ucorgname_joint_del = findViewById(R.id.txt_ucorgname_joint_del);
        txt_ucorgdate_joint_del = findViewById(R.id.txt_ucorgdate_joint_del);
        txt_ucorguser_joint_del = findViewById(R.id.txt_ucorguser_joint_del);
        txt_ucorgthing_joint_del = findViewById(R.id.txt_ucorgthing_joint_del);
        txt_uccontent_joint_del = findViewById(R.id.txt_uccontent_joint_del);
        txt_ucresult_joint_del = findViewById(R.id.txt_ucresult_joint_del);
        txt_ucuser_joint_del = findViewById(R.id.txt_ucuser_joint_del);
        txt_ucdate_joint_del = findViewById(R.id.txt_ucdate_joint_del);
    }
}
