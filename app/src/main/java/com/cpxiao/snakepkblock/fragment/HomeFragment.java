package com.cpxiao.snakepkblock.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.RateAppUtils;
import com.cpxiao.androidutils.library.utils.ShareAppUtils;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.snakepkblock.mode.extra.Extra;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/09/12.
 */

public class HomeFragment extends BaseZAdsFragment implements View.OnClickListener {

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
//        loadZAds(ZAdPosition.POSITION_HOME);

        Button easy = view.findViewById(R.id.play_easy);
        Button normal = view.findViewById(R.id.play_normal);
        Button hard = view.findViewById(R.id.play_hard);
        Button insane = view.findViewById(R.id.play_insane);
        ImageButton rateApp = view.findViewById(R.id.rate_app);
        ImageButton share = view.findViewById(R.id.share);
        ImageButton bestScore = view.findViewById(R.id.best_score);

        easy.setOnClickListener(this);
        normal.setOnClickListener(this);
        hard.setOnClickListener(this);
        insane.setOnClickListener(this);
        rateApp.setOnClickListener(this);
        share.setOnClickListener(this);
        bestScore.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Context context = getHoldingActivity();
        if (id == R.id.play_easy) {
            Bundle bundle = GameFragment.makeBundle(Extra.mode_easy);
            addFragment(GameFragment.newInstance(bundle));
        }
        if (id == R.id.play_normal) {
            Bundle bundle = GameFragment.makeBundle(Extra.mode_normal);
            addFragment(GameFragment.newInstance(bundle));
        }
        if (id == R.id.play_hard) {
            Bundle bundle = GameFragment.makeBundle(Extra.mode_hard);
            addFragment(GameFragment.newInstance(bundle));
        }
        if (id == R.id.play_insane) {
            Bundle bundle = GameFragment.makeBundle(Extra.mode_insane);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.rate_app) {
            RateAppUtils.rate(context);
        } else if (id == R.id.share) {
            String msg = getString(R.string.share_msg) + "\n" +
                    getString(R.string.app_name) + "\n" +
                    "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            ShareAppUtils.share(context, getString(R.string.share), msg);
        } else if (id == R.id.best_score) {
            showBestScoreDialog(context);
        }
    }

    private void showBestScoreDialog(Context context) {
        String msg = getString(R.string.easy) + ": " + Extra.getBestScore(context, Extra.mode_easy)+ "\n"
                + getString(R.string.normal) + ": " + Extra.getBestScore(context, Extra.mode_normal)+ "\n"
                + getString(R.string.hard) + ": " + Extra.getBestScore(context, Extra.mode_hard)+ "\n"
                + getString(R.string.insane) + ": " + Extra.getBestScore(context, Extra.mode_insane);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.best_score)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
