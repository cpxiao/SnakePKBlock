package com.cpxiao.androidutils.library.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Intent 跳转工具类
 */
public class IntentUtils {

    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

    private static final String SCEHEME_MARKET_DETAILS = "market://details?id=%s";

    private static final String URL_GOOGLE_PLAY_DETAILS = "https://play.google.com/store/apps/details?id=%s";

    /**
     * 要过滤的app包名
     */
    private static final Set<String> FILTERED_PACKAGE_NAME_SET = new HashSet<>();

    static {
        FILTERED_PACKAGE_NAME_SET.add("com.wandoujia.phoenix2");// 豌豆荚，仅测试
        FILTERED_PACKAGE_NAME_SET.add("me.onemobile.android");
        FILTERED_PACKAGE_NAME_SET.add("com.dragon.android.mobomarket");
        FILTERED_PACKAGE_NAME_SET.add("com.mobogenie");
        FILTERED_PACKAGE_NAME_SET.add("com.baidu.androidstore");
        FILTERED_PACKAGE_NAME_SET.add("com.mobile.indiapp");
    }

    /**
     * 跳转应用市场
     *
     * @param packageName 应用包名
     */
    public static void goToMarketByObjId(Context context, String packageName) {
        checkCxtAndUrlValid(context, packageName);
        String uriStr = String.format(SCEHEME_MARKET_DETAILS, packageName);
        makeGPIntent(context, uriStr, packageName);
    }

    /**
     * 跳转应用市场
     *
     * @param url 协议地址或广告商地址
     */
    public static void goToMarketByUrl(Context context, String url) {
        checkCxtAndUrlValid(context, url);
        //path修正
        String path;
        if (url.startsWith("http")) {
            path = url;
        } else {
            path = "http://" + url;
        }
        //AndroidID替換
        path = path.replace("{ANDROID_ID}", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        makeGPIntent(context, path, null);
    }

    private static void checkCxtAndUrlValid(Context context, String url) {
        if (context == null) {
            throw new NullPointerException("context must not be null!");
        }

        if (url == null) {
            throw new NullPointerException("url must not be null！");
        }
    }

    private static void makeGPIntent(Context context, String url, String packageName) {
        //啟動gp
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // 设置google play包名
        intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);

        PackageManager packageManager = context.getPackageManager();

        // 先尝试Google Play,如果找不到就使用浏览器
        if (packageManager.resolveActivity(intent, 0) == null) {
            if (packageName != null) {
                url = String.format(URL_GOOGLE_PLAY_DETAILS, packageName);
            }
            intent = createFilteredIntent(context, Uri.parse(url), FILTERED_PACKAGE_NAME_SET);
        }

        if (packageManager.resolveActivity(intent, 0) != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 创建过滤的intent
     *
     * @param context
     * @param uri
     * @param filteredPackageNames 待过滤得包名列表
     * @return
     */
    private static Intent createFilteredIntent(Context context, Uri uri, Set<String> filteredPackageNames) {

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        PackageManager packageManager = context.getPackageManager();

        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);

        // 如果只有一个ResolveInfo符合要求就不需要创建选择器
        if (resolveInfos == null || resolveInfos.size() <= 1) {
            return intent;
        }

        List<Intent> targetedIntents = new ArrayList<>();

        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            String packageName = activityInfo.packageName;

            // 排除属于filteredPackageNames的app
            if (filteredPackageNames.contains(packageName)) {
                continue;
            }

            Intent targetIntent = new Intent();
            targetIntent.setPackage(packageName);
            targetIntent.setData(uri);

            targetedIntents.add(targetIntent);
        }

        // 如果只有一个intent符合条件，直接返回intent
        if (targetedIntents.size() == 1) {
            return targetedIntents.get(0);
        }

        // 如果有多个Intent符合条件，创建chooser
        if (targetedIntents.size() > 1) {

            Intent chooserIntent = Intent.createChooser(targetedIntents.remove(0), null);

            if (chooserIntent == null) {
                return null;
            }

            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntents.toArray(new Parcelable[]{}));
            return chooserIntent;
        }
        return null;
    }
}
