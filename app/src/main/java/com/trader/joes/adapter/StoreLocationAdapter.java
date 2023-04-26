package com.trader.joes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.trader.joes.R;
import com.trader.joes.model.Store;

import java.util.List;

/**
 * Adapter for Store location recycler view
 */
public class StoreLocationAdapter extends RecyclerView.Adapter<StoreLocationAdapter.ViewHolder> {
    private List<Store> storeLocations;

    public StoreLocationAdapter(List<Store> storeLocations) {
        this.storeLocations = storeLocations;
    }

    public void updateStoreList(List<Store> storeLocations) {
        this.storeLocations = storeLocations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item,
                parent, false);
        return new StoreLocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Store store = storeLocations.get(position);

        //Download location image:
        if(store.getLocationimage() != null & !store.getLocationimage().trim().equals("")) {
            Picasso.get().load(store.getLocationimage()).into(holder.mLocationImg);
        }
        holder.mLocationName.setText(store.getName());
        holder.mLocationDistance.setText(store.get_distance() +" "+ store.get_distanceuom());
        holder.mLocationAddr1.setText(store.getAddress1());
        holder.mLocationAddr2.setText(store.getCity() + ", " + store.getState() + ", " + store.getPostalcode());
        holder.mLocationHours.setText(store.getHours());
    }

    @Override
    public int getItemCount() {
        return storeLocations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mLocationImg;
        public TextView mLocationName;
        public TextView mLocationDistance;
        public TextView mLocationAddr1;
        public TextView mLocationAddr2;
        public TextView mLocationHours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mLocationImg = itemView.findViewById(R.id.location_img_holder);
            mLocationName = itemView.findViewById(R.id.location_name);
            mLocationDistance = itemView.findViewById(R.id.location_distance);
            mLocationAddr1 = itemView.findViewById(R.id.location_address1);
            mLocationAddr2 = itemView.findViewById(R.id.location_address2);
            mLocationHours = itemView.findViewById(R.id.location_hours);
        }

    }
}
