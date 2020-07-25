package com.openvpn.vpn.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.openvpn.vpn.ItemPower;
import com.openvpn.vpn.PowerModeAdapter;
import com.openvpn.vpn.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class PopUp_SavingPower extends AppCompatActivity {
    RecyclerView recyclerView;
    PowerModeAdapter mAdapter;
    List<ItemPower> items;
    ImageView applied;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    TextView extendedtime,extendedtimedetail;

    int hour;
    int min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up__saving_power);
        Bundle b=getIntent().getExtras();

        sharedpreferences = getSharedPreferences("was", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        extendedtime=(TextView) findViewById(R.id.addedtime);
        extendedtimedetail=(TextView) findViewById(R.id.addedtimedetail);

        try {
            hour = Integer.parseInt(b.getString("hour").replaceAll("[^0-9]", "")) - Integer.parseInt(b.getString("hournormal").replaceAll("[^0-9]", ""));
            min = Integer.parseInt(b.getString("minutes").replaceAll("[^0-9]", "")) - Integer.parseInt(b.getString("minutesnormal").replaceAll("[^0-9]", ""));
        } catch(Exception e) {
            hour=3;
            min=5;
        }

        if(hour==0 && min==0) {
            hour=3;
            min=5;
        }
        extendedtime.setText("( +"+hour+"h " +Math.abs(min)+"m )");
        extendedtimedetail.setText(""+"\n"+Math.abs(hour)+"h "+Math.abs(min)+"m");



        items = new ArrayList<>();

        applied=(ImageView) findViewById(R.id.applied);
        applied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("mode", "1");
                editor.commit();

                Intent i = new Intent(getApplicationContext(), Saving_Power_Comp.class);
                startActivity(i);

                finish();
            }
        });

        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setItemAnimator(new SlideInLeftAnimator());



        recyclerView.getItemAnimator().setAddDuration(200);

        mAdapter = new PowerModeAdapter(items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        recyclerView.computeHorizontalScrollExtent();
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                add("Limit Brightness Upto 80%", 0);


            }
        }, 1000);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                add("Decrease Device Performance", 1);


            }
        }, 2000);

        final Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                add("Close All Battery Consuming Apps", 2);


            }
        }, 3000);

        final Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {
            @Override
            public void run() {
                add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 3);

            }
        }, 4000);
    }

    public void add(String text, int position) {
        ItemPower item=new ItemPower();
        item.setText(text);
        items.add(item);
        mAdapter.notifyItemInserted(position);
    }




}
