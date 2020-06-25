package com.inapp.vpn.AdapterWrappers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.hydrasdk.api.data.Country;
import com.anchorfree.hydrasdk.api.response.RemainingTraffic;
import com.inapp.vpn.R;
import com.inapp.vpn.Activities.MainActivity;

import java.util.ArrayList;
import java.util.Locale;

public class ServerListAdapterFree extends RecyclerView.Adapter<ServerListAdapterFree.mViewhoder> {

    ArrayList<Country> datalist;
    private Context context;
    RemainingTraffic remainingTrafficResponse;
    public ServerListAdapterFree(ArrayList<Country> datalist, Context ctx) {
        this.datalist = datalist;
        this.context=ctx;
    }

    @NonNull
    @Override
    public mViewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.server_list_free,parent,false);
        mViewhoder mvh=new mViewhoder(item);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewhoder holder, int position)
    {
        remainingTrafficResponse=new RemainingTraffic();
        Country data=datalist.get(position);
        Locale locale=new Locale("",data.getCountry());
        holder.flag.setImageResource(context.getResources().getIdentifier("drawable/"+data.getCountry().toLowerCase(),null,context.getPackageName()));
        holder.app_name.setText(locale.getDisplayCountry());
        holder.limit.setImageResource(R.drawable.server_signal_3);
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

    @Override
    public int getItemCount() {
        return datalist.size();
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

}
