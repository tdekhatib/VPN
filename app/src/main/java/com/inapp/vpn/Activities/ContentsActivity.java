package com.inapp.vpn.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.anchorfree.hydrasdk.HydraSdk;
import com.anchorfree.hydrasdk.api.data.Country;
import com.anchorfree.hydrasdk.api.response.RemainingTraffic;
import com.anchorfree.hydrasdk.callbacks.Callback;
import com.anchorfree.hydrasdk.exceptions.HydraException;
import com.anchorfree.hydrasdk.vpnservice.VPNState;
import com.example.internet_speed_testing.InternetSpeedBuilder;
import com.example.internet_speed_testing.ProgressionModel;
import com.github.anastr.speedviewlib.SpeedView;
import com.inapp.vpn.AdManager;
import com.inapp.vpn.BuildConfig;
import com.inapp.vpn.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.navigation.NavigationView;
import com.inapp.vpn.AdapterWrappers.ServerListAdapterFree;
import com.inapp.vpn.Config;
import com.inapp.vpn.Utils.LocalFormatter;
import com.inapp.vpn.speed.Speed;
import com.onesignal.OneSignal;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import fr.bmartel.speedtest.SpeedTestSocket;
import pl.droidsonroids.gif.GifImageView;

public abstract class ContentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LottieAnimationView lottieAnimationView;
    boolean vpn_toast_check = true;
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    Handler handlerTrafic = null;

    protected static final String TAG = MainActivity.class.getSimpleName();

    private int adCount = 0;
    VPNState state;
    int progressBarValue = 0;
    Handler handler = new Handler();
    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.downloading)
    TextView textDownloading;

    @BindView(R.id.uploading)
    TextView textUploading;

    @BindView(R.id.connection_status)
    TextView t_connection_status;

    @BindView(R.id.connection_status_image)
    ImageView i_connection_status_image;

    @BindView(R.id.vpn_details)
    ImageView vpn_detail_image;


    @BindView(R.id.tv_timer)
    TextView timerTextView;

    @BindView(R.id.connect_btn)
    ImageView connectBtnTextView;

    @BindView(R.id.connection_state)
    TextView connectionStateTextView;


    @BindView(R.id.flag_image)
    ImageView imgFlag;

    @BindView(R.id.rcv_free)
    RecyclerView rcvFree;

    @BindView(R.id.flag_name)
    TextView flagName;

    @BindView(R.id.footer)
    RelativeLayout footer;

    @BindView(R.id.speedButton)
    Button speedButton;

    @BindView((R.id.cpuButton))
    Button cpuButton;

    @BindView((R.id.BatteryButton))
    Button batteryButton;

    @BindView(R.id.gifImageView1)
    GifImageView gifImageView1;

    @BindView(R.id.gifImageView2)
    GifImageView gifImageView2;

    //admob native advance)
    private UnifiedNativeAd nativeAd;
    public InterstitialAd mInterstitialAd;
    private String STATUS;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Lottie animation to show animation in the project

        lottieAnimationView = findViewById(R.id.animation_view);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //interstitial
                if(mInterstitialAd != null){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        AdRequest request = new AdRequest.Builder()
                                .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                .build();
                        mInterstitialAd.loadAd(request);
                  /*  Intent intent=new Intent(ContentsActivity.this,SpeedBoosterActivity.class);
                    startActivity(intent);*/
                    }
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                AdRequest request = new AdRequest.Builder()
                                        .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                        .build();
                                mInterstitialAd.loadAd(request);
                  /*  Intent intent=new Intent(ContentsActivity.this,SpeedBoosterActivity.class);
                    startActivity(intent);*/
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the interstitial ad is closed.
                            Intent intent=new Intent(ContentsActivity.this,SpeedBoosterActivity.class);
                            startActivity(intent);
                        }
                    });
                }else {
                    // Code to be executed when the interstitial ad is closed.
                    Intent intent=new Intent(ContentsActivity.this,SpeedBoosterActivity.class);
                    startActivity(intent);
                }




            }
        });
        cpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterstitialAd != null){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        AdRequest request = new AdRequest.Builder()
                                .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                .build();
                        mInterstitialAd.loadAd(request);
                    }
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                AdRequest request = new AdRequest.Builder()
                                        .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                        .build();
                                mInterstitialAd.loadAd(request);
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the interstitial ad is closed.
                            Intent intent=new Intent(ContentsActivity.this,CPUCoolerActivity.class);
                            startActivity(intent);
                        }
                    });
                }else {
                    Intent intent=new Intent(ContentsActivity.this,CPUCoolerActivity.class);
                    startActivity(intent);
                }


            }
        });
        batteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterstitialAd != null){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        AdRequest request = new AdRequest.Builder()
                                .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                .build();
                        mInterstitialAd.loadAd(request);
                    }
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                AdRequest request = new AdRequest.Builder()
                                        .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                        .build();
                                mInterstitialAd.loadAd(request);
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the interstitial ad is closed.
                            Intent intent=new Intent(ContentsActivity.this,BatteryActivity.class);
                            startActivity(intent);
                        }
                    });

                }else {
                    Intent intent=new Intent(ContentsActivity.this,BatteryActivity.class);
                    startActivity(intent);
                }



            }
        });
     /*   InternetSpeedBuilder builder = new InternetSpeedBuilder(ContentsActivity.this);
        builder.setOnEventInternetSpeedListener(new InternetSpeedBuilder.OnEventInternetSpeedListener() {
            @Override
            public void onDownloadProgress(int i, @NotNull ProgressionModel progressionModel) {


                //double speed = progressModel.getUploadSpeed()/((Double)1000000);
                java.math.BigDecimal bigDecimal = new java.math.BigDecimal(""+progressionModel.getDownloadSpeed());
                float finalDownload = (bigDecimal.longValue()/1000000);

                Log.d("NET_SPEED" , ""+(float)(bigDecimal.longValue()/1000000));


                java.math.BigDecimal bd = progressionModel.getDownloadSpeed();

                final double d = bd.doubleValue();
                Log.d("SHOW_SPEED" , ""+formatFileSize(d));
                position = getPositionByRate(finalDownload);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textDownloading.setText(formatFileSize(d));
                    }
                });

                lastPosition = position;
            }

            @Override
            public void onUploadProgress(int i, @NotNull ProgressionModel progressionModel) {
                java.math.BigDecimal bigDecimal = new java.math.BigDecimal(""+progressionModel.getUploadSpeed());
                float finalDownload = (bigDecimal.longValue()/1000000);

                Log.d("NET_SPEED" , ""+(float)(bigDecimal.longValue()/1000000));


                java.math.BigDecimal bd = progressionModel.getUploadSpeed();

                final double d = bd.doubleValue();
                Log.d("SHOW_SPEED" , ""+formatFileSize(d));


                Log.d("ANGLE" , ""+getPositionByRate(finalDownload));


                position = getPositionByRate(finalDownload);

                runOnUiThread(() -> textUploading.setText(formatFileSize(d)));

                lastPosition = position;
            }

            @Override
            public void onTotalProgress(int i, @NotNull ProgressionModel progressionModel) {

            }
        });
        builder.start("http://ipv4.ikoula.testdebit.info/", 10000000);*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        vpn_detail_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showServerList();
                //startActivity(new Intent(ContentsActivity.this, Servers.class));
               //showOrHideAppendLayout();
            }
        });


        if (getResources().getBoolean(R.bool.ads_switch) && (!Config.ads_subscription && !Config.all_subscription)) {
            // Initialize the Mobile Ads SDK.

            MobileAds.initialize(ContentsActivity.this, getString(R.string.admob_appid));

            //interstitial
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.admob_intersitail));
            mInterstitialAd.loadAd(new AdRequest.Builder()
                    .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                    .build());

        }

        if (Prefs.contains("connectStart") && Prefs.getString("connectStart", "").equals("on")) {

            isConnected(new Callback<Boolean>() {
                @Override
                public void success(@NonNull Boolean aBoolean) {
                    if (aBoolean) {
                        STATUS = "Disconnect";

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            disconnectAlert();
                        }
                    } else {

                        STATUS = "Connect";

                        if (mInterstitialAd.isLoaded()) {
//                        Interstitial Ad loaded successfully...
                            mInterstitialAd.show();
                        } else {

                            updateUI();
                            connectToVpn();
                        }
                    }
                }

                @Override
                public void failure(@NonNull HydraException e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        if (Prefs.contains("noti") && Prefs.getString("noti", "off").equals("off")) {
            OneSignal.setSubscription(false);
        } else if (Prefs.contains("noti") && Prefs.getString("noti", "off").equals("on")) {
            OneSignal.setSubscription(true);
        } else {
            OneSignal.setSubscription(false);
        }
        //loadFreeServers();
        handlerTrafic = new Handler();
        handleTraficData();
    }

    private void showOrHideAppendLayout() {
        if(footer.getVisibility() == View.VISIBLE){
            vpn_detail_image.setImageResource(R.drawable.ic_drop_down);
            footer.setVisibility(View.GONE);
        }else {
            vpn_detail_image.setImageResource(R.drawable.ic_up);
            footer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getResources().getBoolean(R.bool.ads_switch) && (!Config.ads_subscription && !Config.all_subscription)) {
            //native
            refreshAd();
            //interstitital
            mInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();

                    if (STATUS.equals("Connect")) {
                        updateUI();
                        connectToVpn();
                        loadAdAgain();
                    } else if (STATUS.equals("Disconnect")) {
                        disconnectAlert();
                        loadAdAgain();
                    }
                }
            });
        }else {

        }

    }

    private void loadAdAgain() {
//        load Ads for multiple times in background
//        if (getResources().getBoolean(R.bool.ads_switch)) {
//            mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_glob) {
            startActivity(new Intent(this, Servers.class));
            return true;
        }

   *//*     if (id == R.id.action_purchase) {
            startActivity(new Intent(this, PurchasesActivity.class));
            return true;
        }*//*


        return super.onOptionsItemSelected(item);
    }
*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upgrade) {
//            upgrade application is available...
            //startActivity(new Intent(this, Servers.class));
        } /*else if (id == R.id.nav_unlock) {
            startActivity(new Intent(this, PurchasesActivity.class));
        }*/ else if (id == R.id.nav_helpus) {
//            find help about the application
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kumar.indra.raaj@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Send Bug Report");
            intent.putExtra(Intent.EXTRA_TEXT, "Please Give Your Feedback ");

            try {
                startActivity(Intent.createChooser(intent, "send mail"));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "No mail app found!!!", Toast.LENGTH_SHORT);
            } catch (Exception ex) {
                Toast.makeText(this, "Unexpected Error!!!", Toast.LENGTH_SHORT);
            }
        } else if (id == R.id.nav_rate) {
//            rate application...
            rateUs();
        } else if (id == R.id.nav_share) {
//            share the application...
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm using this Free VPN App, it's provide all servers free https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
            }

        } /*else if (id == R.id.nav_setting) {
//            Application settings...
            startActivity(new Intent(this, Settings.class));
        }*/ /*else if (id == R.id.nav_faq) {
            startActivity(new Intent(this, Faq.class));
        }*/ else if (id == R.id.nav_policy) {
            Uri uri = Uri.parse(getResources().getString(R.string.privacy_policy_link)); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    final Runnable mUIUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            checkRemainingTraffic();
            mUIHandler.postDelayed(mUIUpdateRunnable, 10000);
        }
    };

    @Override
    protected void onResume() {
//        if the application again available from background state...
        super.onResume();
        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    startUIUpdateTask();
                }
            }

            @Override
            public void failure(@NonNull HydraException e) {

            }
        });
    }

    @Override
    protected void onPause() {
//        application in the background state...
        super.onPause();
        stopUIUpdateTask();
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }

    protected abstract void loginToVpn();

    @OnClick(R.id.connect_btn)
    public void onConnectBtnClick(View v) {

        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    STATUS = "Disconnect";
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                AdRequest request = new AdRequest.Builder()
                                        .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                        .build();
                                mInterstitialAd.loadAd(request);
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the interstitial ad is closed.
                            disconnectAlert();
                        }
                    });
                    if (getResources().getBoolean(R.bool.ads_switch)&& (!Config.ads_subscription || !Config.all_subscription)) {

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            AdRequest request = new AdRequest.Builder()
                                    .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                    .build();
                            mInterstitialAd.loadAd(request);

                  /*  Intent intent=new Intent(ContentsActivity.this,SpeedBoosterActivity.class);
                    startActivity(intent);*/
                        }

                    } else {
                        disconnectAlert();
                    }
                } else {

                    STATUS = "Connect";

                    if (getResources().getBoolean(R.bool.ads_switch)&& (!Config.ads_subscription || !Config.all_subscription)) {
//                        Interstitial Ad loaded successfully...
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    AdRequest request = new AdRequest.Builder()
                                            .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                            .build();
                                    mInterstitialAd.loadAd(request);
                                }
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                // Code to be executed when an ad request fails.
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                // Code to be executed when the interstitial ad is closed.
                                updateUI();
                                connectToVpn();                            }
                        });
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            AdRequest request = new AdRequest.Builder()
                                    .addTestDevice("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
                                    .build();
                            mInterstitialAd.loadAd(request);
                            updateUI();
                            connectToVpn();
                  /*  Intent intent=new Intent(ContentsActivity.this,SpeedBoosterActivity.class);
                    startActivity(intent);*/
                        }


                    } else {
                        updateUI();
                        connectToVpn();
                    }
                }
            }

            @Override
            public void failure(@NonNull HydraException e) {
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    /*  Different functions defining the state of the vpn
     *  */
    protected abstract void isConnected(Callback<Boolean> callback);

    protected abstract void connectToVpn();

    protected abstract void disconnectFromVnp();

    protected abstract void chooseServer();

    protected abstract void getCurrentServer(Callback<String> callback);

    protected void startUIUpdateTask() {
        stopUIUpdateTask();
        mUIHandler.post(mUIUpdateRunnable);
    }

    protected void stopUIUpdateTask() {
        mUIHandler.removeCallbacks(mUIUpdateRunnable);
        updateUI();
    }

    protected abstract void checkRemainingTraffic();

    protected void updateUI() {
        //textDownloading.setVisibility(View.INVISIBLE);
        //textUploading.setVisibility(View.INVISIBLE);
        gifImageView1.setBackgroundResource(R.drawable.static_img);
        gifImageView2.setBackgroundResource(R.drawable.static_img);
//        To find vpn state...
        HydraSdk.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                state = vpnState;
                switch (vpnState) {
                    case IDLE: {
//                        vpn is idle...
                        loadIcon();
                        connectBtnTextView.setEnabled(true);
                        connectionStateTextView.setText(R.string.disconnected);
                        timerTextView.setVisibility(View.GONE);
                        hideConnectProgress();
                        break;
                    }
                    case CONNECTED: {
                        textDownloading.setVisibility(View.VISIBLE);
                        textUploading.setVisibility(View.VISIBLE);
                        //gifImageView1.setVisibility(View.VISIBLE);
                        //gifImageView2.setVisibility(View.VISIBLE);
                        gifImageView1.setBackgroundResource(R.drawable.gif);
                        gifImageView2.setBackgroundResource(R.drawable.gif);
//                        vpn Connected Successfully...
                        loadIcon();
                        connectBtnTextView.setEnabled(true);
                        connectionStateTextView.setText(R.string.connected);
                        timer();
                        timerTextView.setVisibility(View.VISIBLE);
                        hideConnectProgress();
                       // mRunnable.run();
                        break;
                    }
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS: {
//                        during connecting vpn
                        loadIcon();
                        connectionStateTextView.setText(R.string.connecting);
                        connectBtnTextView.setEnabled(true);
                        timerTextView.setVisibility(View.GONE);
                        showConnectProgress();
                        break;
                    }
                    case PAUSED: {
//                        vpn paused...
                        connectBtnTextView.setImageResource(R.drawable.ic_power);
                        t_connection_status.setText("Not Selected");
                        connectionStateTextView.setText(R.string.paused);
                        i_connection_status_image.setImageResource(R.drawable.ic_dot);
                        break;
                    }
                    default:{
                        gifImageView1.setVisibility(View.INVISIBLE);
                        gifImageView2.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void failure(@NonNull HydraException e) {

            }
        });

        getCurrentServer(new Callback<String>() {
            //            try to connect to current vpn server...
            @Override
            public void success(@NonNull final String currentServer) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void failure(@NonNull HydraException e) {
            }
        });
    }

    protected void updateTrafficStats(long outBytes, long inBytes) {
//        try to update the traffic state of the vpn...
        String outString = LocalFormatter.easyRead(outBytes, false);
        String inString = LocalFormatter.easyRead(inBytes, false);

    }

    protected void updateRemainingTraffic(RemainingTraffic remainingTrafficResponse) {
        if (remainingTrafficResponse.isUnlimited()) {
        } else {
            String trafficUsed = LocalFormatter.byteCounter(remainingTrafficResponse.getTrafficUsed()) + "Mb";
            String trafficLimit = LocalFormatter.byteCounter(remainingTrafficResponse.getTrafficLimit()) + "Mb";

        }
    }

    protected void showConnectProgress() {
//        Updating progressbar
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                while (state == VPNState.CONNECTING_VPN || state == VPNState.CONNECTING_CREDENTIALS) {
                    progressBarValue++;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                        }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    protected void hideConnectProgress() {
        connectionStateTextView.setVisibility(View.VISIBLE);
    }

    protected void showMessage(String msg) {
        Toast.makeText(ContentsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void rateUs() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flag to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    protected void timer() {
        if (adCount == 0) {
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            timeSwapBuff += timeInMilliseconds;

        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerTextView.setText(String.format("%02d", hrs) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };


    protected void loadIcon() {
        if (state == VPNState.IDLE) {
            //Glide.with(this).load(R.drawable.ic_power).into(connectBtnTextView);
            t_connection_status.setText("Not Selected");
            i_connection_status_image.setImageResource(R.drawable.ic_dot);

        } else if (state == VPNState.CONNECTING_VPN || state == VPNState.CONNECTING_CREDENTIALS) {
            connectBtnTextView.setVisibility(View.VISIBLE);/*INVISIBLE IS CHEANGED TO VISIBLE*/
            lottieAnimationView.setVisibility(View.VISIBLE);
        } else if (state == VPNState.CONNECTED) {
            //Glide.with(this).load(R.drawable.ic_power).into(connectBtnTextView);
            connectBtnTextView.setVisibility(View.VISIBLE);
            t_connection_status.setText("Selected");
            lottieAnimationView.setVisibility(View.GONE);
            if (vpn_toast_check == true) {
                Toasty.success(ContentsActivity.this, "Server Connected", Toast.LENGTH_SHORT).show();
                vpn_toast_check = false;
            }
            i_connection_status_image.setImageResource(R.drawable.ic_dot);

        }
    }

    protected void disconnectAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to disconnect?");
        builder.setPositiveButton("Disconnect",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        disconnectFromVnp();
                        vpn_toast_check = true;
                        Toasty.success(ContentsActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.success(ContentsActivity.this, "VPN Remains Connected", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }


    //loading native ad
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

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
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

    private void loadFreeServers() {
        HydraSdk.countries(new Callback<List<Country>>() {
            @Override
            public void success(List<Country> countries) {
                ArrayList<Country> countryArrayList = new ArrayList<>();
                for (int i = 0; i < countries.size(); i++) {
                    if (i % 2 == 0) {
                        countryArrayList.add(countries.get(i));
                    }
                }
                ServerListAdapterFree adapter = new ServerListAdapterFree(ContentsActivity.this);
                rcvFree.setHasFixedSize(true);
                rcvFree.setLayoutManager(new LinearLayoutManager(ContentsActivity.this,LinearLayoutManager.VERTICAL,false));
                rcvFree.setAdapter(adapter);

            }

            @Override
            public void failure(HydraException e) {

            }
        });
    }
    @OnClick(R.id.purchase_layout)
    void goPurchase(){
        startActivity(new Intent(this, UnlockAllActivity.class));
    }
    @OnClick(R.id.btnServerList)
    void showServerList(){
        startActivity(new Intent(this, Servers.class));
    }

   /* @OnClick(R.id.vpn_location)
    void showServerList1(){
        startActivity(new Intent(this, Servers.class));
    }*/
    @OnClick(R.id.category)
    void openSideNavigation(){
        drawer.openDrawer(GravityCompat.START,true);
    }

    private long mLastRxBytes = 0;
    private long mLastTxBytes = 0;
    private long mLastTime = 0;

    private Speed mSpeed;


    private void handleTraficData() {

        if (handlerTrafic == null)
            return;


        handlerTrafic.postDelayed(this::setTraficData, 1000);


    }

    private void setTraficData() {
        long currentRxBytes = TrafficStats.getTotalRxBytes();

        long currentTxBytes = TrafficStats.getTotalTxBytes();

        long usedRxBytes = currentRxBytes - mLastRxBytes;
        long usedTxBytes = currentTxBytes - mLastTxBytes;
        long currentTime = System.currentTimeMillis();
        long usedTime = currentTime - mLastTime;

        mLastRxBytes = currentRxBytes;
        mLastTxBytes = currentTxBytes;
        mLastTime = currentTime;

        mSpeed = new Speed(this);
        mSpeed.calcSpeed(usedTime, usedRxBytes, usedTxBytes);

//            mIndicatorNotification.updateNotification(mSpeed);
        Log.e("speed-->>", "down-->>" + mSpeed.down.speedValue + "    upload-->>" + mSpeed.up.speedValue);


        if (mSpeed != null && mSpeed.up != null && mSpeed.down != null && state.equals(VPNState.CONNECTED)) {


            textDownloading.setText(mSpeed.down.speedValue + " " + mSpeed.down.speedUnit);
            textUploading.setText(mSpeed.up.speedValue + " " + mSpeed.up.speedUnit);

//            sendBroadcast(traffic);
        }else  {
            textDownloading.setText( "0  " + mSpeed.down.speedUnit);
            textUploading.setText("0  "  + " " + mSpeed.up.speedUnit);
        }

        handleTraficData();
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }
}
