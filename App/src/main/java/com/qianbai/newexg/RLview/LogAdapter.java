package com.qianbai.newexg.RLview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianbai.newexg.R;
import com.qianbai.newexg.utils.DateUtil;
import com.qianbai.newexg.utils.LogUtils;


/**
 * Created by fl on 2016/12/30.
 */

public class LogAdapter extends ListBaseAdapter<Log> {
    Context mContext;
    public LogAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_log;
    }
    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final Log item = mDataList.get(position);

        TextView txt_user_llog = holder.getView(R.id.txt_user_llog);
        TextView txt_com_lcllog = holder.getView(R.id.txt_com_lcllog);
        TextView txt_date_lcllog = holder.getView(R.id.txt_date_lcllog);

        txt_user_llog.setText(item.getUser());
        txt_com_lcllog.setText(item.getAdd());
        txt_date_lcllog.setText(item.getDate());
        LogUtils.e(item.getOrg()+"子条目的设置Adapter");

    }
}