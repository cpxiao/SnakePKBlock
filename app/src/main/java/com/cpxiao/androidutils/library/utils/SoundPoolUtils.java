package com.cpxiao.androidutils.library.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * SoundPoolUtils
 * 可播放文件格式：wav
 * 不能播放：
 *
 * @author cpxiao on 2016/5/7
 */
public class SoundPoolUtils {

    private SoundPoolUtils() {
    }

    /**
     * 双重校验锁单例模式，注意不要遗漏volatile关键字
     */
    private static volatile SoundPoolUtils soundPoolUtils = null;

    public static SoundPoolUtils getInstance() {
        if (soundPoolUtils == null) {
            synchronized (SoundPoolUtils.class) {
                if (soundPoolUtils == null) {
                    soundPoolUtils = new SoundPoolUtils();
                }
            }
        }
        return soundPoolUtils;
    }


    /**
     * SoundPool
     */
    private SoundPool mSoundPool;

    /**
     * 音效是否加载完成
     */
    private boolean isSoundPoolLoaded = false;

    /**
     * 资源map
     */
    private HashMap<Integer, Integer> mHashMap = new HashMap<>();

    private class PlaySound extends Thread {
        private int soundId = -1;

        public PlaySound(int id) {
            soundId = id;
        }

        @Override
        public void run() {
            playSound(soundId);
        }

        private void playSound(int id) {
            if (isSoundPoolLoaded) {
                mSoundPool.play(id, 1, 1, 1, 0, 1);
            }
        }
    }

    /**
     * 播放
     *
     * @param key key
     */
    public void play(Integer key) {
        if (mHashMap != null) {
            try {
                int id = mHashMap.get(key);
                new PlaySound(id).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建SoundPool
     *
     * @param maxStreams maxStreams
     */
    public void createSoundPool(int maxStreams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool(maxStreams);
        } else {
            createOldSoundPool(maxStreams);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool(int maxStreams) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(maxStreams)
                .build();
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundPool(int maxStreams) {
        mSoundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * 加载音效
     *
     * @param context context
     * @param resId   resId
     * @param key     key
     */
    private void loadSound(Context context, int resId, Integer key) {
        if (mSoundPool != null) {
            int id = mSoundPool.load(context, resId, 1);
            Log.d("CPXIAO", "id = " + id);
            mHashMap.put(key, id);
        }
    }

    /**
     * 加载音效
     *
     * @param context context
     * @param map     音效源
     */
    public void loadSound(Context context, HashMap<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            int id = entry.getValue();
            loadSound(context, id, key);
        }

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isSoundPoolLoaded = true;    //  表示加载完成
            }
        });

    }
}
