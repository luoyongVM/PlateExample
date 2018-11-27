package com.lygit.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 类作用：主界面
 * Created ly on 17-7-17.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void spider(View view) {
        Intent spider = new Intent(this, SpiderActivity.class);
        startActivity(spider);
    }

    public void credit(View view) {
        Intent credit = new Intent(this, CreditActivity.class);
        startActivity(credit);
    }
}
