package com.xaqb.policescan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.xaqb.policescan.utils.CheckNetwork;

public class SplashActivity extends BaseActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        iv = findViewById(R.id.iv);
        AlphaAnimation animation = new AlphaAnimation(0.8f, 1.0f);
        animation.setDuration(1000);
        iv.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 动画开始前
             * @param animation
             */
            @Override
            public void onAnimationStart(Animation animation) {
                //检查是否有网络连接
                CheckNetwork.checkNetwork(SplashActivity.this);
            }

            /**
             * 动画结束后
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                if (CheckNetwork.isNetworkAvailable(SplashActivity.this)) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }
            }

            /**
             * 动画重复的时候
             * @param animation
             */
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }
}
