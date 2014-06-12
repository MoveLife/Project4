package com.resist.movelife;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Advertisement extends Fragment {
    private AdView adView;

    public View onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.advertisement, container, false);

        adView=(AdView)view.findViewById(R.id.MyAdView);
        AdRequest adRequest=new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .build();
        adView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }
}