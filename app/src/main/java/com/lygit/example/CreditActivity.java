package com.lygit.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lygit.plate.CreditCheckView;

/**
 * 类作用：credit example
 * Created ly on 17-7-17.
 */
public class CreditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        ((CreditCheckView) findViewById(R.id.ccv_credit)).setCurrentCreditValue(1400);
    }
}
