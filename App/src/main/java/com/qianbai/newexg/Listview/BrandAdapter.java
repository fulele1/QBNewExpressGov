package com.qianbai.newexg.Listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.qianbai.newexg.BrandActivity;
import com.qianbai.newexg.R;

import java.util.ArrayList;
import java.util.HashMap;


public class BrandAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    /**
     * 首字母
     */
    public HashMap<String, Integer> map_IsHead;
    private LayoutInflater layoutInflater;
    /**
     * 数据集
     */
    private ArrayList<BrandBean> list;

    public BrandAdapter(Context context, ArrayList<BrandBean> list, HashMap<String, Integer> map_IsHead) {
        this.list = list;
        this.map_IsHead = map_IsHead;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    //实现自定义listview的接口
    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == BrandActivity.TITLE;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        switch (getItemViewType(i)) {

            case BrandActivity.ITEM:
                if (view == null) {
                    viewHolder = new ViewHolder();
                    view = layoutInflater.inflate(R.layout.item_phone_item, null);
                    viewHolder.txt = (TextView) view.findViewById(R.id.item_phone_txt_name);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                //设置名字
                viewHolder.txt.setText(list.get(i).getName());
                break;
            case BrandActivity.TITLE:
                if (view == null) {
                    viewHolder = new ViewHolder();
                    view = layoutInflater.inflate(R.layout.item_phone_title, null);
                    viewHolder.txt = (TextView) view.findViewById(R.id.item_phone_txt_head);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                //设置标题
                viewHolder.txt.setText(list.get(i).getHeadChar());
                break;
        }
        return view;
    }


    private class ViewHolder {
        private TextView txt;
    }

}
