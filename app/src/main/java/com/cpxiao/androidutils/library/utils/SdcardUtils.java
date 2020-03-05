package com.cpxiao.androidutils.library.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SdcardUtils
 * 读写SD卡记得加这两个权限：
 * <!--在SDCard中创建与删除文件权限 -->
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 * <!--往SDCard写入数据权限 -->
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *
 * @author cpxiao on 5/24/16.
 */
public class SdcardUtils {

    /**
     * cannot be instantiated
     */
    private SdcardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断Sd卡是否可用
     *
     * @return boolean
     */
    public static boolean isSdcardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * eg:
     * /mnt/sdcard/
     * /storage/sdcard0/
     *
     * @return String
     */
    public static String getSdcardPath() {
        if (isSdcardEnable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        }
        return null;
    }

    /**
     * 获取内存存储的路径
     * eg:
     * /data
     *
     * @return String
     */
    public static String getDataDirectoryPath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }


    /**
     * 获取系统存储路径
     * eg:
     * /system
     *
     * @return String
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获得SD卡总容量，单位byte
     *
     * @return long
     */
    public static long getSdcardTotalSize() {
        if (!isSdcardEnable()) {
            return 0;
        }
        String path = getSdcardPath();
        if (path == null) {
            return 0;
        }

        StatFs stat = new StatFs(path);
        return getTotalSize(stat);
    }


    /**
     * 获得sd卡剩余容量，单位byte
     *
     * @return long
     */
    public static long getSdcardAvailableSize() {
        if (!isSdcardEnable()) {
            return 0;
        }

        String path = getSdcardPath();
        if (path == null) {
            return 0;
        }

        StatFs stat = new StatFs(path);
        return getAvailableSize(stat);
    }


    /**
     * 获得机身内存总容量，单位byte
     *
     * @return long
     */
    public static long getRomTotalSize() {
        String path = getDataDirectoryPath();

        StatFs stat = new StatFs(path);
        return getTotalSize(stat);
    }

    /**
     * 获得机身内存剩余容量，单位byte
     *
     * @return long
     */
    public static long getRomAvailableSize() {
        String path = getDataDirectoryPath();
        StatFs stat = new StatFs(path);
        return getAvailableSize(stat);
    }

    /**
     * 获得总容量，单位byte
     *
     * @return long
     */
    @SuppressWarnings("deprecation")
    private static long getTotalSize(StatFs stat) {
        long blockSize;
        long totalBlocks;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        }
        return blockSize * totalBlocks;
    }

    /**
     * 获得剩余容量，单位byte
     *
     * @return long
     */
    @SuppressWarnings("deprecation")
    private static long getAvailableSize(StatFs stat) {
        long blockSize;
        long availableBlocks;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }


}
