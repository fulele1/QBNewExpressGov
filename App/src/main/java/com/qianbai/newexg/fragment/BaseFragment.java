package com.qianbai.newexg.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qianbai.newexg.R;


/**
 * Created by fl on 2017/5/18.
 */

public class BaseFragment extends Fragment {

    private Context mContext;
    private View view;
    private Intent mIntent;
    private Toast mToast;
    private AlertDialog.Builder mDialog;//对话框


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one,null);
        return view;
    }

    /**
     * 弹出普通对话框
     *
     * @param tit
     * @param icon
     * @param mes
     * @param ok
     * @param no
     */
    public AlertDialog.Builder showDialogB(Context context, String tit, int icon, String mes, String ok, String no) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setIcon(icon);
        mDialog.setTitle(tit);
        mDialog.setMessage(mes);
        mDialog.setPositiveButton(ok, new DialogEvent());
        mDialog.setNegativeButton(no, new DialogEvent());
        mDialog.show();
        return mDialog;
    }


    class DialogEvent implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    dialogOkB();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    dialogNoB();
                    break;
            }
        }
    }


    public AlertDialog.Builder showDialogB(Context context, String [] item){
        mDialog = new AlertDialog.Builder(context);
        mDialog.setItems(item,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogItemEventB(which);

            }
        });
        mDialog.show();
        return mDialog;
    }

    /**
     * 对话框的子条目的点击事件
     * @param witch
     */
    public void dialogItemEventB(int witch){
    }

    /**
     * 对话框确定键
     */
    public void dialogOkB() {
    }

    /**
     * 对话框取消键
     */
    public void dialogNoB() {
    }



    AlertDialog alertDialog;

    /**
     * 自定义对话框
     * @param context
     * @param message
     * @param ok
     * @param no
     * @return
     */
    public AlertDialog showAdialog(final Context context, String message, String ok, String no){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.loading_my_layout);
        TextView tvMessage = (TextView) window.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);
        Button btOk = (Button) window.findViewById(R.id.btn_dia_ok);
        btOk.setText(ok);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okDialog();
            }

        });
        Button btNo = (Button) window.findViewById(R.id.btn_dia_no);
        btNo.setText(no);
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
            public void okDialog() {
            }


}
