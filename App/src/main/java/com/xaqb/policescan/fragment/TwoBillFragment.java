package com.xaqb.policescan.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.xaqb.policescan.R;
import com.xaqb.policescan.RLview.LogBill;
import com.xaqb.policescan.RLview.LogBillAdapter;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by fl on 2017/5/18.
 *
 * 检查快递单
 *
 */

public class TwoBillFragment extends BaseFragment {

    private Context instance;
    private View view;

    private TextView txt_size;
    private LRecyclerView list_r;
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


    private LogBillAdapter mDataAdapter = null;

    private TwoBillFragment.PreviewHandler mHandler = new TwoBillFragment.PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_two, null);
        instance = TwoBillFragment.this.getActivity();
        list_r = view.findViewById(R.id.list_recycleview);
        txt_size = view.findViewById(R.id.txt_size);
        setRecycleView();
        initList();
        return view;
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

        list_r.refresh();//刷新数据
    }

    private void setRecycleView() {

        mDataAdapter = new LogBillAdapter(instance);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        list_r.setAdapter(mLRecyclerViewAdapter);


        //设置距离
//        DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
//                .setHeight(R.dimen.list_line)
////                .setPadding(R.dimen.line)
////                .setColorResource(R.color.wirte)
//                .build();
//
//        //mRecyclerView.setHasFixedSize(true);
//        list_r.addItemDecoration(divider);

        list_r.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        list_r.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        list_r.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        list_r.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        //add a HeaderView
        final View header = LayoutInflater.from(this.getActivity()).inflate(R.layout.sample_header, (ViewGroup) this.getActivity().findViewById(android.R.id.content), false);
        mLRecyclerViewAdapter.addHeaderView(header);
//
//        list_r.setOnRefreshL
        //设置头部加载颜色
        list_r.setHeaderViewColor(R.color.colorAccent, R.color.colorPrimary, android.R.color.white);
        //设置底部加载颜色
        list_r.setFooterViewColor(R.color.colorAccent, R.color.colorPrimary, android.R.color.white);
        //设置底部加载文字提示
        list_r.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");

        //子条目的点击事件
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().size() > position) {

                    Bundle bundle = new Bundle();
                    bundle.putString("id",mClues.get(position).getId());
                    ARouterUtil.intentPar("/qb/BillQueryLogActivity", view,bundle);
                }
            }

        });

    }

    List<LogBill> mClue;
    List<LogBill> mClues;
    private void connecting(int p) {

        android.util.Log.e("fule",HttpUrlUtils.getHttpUrl().bill_list()+"?access_token="+ SPUtils.get(instance,"access_token","")+"&p="+p);
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().bill_list()+"?access_token="+ SPUtils.get(instance,"access_token","")+"&p="+p)
//                .params("","")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });
                        android.util.Log.e("fule", response);
                        Map<String, Object> mess = JSON.parseObject(map1.get("mess").toString(), new TypeReference<Map<String, Object>>() {
                        });
                        String num = mess.get("num").toString();
                        String count = mess.get("count").toString();
                        if (NullUtil.getString(map1.get("state")).equals("0")) {
                            if (!count.equals("0")){
                                mHandler.sendEmptyMessage(-1);
                                list_r.setBackgroundColor(getResources().getColor(R.color.background));
                                String table = map1.get("table").toString();

                                List<Map> list1 = JSON.parseArray(table, Map.class);
                                mClue = new ArrayList<>();
                                for (Map<String, Object> map : list1) {
                                    LogBill com = new LogBill();
                                    com.setId( NullUtil.getString(map.get("querylogid")));//ID
                                    com.setUser( NullUtil.getString(map.get("queryuser")));//
                                    com.setOrg( NullUtil.getString(map.get("soname")));
                                    com.setAdd( NullUtil.getString(map.get("queryaddress")));
                                    com.setDate( NullUtil.getString(map.get("querydate")));
                                    com.setCom( NullUtil.getString(map.get("querycode")));
                                    mClue.add(com);
                                    mClues.add(com);
                            }

                            }else {
                                mHandler.sendEmptyMessage(-3);
                            }
                        }

                        TOTAL_COUNTER = Integer.valueOf(count).intValue();
                        REQUEST_COUNT = Integer.valueOf(num).intValue();
                        txt_size.setText("共查询到"+count+"条数据");
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

    private void addItems(ArrayList<LogBill> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private int size;

    private class PreviewHandler extends Handler {

        private WeakReference<TwoBillFragment> ref;

        PreviewHandler(TwoBillFragment activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final TwoBillFragment activity = ref.get();
            if (activity == null || activity.getActivity().isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:

                    int currentSize = activity.mDataAdapter.getItemCount();

                    //模拟组装15个数据
                    ArrayList<LogBill> newList = new ArrayList<>();
                    for (int i = 0; i < mClue.size(); i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        LogBill item = new LogBill();
                        item.setUser(mClue.get(i).getUser());
                        item.setCom(mClue.get(i).getCom());
                        item.setDate(mClue.get(i).getDate());
                        item.setOrg(mClue.get(i).getOrg());
                        item.setAdd(mClue.get(i).getAdd());
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
    public void onPrepareOptionsMenu(Menu menu) {
        this.getActivity().getMenuInflater().inflate(R.menu.menu_main_refresh, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.getActivity().finish();
        } else if (item.getItemId() == R.id.menu_refresh) {
            list_r.forceToRefresh();
        }
        return true;
    }





//    private void setRecycleView() {
//
//        mDataAdapter = new LogBillAdapter(instance);
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
//        list_r.setAdapter(mLRecyclerViewAdapter);
//
//        DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
//                .setHeight(R.dimen.list_line)
//                .setPadding(R.dimen.default_divider_padding)
//                .setColorResource(R.color.wirte)
//                .build();
//
//        //mRecyclerView.setHasFixedSize(true);
//        list_r.addItemDecoration(divider);
//
//        list_r.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//
//        list_r.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        list_r.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
//        list_r.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//
//        //add a HeaderView
//        final View header = LayoutInflater.from(this.getActivity()).inflate(R.layout.sample_header, (ViewGroup) this.getActivity().findViewById(android.R.id.content), false);
//        mLRecyclerViewAdapter.addHeaderView(header);
//
//        list_r.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                mDataAdapter.clear();
//                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
//                mCurrentCounter = 0;
//                connecting(1);
//            }
//        });
//
//        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
//        list_r.setLoadMoreEnabled(true);
//
//        list_r.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//
//                if (mCurrentCounter < TOTAL_COUNTER) {
//                    // loading more
//                    mCurrentpage = mCurrentpage + 1;
//                    connecting(mCurrentpage);
//                } else {
//                    //the end
//                    list_r.setNoMore(true);
//                }
//            }
//        });
//
//        list_r.setLScrollListener(new LRecyclerView.LScrollListener() {
//
//            @Override
//            public void onScrollUp() {
//            }
//
//            @Override
//            public void onScrollDown() {
//            }
//
//            @Override
//            public void onScrolled(int distanceX, int distanceY) {
//            }
//
//            @Override
//            public void onScrollStateChanged(int state) {
//
//            }
//
//        });
//
//        //设置头部加载颜色
//        list_r.setHeaderViewColor(R.color.colorAccent, R.color.colorPrimary, android.R.color.white);
//        //设置底部加载颜色
//        list_r.setFooterViewColor(R.color.colorAccent, R.color.colorPrimary, android.R.color.white);
//        //设置底部加载文字提示
//        list_r.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
//
//        list_r.refresh();
//
//        //子条目的点击事件
//        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onItemClick(View view, int position) {
//                if (mDataAdapter.getDataList().size() > position) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", mClues.get(position).getId());
//                    ARouterUtil.intentPar("/qb/LogDelActivity", view, bundle);
//                }
//            }
//
//        });
//
//    }
//
//
//    List<LogBill> mClue = new ArrayList<>();
//    ;
//    List<LogBill> mClues = new ArrayList<>();
//
//    private void connecting(int p) {
//
//        LogUtils.e(HttpUrlUtils.getHttpUrl().log_list() + "?access_token=" + SPUtils.get(instance, "access_token", "") + "&p=" + p);
//        RestClient.builder()
//                .url(HttpUrlUtils.getHttpUrl().log_list() + "?access_token=" + SPUtils.get(instance, "access_token", "") + "&p=" + p)
////                .params("","")
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
//                        });
//                        LogUtils.e(response);
//                        Map<String, Object> mess = JSON.parseObject(map1.get("mess").toString(), new TypeReference<Map<String, Object>>() {
//                        });
//                        LogUtils.e(mess.get("num").toString());
//                        LogUtils.e(mess.get("count").toString());
//
//                        String num = mess.get("num").toString();
//                        String count = mess.get("count").toString();
//                        if (NullUtil.getString(map1.get("state")).equals("0")) {
//                            mHandler.sendEmptyMessage(-1);
//                            list_r.setBackgroundColor(getResources().getColor(R.color.wirte));
//                            String table = map1.get("table").toString();
//                            LogUtils.e(table);
//
//                            List<Map> list1 = JSON.parseArray(table, Map.class);
//                            for (Map<String, Object> map : list1) {
//                                LogBill item = new LogBill();
//                                item.setId(NullUtil.getString(map.get("querylogid")));//ID
//                                item.setOrg(NullUtil.getString(map.get("querysecurityorg")));//管辖机构
//                                item.setAdd(NullUtil.getString(map.get("queryaddress")));//地址
//                                item.setDate(NullUtil.getString(map.get("querydate")));//日期
//                                item.setCom(NullUtil.getString(map.get("querycom")));//查询企业
//                                item.setUser(NullUtil.getString(map.get("queryuser")));//查询人员
//                                LogUtils.e(map.get("querysecurityorg")+"子条目的设置");
//                                mClue.add(item);
//                                mClues.add(item);
//                            }
//                        }
//
//                        TOTAL_COUNTER = Integer.valueOf(count).intValue();
//                        REQUEST_COUNT = Integer.valueOf(num).intValue();
//                        txt_size.setText("共查询到" + count + "条数据");
//                    }
//                })
//                .failure(new IFailure() {
//                    @Override
//                    public void onFailure() {
//                        mHandler.sendEmptyMessage(-3);
//
//                    }
//                })
//                .error(new IError() {
//                    @Override
//                    public void onError(int code, String msg) {
//                        mHandler.sendEmptyMessage(-3);
//                    }
//                })
//                .build()
//                .get();
//    }
//
//    private void notifyDataSetChanged() {
//        mLRecyclerViewAdapter.notifyDataSetChanged();
//    }
//
//    private void addItems(ArrayList<LogBill> list) {
//
//        mDataAdapter.addAll(list);
//        mCurrentCounter += list.size();
//
//    }
//
//    private int size;
//
//    private class PreviewHandler extends Handler {
//
//        private WeakReference<TwoFragment> ref;
//
//        PreviewHandler(TwoFragment activity) {
//            ref = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            final TwoFragment activity = ref.get();
//            if (activity == null || activity.getActivity().isFinishing()) {
//                return;
//            }
//            switch (msg.what) {
//
//                case -1:
//
//                    int currentSize = activity.mDataAdapter.getItemCount();
//
//                    //模拟组装15个数据
//                    ArrayList<LogBill> newList = new ArrayList<>();
//                    for (int i = 0; i < mClue.size(); i++) {
//                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
//                            break;
//                        }
//                        LogBill item = new LogBill();
//                        item.setAdd(mClue.get(i).getAdd());
//                        item.setOrg(mClue.get(i).getOrg());
//                        item.setDate(mClue.get(i).getDate());
//                        item.setUser(mClue.get(i).getUser());
//                        item.setCom(mClue.get(i).getCom());
//                        LogUtils.e(mClue.get(i).getOrg()+"子条目的设置传递");
//
//                    }
//
//                    activity.addItems(newList);
//
//                    activity.list_r.refreshComplete(REQUEST_COUNT);
//
//                    break;
//                case -3:
//                    activity.list_r.refreshComplete(REQUEST_COUNT);
//                    activity.notifyDataSetChanged();
//                    activity.list_r.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
//                        @Override
//                        public void reload() {
////                            connecting(2);
//                        }
//                    });
//
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }
//
//
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        this.getActivity().getMenuInflater().inflate(R.menu.menu_main_refresh, menu);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            this.getActivity().finish();
//        } else if (item.getItemId() == R.id.menu_refresh) {
//            list_r.forceToRefresh();
//        }
//        return true;
//    }

}