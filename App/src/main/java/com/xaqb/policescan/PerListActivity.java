package com.xaqb.policescan;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.xaqb.policescan.RLview.Per;
import com.xaqb.policescan.RLview.PerAdapter;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.ConditionUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/qb/PerListActivity")
public class PerListActivity extends BaseActivity {

    private PerListActivity instance;
    private TextView tv_title_child;
    private TextView txt_size;
    private LRecyclerView list_r;
    private RelativeLayout empty_view;
    /**
     * 服务器端一共多少条数据
     */
    private int TOTAL_COUNTER;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**
     * 每一页展示多少条数据
     */
    private int REQUEST_COUNT;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;
    private int mCurrentpage = 1;


    private PerAdapter mDataAdapter = null;

    private PerListActivity.PreviewHandler mHandler = new PerListActivity.PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lrecycleview_layout);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
        initRecycle();
        initList();
    }

    private void initList() {

        list_r.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mClues = new ArrayList<>();
                mDataAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                connecting(1);
            }
        });

        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        list_r.setLoadMoreEnabled(true);

        list_r.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    mCurrentpage = mCurrentpage + 1;
                    connecting(mCurrentpage);
                } else {
                    //the end
                    list_r.setNoMore(true);
                }
            }
        });
        list_r.refresh();
    }

    private void addEvent() {
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("快递员");

        list_r = findViewById(R.id.list_recycleview);

        txt_size = findViewById(R.id.txt_size);
        empty_view = findViewById(R.id.empty_view);

    }

    public String getIntentData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        String comname = bundle.getString("comname");
        String empname = bundle.getString("empname");
        String empcertcode = bundle.getString("empcertcode");
        String empphone = bundle.getString("empphone");
        String comsecurityorg = bundle.getString("comsecurityorg");

        HashMap map = new HashMap();
        map.put("\"comname\"", "\"" + comname + "\"");
        map.put("\"empname\"", "\"" + empname + "\"");
        map.put("\"empcertcode\"", "\"" + empcertcode + "\"");
        map.put("\"empphone\"", "\"" + empphone + "\"");
        map.put("\"comsecurityorg\"", "\"" + comsecurityorg + "\"");

        return "&condition=" + ConditionUtil.getConditionString(map);
    }

    private void initRecycle() {

        mDataAdapter = new PerAdapter(instance);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        list_r.setAdapter(mLRecyclerViewAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.list_line)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.wirte)
                .build();

        //mRecyclerView.setHasFixedSize(true);
        list_r.addItemDecoration(divider);

        list_r.setLayoutManager(new LinearLayoutManager(this));

        list_r.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        list_r.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        list_r.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        //add a HeaderView
        final View header = LayoutInflater.from(this).inflate(R.layout.sample_header, (ViewGroup) findViewById(android.R.id.content), false);
        mLRecyclerViewAdapter.addHeaderView(header);

        //设置头部加载颜色
        list_r.setHeaderViewColor(R.color.colorAccent, R.color.colorPrimary, android.R.color.white);
        //设置底部加载颜色
        list_r.setFooterViewColor(R.color.colorAccent, R.color.colorPrimary, android.R.color.white);
        //设置底部加载文字提示
        list_r.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");

    }


    List<Per> mClue;
    List<Per> mClues;

    private void connecting(int p) {
        LogUtils.e(HttpUrlUtils.getHttpUrl().query_per(instance) + "?access_token=" + SPUtils.get(instance, "access_token", "") + getIntentData() + "&p=" + p);

        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().query_per(instance) + "?access_token=" + SPUtils.get(instance, "access_token", "") + getIntentData() + "&p=" + p)
                .success(new ISuccess() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String response) {
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });
                        Log.e("fule", response);

                        if (NullUtil.getString(map1.get("state")).equals("0")) {
                            Map<String, Object> mess = JSON.parseObject(map1.get("mess").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            String num = mess.get("num").toString();
                            String count = mess.get("count").toString();
                            if (!NullUtil.getString(mess.get("count")).equals("0")) {
                                mHandler.sendEmptyMessage(-1);
                                list_r.setBackgroundColor(getResources().getColor(R.color.wirte));
                                String table = map1.get("table").toString();
                                List<Map> list1 = JSON.parseArray(table, Map.class);
                                mClue = new ArrayList<>();
                                for (Map<String, Object> map : list1) {
                                    Per com = new Per();
                                    com.setId(NullUtil.getString(map.get("empcode")));//ID
                                    com.setName(NullUtil.getString(map.get("empname")));//姓名
                                    com.setTel(NullUtil.getString(map.get("empphone")));//电话加星
                                    com.setSix(NullUtil.getString(map.get("sexname")));//性别
                                    com.setIde(NullUtil.getString(map.get("empcertcode")));//身份证号码
                                    com.setCom(NullUtil.getString(map.get("comname")));//公司

                                    mClue.add(com);
                                    mClues.add(com);
                                }

                                TOTAL_COUNTER = Integer.valueOf(count).intValue();
                                REQUEST_COUNT = Integer.valueOf(num).intValue();
                                txt_size.setText("共查询到" + count + "条数据");
                                //子条目的点击事件
                                mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (mDataAdapter.getDataList().size() > position) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", mClues.get(position).getId());
                                            ARouterUtil.intentPar("/qb/PerDelActivity", view, bundle);
                                        }
                                    }

                                });
                                empty_view.setVisibility(View.GONE);
                                list_r.setVisibility(View.VISIBLE);
                            } else {
                                txt_size.setVisibility(View.GONE);
                                list_r.setEmptyView(empty_view);
                                mHandler.sendEmptyMessage(-3);
                            }
                        } else if (NullUtil.getString(map1.get("state")).equals("10")) {
                            ARouterUtil.intentNoPar("/qb/loginActivity", tv_title_child);

                        } else {
                            Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();

                        }


                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
                        mHandler.sendEmptyMessage(-3);

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        mHandler.sendEmptyMessage(-3);
                    }
                })
                .build()
                .get();
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Per> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private int size;

    private class PreviewHandler extends Handler {

        private WeakReference<PerListActivity> ref;

        PreviewHandler(PerListActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final PerListActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:

                    int currentSize = activity.mDataAdapter.getItemCount();

                    //模拟组装15个数据
                    ArrayList<Per> newList = new ArrayList<>();
                    for (int i = 0; i < mClue.size(); i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        Per item = new Per();
                        item.setName(mClue.get(i).getName());
                        item.setTel(mClue.get(i).getTel());
                        item.setCom(mClue.get(i).getCom());
                        item.setIde(mClue.get(i).getIde());
                        item.setSix(mClue.get(i).getSix());
                        newList.add(item);
                    }

                    activity.addItems(newList);

                    activity.list_r.refreshComplete(REQUEST_COUNT);

                    break;
                case -3:
                    activity.list_r.refreshComplete(REQUEST_COUNT);
                    activity.notifyDataSetChanged();
                    activity.list_r.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
//                            connecting(2);
                        }
                    });

                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_refresh) {
            list_r.forceToRefresh();
        }
        return true;
    }


}
