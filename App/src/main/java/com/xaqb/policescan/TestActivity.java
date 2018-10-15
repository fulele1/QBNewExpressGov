package com.xaqb.policescan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xaqb.qb_core.app.QBProject;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toast.makeText(QBProject.getApplication(), "初始化项目成功了", Toast.LENGTH_SHORT).show();

    }
}