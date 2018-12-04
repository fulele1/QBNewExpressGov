package com.xaqb.policescan.RLview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xaqb.policescan.R;


/**
 * Created by fl on 2016/12/30.
 */

public class ComAdapter extends ListBaseAdapter<Com> {
    Context mContext;
    public ComAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_com;
    }
    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final Com item = mDataList.get(position);

        ImageView tv_pic = holder.getView(R.id.img_pic_lc);
        TextView txt_name_lc = holder.getView(R.id.txt_name_lc);
        TextView txt_name_total_lc = holder.getView(R.id.txt_name_total_lc);
        TextView txt_address_lc = holder.getView(R.id.txt_address_lc);


        txt_name_lc.setText(item.getName());
        txt_name_total_lc.setText(item.getBelongs());
        txt_address_lc.setText(item.getAddress());
//        if(!item.getPic().equals("")&&item.getPic()!=null){
//            Glide.with(mContext).load(item.getPic()).transform(new GlideRoundTransform(mContext,10))
//                    .placeholder(R.mipmap.per).error(R.mipmap.ic_launcher).into(tv_pic);
//        }


    }
}