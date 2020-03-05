package com.cpxiao.snakepkblock.mode.extra;

import android.content.Context;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;

/**
 * @author cpxiao on 2017/07/27.
 */

public final class Extra {
    public static final int mode_easy = 4;
    public static final int mode_normal = 5;
    public static final int mode_hard = 6;
    public static final int mode_insane = 7;
    public static final int mode_default = mode_normal;


    public static final class Key {


        public static final String BEST_SCORE = "BEST_SCORE";

        private static final String BEST_SCORE_EASY = "BEST_SCORE_EASY";
        private static final String BEST_SCORE_NORMAL = "BEST_SCORE_NORMAL";
        private static final String BEST_SCORE_HARD = "BEST_SCORE_HARD";
        private static final String BEST_SCORE_INSANE = "BEST_SCORE_INSANE";
    }

    public static final class Name {
        public static final String GAME_MODE = "GAME_MODE";
    }

    public static void setBestScore(Context context, int gameMode, int bestScore) {
        if (gameMode == mode_easy) {
            PreferencesUtils.putInt(context, Extra.Key.BEST_SCORE_EASY, bestScore);
        } else if (gameMode == mode_normal) {
            PreferencesUtils.putInt(context, Key.BEST_SCORE_NORMAL, bestScore);
        } else if (gameMode == mode_hard) {
            PreferencesUtils.putInt(context, Key.BEST_SCORE_HARD, bestScore);
        } else if (gameMode == mode_insane) {
            PreferencesUtils.putInt(context, Key.BEST_SCORE_INSANE, bestScore);
        }
    }

    public static int getBestScore(Context context, int gameMode) {
        if (gameMode == mode_easy) {
            return PreferencesUtils.getInt(context, Extra.Key.BEST_SCORE_EASY, 0);
        } else if (gameMode == mode_normal) {
            return PreferencesUtils.getInt(context, Key.BEST_SCORE_NORMAL, 0);
        } else if (gameMode == mode_hard) {
            return PreferencesUtils.getInt(context, Key.BEST_SCORE_HARD, 0);
        } else if (gameMode == mode_insane) {
            return PreferencesUtils.getInt(context, Key.BEST_SCORE_INSANE, 0);
        }
        return 0;
    }

    public static String getGameModeStr(Context context, int gameMode) {
        if (gameMode == mode_easy) {
            return context.getString(R.string.easy);
        } else if (gameMode == mode_normal) {
            return context.getString(R.string.normal);
        } else if (gameMode == mode_hard) {
            return context.getString(R.string.hard);
        } else if (gameMode == mode_insane) {
            return context.getString(R.string.insane);
        }
        return "";
    }
}
