package com.medicapp.medicappprojectcomp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.medicapp.medicappprojectcomp.R;
import com.medicapp.medicappprojectcomp.models.PositionMap;

import java.util.List;

public class AdapterPointMap  extends  RecyclerView.Adapter<AdapterPointMap.ViewHolder> {

    private List<PositionMap> dataSet;
    Context mContext;

    int resourcesId;

    public AdapterPointMap(List<PositionMap> data) {
        this.dataSet = data;
    }

    public void setList(List<PositionMap> data) {
        this.dataSet = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_point_map, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textTitle.setText(dataSet.get(position).getTitle());
        holder.textAddress.setText(dataSet.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView textTitle;
         TextView textAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textNamePoint);
            textAddress = itemView.findViewById(R.id.textAddressPoint);
        }
    }

}
