package com.cpxiao.snakepkblock.activity;

import android.os.Bundle;

import com.cpxiao.R;
import com.cpxiao.gamelib.activity.BaseActivity;
import com.cpxiao.snakepkblock.GameView;


public class GameActivity extends BaseActivity {

    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameView = (GameView) findViewById(R.id.game_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGameView != null) {
            mGameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGameView != null) {
            mGameView.destroy();
        }

    }

}
