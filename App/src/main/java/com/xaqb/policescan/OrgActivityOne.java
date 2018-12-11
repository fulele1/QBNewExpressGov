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
import com.xaqb.policescan.threeLevel.MenuData;
import com.xaqb.policescan.threeLevel.MenuDialogAdapter;
import com.xaqb.policescan.threeLevel.MyPagerAdapter;
import com.xaqb.policescan.threeLevel.MyViewPager;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/qb/OrgActivityOne")
public class OrgActivityOne extends BaseActivity {

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

        setContentView(R.layout.activity_org_one);
        mContext = this;
        StatuBarUtil.setStatuBarLightModeClild(this, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initViews();
        TextView mTxtTitle = (TextView) findViewById(R.id.tv_title_child_tilte);
        mTxtTitle.setText("管辖机构");
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
        mViewPager.setAdapter(new MyPagerAdapter(views,1f));


        //二级
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView1Adapter != null)
                    mListView1Adapter.setSelectedPos(position);
                if (mListView2Adapter != null)
                    mListView2Adapter.setSelectedPos(-1);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("orgName", list1.get(position).name);
                bundle.putString("orgCode", list1.get(position).id);
                intent.putExtras(bundle);
                OrgActivityOne.this.setResult(RESULT_OK, intent);
                OrgActivityOne.this.finish();


                }
        });
    }



}
