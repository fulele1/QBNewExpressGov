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

public class TwoFragment extends BaseFragment {

    private Context instance;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_two,null);
        instance = TwoFragment.this.getActivity();

        return view;

    }


}
