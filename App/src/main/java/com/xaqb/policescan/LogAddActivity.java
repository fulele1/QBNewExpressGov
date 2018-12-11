package com.xaqb.policescan;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;

@Route(path = "/qb/LogAddActivity")
public class LogAddActivity extends BaseActivity {

    private LogAddActivity instance;
    private Button bt_finish_addLog;
    private EditText txt_com_logadd, txt_org_logadd, txt_per_logadd, txt_head_logadd, edit_duty_logadd,
            edit_date_logadd, edit_question_logadd, edit_result_logadd, edit_remark_logadd;
    private TextView tv_title_child_tilte;
    private TimePickerView pvTime;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_add);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
    }


    private void addEvent() {
        bt_finish_addLog.setOnClickListener(instance);
        txt_com_logadd.setOnClickListener(instance);
        txt_org_logadd.setOnClickListener(instance);
        edit_date_logadd.setOnClickListener(instance);
        txt_per_logadd.setOnClickListener(instance);
        txt_head_logadd.setOnClickListener(instance);
        edit_duty_logadd.setOnClickListener(instance);
        edit_question_logadd.setOnClickListener(instance);
        edit_result_logadd.setOnClickListener(instance);
        edit_remark_logadd.setOnClickListener(instance);
        img_clear_com_logadd.setOnClickListener(instance);
        img_clear_org_logadd.setOnClickListener(instance);
        img_clear_per_logadd.setOnClickListener(instance);
        img_clear_head_logadd.setOnClickListener(instance);
        img_clear_duty_logadd.setOnClickListener(instance);
        img_clear_date_logadd.setOnClickListener(instance);
        img_clear_questtion_logadd.setOnClickListener(instance);
        img_clear_result_logadd.setOnClickListener(instance);
        img_clear_remark_logadd.setOnClickListener(instance);
        EditClearUtils.clearText(txt_com_logadd, img_clear_com_logadd);
        EditClearUtils.clearText(txt_org_logadd, img_clear_org_logadd);
        EditClearUtils.clearText(txt_per_logadd, img_clear_per_logadd);
        EditClearUtils.clearText(txt_head_logadd, img_clear_head_logadd);
        EditClearUtils.clearText(edit_duty_logadd, img_clear_duty_logadd);
        EditClearUtils.clearText(edit_date_logadd, img_clear_date_logadd);
        EditClearUtils.clearText(edit_question_logadd, img_clear_questtion_logadd);
        EditClearUtils.clearText(edit_result_logadd, img_clear_result_logadd);
        EditClearUtils.clearText(edit_remark_logadd, img_clear_remark_logadd);

        checkTime();
    }

    private void checkTime() {
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                edit_date_logadd.setText(df.format(date));
            }
        });
    }

    private String comcode = "";
    private String orgcode = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                String name = data.getStringExtra("comname");
                comcode = data.getStringExtra("comcode");
                txt_com_logadd.setText(name);
            }
        } else if (resultCode == RESULT_OK && requestCode == 0) {
            if (data != null) {
                String name = data.getStringExtra("orgName");
                orgcode = data.getStringExtra("orgCode");
                txt_org_logadd.setText(name);
            }
        }
    }

    ImageView img_clear_com_logadd, img_clear_org_logadd, img_clear_per_logadd, img_clear_head_logadd,
            img_clear_duty_logadd, img_clear_date_logadd, img_clear_questtion_logadd, img_clear_result_logadd,
            img_clear_remark_logadd;

    private void initView() {
        bt_finish_addLog = findViewById(R.id.bt_finish_addLog);
        txt_com_logadd = findViewById(R.id.txt_com_logadd);
        txt_org_logadd = findViewById(R.id.txt_org_logadd);
        txt_per_logadd = findViewById(R.id.txt_per_logadd);
        txt_head_logadd = findViewById(R.id.txt_head_logadd);
        edit_duty_logadd = findViewById(R.id.edit_duty_logadd);
        edit_date_logadd = findViewById(R.id.edit_date_logadd);
        edit_question_logadd = findViewById(R.id.edit_question_logadd);
        edit_result_logadd = findViewById(R.id.edit_result_logadd);
        edit_remark_logadd = findViewById(R.id.edit_remark_logadd);
        tv_title_child_tilte = findViewById(R.id.tv_title_child_tilte);
        img_clear_com_logadd = findViewById(R.id.img_clear_com_logadd);
        img_clear_org_logadd = findViewById(R.id.img_clear_org_logadd);
        img_clear_per_logadd = findViewById(R.id.img_clear_per_logadd);
        img_clear_head_logadd = findViewById(R.id.img_clear_head_logadd);
        img_clear_duty_logadd = findViewById(R.id.img_clear_duty_logadd);
        img_clear_date_logadd = findViewById(R.id.img_clear_date_logadd);
        img_clear_questtion_logadd = findViewById(R.id.img_clear_questtion_logadd);
        img_clear_result_logadd = findViewById(R.id.img_clear_result_logadd);
        img_clear_remark_logadd = findViewById(R.id.img_clear_remark_logadd);
        tv_title_child_tilte.setText("企业检查");
        pvTime = new TimePickerView(instance, TimePickerView.Type.YEAR_MONTH_DAY);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_finish_addLog:
                DialogLoadingUtil.getInstance(instance).show();
                bt_finish_addLog.setClickable(false);
                connecting();
                break;
            case R.id.txt_com_logadd:
                ARouterUtil.intentNoParRequest("/qb/CompanyActivity", view, instance, 1);
                break;
            case R.id.txt_org_logadd:
                if (SPUtils.get(instance, "solevel", "").equals("3")) {
                    ARouterUtil.intentNoParRequest("/qb/OrgActivityOne", view, instance, 0);
                } else if (SPUtils.get(instance, "solevel", "").equals("2")) {
                    ARouterUtil.intentNoParRequest("/qb/OrgActivityTwo", view, instance, 0);
                } else if (SPUtils.get(instance, "solevel", "").equals("1")) {
                    ARouterUtil.intentNoParRequest("/qb/OrgActivityThree", view, instance, 0);
                } else if (SPUtils.get(instance, "solevel", "").equals("0")) {
                    ARouterUtil.intentNoParRequest("/qb/OrgActivityFour", view, instance, 0);
                }
                break;
            case R.id.edit_date_logadd:
                pvTime.show();
                break;
            case R.id.img_clear_com_logadd:
                txt_com_logadd.setText("");
                img_clear_com_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_org_logadd:
                txt_org_logadd.setText("");
                img_clear_org_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_per_logadd:
                txt_per_logadd.setText("");
                img_clear_per_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_head_logadd:
                txt_head_logadd.setText("");
                img_clear_head_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_duty_logadd:
                edit_duty_logadd.setText("");
                img_clear_duty_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_date_logadd:
                edit_date_logadd.setText("");
                img_clear_date_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_questtion_logadd:
                edit_question_logadd.setText("");
                img_clear_questtion_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_result_logadd:
                edit_result_logadd.setText("");
                img_clear_result_logadd.setVisibility(View.GONE);
                break;
            case R.id.img_clear_remark_logadd:
                edit_remark_logadd.setText("");
                img_clear_remark_logadd.setVisibility(View.GONE);
                break;
        }
    }

    private void connecting() {
        String com = txt_com_logadd.getText().toString().trim();
        String org = txt_org_logadd.getText().toString().trim();
        String per = txt_per_logadd.getText().toString().trim();
        String head = txt_head_logadd.getText().toString().trim();
        String duty = edit_duty_logadd.getText().toString().trim();
        String date = edit_date_logadd.getText().toString().trim();
        if (!date.equals("")) {
            date = DateUtil.data2(date);
        }
        String question = edit_question_logadd.getText().toString().trim();
        String result = edit_result_logadd.getText().toString().trim();
        String remark = edit_remark_logadd.getText().toString().trim();

        if (com.equals("")) {
            Toast.makeText(instance, "选择企业名称", Toast.LENGTH_SHORT).show();
            bt_finish_addLog.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (org.equals("")) {
            Toast.makeText(instance, "请输入联合机构", Toast.LENGTH_SHORT).show();
            bt_finish_addLog.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (per.equals("")) {
            Toast.makeText(instance, "请输入检查人员", Toast.LENGTH_SHORT).show();
            bt_finish_addLog.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();

            return;
        } else if (head.equals("")) {
            Toast.makeText(instance, "请输入负责人", Toast.LENGTH_SHORT).show();
            bt_finish_addLog.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (date.equals("")) {
            Toast.makeText(instance, "请选择日期", Toast.LENGTH_SHORT).show();
            bt_finish_addLog.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();
            return;
        } else if (question.equals("")) {
            Toast.makeText(instance, "请选输入问题", Toast.LENGTH_SHORT).show();
            bt_finish_addLog.setClickable(true);
            DialogLoadingUtil.getInstance(instance).dismiss();

            return;
        } else if (result.equals("")) {
            Toast.makeText(instance, "请输入处理结果", Toast.LENGTH_SHORT).show();
            bt_finish_addLog.setClickable(true);

            return;
        }

        OkHttpUtils
                .post()
                .url(HttpUrlUtils.getHttpUrl().com_list() + "?access_token=" +
                        SPUtils.get(instance, "access_token", ""))
                .addParams("dccompanycode", comcode)
                .addParams("dcorgcode", orgcode)
                .addParams("dcorguser", per)
                .addParams("dcdutyuser", head)
                .addParams("dcorgjob", duty)
                .addParams("dcorgdate", date)
                .addParams("dccontent", question)
                .addParams("dcresult", result)
                .addParams("dcmark", remark)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
//                        loadingDialog.dismiss();
//                        showToast(e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        DialogLoadingUtil.getInstance(instance).dismiss();
                        Map<String, Object> map1 = JSON.parseObject(s, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            bt_finish_addLog.setText("发送成功");
                            bt_finish_addLog.setClickable(false);
                            SPUtils.put(instance, "addlogSuccess", "yes");
                            instance.finish();
                        } else {
                            bt_finish_addLog.setClickable(true);

                        }
                    }
                });
    }

}
