package com.xaqb.policescan.RLview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xaqb.policescan.R;


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
        TextView txt_com_lp = holder.getView(R.id.txt_com_lp);
        TextView txt_ide_lp = holder.getView(R.id.txt_ide_lp);
        ImageView img_sex_lp = holder.getView(R.id.img_sex_lp);


        txt_name_lp.setText(item.getName());
        txt_tel_lp.setText(item.getTel());
        txt_com_lp.setText(item.getCom());
        txt_ide_lp.setText(item.getIde());
        txt_ide_lp.setText(item.getIde());
        if(item.getSix().equals("男")){
            img_sex_lp.setImageResource(R.mipmap.man);
        }else if (item.getSix().equals("女")){
            img_sex_lp.setImageResource(R.mipmap.woman);
        }


    }
}