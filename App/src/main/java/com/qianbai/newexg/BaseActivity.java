package com.qianbai.newexg;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

    }

    AlertDialog alertDialog;

    //自定义普通对话框
    public AlertDialog showAdialog(final Context context, String title, String message, String ok, int view) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.loading_my_layout);
        TextView tvTitle = (TextView) window.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(title);
        TextView tvMessage = (TextView) window.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);
        Button btOk = (Button) window.findViewById(R.id.btn_dia_ok);
        btOk.setText(ok);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.this.dialogOk();
                alertDialog.dismiss();

            }
        });
        Button btNo = (Button) window.findViewById(R.id.btn_dia_no);
        btNo.setVisibility(view);
        btNo.setText("取消");
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }


    //漂亮的不规则对话框
    public AlertDialog showAdialog(final Context context) {
        alertDialog = new AlertDialog.Builder(context,R.style.beautiful_dialog).create();
        alertDialog.setCancelable(false);// 不可以用“返回键”取消
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.loading_beautiful);
        Button btNo = window.findViewById(R.id.btn_dia_no);
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }




    public void dialogOk() {
    }

    public void onBack(View view){
        finish();
    }

    public void onChildBack(View view){
        finish();
    }


}
