package com.openvpn.vpn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.openvpn.vpn.Activities.SpeedBoosterActivity;

public class BoostAlarm extends BroadcastReceiver {

    public final static String PREFERENCES_RES_BOOSTER = "akash";

    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedpreferences = context.getSharedPreferences(PREFERENCES_RES_BOOSTER, Context.MODE_PRIVATE);
//        Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();

        /// when memory is orveloaded or increased

        editor = sharedpreferences.edit();
        editor.putString("booster", "1");
        editor.commit();

        try {
            SpeedBoosterActivity.optimizebutton.setBackgroundResource(0);
            SpeedBoosterActivity.optimizebutton.setImageResource(0);
            SpeedBoosterActivity.optimizebutton.setImageResource(R.drawable.n_bt);
        }
        catch(Exception e)
        {

        }

    }
}
