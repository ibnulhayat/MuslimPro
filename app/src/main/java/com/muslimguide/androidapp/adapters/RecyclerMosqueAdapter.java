package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.NearestMostItem;

import java.util.ArrayList;

public class RecyclerMosqueAdapter extends RecyclerView.Adapter<RecyclerMosqueAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(NearestMostItem item, int pos);
    }

    private Context mContext;
    private ArrayList<NearestMostItem> nearestMostItems;
    private final OnItemClickListener listener;

    public RecyclerMosqueAdapter(Context mContext, ArrayList<NearestMostItem> nearestMostItems, OnItemClickListener listener) {
        this.mContext = mContext;
        this.nearestMostItems = nearestMostItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.nearest_mosque_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NearestMostItem mostItem=nearestMostItems.get(i);
        String Name=mostItem.getPlaceName();
        String distance=mostItem.getPlaceDistance();
        String icon=mostItem.getImage_link();

        viewHolder.txt_nearest_mosque_name.setText(Name);
        viewHolder.txt_nearest_mosque_distance.setText(distance);

        viewHolder.img_mosque.setImageResource(R.drawable.nearby_mosque);
        viewHolder.bind(nearestMostItems.get(i), listener);




    }

    @Override
    public int getItemCount() {
        return nearestMostItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nearest_mosque_name,txt_nearest_mosque_distance;

        ImageView img_mosque;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nearest_mosque_name=itemView.findViewById(R.id.txt_nearest_mosque_name);
            txt_nearest_mosque_distance=itemView.findViewById(R.id.txt_nearest_mosque_distance);
            img_mosque=itemView.findViewById(R.id.img_mosque);

        }

        public void bind(final NearestMostItem item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item,getAdapterPosition());
                }
            });
        }
    }
}
