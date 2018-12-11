package com.xaqb.policescan;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xaqb.policescan.Listview.BrandAdapter;
import com.xaqb.policescan.Listview.BrandBean;
import com.xaqb.policescan.Listview.LetterIndexView;
import com.xaqb.policescan.Listview.PinnedSectionListView;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/qb/BrandActivity")
public class BrandActivity extends AppCompatActivity {

    /**
     * item标识为0
     */
    public static final int ITEM = 0;
    /**
     * item标题标识为1
     */
    public static final int TITLE = 1;
    public HashMap<String, Integer> map_IsHead;
    private BrandActivity instance;
    private EditText edit_search;
    private PinnedSectionListView listView;
    private LetterIndexView letterIndexView;
    private TextView txt_center,mTxtTitle;
    private ArrayList<BrandBean> list_all;
    private ArrayList<BrandBean> list_show;
    private BrandAdapter adapter;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        DialogLoadingUtil.getInstance(instance).show();
        initData();

    }

    public void onBack(View view){
        finish();
    }

    private void initView() {

        edit_search = findViewById(R.id.edit_search_org);
        listView = findViewById(R.id.phone_listview_org);
        letterIndexView = findViewById(R.id.phone_LetterIndexView_org);
        txt_center = findViewById(R.id.phone_txt_center_org);
        mTxtTitle = findViewById(R.id.tv_title_child_tilte);
        mTxtTitle.setText("品牌选择");
    }


    public void initData() {
        list_all = new ArrayList<>();
        list_show = new ArrayList<>();
        map_IsHead = new HashMap<>();
        adapter = new BrandAdapter(this, list_show, map_IsHead);
        listView.setAdapter(adapter);
        okConnection();
        addListener();
    }

    public void okConnection() {
        LogUtils.e(HttpUrlUtils.getHttpUrl().brandcode()+"?access_token="+ SPUtils.get(instance,"access_token","")+"&nopage");
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().brandcode()+"?access_token="+ SPUtils.get(instance,"access_token","")+"&nopage")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        DialogLoadingUtil.getInstance(instance).dismiss();
                        LogUtils.e(response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });
                        Map<String, Object> mess = JSON.parseObject(map1.get("mess").toString(), new TypeReference<Map<String, Object>>() {
                        });
                        String num = mess.get("num").toString();
                        String count = mess.get("count").toString();
                        if (NullUtil.getString(map1.get("state")).equals("0")) {

                            if (!count.equals("0")){
                                String table = map1.get("table").toString();
                                LogUtils.e(table);

                                List<Map> list1 = JSON.parseArray(table, Map.class);
                                for (Map<String, Object> map : list1) {

                                    BrandBean cityBean = new BrandBean();
                                    cityBean.setName(NullUtil.getString(map.get("bcname")));
                                    cityBean.setCode(NullUtil.getString(map.get("bccode")));
                                    list_all.add(cityBean);

                                }

                                getData();

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

    /**
     * 获取数据并进行排序
     */
    public void getData() {

        //按拼音排序
        MemberSortUtil sortUtil = new MemberSortUtil();
        Collections.sort(list_all, sortUtil);

        // 初始化数据，顺便放入把标题放入map集合
        for (int i = 0; i < list_all.size(); i++) {
            BrandBean cityBean = list_all.get(i);
            if (!map_IsHead.containsKey(cityBean.getHeadChar())) {// 如果不包含就添加一个标题
                BrandBean cityBean1 = new BrandBean();
                // 设置名字
                cityBean1.setName(cityBean.getName());
                // 设置标题type
                cityBean1.setType(BrandActivity.TITLE);
                list_show.add(cityBean1);

                // map的值为标题的下标
                map_IsHead.put(cityBean1.getHeadChar(), list_show.size() - 1);
            }
            list_show.add(cityBean);
        }

        adapter.notifyDataSetChanged();
    }

    public void addListener() {

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //显示和隐藏字母条
                if (!editable.toString().equals("")) {
                    letterIndexView.setVisibility(View.GONE);
                } else if (editable.toString().equals("")) {
                    letterIndexView.setVisibility(View.VISIBLE);
                }

                //重新获取需要现实的数据
                list_show.clear();
                map_IsHead.clear();
                //把输入的字符改成大写
                String search = editable.toString().trim().toUpperCase();

                if (TextUtils.isEmpty(search)) {
                    for (int i = 0; i < list_all.size(); i++) {
                        BrandBean bean = list_all.get(i);
                        //中文字符匹配首字母和英文字符匹配首字母
                        if (!map_IsHead.containsKey(bean.getHeadChar())) {// 如果不包含就添加一个标题
                            BrandBean bean1 = new BrandBean();
                            // 设置名字
                            bean1.setName(bean.getName());
                            // 设置标题type
                            bean1.setType(BrandActivity.TITLE);
                            list_show.add(bean1);
                            // map的值为标题的下标
                            map_IsHead.put(bean1.getHeadChar(),
                                    list_show.size() - 1);
                        }
                        // 设置Item type
                        bean.setType(BrandActivity.ITEM);
                        list_show.add(bean);
                    }
                } else {
                    for (int i = 0; i < list_all.size(); i++) {
                        BrandBean bean = list_all.get(i);
                        //中文字符匹配首字母和英文字符匹配首字母
                        if (bean.getName().indexOf(search) != -1 || bean.getName_en().indexOf(search) != -1) {
                            if (!map_IsHead.containsKey(bean.getHeadChar())) {// 如果不包含就添加一个标题
                                BrandBean bean1 = new BrandBean();
                                // 设置名字
                                bean1.setName(bean.getName());
                                // 设置标题type
                                bean1.setType(BrandActivity.TITLE);
                                list_show.add(bean1);
                                // map的值为标题的下标
                                map_IsHead.put(bean1.getHeadChar(),
                                        list_show.size() - 1);
                            }
                            // 设置Item type
                            bean.setType(BrandActivity.ITEM);
                            list_show.add(bean);
                        }
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });


        // 右边字母竖排的初始化以及监听
        letterIndexView.init(new LetterIndexView.OnTouchLetterIndex() {
            //实现移动接口
            @Override
            public void touchLetterWitch(String letter) {
                // 中间显示的首字母
                txt_center.setVisibility(View.VISIBLE);
                txt_center.setText(letter);
                // 首字母是否被包含
                if (adapter.map_IsHead.containsKey(letter)) {
                    // 设置首字母的位置
                    listView.setSelection(adapter.map_IsHead.get(letter));
                }
            }

            //实现抬起接口 隐藏字母
            @Override
            public void touchFinish() {
                txt_center.setVisibility(View.GONE);
            }
        });


        /**子条目的点击事件 */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list_show.get(i).getType() == BrandActivity.ITEM) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("bname", list_show.get(i).getName());
                    bundle.putString("bid", list_show.get(i).getCode());
                    intent.putExtras(bundle);
                    instance.setResult(RESULT_OK, intent);
                    instance.finish();
                }
            }
        });
    }



    public class MemberSortUtil implements Comparator<BrandBean> {
        /**
         * 按拼音排序
         */
        @Override
        public int compare(BrandBean lhs, BrandBean rhs) {
            Comparator<Object> cmp = Collator
                    .getInstance(java.util.Locale.CHINA);
            return cmp.compare(lhs.getName_en(), rhs.getName_en());
        }
    }






}




