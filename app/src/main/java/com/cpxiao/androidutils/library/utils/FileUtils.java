package com.cpxiao.androidutils.library.utils;

import android.text.TextUtils;

import java.io.File;

public class FileUtils {

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径，绝对路径
     * @return boolean
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径，绝对路径
     * @return boolean
     */
    public static boolean deleteSDFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return !(!file.exists() || file.isDirectory()) && file.delete();
    }

}
