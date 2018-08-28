package com.qianbai.newexg;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.qianbai.newexg.RLview.Com;
import com.qianbai.newexg.RLview.ComAdapter;
import com.qianbai.newexg.net.RestClient;
import com.qianbai.newexg.net.callback.IError;
import com.qianbai.newexg.net.callback.IFailure;
import com.qianbai.newexg.net.callback.ISuccess;
import com.qianbai.newexg.utils.ARouterUtil;
import com.qianbai.newexg.utils.HttpUrlUtils;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route(path = "/qb/ComListActivity")
public class ComListActivity extends BaseActivity {

   private ComListActivity instance;
    private TextView tv_title_child;
    private TextView txt_size;
    private LRecyclerView list_r;
    /**服务器端一共多少条数据*/
    private int TOTAL_COUNTER;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private int REQUEST_COUNT;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;
    private  int mCurrentpage = 1;


    private ComAdapter mDataAdapter = null;

    private ComListActivity.PreviewHandler mHandler = new ComListActivity.PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lrecycleview_layout);
        instance = this;
        StatuBarUtil.setStatuBarLightMode(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
        setRecycleView();
    }

    private void addEvent() {
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("企业");

        list_r = findViewById(R.id.list_recycleview);

        txt_size = findViewById(R.id.txt_size);
    }

    private void setRecycleView() {

        mDataAdapter = new ComAdapter(instance);
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
        final View header = LayoutInflater.from(this).inflate(R.layout.sample_header,(ViewGroup)findViewById(android.R.id.content), false);
        mLRecyclerViewAdapter.addHeaderView(header);

        list_r.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

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
                    mCurrentpage =mCurrentpage+1;
                    connecting(mCurrentpage);
                } else {
                    //the end
                    list_r.setNoMore(true);
                }
            }
        });

        list_r.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

            @Override
            public void onScrollStateChanged(int state) {

            }

        });

        //设置头部加载颜色
        list_r.setHeaderViewColor(R.color.colorAccent, R.color.colorPrimary ,android.R.color.white);
        //设置底部加载颜色
        list_r.setFooterViewColor(R.color.colorAccent, R.color.colorPrimary ,android.R.color.white);
        //设置底部加载文字提示
        list_r.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");

        list_r.refresh();

        //子条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {
                    ARouterUtil.intentNoPar("/qb/ComDelActivity",view);
                }

            }

        });



    }


    List<Com> mClue = new ArrayList<>();;
    List<Com> mClues = new ArrayList<>();
    private void connecting(int p) {

        RestClient.builder()
                .url("https://www.baidu.com/")
//                .params("","")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(instance, response, Toast.LENGTH_SHORT).show();

//
//                        try{
//                            mClue = new ArrayList<>();
//                            Map<String, Object> map = GsonUtil.JsonToMap(s);
//
//                            LogUtils.e(map.toString());
//                            if (map.get("state").toString().equals("1")) {
//                                mHandler.sendEmptyMessage(-3);
//                                Toast.makeText(instance,map.get("mess").toString(),Toast.LENGTH_SHORT).show();
//                                return;
//                            } else if (map.get("state").toString().equals("0")) {
//                                if (!map.get("count").toString().equals("0")){
                                    mHandler.sendEmptyMessage(-1);
                                    list_r.setBackgroundColor(getResources().getColor(R.color.wirte));
//                                    List<Map<String, Object>> data = GsonUtil.GsonToListMaps(GsonUtil.GsonString(map.get("table")));//参数[{},{}]
                                    for (int j = 0; j < 25; j++) {
                                        Com com = new Com();
                                        com.setId(j+"");//ID
                                        com.setName("测试name"+j);//姓名
                                        com.setBelongs("测试belongs"+j);//
                                        com.setAddress("测试address"+j);

                                        mClue.add(com);
                                        mClues.add(com);
                                    }

                                    String count = "25";
                                    String  num = "10";
                                    TOTAL_COUNTER = Integer.valueOf(count).intValue();
                                    REQUEST_COUNT = Integer.valueOf(num).intValue();
                                    txt_size.setText("共查询到"+count+"条数据");
//                                } else {
//                                    mHandler.sendEmptyMessage(-3);
//                                    txt_size.setVisibility(View.GONE);
//                                }


//                            } else if (map.get("state").toString().equals("19")) {
//                                //响应失败
//                                mHandler.sendEmptyMessage(-3);
//                                txt_size.setVisibility(View.GONE);
//                            }else if (map.get("state").toString().equals("10")) {
//                                mHandler.sendEmptyMessage(-3);
//                                //响应失败
//                                Toast.makeText(instance, map.get("mess").toString(), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(instance,LoginActivity.class));
//                                finish();
//                            }
//                        }catch (Exception e){
//                            mHandler.sendEmptyMessage(-3);
//                            Toast.makeText(instance,e.toString(),Toast.LENGTH_SHORT).show();
//                        }




                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        mHandler.sendEmptyMessage(-3);

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        mHandler.sendEmptyMessage(-3);
//
                    }
                })
                .build()
                .get();
