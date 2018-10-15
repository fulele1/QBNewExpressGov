package com.xaqb.policescan;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

@Route(path = "/qb/ComQueryActivity")
public class ComQueryActivity extends BaseActivity {

    private ComQueryActivity instance;
    private TextView tv_title_child;
    private Button btn_query;
    private EditText txt_brand_cq,txt_org_cq,et_com_cq;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_query);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
    }

    private void addEvent() {
        txt_brand_cq.setOnClickListener(instance);
        btn_query.setOnClickListener(instance);
        txt_org_cq.setOnClickListener(instance);
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("企业查询");

        txt_brand_cq = findViewById(R.id.txt_brand_cq);
        txt_org_cq = findViewById(R.id.txt_org_cq);
        et_com_cq = findViewById(R.id.et_com_cq);
        btn_query = findViewById(R.id.btn_query_cq);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_query_cq:
                Bundle bundle = new Bundle();
                bundle.putString("brand",bid);
                bundle.putString("org",oid);
                bundle.putString("comname",et_com_cq.getText().toString().trim());
                ARouterUtil.intentPar("/qb/ComListActivity",view,bundle);
                break;

                case R.id.txt_brand_cq:
                ARouterUtil.intentNoParRequest("/qb/BrandActivity",view,instance,1);
                break;
                case R.id.txt_org_cq:
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
        }
    }

    private String bid = "";
    private String oid = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode ==1){
            if (data !=null){
                String name = data.getStringExtra("bname");
                bid = data.getStringExtra("bid");
                txt_brand_cq.setText(name);
            }
        }if (resultCode == RESULT_OK&&requestCode ==2){
            if (data !=null){
                String name = data.getStringExtra("orgName");
                oid = data.getStringExtra("orgCode");
                txt_org_cq.setText(name);
            }
        }
    }
}