package com.ristana.e_news.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ristana.e_news.R;
import com.ristana.e_news.manager.PrefManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hsn on 11/03/2017.
 */

public class  AdsService extends Service {
    private InterstitialAd mInterstitialAd;
    public static final long INTERVAL=10000;//variable for execute services every 1 minute
    private Handler mHandler=new Handler(); // run on another Thread to avoid crash
    private Timer mTimer=null; // timer handling

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("unsupported Operation");
    }
    @Override
    public void onCreate() {
        // cancel if service is  already existed
        long S= Integer.parseInt(getResources().getString(R.string.AD_MOB_TIME_FULL_AUTO));
        long INTERVAL_T= S*1000;
        if(mTimer!=null)
            mTimer.cancel();
        else
            mTimer=new Timer(); // recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(),0,INTERVAL_T);// schedule task

    }
    @Override
    public void onDestroy() {
        mTimer.cancel();//cancel the timer
    }
    //inner class of TimeDisplayTimerTask
    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast at every 1 minute

                    PrefManager prf= new PrefManager(getApplicationContext());

                    if (prf.getString("APP_RUN").equals("true")){
                        showAds();
                    }
                }
            });
        }
    }

    private void showAds(){
        if(getResources().getString(R.string.AD_MOB_ENABLED_FULL_SCREEN).equals("true")) {
            mInterstitialAd = new InterstitialAd(this);

            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Load ads into Interstitial Ads
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    PrefManager prf= new PrefManager(getApplicationContext());
                    if (prf.getString("APP_RUN").equals("true")) {
                        showInterstitial();
                   }
                }
            });
        }
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}