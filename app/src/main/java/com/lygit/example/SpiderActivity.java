package com.lygit.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lygit.plate.SpiderChartView;

/**
 * 类作用：spider example
 * Created ly on 17-7-17.
 */
public class SpiderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider);
        ((SpiderChartView) findViewById(R.id.scv_spider)).setValues(new int[]{
                70,
                60,
                41,
                68,
                90,
                30}
        );
    }
}
