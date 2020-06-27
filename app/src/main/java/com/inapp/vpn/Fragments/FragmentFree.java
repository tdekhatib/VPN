package com.inapp.vpn.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.hydrasdk.HydraSdk;
import com.anchorfree.hydrasdk.api.data.Country;
import com.anchorfree.hydrasdk.callbacks.Callback;
import com.anchorfree.hydrasdk.exceptions.HydraException;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.inapp.vpn.Activities.MainActivity;
import com.inapp.vpn.R;
import com.efaso.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter;
import com.inapp.vpn.AdapterWrappers.FBNativeAdapter;
import com.inapp.vpn.AdapterWrappers.ServerListAdapterFree;
import com.inapp.vpn.Config;

import java.util.ArrayList;
import java.util.List;

public class FragmentFree extends Fragment implements ServerListAdapterFree.RegionListAdapterInterface {
    private RecyclerView recyclerView;
    private ServerListAdapterFree adapter;
    private ArrayList<Country> countryArrayList;
    private FragmentVip.RegionChooserInterface regionChooserInterface;
    int server;
    InterstitialAd mInterstitialAd;
    boolean isAds;
    private RelativeLayout animationHolder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        recyclerView = view.findViewById(R.id.region_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        countryArrayList = new ArrayList<>();
        animationHolder = view.findViewById(R.id.animation_layout);

        adapter = new ServerListAdapterFree(getActivity());
        recyclerView.setAdapter(adapter);

        if (getResources().getBoolean(R.bool.ads_switch) && getResources().getBoolean(R.bool.facebook_list_ads) && (!Config.ads_subscription || !Config.all_subscription)) {
            //facebook adapter
           /* FBNativeAdapter nativeAdapter = FBNativeAdapter.Builder.with(getString(R.string.facebook_placement_id), adapter)
                    .adItemInterval(4)
                    .build();
            recyclerView.setAdapter(nativeAdapter);*/
            isAds = true;
        } else if (getResources().getBoolean(R.bool.ads_switch) && getResources().getBoolean(R.bool.admob_list_ads) && (!Config.ads_subscription || !Config.all_subscription)) {
            //admob adapter here
           /* AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder
                    .with(
                            getString(R.string.admob_native),
                            adapter,
                            "small"
                    )
                    .adItemIterval(4)
                    .build();
            recyclerView.setAdapter(admobNativeAdAdapter);*/
            isAds = true;
        } else {
            //simple adapter
            isAds = false;

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadServers();
    }

    private void loadServers() {
        HydraSdk.countries(new Callback<List<Country>>() {
            @Override
            public void success(List<Country> countries) {
                for (int i = 0; i < countries.size(); i++) {
                    if(isAds){
                        if (i % 2 == 0) {
                            countryArrayList.add(countries.get(i));
                        }else if(i%5 == 0){
                            countryArrayList.add(null);
                        }
                    }else {
                        countryArrayList.add(countries.get(i));

                    }

                }
                adapter.setData(countryArrayList);
                //adapter.notifyDataSetChanged();

                animationHolder.setVisibility(View.GONE);

            }

            @Override
            public void failure(HydraException e) {

            }
        });
    }

    @Override
    public void onCountrySelected(Country item) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        regionChooserInterface.onRegionSelected(item);
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        if (ctx instanceof FragmentVip.RegionChooserInterface) {
            regionChooserInterface = (FragmentVip.RegionChooserInterface) ctx;
        }
    }
}
