package com.xaqb.policescan;

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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.threeLevel.MenuData;
import com.xaqb.policescan.threeLevel.MenuDialogAdapter;
import com.xaqb.policescan.threeLevel.MyPagerAdapter;
import com.xaqb.policescan.threeLevel.MyViewPager;
import com.xaqb.policescan.utils.ConditionUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Route(path = "/qb/OrgActivityThree")
public class OrgActivityThree extends BaseActivity {

    List<MenuData> list1, list2, list3;
    private Context mContext;
    private MyViewPager mViewPager;
    private View view1, view2, view3;
    private ListView mListView1, mListView2, mListView3;
    private MenuDialogAdapter mListView1Adapter, mListView2Adapter, mListView3Adapter;
    private List<View> views = new ArrayList<View>();
    private MenuData resultDate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_org_three);
        mContext = this;
        StatuBarUtil.setStatuBarLightModeClild(this, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
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
        view3 = inflater.inflate(R.layout.pager_number, null);
        mListView1 = (ListView) view1.findViewById(R.id.listview);
        mListView2 = (ListView) view2.findViewById(R.id.listview);
        mListView3 = (ListView) view3.findViewById(R.id.listview);


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
        mViewPager.setAdapter(new MyPagerAdapter(views,0.33f));


        //二级
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView1Adapter != null)
                    mListView1Adapter.setSelectedPos(position);
                if (mListView2Adapter != null)
                    mListView2Adapter.setSelectedPos(-1);

                if (views.contains(view3)) {
                    views.remove(view3);
                    mViewPager.getAdapter().notifyDataSetChanged();
                }
                LogUtils.e("二级"+SPUtils.get(mContext, "url", "").toString() + HttpUrlUtils.getHttpUrl().getOrg() +
                        "?access_token=" + SPUtils.get(mContext, "access_token", "").toString() + "&code=" +
                        SPUtils.get(mContext, "ou_securityorg", "").toString());
                LogUtils.e("二级"+ SPUtils.get(mContext, "ou_securityorg", "").toString());
                LogUtils.e("二级"+ list1.get(position).name+"-------------"+list1.get(position).id);

                RestClient.builder()
                        .url(HttpUrlUtils.getHttpUrl().getOrg() +
                                "?access_token=" + SPUtils.get(mContext, "access_token", "").toString()
                                + getIntentData(SPUtils.get(mContext, "ou_securityorg", "").toString())+"&nopage")
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                LogUtils.e(response);
                                Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                                });
                                if (NullUtil.getString(map1.get("state")).equals("0")) {
                                    list2 = new ArrayList<>();
                                    list2.add(new MenuData("0", "不限",""));
                                        if (!NullUtil.getString(map1.get("table")).equals("")){
                                        String table = map1.get("table").toString();
                                            LogUtils.e(table);

                                            List<Map> list1 = JSON.parseArray(table, Map.class);
                                            for (Map<String, Object> map : list1) {
                                                MenuData menuData = new MenuData(NullUtil.getString(map.get("socode")),
                                                        NullUtil.getString(map.get("soname")),"");
                                                list2.add(menuData);
                                            }
                                        }
//                                        LogUtils.e(table);
//
//                                        List<Map> list1 = JSON.parseArray(table, Map.class);
//                                        for (Map<String, Object> map : list1) {
//                                            MenuData menuData = new MenuData(NullUtil.getString(map.get("socode")),
//                                                    NullUtil.getString(map.get("soname")),"");
//                                            list2.add(menuData);
//                                        }

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
//                            }
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


        //三级
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (mListView2Adapter != null) {
                    mListView2Adapter.setSelectedPos(position);
                    mListView2Adapter.setSelectedBackgroundResource(R.drawable.select_gray);//选中时
                }

                if (views.contains(view3)) {
                    views.remove(view3);
                }

                LogUtils.e("三级"+SPUtils.get(mContext, "url", "").toString() + HttpUrlUtils.getHttpUrl().getOrg() +
                        "?access_token=" + SPUtils.get(mContext, "access_token", "").toString() + "&code=" +
                        list2.get(position).id);
                LogUtils.e("三级"+ list2.get(position).name+"------"+list2.get(position).id);
                SPUtils.put(mContext,"codelist2",list2.get(position).id);
                SPUtils.put(mContext,"namelist2",list2.get(position).name);

                if (list2.get(position).id.equals("0")){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgName", list1.get(position).name);
                    bundle.putString("orgCode", list1.get(position).id);
                    intent.putExtras(bundle);
                    OrgActivityThree.this.setResult(RESULT_OK, intent);
                    OrgActivityThree.this.finish();
                }else {


                    RestClient.builder()
                            .url(HttpUrlUtils.getHttpUrl().getOrg() +
                                    "?access_token=" + SPUtils.get(mContext, "access_token", "").toString()
                                    + getIntentData(list2.get(position).id))
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    LogUtils.e(response);
                                    Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                                    });
                                    if (NullUtil.getString(map1.get("state")).equals("0")) {
                                        list3 = new ArrayList<>();
                                        list3.add(new MenuData("0", "不限",""));
                                        if (!NullUtil.getString(map1.get("table")).equals("")){
                                            String table = map1.get("table").toString();
                                            LogUtils.e(table);

                                            List<Map> list1 = JSON.parseArray(table, Map.class);
                                            for (Map<String, Object> map : list1) {
                                                MenuData menuData = new MenuData(NullUtil.getString(map.get("socode")),
                                                        NullUtil.getString(map.get("soname")),"");
                                                list3.add(menuData);
                                            }
                                        }

                                            if (mListView3Adapter == null) {
                                                mListView3Adapter = new MenuDialogAdapter(mContext, list3);
                                                mListView3Adapter.setNormalBackgroundResource(R.color.wirte);
                                                mListView3.setAdapter(mListView3Adapter);
                                            } else {
                                                mListView3Adapter.setData(list3);
                                                mListView3Adapter.notifyDataSetChanged();
                                            }

                                            views.add(view3);
                                            mViewPager.getAdapter().notifyDataSetChanged();
                                            mViewPager.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mViewPager.setCurrentItem(views.size() - 1);
                                                }
                                            }, 300);
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
            }
        });

        //四级的自条目点击事件
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgName", SPUtils.get(mContext,"namelist2","").toString());
                    bundle.putString("orgCode", SPUtils.get(mContext,"codelist2","").toString());
                    intent.putExtras(bundle);
                    OrgActivityThree.this.setResult(RESULT_OK, intent);
                    OrgActivityThree.this.finish();
                }else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgName", list3.get(position).name);
                    bundle.putString("orgCode", list3.get(position).id);
                    intent.putExtras(bundle);
                    OrgActivityThree.this.setResult(RESULT_OK, intent);
                    OrgActivityThree.this.finish();
                }
            }
        });
    }

}
