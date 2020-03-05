package com.cpxiao.androidutils.library.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cpxiao on 2016/11/03.
 */

public class CoreMediaPlayer implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    public static final int STATE_NONE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSING = 3;
    private static List<MediaPlayer> mMediaPlayerList = new ArrayList<>();
    private MediaPlayer mMediaPlayer = null;
    private String mUrl = null;
    private MediaPlayerListener mMediaPlayerListener = null;
    private boolean mCancelled = false;
    private boolean mPlaying = false;
    private boolean mPreparing = false;

    public CoreMediaPlayer() {

    }

    public void play(String url) {
        mPlaying = false;
        mUrl = url;

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        if (!mMediaPlayerList.contains(mMediaPlayer)) {
            mMediaPlayerList.add(mMediaPlayer);
        }

        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.prepareAsync();

            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onLoadMusic(mUrl);
            }

            mPreparing = true;

        } catch (Exception exception) {
            // 处理异常
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onPlayMusicFailed(url);
            }
            mPreparing = false;
            // 销毁mediaplayer
            release();
        }
    }

    public void pause() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.pause();

        mPlaying = false;

        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onPauseMusic(mUrl);
        }
    }

    public void play() {
        if (mMediaPlayer == null) {
            return;
        }

        mMediaPlayer.start();

        mPlaying = true;

        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onPlayMusic(mUrl);
        }

    }

    public void release() {

        if (mMediaPlayer != null) {
            mMediaPlayerList.remove(mMediaPlayer);
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mCancelled = false;
        mPlaying = false;
        mMediaPlayerListener = null;
        mUrl = null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mCancelled) {
            release();
            return;
        }
        mPreparing = false;

        play();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlaying = false;

        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onFinishPlayMusic(mUrl);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        mPlaying = false;

        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onError(mUrl);
        }

        return false;
    }

    public void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        mMediaPlayerListener = mediaPlayerListener;
    }

    public void setCancelled(boolean cancelled) {
        mCancelled = cancelled;
    }

    public boolean isPlaying() {
        return mPlaying;
    }

    public boolean isPreparing() {
        return mPreparing;
    }

    public interface MediaPlayerListener {

        /**
         * 播放音乐失败
         *
         * @param url url
         */
        void onPlayMusicFailed(String url);

        /**
         * 正在加载音乐
         *
         * @param url url
         */
        void onLoadMusic(String url);

        /**
         * 播放音乐
         *
         * @param url url
         */
        void onPlayMusic(String url);

        /**
         * 暂停播放音乐
         *
         * @param url url
         */
        void onPauseMusic(String url);

        /**
         * 完成播放音乐
         *
         * @param url url
         */
        void onFinishPlayMusic(String url);

        /**
         * 播放发生问题
         *
         * @param url url
         */
        void onError(String url);
    }


}