//
//        LogUtils.e(HttpUrlUtils.getHttpUrl().clueList()+"?access_token="+ SPUtils.get(instance,"access_token",""));
//        OkHttpUtils
//                .get()
//                .url(HttpUrlUtils.getHttpUrl().clueList()+"?access_token="+ SPUtils.get(instance,"access_token","")+"&p="+p)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//                        Toast.makeText(instance,e.toString(),Toast.LENGTH_SHORT).show();
//                        mHandler.sendEmptyMessage(-3);
//                    }
//
//                    @Override
//                    public void onResponse(String s, int i) {
//
//                        try{
//                            mClue = new ArrayList<>();
//                            Map<String, Object> map = GsonUtil.JsonToMap(s);
//
//                            LogUtils.e(map.toString());
//                            if (map.get("state").toString().equals("1")) {
//                                mHandler.sendEmptyMessage(-3);
//                                Toast.makeText(instance,map.get("mess").toString(),Toast.LENGTH_SHORT).show();
//                                return;
//                            } else if (map.get("state").toString().equals("0")) {
//                                if (!map.get("count").toString().equals("0")){
//                                    mHandler.sendEmptyMessage(-1);
//                                    list_r.setBackgroundColor(getResources().getColor(R.color.white));
//                                    List<Map<String, Object>> data = GsonUtil.GsonToListMaps(GsonUtil.GsonString(map.get("table")));//参数[{},{}]
//                                    for (int j = 0; j < data.size(); j++) {
//                                        Clue clue = new Clue();
//                                        clue.setId(NullUtil.getString(data.get(j).get("sc_id")));//ID
//                                        String pk = NullUtil.getString(data.get(j).get("pk"));
//                                        String sc_id = NullUtil.getString(data.get(j).get(pk));
//                                        String img = NullUtil.getString(data.get(j).get("img"));
//                                        LogUtils.e("头像"+HttpUrlUtils.getHttpUrl().picInclue()+sc_id+"/"+img
//                                                +"?access_token="+ SPUtils.get(instance,"access_token",""));
//
//                                        clue.setPic(HttpUrlUtils.getHttpUrl().picInclue()+sc_id+"/"+img
//                                                +"?access_token="+ SPUtils.get(instance,"access_token",""));
//                                        clue.setName(NullUtil.getString(data.get(j).get("people")));//姓名
//                                        clue.setDate(NullUtil.getString(data.get(j).get("createtime")));//
//                                        clue.setTel(NullUtil.getString(data.get(j).get("mp")));
//                                        clue.setAddress(NullUtil.getString(data.get(j).get("address")));//地址
//                                        clue.setGood(NullUtil.getString(data.get(j).get("goods")));//地址
//
//                                        mClue.add(clue);
//                                        mClues.add(clue);
//                                    }
//
//                                    String count = map.get("count").toString();
//                                    String  num = map.get("num").toString();
//                                    TOTAL_COUNTER = Integer.valueOf(count).intValue();
//                                    REQUEST_COUNT = Integer.valueOf(num).intValue();
//                                    txt_size.setText("共查询到"+count+"条数据");
//                                } else {
//                                    mHandler.sendEmptyMessage(-3);
//                                    txt_size.setVisibility(View.GONE);
//                                }
//
//
//                            } else if (map.get("state").toString().equals("19")) {
//                                //响应失败
//                                mHandler.sendEmptyMessage(-3);
//                                txt_size.setVisibility(View.GONE);
//                            }else if (map.get("state").toString().equals("10")) {
//                                mHandler.sendEmptyMessage(-3);
//                                //响应失败
//                                Toast.makeText(instance, map.get("mess").toString(), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(instance,LoginActivity.class));
//                                finish();
//                            }
//                        }catch (Exception e){
//                            mHandler.sendEmptyMessage(-3);
//                            Toast.makeText(instance,e.toString(),Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//                });
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<Com> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private int size;

    private class PreviewHandler extends Handler {

        private WeakReference<ComListActivity> ref;

        PreviewHandler(ComListActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ComListActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:

                    int currentSize = activity.mDataAdapter.getItemCount();

                    //模拟组装15个数据
                    ArrayList<Com> newList = new ArrayList<>();
                    for (int i = 0; i < mClue.size(); i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        Com item = new Com();
                        item.setName(mClue.get(i).getName());
                        item.setBelongs(mClue.get(i).getBelongs());
                        item.setAddress(mClue.get(i).getAddress());
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
