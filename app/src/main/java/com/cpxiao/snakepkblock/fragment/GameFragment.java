package com.cpxiao.snakepkblock.fragment;

import android.os.Bundle;
import android.view.View;

import com.cpxiao.R;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.snakepkblock.GameView;

/**
 * @author cpxiao on 2017/09/12.
 */

public class GameFragment extends BaseZAdsFragment {
    private GameView mGameView;

    public static GameFragment newInstance(Bundle bundle) {
        GameFragment fragment = new GameFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mGameView = (GameView) view.findViewById(R.id.game_view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameView != null) {
            mGameView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGameView != null) {
            mGameView.destroy();
        }

    }

}
