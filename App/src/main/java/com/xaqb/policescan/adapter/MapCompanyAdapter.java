package com.xaqb.policescan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.MultiPointItem;
import com.xaqb.policescan.R;
import com.xaqb.policescan.RLview.Com;
import com.xaqb.policescan.utils.GoMapUtil;
import com.xaqb.policescan.utils.LogUtils;

import java.net.URISyntaxException;
import java.util.List;

import static com.amap.api.mapcore2d.q.i;

/**
 * Created by fl on 2017/3/15.
 */

public class MapCompanyAdapter extends BaseAdapter {
    private Context mContext;
    private List<Com> mCom;
    public MapCompanyAdapter(Context context, List<Com> com) {
        mContext = context;
        mCom = com;
        LogUtils.e("长度------"+com.size());
    }

    @Override
    public int getCount() {
        return mCom.size();
    }

    @Override
    public Object getItem(int i) {
        return mCom.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    ViewHolder holder;
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_com_map,null);

            holder.tvName = (TextView) view.findViewById(R.id.txt_name_map);
            holder.tvSix = (TextView) view.findViewById(R.id.txt_name_total_map);
            holder.tvAdd = (TextView) view.findViewById(R.id.txt_name_add_map);
            holder.navagition = (ImageView) view.findViewById(R.id.img_pic_map);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(mCom.get(i).getName());
        holder.tvSix.setText(mCom.get(i).getBelongs());
        holder.tvAdd.setText(mCom.get(i).getAddress());
        holder.navagition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String latlug = mCom.get(i).getLatLng()+"";
                String latmark = latlug.substring(10, latlug.indexOf(","));
                String lugmark = latlug.substring(latlug.indexOf(",")+1,latlug.indexOf(")"));
                showchoseAdialog(mContext,latmark,lugmark,mCom.get(i).getName());

            }
        });
        return view;
    }


AlertDialog alertDialog;
    /**
     * 自定义对话框
     * @return
     */
    public AlertDialog showchoseAdialog(Context context, final String latmark,
                                        final String lugmark,
                                        final String name){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.loading_choose_layout);
        TextView gaode =  window.findViewById(R.id.gaode_choose);
        TextView baidu =  window.findViewById(R.id.baidu_choose);
        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoMapUtil.setUpGaodeAppByMine(mContext,latmark, lugmark,name);

            }
        });

        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoMapUtil.setUpBaiduAPPByMine(mContext,name);
            }
        });
        return alertDialog;

    }



}

class ViewHolder {
    TextView tvName;
    TextView tvSix;
    TextView tvAdd;
    ImageView navagition;
}
