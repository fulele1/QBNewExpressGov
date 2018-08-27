package com.qianbai.newexg.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qianbai.newexg.R;


/**
 * Created by fl on 2017/5/18.
 */

public class FourFragment extends BaseFragment implements View.OnClickListener{

    private Context instance;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = FourFragment.this.getActivity();
        view = inflater.inflate(R.layout.fragment_four,null);
        return view;
    }

    private void setEvent() {
    }


    @Override
    public void onClick(View view) {

    }
}
