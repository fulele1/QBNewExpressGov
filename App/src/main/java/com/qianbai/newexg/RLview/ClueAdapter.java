package com.qianbai.newexg.RLview;

import android.content.Context;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianbai.newexg.R;
import com.qianbai.newexg.utils.DateUtil;


/**
 * Created by fl on 2016/12/30.
 */

public class ClueAdapter extends ListBaseAdapter<Clue> {
    Context mContext;
    public ClueAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_clue;
    }
    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final Clue item = mDataList.get(position);

        ImageView tv_pic = holder.getView(R.id.img_pic_lcl);
        TextView txt_message_lcl = holder.getView(R.id.txt_message_lcl);
        TextView txt_com_lcl = holder.getView(R.id.txt_com_lcl);
        TextView txt_date_lcl = holder.getView(R.id.txt_date_lcl);


        txt_message_lcl.setText(item.getMeg());
        txt_com_lcl.setText(item.getCom());
        txt_date_lcl.setText(DateUtil.getDate(item.getDate()));
//        if(!item.getPic().equals("")&&item.getPic()!=null){
//            Glide.with(mContext).load(item.getPic()).transform(new GlideRoundTransform(mContext,10))
//                    .placeholder(R.mipmap.per).error(R.mipmap.ic_launcher).into(tv_pic);
//        }


    }
}