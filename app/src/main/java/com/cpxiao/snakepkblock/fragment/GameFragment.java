package com.cpxiao.snakepkblock.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cpxiao.R;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.snakepkblock.GameView;
import com.cpxiao.snakepkblock.mode.extra.Extra;
import com.cpxiao.zads.core.ZAdPosition;

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
//        loadZAds(ZAdPosition.POSITION_GAME);

        LinearLayout layout = view.findViewById(R.id.game_view_layout);

        int gameMode = Extra.mode_default;
        Bundle bundle = getArguments();
        if (bundle != null) {
            gameMode = bundle.getInt(Extra.Name.GAME_MODE, Extra.mode_default);
        }
        mGameView = new GameView(getContext(), gameMode);

        layout.addView(mGameView);
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

    public static Bundle makeBundle(int gameMode) {
        Bundle bundle = new Bundle();
        bundle.putInt(Extra.Name.GAME_MODE, gameMode);
        return bundle;
    }

}
