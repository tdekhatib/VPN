package com.openvpn.vpn;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CPUApplications_Scanning extends RecyclerView.Adapter<CPUApplications_Scanning.MyViewHolder> {


    /// Get List of Apps Causing Junk Files

    public List<Apps> apps;

    public CPUApplications_Scanning(List<Apps> getapps) {
        apps = getapps;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_cpu_apps, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Apps app = apps.get(position);
        holder.size.setText("");
        holder.image.setImageDrawable(app.getImage());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView size;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            size = (TextView) view.findViewById(R.id.apptext);
            image = (ImageView) view.findViewById(R.id.appimage);

        }
    }
}
