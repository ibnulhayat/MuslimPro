package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.Ramadan;

import java.util.List;

public class RamadanAdapter extends RecyclerView.Adapter<RamadanAdapter.ViewHolder> {
    private Context mContext;
    private List<Ramadan> ramadanList;

    public RamadanAdapter(Context mContext, List<Ramadan> ramadanList) {
        this.mContext = mContext;
        this.ramadanList = ramadanList;
    }

    @NonNull
    @Override
    public RamadanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.ramadan_item_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RamadanAdapter.ViewHolder viewHolder, int i) {
        Ramadan ramadan = ramadanList.get(i);
        String engDate = ramadan.getEngDate();
        String dayName = ramadan.getEngday();
        String sehriTime = ramadan.getSehriTime();
        String iftarTime = ramadan.getIftarTime();
        String numberOfDay = ramadan.getHijriDay();
        viewHolder.tvNumberOfDay.setText(numberOfDay+" Ramadan");
        viewHolder.tvEnglishDate.setText(engDate);
        viewHolder.tvNameOfDay.setText(dayName);
        viewHolder.tvRamadanSTime.setText(sehriTime);
        viewHolder.tvRamadanITime.setText(iftarTime);
    }

    @Override
    public int getItemCount() {
        return ramadanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNumberOfDay,tvEnglishDate,tvNameOfDay,tvRamadanSTime,tvRamadanITime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumberOfDay = itemView.findViewById(R.id.tvNumOfDay);
            tvEnglishDate = itemView.findViewById(R.id.tvEnglishDate);
            tvNameOfDay = itemView.findViewById(R.id.tvNameOfDay);
            tvRamadanSTime = itemView.findViewById(R.id.tvRamadanSTime);
            tvRamadanITime = itemView.findViewById(R.id.tvRamadanITime);
        }
    }
}
