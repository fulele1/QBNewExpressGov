package com.xaqb.policescan.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;
import com.xaqb.policescan.R;

/**
 * Created by lenovo on 2018/11/30.
 */

public class DialogLoadingUtil extends AlertDialog {

    private static DialogLoadingUtil loadingDialog;
    private AVLoadingIndicatorView avi;


    public static DialogLoadingUtil getInstance(Context context){
        loadingDialog = new DialogLoadingUtil(context,R.style.TransparentDialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }
    Context mContext;
    public DialogLoadingUtil(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        avi = this.findViewById(R.id.avi);
    }

    @Override
    public void show() {
        super.show();
        avi.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        avi.hide();
    }
}