package com.xaqb.policescan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.model.MultiPointItem;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

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
//        TextView tvTitle = (TextView) window.findViewById(R.id.tv_dialog_title);
//        tvTitle.setText(title);
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
        alertDialog = new AlertDialog.Builder(context, R.style.beautiful_dialog).create();
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


    //对话框的调用
    protected AlertDialog showDialog(String sCaption,
                                     String sText,
                                     String sOk,
                                     String sCancel,
                                     int iLayout) {
        AlertDialog.Builder oBuilder = new AlertDialog.Builder(this);
        if (iLayout > 0) {
            LayoutInflater oInflater = getLayoutInflater();
            View oLayout = oInflater.inflate(iLayout, null, false);
            oBuilder.setView(oLayout);
        } else
            oBuilder.setMessage(sText);
        oBuilder.setTitle(sCaption);
        if (sOk.length() > 0) {
            oBuilder.setPositiveButton(sOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BaseActivity.this.dialogOk();
                    dialog.dismiss();
                }
            });
        }
        if (sCancel.length() > 0) {
            oBuilder.setNegativeButton(sCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BaseActivity.this.dialogCancel();
                    dialog.dismiss();
                }
            });
        }
        AlertDialog oDialog = oBuilder.create();
        oDialog.show();
        return oDialog;
    }


    public void dialogOk() {
    }


    public void dialogCancel() {
    }

    public void onBack(View view) {
        //取消软键盘
        View v = this.getWindow().peekDecorView();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
    }

    public void onChildBack(View view) {
        View v = this.getWindow().peekDecorView();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
    }


    public String readConfig(String sName) {

        SharedPreferences oConfig = getSharedPreferences("config", Activity.MODE_PRIVATE);
        return oConfig.getString(sName, "");
    }

    public void writeConfig(String sName, String sValue) {
        SharedPreferences oConfig = getSharedPreferences("config", Activity.MODE_PRIVATE);
        SharedPreferences.Editor oEdit = oConfig.edit();//获得编辑器
        oEdit.putString(sName, sValue);
        oEdit.commit();//提交内容

    }


    /**
     * 自定义对话框
     *
     * @return
     */
    public AlertDialog showchoseAdialog(Context context, final MultiPointItem pointItem) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.loading_choose_layout);
        TextView gaode = window.findViewById(R.id.gaode_choose);
        TextView baidu = window.findViewById(R.id.baidu_choose);
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
