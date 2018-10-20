package com.xaqb.policescan.RLview;

import android.content.Context;
import android.widget.TextView;

import com.xaqb.policescan.R;
import com.xaqb.policescan.utils.DateUtil;


/**
 * Created by fl on 2016/12/30.
 */

public class JointAdapter extends ListBaseAdapter<Joint> {
    Context mContext;
    public JointAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_joint;
    }
    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final Joint item = mDataList.get(position);

        TextView txt_com_joint = holder.getView(R.id.txt_com_joint);
        TextView txt_org_joint = holder.getView(R.id.txt_org_joint);
        TextView txt_thing_joint = holder.getView(R.id.txt_thing_joint);
        TextView txt_question_joint = holder.getView(R.id.txt_question_joint);
        TextView txt_result_joint = holder.getView(R.id.txt_result_joint);
//
//
        txt_com_joint.setText(item.getCom());
        txt_org_joint.setText(item.getOrg());
        txt_thing_joint.setText(item.getThing());
        txt_question_joint.setText(item.getQuestion());
        txt_result_joint.setText(item.getResult());

    }
}