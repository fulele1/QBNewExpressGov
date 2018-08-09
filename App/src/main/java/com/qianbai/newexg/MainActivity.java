package com.qianbai.newexg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.qianbai.qb_core.app.QBProject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(QBProject.getApplication(), "初始化项目成功了", Toast.LENGTH_SHORT).show();
    }
}