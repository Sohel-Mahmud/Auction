package com.devlearn.sohel.auction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devlearn.sohel.auction.LoginRegisterPanel.LoginRegisterActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(SplashActivity.this,LoginRegisterActivity.class));
        finish();
    }
}
