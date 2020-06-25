package com.inapp.vpn;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdManager {
    // Static fields are shared between all instances.
    static InterstitialAd ad;
    private Context ctx;

    public AdManager(Context ctx) {
        this.ctx = ctx;
        createAd();
    }

    public void createAd() {
        // Create an ad.
        ad = new InterstitialAd(ctx);
        ad.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        final AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f").build();

        // Load the interstitial ad.
        ad.loadAd(adRequest);
    }

    public InterstitialAd getAd() {
        return ad;
    }
}