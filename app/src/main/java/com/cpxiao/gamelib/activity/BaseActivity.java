//package com.cpxiao.gamelib.activity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//
//import com.cpxiao.R;
//import com.cpxiao.AppConfig;
//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdListener;
//import com.facebook.ads.AdSettings;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.google.android.gms.ads.AdRequest;
//import com.umeng.analytics.MobclickAgent;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//
///**
// * @author cpxiao on 2017/3/1.
// * @version 2017/3/17更新打印log信息
// */
//public class BaseActivity extends Activity {
//    protected static final boolean DEBUG = AppConfig.DEBUG;
//    protected final String TAG = "CPXIAO--" + getClass().getSimpleName();
//
//    protected AdView mFbAdView;
//    protected com.google.android.gms.ads.AdView mAdMobAdView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //no title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        //隐藏状态栏部分（电池电量、时间等部分）
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        if (DEBUG) {
//            Log.d(TAG, "onDestroy: ");
//        }
//        if (mFbAdView != null) {
//            mFbAdView.destroy();
//            mFbAdView = null;
//        }
//        if (mAdMobAdView != null) {
//            mAdMobAdView.destroy();
//            mAdMobAdView = null;
//        }
//        super.onDestroy();
//    }
//
//    protected void initFbAds50(String placementId) {
////        initFbAds(placementId, AdSize.BANNER_HEIGHT_50);
//    }
//
//    protected void initFbAds250(String placementId) {
//        initFbAds(placementId, AdSize.RECTANGLE_HEIGHT_250);
//    }
//
//    private void initFbAds(String placeId, AdSize adSize) {
//        if (DEBUG) {
//            Log.d(TAG, "initFbAds: ");
//        }
//
//        if (TextUtils.isEmpty(placeId)) {
//            if (DEBUG) {
//                throw new IllegalArgumentException("placeId is empty!");
//            }
//            return;
//        }
//
//        mFbAdView = new AdView(this, placeId, adSize);
//
//        mFbAdView.setAdListener(new AdListener() {
//
//            @Override
//            public void onError(Ad ad, AdError error) {
//                if (DEBUG) {
//                    Log.d(TAG, "Fb -> " + "onError: " + error.getErrorCode() + "," + error.getErrorMessage());
//                }
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                if (DEBUG) {
//                    Log.d(TAG, "Fb -> " + "onAdLoaded: ");
//                }
//                addToLayout(mFbAdView);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                if (DEBUG) {
//                    Log.d(TAG, "Fb -> " + "onAdClicked: ");
//                }
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                if (DEBUG) {
//                    Log.d(TAG, "onLoggingImpression: ");
//                }
//            }
//
//        });
//        if (DEBUG) {
//            //            AdSettings.addTestDevice("7d7fcc8ff3a053e48671f85990f1ab6d");//nexus 5
//            //            AdSettings.addTestDevice("55c4f301d7c1183f1fa6ede6b3f2fe2e");//坚果
//            //            AdSettings.addTestDevice("e6298923190b4e7e7119e0f14c44f097");//坚果
//
//            // 如果想要添加多台测试设备，只需创建一个字符串列表，添加到加载广告前的位置：
//            List<String> testDevices = new ArrayList<>();
//            testDevices.add("7d7fcc8ff3a053e48671f85990f1ab6d");
//            testDevices.add("55c4f301d7c1183f1fa6ede6b3f2fe2e");
//            testDevices.add("e6298923190b4e7e7119e0f14c44f097");
//            AdSettings.addTestDevices(testDevices);
//        }
//        if (DEBUG) {
//            Log.d(TAG, "initFbAds:  mFbAdView.loadAd();");
//        }
//        mFbAdView.loadAd();
//
//    }
//
//    protected void initAdMobAds50(String placementId) {
//        initAdMobAds(placementId, com.google.android.gms.ads.AdSize.SMART_BANNER);
//    }
//
//    protected void initAdMobAds100(String placementId) {
//        initAdMobAds(placementId, com.google.android.gms.ads.AdSize.LARGE_BANNER);
//    }
//
//    protected void initAdMobAds250(String placementId) {
//        initAdMobAds(placementId, com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE);
//    }
//
//    private void initAdMobAds(String unitId, com.google.android.gms.ads.AdSize adSize) {
//        if (DEBUG) {
//            Log.d(TAG, "initAdMobAds: ");
//        }
//
//        if (TextUtils.isEmpty(unitId)) {
//            if (DEBUG) {
//                throw new IllegalArgumentException("unitId is empty!");
//            }
//            return;
//        }
//        mAdMobAdView = new com.google.android.gms.ads.AdView(this);
//        mAdMobAdView.setAdUnitId(unitId);
//        mAdMobAdView.setAdSize(adSize);
//        mAdMobAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                if (DEBUG) {
//                    Log.d(TAG, "AdMob -> " + "onAdClosed: ");
//                }
//
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                if (DEBUG) {
//                    Log.d(TAG, "AdMob -> " + "onAdFailedToLoad: i = " + i);
//                }
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                super.onAdLeftApplication();
//                if (DEBUG) {
//                    Log.d(TAG, "AdMob -> " + "onAdLeftApplication: ");
//                }
//
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//                if (DEBUG) {
//                    Log.d(TAG, "AdMob -> " + "onAdOpened: ");
//                }
//
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                if (DEBUG) {
//                    Log.d(TAG, "AdMob -> " + "onAdLoaded: ");
//                }
//                addToLayout(mAdMobAdView);
//            }
//        });
//        AdRequest adRequest;
//        if (DEBUG) {
//            adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)// All emulators
//                    //                    .addTestDevice("E89B10531C8CCB95C447A97261F6AA0E")//坚果
//                    .addTestDevice("E1E0F81BBFC3DDCC151FE415046C6E40")//坚果
//                    .build();
//        } else {
//            adRequest = new AdRequest.Builder()
//                    .build();
//        }
//        if (DEBUG) {
//            Log.d(TAG, "initAdMobAds: mAdMobAdView.loadAd(adRequest);");
//        }
//        mAdMobAdView.loadAd(adRequest);
//
//    }
//
//    private void addToLayout(View view) {
//        if (DEBUG) {
//            Log.d(TAG, "addToLayout: ");
//        }
//        if (view == null) {
//            return;
//        }
//        removeFromParent(view);
//
//        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_ads);
//        layout.removeAllViews();
//        layout.addView(view);
//    }
//
//    private void removeFromParent(View view) {
//        if (view == null) {
//            return;
//        }
//        ViewGroup parent = (ViewGroup) view.getParent();
//        if (parent != null) {
//            parent.removeView(view);
//        }
//    }
//}
