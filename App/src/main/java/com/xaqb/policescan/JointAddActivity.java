package com.xaqb.policescan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.TimePickerView;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.DateUtil;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.EditClearUtils;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


@Route(path = "/qb/JointAddActivity")
public class JointAddActivity extends BaseActivity {

    private JointAddActivity instance;
    private Button bt_finish_add_joint;
    private TextView tv_title_child_tilte;
    private EditText txt_com_add_joint, txt_org_add_joint, txt_date_add_joint, txt_per_add_joint,
            txt_content_add_joint, txt_question_add_joint, txt_result_add_joint;
    private TimePickerView pvTime;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_add);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
    }

    private void addEvent() {
        bt_finish_add_joint.setOnClickListener(instance);
        txt_com_add_joint.setOnClickListener(instance);
        txt_org_add_joint.setOnClickListener(instance);
        txt_date_add_joint.setOnClickListener(instance);
        txt_per_add_joint.setOnClickListener(instance);
        txt_content_add_joint.setOnClickListener(instance);
        txt_question_add_joint.setOnClickListener(instance);
        txt_result_add_joint.setOnClickListener(instance);
        img_clear_com_joint.setOnClickListener(instance);
        img_clear_org_joint.setOnClickListener(instance);
        img_clear_date_joint.setOnClickListener(instance);
        img_clear_per_joint.setOnClickListener(instance);
        img_clear_content_joint.setOnClickListener(instance);
        img_clear_questtion_joint.setOnClickListener(instance);
        img_clear_result_joint.setOnClickListener(instance);
        checkTime();
    }

    private void initView() {
        bt_finish_add_joint = findViewById(R.id.bt_finish_add_joint);
        tv_title_child_tilte = findViewById(R.id.tv_title_child_tilte);
        tv_title_child_tilte.setText("添加联合检查");
        txt_com_add_joint = findViewById(R.id.txt_com_add_joint);
        txt_org_add_joint = findViewById(R.id.txt_org_add_joint);
        txt_date_add_joint = findViewById(R.id.txt_date_add_joint);
        txt_per_add_joint = findViewById(R.id.txt_per_add_joint);
        txt_content_add_joint = findViewById(R.id.txt_content_add_joint);
        txt_question_add_joint = findViewById(R.id.txt_question_add_joint);
        txt_result_add_joint = findViewById(R.id.txt_result_add_joint);
        img_clear_com_joint = findViewById(R.id.img_clear_com_joint);
        img_clear_org_joint = findViewById(R.id.img_clear_org_joint);
        img_clear_date_joint = findViewById(R.id.img_clear_date_joint);
        img_clear_per_joint = findViewById(R.id.img_clear_per_joint);
        img_clear_content_joint = findViewById(R.id.img_clear_content_joint);
        img_clear_questtion_joint = findViewById(R.id.img_clear_questtion_joint);
        img_clear_result_joint = findViewById(R.id.img_clear_result_joint);

        EditClearUtils.clearText(txt_com_add_joint, img_clear_com_joint);
        EditClearUtils.clearText(txt_org_add_joint, img_clear_org_joint);
        EditClearUtils.clearText(txt_date_add_joint, img_clear_date_joint);
        EditClearUtils.clearText(txt_per_add_joint, img_clear_per_joint);
        EditClearUtils.clearText(txt_content_add_joint, img_clear_content_joint);
        EditClearUtils.clearText(txt_question_add_joint, img_clear_questtion_joint);
        EditClearUtils.clearText(txt_result_add_joint, img_clear_result_joint);

        pvTime = new TimePickerView(instance, TimePickerView.Type.YEAR_MONTH_DAY);
    }


    ImageView img_clear_com_joint, img_clear_org_joint, img_clear_date_joint, img_clear_per_joint,
            img_clear_content_joint, img_clear_questtion_joint, img_clear_result_joint;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_finish_add_joint:
                DialogLoadingUtil.getInstance(instance).show();
                bt_finish_add_joint.setClickable(false);
                connecting();
                break;
            case R.id.txt_com_add_joint:
                ARouterUtil.intentNoParRequest("/qb/CompanyActivity", view, instance, 1);
                break;
            case R.id.txt_date_add_joint:
                pvTime.show();
                break;
            case R.id.img_clear_com_joint:
                txt_com_add_joint.setText("");
                img_clear_com_joint.setVisibility(View.GONE);
                break;
            case R.id.img_clear_org_joint:
                txt_org_add_joint.setText("");
                img_clear_org_joint.setVisibility(View.GONE);
                break;
            case R.id.img_clear_date_joint:
                txt_date_add_joint.setText("");
                img_clear_date_joint.setVisibility(View.GONE);
                break;
            case R.id.img_clear_per_joint:
                txt_per_add_joint.setText("");
                img_clear_per_joint.setVisibility(View.GONE);
                break;
            case R.id.img_clear_content_joint:
                txt_content_add_joint.setText("");
                img_clear_content_joint.setVisibility(View.GONE);
                break;
            case R.id.img_clear_questtion_joint:
                txt_question_add_joint.setText("");
                img_clear_questtion_joint.setVisibility(View.GONE);
                break;
            case R.id.img_clear_result_joint:
                txt_result_add_joint.setText("");
                img_clear_result_joint.setVisibility(View.GONE);
                break;
        }
    }

    private void checkTime() {
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                txt_date_add_joint.setText(df.format(date));
            }
        });
    }

    private String comcode = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                String name = data.getStringExtra("comname");
                comcode = data.getStringExtra("comcode");
                txt_com_add_joint.setText(name);
            }
        }
    }

    private void connecting() {
        String com = txt_com_add_joint.getText().toString().trim();
        String org = txt_org_add_joint.getText().toString().trim();
        String date = txt_date_add_joint.getText().toString().trim();
        String per = txt_per_add_joint.getText().toString().trim();
        String content = txt_content_add_joint.getText().toString().trim();
        String question = txt_question_add_joint.getText().toString().trim();
        String result = txt_result_add_joint.getText().toString().trim();

        if (com.equals("")) {
            Toast.makeText(instance, "选择企业名称", Toast.LENGTH_SHORT).show();
            bt_finish_add_joint.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (org.equals("")) {
            Toast.makeText(instance, "请输入联合机构", Toast.LENGTH_SHORT).show();
            bt_finish_add_joint.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (date.equals("")) {
            Toast.makeText(instance, "请选择日期", Toast.LENGTH_SHORT).show();
            bt_finish_add_joint.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (per.equals("")) {
            Toast.makeText(instance, "请输入检查人员", Toast.LENGTH_SHORT).show();
            bt_finish_add_joint.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (content.equals("")) {
            Toast.makeText(instance, "请输入联查事项", Toast.LENGTH_SHORT).show();
            bt_finish_add_joint.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (question.equals("")) {
            Toast.makeText(instance, "请输入发现的问题", Toast.LENGTH_SHORT).show();
            bt_finish_add_joint.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (result.equals("")) {
            Toast.makeText(instance, "请输入处理结果", Toast.LENGTH_SHORT).show();
            bt_finish_add_joint.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        }


        LogUtils.e(HttpUrlUtils.getHttpUrl().joint_list(instance) + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        LogUtils.e("uccompanycode= " + comcode);
        LogUtils.e("ucorgname= " + org);
        LogUtils.e("ucorgdate= " + DateUtil.data2(date));
        LogUtils.e("ucorguser= " + per);
        LogUtils.e("ucorgthing= " + content);
        LogUtils.e("uccontent= " + question);
        LogUtils.e("ucresult= " + result);
//        RestClient.builder()
//
//
//                .url(HttpUrlUtils.getHttpUrl().joint_list()+"?access_token="+
//                        SPUtils.get(instance,"access_token",""))
//                .params("uccompanycode",comcode)//企业名称编码
//                .params("ucorgname",org)//联查机构
//                .params("ucorgdate", DateUtil.data2(date))//联查日期
//                .params("ucorguser",per)//联查人员
//                .params("ucorgthing",content)//联查事项
//                .params("uccontent",question)//发现问题
//                .params("ucresult",result)//处理结果
//                .build()
//                .post();

        OkHttpUtils
                .post()
                .url(HttpUrlUtils.getHttpUrl().joint_list(instance) + "?access_token=" +
                        SPUtils.get(instance, "access_token", ""))
                .addParams("uccompanycode", comcode)//企业名称编码
                .addParams("ucorgname", org)//联查机构
                .addParams("ucorgdate", DateUtil.data2(date))//联查日期
                .addParams("ucorguser", per)//联查人员
                .addParams("ucorgthing", content)//联查事项
                .addParams("uccontent", question)//发现问题
                .addParams("ucresult", result)//处理结果
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
//                        loadingDialog.dismiss();
//                        showToast(e.toString());
                        DialogLoadingUtil.getInstance(instance).dismiss();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        DialogLoadingUtil.getInstance(instance).dismiss();
                        LogUtils.e(s);
                        Map<String, Object> map1 = JSON.parseObject(s, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            bt_finish_add_joint.setText("发送成功");
                            bt_finish_add_joint.setClickable(false);
                            SPUtils.put(instance, "addSuccess", "yes");
                            instance.finish();
                        } else {
                            bt_finish_add_joint.setClickable(true);

                        }
                    }
                });
    }


}