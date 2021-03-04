package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.HolidayItem;

import java.util.ArrayList;

public class RecyclerHolidayAdapter extends RecyclerView.Adapter<RecyclerHolidayAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<HolidayItem> holidayItems;

    public RecyclerHolidayAdapter(Context mContext, ArrayList<HolidayItem> holidayItems) {
        this.mContext = mContext;
        this.holidayItems = holidayItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.holiday_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HolidayItem holidayItem=holidayItems.get(i);
        String hName=holidayItem.gethName();
        String aDate=holidayItem.getaDate();
        String eDate=holidayItem.geteDate();

        viewHolder.txtHoldayName.setText(hName);
        viewHolder.txtHolidayADate.setText(aDate);
        viewHolder.txtHolidayEDate.setText(eDate);


    }

    @Override
    public int getItemCount() {
        if (holidayItems.size()!=0)
            return holidayItems.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHoldayName,txtHolidayADate,txtHolidayEDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHoldayName=itemView.findViewById(R.id.txt_holiday_name);
            txtHolidayADate=itemView.findViewById(R.id.txt_holiday_date);
            txtHolidayEDate=itemView.findViewById(R.id.txt_holiday_eng_date);
        }
    }
}
