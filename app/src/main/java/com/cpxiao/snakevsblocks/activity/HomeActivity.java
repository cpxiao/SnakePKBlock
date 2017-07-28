package com.cpxiao.snakevsblocks.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cpxiao.R;
import com.cpxiao.gamelib.activity.BaseActivity;


public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }


}
