package com.qianbai.newexg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jaeger.library.StatusBarUtil;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

public class OrgActivityFour extends BaseActivity {

    List<MenuData> list1, list2, list3, list4;
    private Context mContext;
    private MyViewPager mViewPager;
    private View view1, view2, view3, view4;
    private ListView mListView1, mListView2, mListView3, mListView4;
    private MenuDialogAdapter mListView1Adapter, mListView2Adapter, mListView3Adapter, mListView4Adapter;
    private List<View> views = new ArrayList<View>();
    private MenuData resultDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this, 0);
        setContentView(R.layout.activity_org_four);
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
        view3 = inflater.inflate(R.layout.pager_number, null);
        view4 = inflater.inflate(R.layout.pager_number, null);
        mListView1 = (ListView) view1.findViewById(R.id.listview);
        mListView2 = (ListView) view2.findViewById(R.id.listview);
        mListView3 = (ListView) view3.findViewById(R.id.listview);
        mListView4 = (ListView) view4.findViewById(R.id.listview);


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
        views.add(view3);
        mViewPager.setAdapter(new MyPagerAdapter(views,0.25f));


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
                                    if (!NullUtil.getString(map1.get("table")).equals("")){
                                        list2 = new ArrayList<>();
                                        list2.add(new MenuData("0", "不限",""));
                                        String table = map1.get("table").toString();
                                        LogUtils.e(table);

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
                    OrgActivityFour.this.setResult(RESULT_OK, intent);
                    OrgActivityFour.this.finish();
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


        //四级
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (mListView3Adapter != null) {
                    mListView3Adapter.setSelectedPos(position);
                    mListView3Adapter.setSelectedBackgroundResource(R.drawable.select_gray);//选中时
                }

                if (views.contains(view4)) {
                    views.remove(view4);
                }

                LogUtils.e("四级"+SPUtils.get(mContext, "url", "").toString() + HttpUrlUtils.getHttpUrl().getOrg() +
                        "?access_token=" + SPUtils.get(mContext, "access_token", "").toString() + "&code=" +
                        list3.get(position).id);

                LogUtils.e("四级"+list3.get(position).name+"------"+list3.get(position).id);

                SPUtils.put(mContext,"namelist3",list3.get(position).name);
                SPUtils.put(mContext,"codelist3",list3.get(position).id);

                if (list3.get(position).id.equals("0")){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgName", SPUtils.get(mContext,"namelist2","").toString());
                    bundle.putString("orgCode", SPUtils.get(mContext,"codelist2","").toString());
                    intent.putExtras(bundle);
                    OrgActivityFour.this.setResult(RESULT_OK, intent);
                    OrgActivityFour.this.finish();
                }else{
                    RestClient.builder()
                            .url(HttpUrlUtils.getHttpUrl().getOrg() +
                                    "?access_token=" + SPUtils.get(mContext, "access_token", "").toString()
                                    + getIntentData(list3.get(position).id))
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    LogUtils.e(response);
                                    Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                                    });
                                    if (NullUtil.getString(map1.get("state")).equals("0")) {
                                        list4 = new ArrayList<>();
                                        list4.add(new MenuData("0", "不限",""));
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


                                        if (mListView4Adapter == null) {
                                            mListView4Adapter = new MenuDialogAdapter(mContext, list4);
                                            mListView4Adapter.setNormalBackgroundResource(R.color.wirte);
                                            mListView4.setAdapter(mListView4Adapter);
                                        } else {
                                            mListView4Adapter.setData(list4);
                                            mListView4Adapter.notifyDataSetChanged();
                                        }

                                        views.add(view4);
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

                }}
        });


        //四级的自条目点击事件
        mListView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgName", SPUtils.get(mContext,"namelist3","").toString());
                    bundle.putString("orgCode", SPUtils.get(mContext,"codelist3","").toString());
                    intent.putExtras(bundle);
                    OrgActivityFour.this.setResult(RESULT_OK, intent);
                    OrgActivityFour.this.finish();
                }else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgName", list4.get(position).name);
                    bundle.putString("orgCode", list4.get(position).id);
                    intent.putExtras(bundle);
                    OrgActivityFour.this.setResult(RESULT_OK, intent);
                    OrgActivityFour.this.finish();
                }


            }
        });
    }

}
