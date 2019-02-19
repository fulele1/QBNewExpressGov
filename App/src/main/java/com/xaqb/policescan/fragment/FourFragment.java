package com.xaqb.policescan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaqb.policescan.OrgActivityFour;
import com.xaqb.policescan.OrgActivityOne;
import com.xaqb.policescan.OrgActivityThree;
import com.xaqb.policescan.OrgActivityTwo;
import com.xaqb.policescan.R;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.DateUtil;
import com.xaqb.policescan.utils.DoubleDateUtil;
import com.xaqb.policescan.utils.EditClearUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import static android.app.Activity.RESULT_OK;


/**
 * Created by fl on 2017/5/18.
 */

public class FourFragment extends BaseFragment implements View.OnClickListener {

    private Context instance;
    private View view;
    private Button btn_query;
    private EditText txt_org_three, txt_date_three;
    private ImageView img_clear_org_four, img_clear_date_four;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = FourFragment.this.getActivity();
        view = inflater.inflate(R.layout.fragment_four, null);
        StatuBarUtil.setStatuBarLightMode(this.getActivity(), getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色

        btn_query = view.findViewById(R.id.bt_query_three);
        txt_org_three = view.findViewById(R.id.txt_org_three);
        txt_date_three = view.findViewById(R.id.txt_date_three);
        txt_date_three = view.findViewById(R.id.txt_date_three);
        img_clear_org_four = view.findViewById(R.id.img_clear_org_four);
        img_clear_date_four = view.findViewById(R.id.img_clear_date_four);
        setEvent();
        return view;
    }

    private void setEvent() {
        btn_query.setOnClickListener(this);
        txt_org_three.setOnClickListener(this);
        txt_date_three.setOnClickListener(this);
        img_clear_org_four.setOnClickListener(this);
        img_clear_date_four.setOnClickListener(this);
        EditClearUtils.clearText(txt_org_three, img_clear_org_four);
        EditClearUtils.clearText(txt_date_three, img_clear_date_four);
        txt_date_three.setText(DateUtil.get7Time()+"--->"+DateUtil.getNow7Time());
    }


    private String mOrg = "";
    private String mTime;
    private String mEnd = "";
    private String mStart = "";

    private void getIntentData() {
        mTime = NullUtil.getString(txt_date_three.getText().toString().trim());
        LogUtils.e("time----------------" + mTime);


        if (!mTime.equals("") && mTime != null) {
            mStart = NullUtil.getString(mTime.substring(0, mTime.indexOf("--->")));
            mEnd = NullUtil.getString(mTime.substring(mTime.indexOf("--->") + 4));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_query_three:
                getIntentData();
                Bundle bundle = new Bundle();
                bundle.putString("org", oid);
                bundle.putString("orgname", txt_org_three.getText().toString());
                bundle.putString("start", mStart);
                bundle.putString("end", mEnd);
                ARouterUtil.intentPar("/qb/DataDelActivity", view, bundle);
                break;

            case R.id.txt_org_three:

//                    Intent intent = new Intent(this.getContext(), OrgActivityThree.class);
//                    startActivityForResult(intent,0);

                if (SPUtils.get(instance, "solevel", "").equals("3")) {
                    Intent intent1 = new Intent(this.getContext(), OrgActivityOne.class);
                    startActivityForResult(intent1, 0);
//                        ARouterUtil.intentNoParRequest("/qb/OrgActivityOne",view,this.getActivity(),0);
                } else if (SPUtils.get(instance, "solevel", "").equals("2")) {
                    Intent intent2 = new Intent(this.getContext(), OrgActivityTwo.class);
                    startActivityForResult(intent2, 0);
//                        ARouterUtil.intentNoParRequest("/qb/OrgActivityTwo",view,this.getActivity(),0);
                } else if (SPUtils.get(instance, "solevel", "").equals("1")) {
                    Intent intent3 = new Intent(this.getContext(), OrgActivityThree.class);
                    startActivityForResult(intent3, 0);
                } else if (SPUtils.get(instance, "solevel", "").equals("0")) {
                    Intent intent4 = new Intent(this.getContext(), OrgActivityFour.class);
                    startActivityForResult(intent4, 0);
//                        ARouterUtil.intentNoParRequest("/qb/OrgActivityFour",view,this.getActivity(),0);
                }
                break;
            case R.id.txt_date_three:
                DoubleDateUtil.show(instance, txt_date_three);
                break;
            case R.id.img_clear_org_four:
                txt_org_three.setText("");
                mOrg = "";
                img_clear_org_four.setVisibility(View.GONE);
                break;
            case R.id.img_clear_date_four:
                txt_date_three.setText("");
                img_clear_date_four.setVisibility(View.GONE);
                break;
        }
    }
    private String oid = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            if (data != null) {
                String name = data.getStringExtra("orgName");
                oid = data.getStringExtra("orgCode");
                txt_org_three.setText(name);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        txt_org_three.setText("");
        mOrg = "";
        txt_date_three.setText("");
        img_clear_org_four.setVisibility(View.GONE);
        img_clear_date_four.setVisibility(View.GONE);
    }
}
