package com.qianbai.newexg.RLview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianbai.newexg.R;


/**
 * Created by fl on 2016/12/30.
 */

public class PerAdapter extends ListBaseAdapter<Per> {
    Context mContext;
    public PerAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_per;
    }
    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final Per item = mDataList.get(position);

        TextView txt_name_lp = holder.getView(R.id.txt_name_lp);
        TextView txt_tel_lp = holder.getView(R.id.txt_tel_lp);


        txt_name_lp.setText(item.getName());
        txt_tel_lp.setText(item.getTel());
//        if(!item.getPic().equals("")&&item.getPic()!=null){
//            Glide.with(mContext).load(item.getPic()).transform(new GlideRoundTransform(mContext,10))
//                    .placeholder(R.mipmap.per).error(R.mipmap.ic_launcher).into(tv_pic);
//        }


    }
}