package com.openvpn.vpn.AdapterWrappers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.hydrasdk.api.data.Country;
import com.anchorfree.hydrasdk.api.response.RemainingTraffic;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.openvpn.vpn.R;
import com.openvpn.vpn.Activities.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServerListAdapterFree extends RecyclerView.Adapter<ServerListAdapterFree.mViewhoder> {

    ArrayList<Country> datalist = new ArrayList<>();
    private Context context;
    RemainingTraffic remainingTrafficResponse;
    private int AD_TYPE = 0;
    private int CONTENT_TYPE = 1;
    public ServerListAdapterFree( Context ctx) {
        this.context=ctx;
    }

    @NonNull
    @Override
    public mViewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        AdView adview;

        if (viewType == AD_TYPE) {
            adview = new AdView(context);
            adview.setAdSize(AdSize.BANNER);
            adview.setAdUnitId(context.getString(R.string.banner_ads_id));
            float density = context.getResources().getDisplayMetrics().density;
            int height = Math.round(AdSize.BANNER.getHeight() * density);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
            adview.setLayoutParams(params);
            AdRequest request = new AdRequest.Builder().build();
            adview.loadAd(request);
            return new mViewhoder(adview);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.server_list_free, parent, false);
            return new mViewhoder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewhoder holder, int position) {
        if(getItemViewType(position) == CONTENT_TYPE){
            remainingTrafficResponse=new RemainingTraffic();
            Country data=datalist.get(position);
            Locale locale=new Locale("",data.getCountry());
            holder.flag.setImageResource(context.getResources().getIdentifier("drawable/"+data.getCountry().toLowerCase(),null,context.getPackageName()));
            holder.app_name.setText(locale.getDisplayCountry());
            holder.limit.setImageResource(R.drawable.server_signal_3);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(context, MainActivity.class);
                    intent.putExtra("c",data.getCountry());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });

        }


      /*  if (position==0)
        {
            holder.flag.setImageResource(context.getResources().getIdentifier("drawable/flag_default",null,context.getPackageName()));
            holder.app_name.setText("Best Performance Server");
            holder.limit.setVisibility(View.GONE);
        }
        else
        {
            holder.flag.setImageResource(context.getResources().getIdentifier("drawable/"+data.getCountry().toLowerCase(),null,context.getPackageName()));
            holder.app_name.setText(locale.getDisplayCountry());
            holder.limit.setImageResource(R.drawable.server_signal_3);
        }*/

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    @Override
    public int getItemViewType(int position) {
        return datalist.get(position) ==null? AD_TYPE:CONTENT_TYPE;
    }

    public static class mViewhoder extends RecyclerView.ViewHolder
    {
        TextView app_name;
        ImageView flag,limit;

        public mViewhoder(View itemView) {
            super(itemView);
            app_name=itemView.findViewById(R.id.region_title);
             limit=itemView.findViewById(R.id.region_limit);
             flag=itemView.findViewById(R.id.country_flag);
        }
    }

    public interface RegionListAdapterInterface {
        void onCountrySelected(Country item);
    }
    public void setData(List<Country> servers) {
        datalist.clear();
        datalist.addAll(servers);
        notifyDataSetChanged();
    }
}
