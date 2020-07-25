package com.openvpn.vpn.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.openvpn.vpn.Config;
import com.openvpn.vpn.R;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import me.itangqi.waveloadingview.WaveLoadingView;

public class BatteryActivity extends AppCompatActivity {
    WaveLoadingView mWaveLoadingView;
    ImageView powersaving, ultrasaving, normal;
    TextView hourn, minutes, hourp, minutep, houru, minutesu, hourmain, minutesmain;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    InterstitialAd mInterstitialAd;
    private UnifiedNativeAd nativeAd;

    private LinearLayout tools, myPage;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            mWaveLoadingView.setProgressValue(level);

            mWaveLoadingView.setCenterTitle(level + "%");

            if (level <= 5) {
                hourn.setText(0 + "");
                minutes.setText(15 + "");

                hourp.setText(2 + "");
                minutep.setText(25 + "");

                houru.setText(3 + "");
                minutesu.setText(55 + "");

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(0 + "");
                    minutesmain.setText(15 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(2 + "");
                    minutesmain.setText(25 + "");
                }
            }
            if (level > 5 && level <= 10) {
                hourn.setText(0 + "");
                minutes.setText(30 + "");

                hourp.setText(3 + "");
                minutep.setText(5 + "");

                houru.setText(6 + "");
                minutesu.setText(0 + "");

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(0 + "");
                    minutesmain.setText(30 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(3 + "");
                    minutesmain.setText(5 + "");
                }
            }
            if (level > 10 && level <= 15) {
                hourn.setText(0 + "");
                minutes.setText(45 + "");

                hourp.setText(3 + "");
                minutep.setText(50 + "");

                houru.setText(8 + "");
                minutesu.setText(25 + "");

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(0 + "");
                    minutesmain.setText(45 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(3 + "");
                    minutesmain.setText(50 + "");
                }
            }
            if (level > 15 && level <= 25) {
                hourn.setText(1 + "");
                minutes.setText(30 + "");

                hourp.setText(4 + "");
                minutep.setText(45 + "");

                houru.setText(12 + "");
                minutesu.setText(55 + "");

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(1 + "");
                    minutesmain.setText(30 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(4 + "");
                    minutesmain.setText(45 + "");
                }
            }
            if (level > 25 && level <= 35) {
                hourn.setText(2 + "");
                minutes.setText(20 + "");

                hourp.setText(6 + "");
                minutep.setText(2 + "");

                houru.setText(19 + "");
                minutesu.setText(2 + "");

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(2 + "");
                    minutesmain.setText(20 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(6 + "");
                    minutesmain.setText(2 + "");
                }
            }
            if (level > 35 && level <= 50) {
                hourn.setText(5 + "");
                minutes.setText(20 + "");

                hourp.setText(9 + "");
                minutep.setText(25 + "");

                houru.setText(22 + "");
                minutesu.setText(0 + "");

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(5 + "");
                    minutesmain.setText(20 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(9 + "");
                    minutesmain.setText(20 + "");
                }
            }
            if (level > 50 && level <= 65) {
                hourn.setText(7 + "");
                minutes.setText(30 + "");

                hourp.setText(11 + "");
                minutep.setText(1 + "");

                houru.setText(28 + "");
                minutesu.setText(15 + "");

                mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(7 + "");
                    minutesmain.setText(30 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(11 + "");
                    minutesmain.setText(1 + "");
                }
            }
            if (level > 65 && level <= 75) {
                hourn.setText(9 + "");
                minutes.setText(10 + "");

                hourp.setText(14 + "");
                minutep.setText(25 + "");

                houru.setText(30 + "");
                minutesu.setText(55 + "");
                mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(9 + "");
                    minutesmain.setText(10 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(14 + "");
                    minutesmain.setText(25 + "");
                }
            }
            if (level > 75 && level <= 85) {
                hourn.setText(14 + "");
                minutes.setText(15 + "");

                hourp.setText(17 + "");
                minutep.setText(10 + "");

                houru.setText(38 + "");
                minutesu.setText(5 + "");
                mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(14 + "");
                    minutesmain.setText(15 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(17 + "");
                    minutesmain.setText(10 + "");
                }
            }
            if (level > 85 && level <= 100) {
                hourn.setText(20 + "");
                minutes.setText(45 + "");

                hourp.setText(30 + "");
                minutep.setText(0 + "");

                houru.setText(60 + "");
                minutesu.setText(55 + "");
                mWaveLoadingView.setCenterTitleColor(R.color.primary_white_text);

                if (sharedpreferences.getString("mode", "0").equals("0")) {
                    hourmain.setText(20 + "");
                    minutesmain.setText(45 + "");
                }

                if (sharedpreferences.getString("mode", "0").equals("1")) {
                    hourmain.setText(30 + "");
                    minutesmain.setText(0 + "");
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        MobileAds.initialize(this,
                getString(R.string.admob_appid));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_intersitail));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.show();


        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.battery_saver);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);

        AdView mAdMobAdView = findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdMobAdView.loadAd(adRequest);
        //checkshowint();
        mWaveLoadingView = findViewById(R.id.waveView);
        powersaving = findViewById(R.id.powersaving);
        ultrasaving = findViewById(R.id.ultra);
        normal = findViewById(R.id.normal);
        hourn = findViewById(R.id.hourn);
        minutes = findViewById(R.id.minutes);
        hourp = findViewById(R.id.hourp);
        minutep = findViewById(R.id.minutesp);
        houru = findViewById(R.id.houru);
        minutesu = findViewById(R.id.minutesu);
        hourmain = findViewById(R.id.hourmain);
        minutesmain = findViewById(R.id.minutesmain);
        sharedpreferences = getSharedPreferences("was", MODE_PRIVATE);
        registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        try {
            powersaving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(BatteryActivity.this, PopUp_SavingPower.class);
                    i.putExtra("hour", hourp.getText());
                    i.putExtra("minutes", minutep.getText());
                    i.putExtra("minutesnormal", minutes.getText());
                    i.putExtra("hournormal", hourn.getText());
                    startActivity(i);
                }
            });

            ultrasaving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(BatteryActivity.this, UPopUp.class);
                    i.putExtra("hour", houru.getText());
                    i.putExtra("minutes", minutesu.getText());
                    i.putExtra("minutesnormal", minutes.getText());
                    i.putExtra("hournormal", hourn.getText());
                    startActivity(i);
                }
            });

            normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(BatteryActivity.this, NormalMode.class);
                    startActivity(i);
                }
            });


            mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);

            mWaveLoadingView.setCenterTitleColor(Color.parseColor("#136af6"));
            mWaveLoadingView.setBottomTitleColor(Color.parseColor("#FFFFFF"));

            mWaveLoadingView.setAmplitudeRatio(30);
            mWaveLoadingView.setWaveColor(Color.parseColor("#136af6"));


            mWaveLoadingView.setTopTitleStrokeWidth(3);
            mWaveLoadingView.setAnimDuration(3000);


            mWaveLoadingView.startAnimation();


        } catch (Exception e) {

        }
    }
    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            unregisterReceiver(mBatInfoReceiver);
        }
        catch(Exception e) {}
    }



    public void checkshowint()
    {
        boolean strPref = false;
        SharedPreferences shf =getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean("batterysaverint", false);

        if(strPref)
        {

        }
        else {
            showint();
        }
    }
    public void showint()
    {
        new FancyAlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.battery_saver))
                .setBackgroundColor(Color.parseColor("#0c7944"))
                .setMessage(getResources().getString(R.string.batterysavertxt))
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                .setNegativeBtnText("Cancel")
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_crown, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        SharedPreferences saveint =getSharedPreferences("config", MODE_PRIVATE);
                        saveint.edit().putBoolean("batterysaverint", true).apply();

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        SharedPreferences saveint = getSharedPreferences("config", MODE_PRIVATE);
                        saveint.edit().putBoolean("batterysaverint", true).apply();

                    }
                })

                .build();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void refreshAd() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native));

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                RelativeLayout frameLayout =
                        findViewById(R.id.fl_adplaceholder);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

                Log.w("asdsadsad", "ads" + errorCode);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder()
                .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                .build());
    }
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView
            adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.

                    super.onVideoEnd();
                }
            });
        } else {

        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (!Config.vip_subscription && !Config.all_subscription && !Config.ads_subscription)  refreshAd();    }
    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }
}

