package com.qianbai.newexg.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qianbai.newexg.R;
import com.qianbai.newexg.utils.StatuBarUtil;


/**
 * Created by fl on 2017/5/18.
 */

public class TwoFragment extends BaseFragment {

    private Context instance;
    private View view;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_two,null);
        instance = TwoFragment.this.getActivity();

        return view;

    }


}
