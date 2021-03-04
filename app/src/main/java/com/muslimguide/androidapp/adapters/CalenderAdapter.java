package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.IslamicCalendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.muslimguide.androidapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.ViewHolder> {
    private int totalDay;
    private Context context;
    private int monthNo;
    private Calendar calendar;
    private IslamicCalendar islamicCalendar;
    private Calendar mCalender = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CalenderAdapter(Context context, Calendar eCalender) {
        this.context = context;
        this.calendar = eCalender;
        this.monthNo = englishDayCalculate(calendar);
        this.totalDay = calendar.getActualMaximum(5) + this.monthNo;
        this.islamicCalendar = new IslamicCalendar();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_calendar_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (i >= this.monthNo) {
            StringBuilder stringBuilder;

            stringBuilder = new StringBuilder();
            stringBuilder.append(calendar.get(5));
            stringBuilder.append("");
            viewHolder.tvEnglishDate.setText(stringBuilder.toString());
            viewHolder.tvEnglishDate.setTextColor(context.getResources().getColor(R.color.colorBlack));
            islamicCalendar.setTimeZone(TimeZone.getDefault());
            int month = calendar.get(2);
            Log.e("LLLLLLLLLLLLL", String.valueOf(month));
            String d = String.valueOf(calendar.get(1)+" "+month+" "+calendar.get(5));
            Date final_date = null;
            try {
                final_date = new SimpleDateFormat("yyyy MM dd").parse(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            islamicCalendar.setTime(final_date);
            int hijridate = islamicCalendar.get(IslamicCalendar.DAY_OF_MONTH);

            viewHolder.tvHijriDate.setText(String.valueOf(hijridate));
            viewHolder.tvHijriDate.setTextColor(context.getResources().getColor(R.color.sample_primary));
            if (calendar.get(5) == mCalender.get(5) && calendar.get(2) == mCalender.get(2) && calendar.get(1) == mCalender.get(1)) {
                viewHolder.tvHijriDate.setBackgroundResource(R.drawable.calender_selector);
                viewHolder.tvHijriDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
                viewHolder.tvHijriDate.setTypeface(null,Typeface.BOLD);

            }
            viewHolder.itemView.setBackgroundResource(R.drawable.main_bg);
//            if (f17363e != false) {
//                if (this.f11699d.get(Integer.valueOf(this.f11696a.f17361c.get(5))) != 0) {
//                    viewHolder.f11693a.setBackground(this.f11701f);
//                    viewHolder.f11693a.setTextColor(C0378b.getColor(this.f11696a.activity, C0799R.color.white));
//                }
//            } else if (C1224g.m3379a(this.f11696a.activity, this.f11696a.f17362d.get(2), this.f11696a.f17362d.get(5)) != 0) {
//                viewHolder.f11693a.setBackground(this.f11701f);
//                viewHolder.f11693a.setTextColor(C0378b.getColor(this.f11696a.activity, C0799R.color.white));
//            }
            calendar.add(5, 1);
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, ""+i +" = "+viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
//
//                    if (i == viewHolder.getAdapterPosition()){
//                        viewHolder.tvEnglishDate.setBackgroundResource(R.drawable.calender_selector);
//                        viewHolder.tvEnglishDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
//
//                    }else {
//                        viewHolder.tvEnglishDate.setBackgroundColor(Color.TRANSPARENT);
//                        viewHolder.tvEnglishDate.setTextColor(Color.TRANSPARENT);
//                    }
//                }
//            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int englishDayCalculate(Calendar calendar) {
        switch (calendar.get(7)) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            default:
                return 0;

        }
    }


    @Override
    public int getItemCount() {
        return this.totalDay;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEnglishDate;
        TextView tvHijriDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvEnglishDate = itemView.findViewById(R.id.txt_day_above);
            this.tvHijriDate = itemView.findViewById(R.id.txt_day_below);
        }
    }
}
