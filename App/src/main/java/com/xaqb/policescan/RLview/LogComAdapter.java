package com.xaqb.policescan.RLview;

import android.content.Context;
import android.widget.TextView;

import com.xaqb.policescan.R;
import com.xaqb.policescan.utils.DateUtil;


/**
 * Created by fl on 2016/12/30.
 */

public class LogComAdapter extends ListBaseAdapter<LogCom> {
    Context mContext;
    public LogComAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_log_com;
    }
    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final LogCom item = mDataList.get(position);

        TextView txt_com_llog = holder.getView(R.id.txt_com_llog);
        TextView txt_org_lcllog = holder.getView(R.id.txt_org_lcllog);
        TextView txt_date_listlog = holder.getView(R.id.txt_date_listlog);
        TextView txt_add_listlog = holder.getView(R.id.txt_add_listlog);
//
//
        txt_com_llog.setText(item.getCom());
        txt_org_lcllog.setText(item.getOrg());
        txt_date_listlog.setText(DateUtil.getDate(item.getDate()));
        txt_add_listlog.setText(item.getAdd());
//        txt_date_lcl.setText(DateUtil.getDate(item.getDate()));
//        if(!item.getPic().equals("")&&item.getPic()!=null){
//            Glide.with(mContext).load(item.getPic()).transform(new GlideRoundTransform(mContext,10))
//                    .placeholder(R.mipmap.per).error(R.mipmap.ic_launcher).into(tv_pic);
//        }


    }
}