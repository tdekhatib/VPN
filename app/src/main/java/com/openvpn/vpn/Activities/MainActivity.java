package com.openvpn.vpn.Activities;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.anchorfree.hydrasdk.HydraSdk;
import com.anchorfree.hydrasdk.SessionConfig;
import com.anchorfree.hydrasdk.SessionInfo;
import com.anchorfree.hydrasdk.api.AuthMethod;
import com.anchorfree.hydrasdk.api.data.Country;
import com.anchorfree.hydrasdk.api.data.ServerCredentials;
import com.anchorfree.hydrasdk.api.response.RemainingTraffic;
import com.anchorfree.hydrasdk.api.response.User;
import com.anchorfree.hydrasdk.callbacks.Callback;
import com.anchorfree.hydrasdk.callbacks.CompletableCallback;
import com.anchorfree.hydrasdk.callbacks.TrafficListener;
import com.anchorfree.hydrasdk.callbacks.VpnStateListener;
import com.anchorfree.hydrasdk.compat.CredentialsCompat;
import com.anchorfree.hydrasdk.dns.DnsRule;
import com.anchorfree.hydrasdk.exceptions.ApiHydraException;
import com.anchorfree.hydrasdk.exceptions.HydraException;
import com.anchorfree.hydrasdk.exceptions.NetworkRelatedException;
import com.anchorfree.hydrasdk.exceptions.RequestException;
import com.anchorfree.hydrasdk.exceptions.VPNException;
import com.anchorfree.hydrasdk.vpnservice.VPNState;
import com.anchorfree.reporting.TrackingConstants;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.openvpn.vpn.Config;
import com.openvpn.vpn.Fragments.FragmentVip;
import com.openvpn.vpn.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ContentsActivity implements TrafficListener, VpnStateListener, FragmentVip.RegionChooserInterface, BillingProcessor.IBillingHandler {

    private String selectedCountry = "";
    private Locale locale;

    private BillingProcessor bp;

    @Override
    protected void onStart() {
        bp = new BillingProcessor(this, Config.IAP_LISENCE_KEY, this);
        bp.initialize();
        RequestConfiguration.Builder requestBuilder = new RequestConfiguration.Builder().setTestDeviceIds(
                Collections.singletonList("91b511f6-d4ab-4a6b-94fa-e538dfbee85f")
        );
        MobileAds.setRequestConfiguration(requestBuilder.build());
        MobileAds.initialize(this);
        super.onStart();

//      Try to connect to the vpn server...
        loginToVpn();
        HydraSdk.addTrafficListener(this);
        HydraSdk.addVpnListener(this);
        Intent intent = getIntent();
        selectedCountry = intent.getStringExtra("c");

        if (selectedCountry != null && !VPNState.CONNECTED.equals(true)) {
            if (getResources().getBoolean(R.bool.ads_switch) && (!Config.ads_subscription && !Config.all_subscription && !Config.vip_subscription)) {

            }
            if (mInterstitialAd != null) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.

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
                            locale = new Locale("", selectedCountry);

                            imgFlag.setImageResource(getResources().getIdentifier("drawable/" + selectedCountry.toLowerCase(), null, getPackageName()));
                            flagName.setText(locale.getDisplayCountry());

                            updateUI();
                            connectToVpn();
                        }
                    });

                } else {
                    locale = new Locale("", selectedCountry);

                    imgFlag.setImageResource(getResources().getIdentifier("drawable/" + selectedCountry.toLowerCase(), null, getPackageName()));
                    flagName.setText(locale.getDisplayCountry());

                    updateUI();
                    connectToVpn();
                }

            }else {
                locale = new Locale("", selectedCountry);

                imgFlag.setImageResource(getResources().getIdentifier("drawable/" + selectedCountry.toLowerCase(), null, getPackageName()));
                flagName.setText(locale.getDisplayCountry());

                updateUI();
                connectToVpn();
            }

        }

    }

    @Override
    protected void onStop() {
//        application stopped...
        super.onStop();
        HydraSdk.removeVpnListener(this);
        HydraSdk.removeTrafficListener(this);
    }

    @Override
    public void onTrafficUpdate(long bytesTx, long bytesRx) {
        updateUI();
        updateTrafficStats(bytesTx, bytesRx);
    }

    @Override
    public void vpnStateChanged(VPNState vpnState) {
        updateUI();
    }

    @Override
    public void vpnError(HydraException e) {
//        Exception to connect to the vpn...
        updateUI();
        handleError(e);
    }

    @Override
    protected void loginToVpn() {
//        try to login to the vpn...
        AuthMethod authMethod = AuthMethod.anonymous();
        HydraSdk.login(authMethod, new Callback<User>() {
            @Override
            public void success(User user) {
            }

            @Override
            public void failure(HydraException e) {
                handleError(e);
            }
        });
    }

    @Override
    protected void isConnected(Callback<Boolean> callback) {
        HydraSdk.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
//                vpn connected successfully...
                callback.success(vpnState == VPNState.CONNECTED);
            }

            @Override
            public void failure(@NonNull HydraException e) {
//                vpn failed to connect...
                callback.success(false);
            }
        });
    }

    @Override
    protected void connectToVpn() {
        if (selectedCountry == null)
            selectedCountry = HydraSdk.COUNTRY_OPTIMAL;
        if (HydraSdk.isLoggedIn()) {
            showConnectProgress();
            List<String> bypassDomains = new LinkedList<>();
            bypassDomains.add("*facebook.com");
            bypassDomains.add("*wtfismyip.com");
            HydraSdk.startVPN(new SessionConfig.Builder()
                    .withReason(TrackingConstants.GprReasons.M_UI)
                    .withVirtualLocation(selectedCountry)
                    .addDnsRule(DnsRule.Builder.bypass().fromDomains(bypassDomains))
                    .build(), new Callback<ServerCredentials>() {
                @Override
                public void success(ServerCredentials serverCredentials) {
                    hideConnectProgress();
                    startUIUpdateTask();
                }

                @Override
                public void failure(HydraException e) {
                    hideConnectProgress();
                    updateUI();

                    handleError(e);
                }
            });
        } else {
            loginToVpn();
            showConnectProgress();
            List<String> bypassDomains = new LinkedList<>();
            bypassDomains.add("*facebook.com");
            bypassDomains.add("*wtfismyip.com");
            HydraSdk.startVPN(new SessionConfig.Builder()
                    .withReason(TrackingConstants.GprReasons.M_UI)
                    .withVirtualLocation(selectedCountry)
                    .addDnsRule(DnsRule.Builder.bypass().fromDomains(bypassDomains))
                    .build(), new Callback<ServerCredentials>() {
                @Override
                public void success(ServerCredentials serverCredentials) {
                    hideConnectProgress();
                    startUIUpdateTask();
                }

                @Override
                public void failure(HydraException e) {
                    hideConnectProgress();
                    updateUI();

                    handleError(e);
                }
            });
        }
    }

    @Override
    protected void disconnectFromVnp() {
//        Disconnect from vpn server...
        showConnectProgress();
        HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                hideConnectProgress();
                stopUIUpdateTask();
            }

            @Override
            public void error(HydraException e) {
                hideConnectProgress();
                updateUI();

                handleError(e);
            }
        });
    }

    @Override
    protected void chooseServer() {

    }

    @Override
    protected void getCurrentServer(final Callback<String> callback) {
//        try to connect to the current or selected vpn...
        HydraSdk.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState state) {
                if (state == VPNState.CONNECTED) {
                    HydraSdk.getSessionInfo(new Callback<SessionInfo>() {
                        @Override
                        public void success(@NonNull SessionInfo sessionInfo) {
//                            vpn connected Successfully...
                            callback.success(CredentialsCompat.getServerCountry(sessionInfo.getCredentials()));
                        }

                        @Override
                        public void failure(@NonNull HydraException e) {
//                            vpn failed to connect...
                            callback.success(selectedCountry);
                        }
                    });

                } else {
                    callback.success(selectedCountry);
                }
            }

            @Override
            public void failure(@NonNull HydraException e) {
                callback.failure(e);
            }
        });
    }

    @Override
    protected void checkRemainingTraffic() {
        HydraSdk.remainingTraffic(new Callback<RemainingTraffic>() {
            @Override
            public void success(RemainingTraffic remainingTraffic) {
                updateRemainingTraffic(remainingTraffic);
            }

            @Override
            public void failure(HydraException e) {
                updateUI();

                handleError(e);
            }
        });
    }


    @Override
    public void onRegionSelected(Country item) {

//        Check which country vpn server selected

        selectedCountry = item.getCountry();
        updateUI();

        HydraSdk.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState state) {
                if (state == VPNState.CONNECTED) {
                    showMessage("Reconnecting to VPN with " + selectedCountry);
                    HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {
                            connectToVpn();
                        }

                        @Override
                        public void error(HydraException e) {
                            // In this case we try to reconnect
                            selectedCountry = "";
                            connectToVpn();
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull HydraException e) {

            }
        });
    }

    // Example of error handling
    public void handleError(Throwable e) {
        Log.w(TAG, e);
        if (e instanceof NetworkRelatedException) {
            showMessage("Check internet connection");
        } else if (e instanceof VPNException) {
//
            switch (((VPNException) e).getCode()) {
                case VPNException.REVOKED:
                    showMessage("User revoked vpn permissions");
                    break;
                case VPNException.VPN_PERMISSION_DENIED_BY_USER:
                    showMessage("User canceled to grant vpn permissions");
                    break;
                case VPNException.HYDRA_ERROR_BROKEN:
                    showMessage("Connection with vpn service was lost");
                    break;
                case VPNException.HYDRA_DCN_BLOCKED_BW:
                    showMessage("Client traffic exceeded");
                    break;
                default:
                    showMessage("Error in VPN Service");
                    break;
            }
        } else if (e instanceof ApiHydraException) {
            switch (((ApiHydraException) e).getContent()) {
                case RequestException.CODE_NOT_AUTHORIZED:
                    /* showMessage("");*/
                    break;
                case RequestException.CODE_TRAFFIC_EXCEED:
                    /* showMessage("Server unavailable");*/
                    break;
                default:
                    /* showMessage("Other error. Check RequestException constants");*/
                    break;
            }
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        checkSubscriptions();
    }

    private void checkSubscriptions() {



        if (bp.isSubscribed(Config.all_month_id) ||
                bp.isSubscribed(Config.all_threemonths_id) ||
                bp.isSubscribed(Config.all_sixmonths_id) ||
                bp.isSubscribed(Config.all_yearly_id)) {

            Config.all_subscription = true;
        }
        updateSubscription();
    }
}
