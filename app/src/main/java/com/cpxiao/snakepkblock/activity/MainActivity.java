package com.cpxiao.snakepkblock.activity;

import android.os.Bundle;

import com.cpxiao.gamelib.activity.BaseZAdsActivity;
import com.cpxiao.gamelib.fragment.BaseFragment;
import com.cpxiao.snakepkblock.fragment.HomeFragment;
import com.cpxiao.zads.ZAdManager;

public class MainActivity extends BaseZAdsActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return HomeFragment.newInstance(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZAdManager.getInstance().init(getApplicationContext());
//        loadAds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAds();
    }

    private void loadAds() {
//        Log.d("MainActivity", "loadAds");
        initAdMobAds50("ca-app-pub-4157365005379790/2627743286");
        initAdMobAds100("ca-app-pub-4157365005379790/2627743286");
//        initAdMobAds250("ca-app-pub-4157365005379790/2627743286");

//        initAdMobAds250("ca-app-pub-3940256099942544/6300978111");//test id
//        initAdMobAds100("ca-app-pub-3940256099942544/6300978111");//test id
//        initAdMobAds50("ca-app-pub-3940256099942544/6300978111");//test id


        initFbAds50("896102797222197_896103430555467");
        initFbAds90("896102797222197_896103083888835");
//        initFbAds90("896102797222197_896103430555467");//id和尺寸一一对应，不可通用。
//        initFbAds250("");
//        loadZAds(ZAdPosition.POSITION_MAIN);
    }

    @Override
    protected void onDestroy() {
        ZAdManager.getInstance().destroyAllPosition(this);
        super.onDestroy();
    }
}
