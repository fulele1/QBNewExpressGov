package com.xaqb.policescan.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.amap.api.maps.model.MultiPointItem;
import com.xaqb.policescan.R;

/**
 * Created by lenovo on 2018/11/27.
 */

public class DialogUtil {
    AlertDialog alertDialog;
    /**
     * 自定义对话框

     * @return
     */
    public AlertDialog showchoseAdialog(Context context, final MultiPointItem pointItem){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.loading_choose_layout);
        TextView gaode =  window.findViewById(R.id.gaode_choose);
        TextView baidu =  window.findViewById(R.id.baidu_choose);
        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickGaode(pointItem);
            }
        });

        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBaidu(pointItem);
            }
        });
        return alertDialog;

    }

    public void clickBaidu(MultiPointItem pointItem) {
    }

    public void clickGaode(MultiPointItem pointItem) {
    }

}
