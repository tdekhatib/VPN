package com.openvpn.vpn.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class UPopUp extends AppCompatActivity {
    RecyclerView recyclerView;
    PowerModeAdapter mAdapter;
    List<ItemPower> items;
    ImageView applied;
    TextView extendedtime,extendedtimedetail;
    int hour;
    int min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getIntent().getExtras();
        setContentView(R.layout.activity_u_pop_up);
        applied =(ImageView) findViewById(R.id.applied);
        extendedtime=(TextView) findViewById(R.id.addedtime);
        extendedtimedetail=(TextView) findViewById(R.id.addedtimedetail);

        try {

            hour = Integer.parseInt(b.getString("hour").replaceAll("[^0-9]", "")) - Integer.parseInt(b.getString("hournormal").replaceAll("[^0-9]", ""));
            min = Integer.parseInt(b.getString("minutes").replaceAll("[^0-9]", "")) - Integer.parseInt(b.getString("minutesnormal").replaceAll("[^0-9]", ""));
        }
        catch(Exception e)
        {
            hour=4;
            min=7;
        }

        if(hour==0 && min==0)
        {
            hour=4;
            min=7;
        }

        extendedtime.setText("( +"+hour+" h " +Math.abs(min)+" m )");
        extendedtimedetail.setText(""+"\n"+Math.abs(hour)+"h "+Math.abs(min)+"m");

        applied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(UPopUp.this,UApplying.class);
                startActivity(i);

                finish();
            }
        });

        items =new ArrayList<>();

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
                add("Limit Brightness Upto 90%", 0);


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
                add("Use Black and White Scheme To Avoid Battery Draning", 3);
            }
        }, 4000);



        final Handler handler5 = new Handler();
        handler5.postDelayed(new Runnable() {
            @Override
            public void run() {
                add("Block Acess to Memory and Battery Draning Apps", 4);

            }
        }, 5000);

        final Handler handler6 = new Handler();
        handler6.postDelayed(new Runnable() {
            @Override
            public void run() {
                add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 5);

            }
        }, 6000);


    }

    public void add(String text, int position) {
        ItemPower item=new ItemPower();
        item.setText(text);
        items.add(item);
        mAdapter.notifyItemInserted(position);

    }



}
