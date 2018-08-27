package com.qianbai.newexg;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.qianbai.newexg.utils.StatuBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

@Route(path="/qb/loginActivity")
public class LoginActivity extends BaseActivity {

    private LoginActivity instance;
    private Unbinder unbinder;
//    @BindView(R.id.txt_finished_login)
    TextView txt_finished;
//    @BindView(R.id.txt_register_login)
    TextView txt_back_pwd,bt_login;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        unbinder= ButterKnife.bind(instance);
        StatuBarUtil.translucentStatusBar(this,true);
        initView();
    }

    private void initView() {
        txt_finished = findViewById(R.id.txt_finished_login);
        txt_finished.setOnClickListener(instance);

        txt_back_pwd = findViewById(R.id.txt_back_pwd_login);
        txt_back_pwd.setOnClickListener(instance);

        bt_login = findViewById(R.id.bt_login_login);
        bt_login.setOnClickListener(instance);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_finished_login:
                showAdialog(instance,"提示","是否退出登录","确定",View.VISIBLE);
                break;
            case R.id.txt_back_pwd_login:
                ARouter.getInstance()
                        .build("/qb/BackPWDActivity")
                        .withOptionsCompat(ActivityOptionsCompat.
                                makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0))//动画效果
                        .navigation();
                break;
            case R.id.bt_login_login:
                ARouter.getInstance()
                        .build("/qb/MainActivity")
                        .withOptionsCompat(ActivityOptionsCompat.
                                makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0))//动画效果
                        .navigation();
                finish();
        }
    }

    @Override
    public void dialogOk() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
