package com.qianbai.newexg.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qianbai.newexg.R;


/**
 * Created by fl on 2017/5/18.
 */

public class ThreeFragment extends BaseFragment implements View.OnClickListener{

    private Context instance;
    private View view;
    private Button btn_query;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = ThreeFragment.this.getActivity();
        view = inflater.inflate(R.layout.fragment_three,null);
        return view;
    }

    private void setEvent() {
        btn_query.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
    }
}
