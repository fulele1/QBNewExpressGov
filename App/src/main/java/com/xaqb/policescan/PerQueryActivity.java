package com.xaqb.policescan;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.EditClearUtils;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

@Route(path = "/qb/PerQueryActivity")
public class PerQueryActivity extends BaseActivity {

    private PerQueryActivity instance;
    private TextView tv_title_child;
    private Button btn_query_pq;
    private ImageView img_clear_com_pq,img_clear_name_pq,img_clear_ide_pq,img_clear_tel_pq,img_clear_org_pq;
    private EditText et_org_pq,et_com_pq,et_name_pq,et_ide_pq,et_tel_pq;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_query);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
    }

    private void addEvent() {
        btn_query_pq.setOnClickListener(instance);
        et_org_pq.setOnClickListener(instance);
        et_com_pq.setOnClickListener(instance);
        et_name_pq.setOnClickListener(instance);
        et_ide_pq.setOnClickListener(instance);
        et_tel_pq.setOnClickListener(instance);
        img_clear_com_pq.setOnClickListener(instance);
        img_clear_name_pq.setOnClickListener(instance);
        img_clear_ide_pq.setOnClickListener(instance);
        img_clear_tel_pq.setOnClickListener(instance);
        img_clear_org_pq.setOnClickListener(instance);
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("人员查询");
        btn_query_pq = findViewById(R.id.btn_query_pq);
        et_org_pq = findViewById(R.id.et_org_pq);
        et_com_pq = findViewById(R.id.et_com_pq);
        et_name_pq = findViewById(R.id.et_name_pq);
        et_ide_pq = findViewById(R.id.et_ide_pq);
        et_tel_pq = findViewById(R.id.et_tel_pq);
        img_clear_com_pq = findViewById(R.id.img_clear_com_pq);
        img_clear_name_pq = findViewById(R.id.img_clear_name_pq);
        img_clear_ide_pq = findViewById(R.id.img_clear_ide_pq);
        img_clear_tel_pq = findViewById(R.id.img_clear_tel_pq);
        img_clear_org_pq = findViewById(R.id.img_clear_org_pq);
        EditClearUtils.clearText(et_com_pq,img_clear_com_pq);
        EditClearUtils.clearText(et_name_pq,img_clear_name_pq);
        EditClearUtils.clearText(et_ide_pq,img_clear_ide_pq);
        EditClearUtils.clearText(et_tel_pq,img_clear_tel_pq);
        EditClearUtils.clearText(et_org_pq,img_clear_org_pq);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_query_pq:
                Bundle bundle = new Bundle();
                bundle.putString("comname",et_com_pq.getText().toString().trim());
                bundle.putString("empname",et_name_pq.getText().toString().trim());
                bundle.putString("empcertcode",et_ide_pq.getText().toString().trim());
                bundle.putString("empphone",et_tel_pq.getText().toString().trim());
                bundle.putString("comsecurityorg",oid);
                ARouterUtil.intentPar("/qb/PerListActivity",view,bundle);
                break;

            case R.id.et_org_pq:
                    if (SPUtils.get(instance,"so_level","").equals("3")){
                        ARouterUtil.intentNoParRequest("/qb/OrgActivityOne",view,instance,2);
                    }else if (SPUtils.get(instance,"so_level","").equals("2")){
                        ARouterUtil.intentNoParRequest("/qb/OrgActivityTwo",view,instance,2);
                    }else if (SPUtils.get(instance,"so_level","").equals("1")){
                        ARouterUtil.intentNoParRequest("/qb/OrgActivityThree",view,instance,2);
                    }else if (SPUtils.get(instance,"so_level","").equals("0")){
                        ARouterUtil.intentNoParRequest("/qb/OrgActivityFour",view,instance,2);
                    }
                break;
            case R.id.img_clear_com_pq:
                et_com_pq.setText("");
                img_clear_com_pq.setVisibility(View.GONE);
                break;
                case R.id.img_clear_name_pq:
                    et_name_pq.setText("");
                    img_clear_name_pq.setVisibility(View.GONE);
                break;
                case R.id.img_clear_ide_pq:
                    et_ide_pq.setText("");
                    img_clear_ide_pq.setVisibility(View.GONE);
                break;
                case R.id.img_clear_tel_pq:
                    et_tel_pq.setText("");
                    img_clear_tel_pq.setVisibility(View.GONE);
                break;
                case R.id.img_clear_org_pq:
                    et_org_pq.setText("");
                    oid = "";
                    img_clear_org_pq.setVisibility(View.GONE);
                break;
        }
    }

private String oid = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (resultCode == RESULT_OK&&requestCode ==2){
            if (data !=null){
                String name = data.getStringExtra("orgName");
                oid = data.getStringExtra("orgCode");
                et_org_pq.setText(name);
            }
        }
    }
}
