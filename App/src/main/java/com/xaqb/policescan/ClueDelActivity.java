package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.DateUtil;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.Map;

@Route(path = "/qb/ClueDelActivity")
public class ClueDelActivity extends BaseActivity {
    private ClueDelActivity instance;
    private TextView tv_title_child, tv_right_child, txt__Per_cld;
    private String id;
    private ImageView img_pic_cdel;
    private TextView txt_responsible_Per_cld,
            txt_responsible_tel_cld, txt_employee_count_cld,
            txt_get_count_cld, txt_post_count_cld, txt_tel_cld;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_del);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
        DialogLoadingUtil.getInstance(instance).show();
        internet();
    }

    private void internet() {
        Log.e("fule", HttpUrlUtils.getHttpUrl().clue_del(instance) + id + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().clue_del(instance) + id + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        DialogLoadingUtil.getInstance(instance).dismiss();
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            txt_responsible_Per_cld.setText(NullUtil.getString(map2.get("scbillcode")));//运单号
                            txt_responsible_tel_cld.setText(NullUtil.getString(map2.get("comname")));//企业名称
                            txt_employee_count_cld.setText(DateUtil.getDate(NullUtil.getString(map2.get("sccreatetime"))));//时间
                            txt_get_count_cld.setText(NullUtil.getString(map2.get("scaddress")));//地址
                            txt_post_count_cld.setText(NullUtil.getString(map2.get("scsketch")));//线索信息
//                            txt_post_count_cld.setText(NullUtil.getString(map2.get("scimg")));//图片
                            txt__Per_cld.setText(NullUtil.getString(map2.get("empname")));//录入人
                            String tel = NullUtil.getString(map2.get("scmp"));
                            if (tel.equals("")) {
                                txt_tel_cld.setText("无");//联系电话
                            } else {
                                txt_tel_cld.setText(tel);//联系电话
                            }

                            String url = NullUtil.getString(map2.get("scimg").toString());
                            Glide.with(instance)
                                    .load(url)
                                    .into(img_pic_cdel);
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
        tv_title_child.setText("线索详情");
        txt_responsible_Per_cld = findViewById(R.id.txt_responsible_Per_cld);
        txt_responsible_tel_cld = findViewById(R.id.txt_responsible_tel_cld);
        txt_employee_count_cld = findViewById(R.id.txt_employee_count_cld);
        txt_get_count_cld = findViewById(R.id.txt_get_count_cld);
        txt_post_count_cld = findViewById(R.id.txt_post_count_cld);
        txt__Per_cld = findViewById(R.id.txt__Per_cld);
        img_pic_cdel = findViewById(R.id.img_pic_cdel);
        txt_tel_cld = findViewById(R.id.txt_tel_cld);
    }

}
