package com.qianbai.newexg.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qianbai.newexg.OrgActivityThree;
import com.qianbai.newexg.R;
import com.qianbai.newexg.utils.ARouterUtil;
import com.qianbai.newexg.utils.SPUtils;
import com.qianbai.newexg.utils.StatuBarUtil;

import static android.app.Activity.RESULT_OK;


/**
 * Created by fl on 2017/5/18.
 */

public class ThreeFragment extends BaseFragment implements View.OnClickListener{

    private Context instance;
    private View view;
    private Button btn_query;
    private TextView txt_org_three;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = ThreeFragment.this.getActivity();
        view = inflater.inflate(R.layout.fragment_three,null);
        StatuBarUtil.setStatuBarLightMode(this.getActivity(), getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色

        btn_query = view.findViewById(R.id.bt_query_three);
        txt_org_three = view.findViewById(R.id.txt_org_three);
        setEvent();
        return view;
    }

    private void setEvent() {
        btn_query.setOnClickListener(this);
        txt_org_three.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_query_three:

                Bundle bundle = new Bundle();
                bundle.putString("org",oid);
                ARouterUtil.intentPar("/qb/DataDelActivity",view,bundle);

                break;

                case R.id.txt_org_three:

                    Intent intent = new Intent(this.getContext(), OrgActivityThree.class);
                    startActivityForResult(intent,0);


//                    if (SPUtils.get(instance,"so_level","").equals("3")){
//                        ARouterUtil.intentNoParRequest("/qb/OrgActivityOne",view,this.getActivity(),0);
//                    }else if (SPUtils.get(instance,"so_level","").equals("2")){
//                        ARouterUtil.intentNoParRequest("/qb/OrgActivityTwo",view,this.getActivity(),0);
//                    }else if (SPUtils.get(instance,"so_level","").equals("1")){
//                        ARouterUtil.intentNoParRequest("/qb/OrgActivityThree",view,this.getActivity(),0);
//                    }else if (SPUtils.get(instance,"so_level","").equals("0")){
//                        ARouterUtil.intentNoParRequest("/qb/OrgActivityFour",view,this.getActivity(),0);
//                    }
                break;
        }
    }


    private String oid = "";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode ==0){
            if (data !=null){
                String name = data.getStringExtra("orgName");
                oid = data.getStringExtra("orgCode");
                txt_org_three.setText(name);
            }
        }
    }





}
