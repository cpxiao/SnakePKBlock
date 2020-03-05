package com.cpxiao.androidutils.library.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 可播放文件格式：wav、mp3
 * 不能播放：mov
 *
 * @author cpxiao on 2016/5/9
 */
public class MediaPlayerUtils {

    /**
     * 双重校验锁单例模式，注意volatile关键字
     */
    private static volatile MediaPlayerUtils mMediaPlayerUtils = null;

    private MediaPlayerUtils() {
    }

    public static MediaPlayerUtils getInstance() {
        if (mMediaPlayerUtils == null) {
            synchronized (MediaPlayerUtils.class) {
                if (mMediaPlayerUtils == null) {
                    mMediaPlayerUtils = new MediaPlayerUtils();
                }
            }
        }
        return mMediaPlayerUtils;
    }

    /**
     * 音乐状态常量
     */
    private static final int MEDIA_STATE_START = 0;
    private static final int MEDIA_STATE_PAUSE = 1;
    private static final int MEDIA_STATE_STOP = 2;

    /**
     * 当前状态
     */
    private int mMediaStat = -1;

    private MediaPlayer mMediaPlayer;

    private static final int setTime = 5000;
    private int currentTime;

    private int musicMaxTime;

    private int currentVol;

    private AudioManager audioManager;

    public void init(Context context, int id) {
        mMediaPlayer = MediaPlayer.create(context, id);

        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(true);

            musicMaxTime = mMediaPlayer.getDuration();

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (context instanceof Activity) {
                ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            }
        }
    }

    public void start() {
        if (mMediaPlayer == null) {
            return;
        }

        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            //                e.printStackTrace();
        } catch (IOException e) {
            //                e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();

    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public void soundUp() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol + 1, AudioManager.FLAG_PLAY_SOUND);

    }

    public void soundDown() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol - 1, AudioManager.FLAG_PLAY_SOUND);
    }

    /**
     * 快进
     */
    public void fastForward() {
        fastForward(setTime);
    }

    /**
     * 快进
     *
     * @param setTime 时长
     */
    public void fastForward(int setTime) {
        int deltaTime = currentTime + setTime;
        if (deltaTime > musicMaxTime) {
            mMediaPlayer.seekTo(musicMaxTime);
        } else {
            mMediaPlayer.seekTo(deltaTime);
        }
    }

    /**
     * 快退
     */
    public void rewind() {
        rewind(setTime);
    }

    /**
     * 快退
     *
     * @param setTime 时长
     */
    public void rewind(int setTime) {
        int deltaTime = currentTime - setTime;
        if (deltaTime < 0) {
            mMediaPlayer.seekTo(0);
        } else {
            mMediaPlayer.seekTo(deltaTime);
        }
    }


}
