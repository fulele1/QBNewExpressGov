package com.qianbai.newexg;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jaeger.library.StatusBarUtil;
import com.qianbai.newexg.Listview.BrandBean;
import com.qianbai.newexg.net.RestClient;
import com.qianbai.newexg.net.callback.IError;
import com.qianbai.newexg.net.callback.IFailure;
import com.qianbai.newexg.net.callback.ISuccess;
import com.qianbai.newexg.threeLevel.MenuData;
import com.qianbai.newexg.threeLevel.MenuDialogAdapter;
import com.qianbai.newexg.threeLevel.MyPagerAdapter;
import com.qianbai.newexg.threeLevel.MyViewPager;
import com.qianbai.newexg.utils.ConditionUtil;
import com.qianbai.newexg.utils.HttpUrlUtils;
import com.qianbai.newexg.utils.LogUtils;
import com.qianbai.newexg.utils.NullUtil;
import com.qianbai.newexg.utils.SPUtils;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@Route(path = "/qb/OrgActivityTwo")
public class OrgActivityTwo extends BaseActivity {

    List<MenuData> list1, list2;
    private Context mContext;
    private MyViewPager mViewPager;
    private View view1, view2;
    private ListView mListView1, mListView2;
    private MenuDialogAdapter mListView1Adapter, mListView2Adapter;
    private List<View> views = new ArrayList<View>();
    private MenuData resultDate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatuBarUtil.setStatuBarLightModeClild(this, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        setContentView(R.layout.activity_org_two);
        mContext = this;
        initViews();
        TextView mTxtTitle = (TextView) findViewById(R.id.tv_title_child_tilte);
        mTxtTitle.setText("管辖机构");
    }


    public String getIntentData(String socode) {
        HashMap map = new HashMap();
        map.put("\"soparent\"", "\"" + socode + "\"");//管辖机构

        return "&condition=" + ConditionUtil.getConditionString(map);
    }




    //操作控件
    public void initViews() {
        //一级
        mViewPager = (MyViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = LayoutInflater.from(this);
        view1 = inflater.inflate(R.layout.pager_number, null);
        view2 = inflater.inflate(R.layout.pager_number, null);
        mListView1 = (ListView) view1.findViewById(R.id.listview);
        mListView2 = (ListView) view2.findViewById(R.id.listview);


        //一级
        list1 = new ArrayList<>();
        MenuData menuData = new MenuData(SPUtils.get(mContext, "ou_securityorg", "").toString(),
                SPUtils.get(mContext, "org", "").toString(),
                "");
        list1.add(menuData);
        mListView1Adapter = new MenuDialogAdapter(this, list1);
        mListView1Adapter.setSelectedBackgroundResource(R.drawable.select_white);//选中时
        mListView1Adapter.setHasDivider(false);
        mListView1Adapter.setNormalBackgroundResource(R.color.menudialog_bg_gray);//未选中
        mListView1.setAdapter(mListView1Adapter);

        views.add(view1);
        views.add(view2);//加载了一二级菜单
        mViewPager.setAdapter(new MyPagerAdapter(views,0.5f));


        //二级
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView1Adapter != null)
                    mListView1Adapter.setSelectedPos(position);
                if (mListView2Adapter != null)
                    mListView2Adapter.setSelectedPos(-1);

                LogUtils.e("二级" + HttpUrlUtils.getHttpUrl().getOrg() +
                        "?access_token=" + SPUtils.get(mContext, "access_token", "").toString()
                        + getIntentData(SPUtils.get(mContext, "ou_securityorg", "").toString()));
                LogUtils.e("二级" + SPUtils.get(mContext, "ou_securityorg", "").toString());
                LogUtils.e("二级" + list1.get(position).name + "-------------" + list1.get(position).id);

                RestClient.builder()
                        .url(HttpUrlUtils.getHttpUrl().getOrg() +
                                "?access_token=" + SPUtils.get(mContext, "access_token", "").toString()
                                + getIntentData(SPUtils.get(mContext, "ou_securityorg", "").toString()))
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                LogUtils.e(response);
                                Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                                });
                                if (NullUtil.getString(map1.get("state")).equals("0")) {
                                    if (!NullUtil.getString(map1.get("table")).equals("")){
                                        String table = map1.get("table").toString();
                                        LogUtils.e(table);
                                        list2 = new ArrayList<>();
                                        list2.add(new MenuData("0", "不限",""));
                                        List<Map> list1 = JSON.parseArray(table, Map.class);
                                        for (Map<String, Object> map : list1) {
                                            MenuData menuData = new MenuData(NullUtil.getString(map.get("socode")),
                                                    NullUtil.getString(map.get("soname")),"");
                                            list2.add(menuData);
                                        }
                                    }


                                        if (mListView2Adapter == null) {
                                            mListView2Adapter = new MenuDialogAdapter(mContext, list2);
                                            mListView2Adapter.setNormalBackgroundResource(R.color.wirte);
                                            mListView2.setAdapter(mListView2Adapter);
                                        } else {
                                            mListView2Adapter.setData(list2);
                                            mListView2Adapter.notifyDataSetChanged();
                                        }
                                    }
                            }
                        })
                        .failure(new IFailure() {
                            @Override
                            public void onFailure(String s) {

                            }
                        })
                        .error(new IError() {
                            @Override
                            public void onError(int code, String msg) {
                            }
                        })
                        .build()
                        .get();
                }
        });

                //四级的自条目点击事件
                mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        if (position == 0) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("orgName", SPUtils.get(mContext, "org", "").toString());
                            bundle.putString("orgCode", SPUtils.get(mContext, "ou_securityorg", "").toString());
                            intent.putExtras(bundle);
                            OrgActivityTwo.this.setResult(RESULT_OK, intent);
                            OrgActivityTwo.this.finish();
                        } else {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("orgName", list2.get(position).name);
                            bundle.putString("orgCode", list2.get(position).id);
                            intent.putExtras(bundle);
                            OrgActivityTwo.this.setResult(RESULT_OK, intent);
                            OrgActivityTwo.this.finish();
                        }


                    }
                });}}


