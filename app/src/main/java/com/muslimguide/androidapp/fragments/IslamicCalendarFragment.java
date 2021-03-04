package com.muslimguide.androidapp.fragments;

import android.annotation.TargetApi;
import android.icu.util.IslamicCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.App.AppController;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.adapters.CalenderAdapter;
import com.muslimguide.androidapp.adapters.RecyclerHolidayAdapter;
import com.muslimguide.androidapp.models.HolidayItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class IslamicCalendarFragment extends Fragment  {

    private RecyclerView recyclerViewIslamicHolidays;
    private ArrayList<HolidayItem> holidayItems;
    private RecyclerHolidayAdapter adapter;
    private RecyclerView recyclerView;
    private Calendar calendar;
    private IslamicCalendar islamicCalendar;
    private CalenderAdapter calenderAdapter;
    private ImageView calendar_prev_button,calendar_next_button;
    private TextView calendar_date_display,english_date_display;
    private int month,year,hijriMonth,hijriYear;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM yyyy");
    private String currentMonth,monthName;
    public IslamicCalendarFragment() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_islamic_calendar, container, false);
        holidayItems = new ArrayList<>();
        recyclerViewIslamicHolidays = view.findViewById(R.id.recycler_holiday_list);
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeZone(TimeZone.getDefault());
        this.islamicCalendar = new IslamicCalendar();

        calendar_next_button = view.findViewById(R.id.calendar_next_button);
        calendar_prev_button = view.findViewById(R.id.calendar_prev_button);
        calendar_date_display = view.findViewById(R.id.calendar_date_display);
        recyclerView =  view.findViewById(R.id.calendar_grid);
        english_date_display =  view.findViewById(R.id.english_date_display);

        calendar_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -15);
                calendar.add(Calendar.MONTH,-0);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                currentMonth = dateFormat2.format(calendar.getTime());
                islamicCalendar.setTime(calendar.getTime());
                hijriMonth = islamicCalendar.get(IslamicCalendar.MONTH);
                hijriYear = islamicCalendar.get(IslamicCalendar.YEAR);
                calendar_date_display.setText(currentMonth(hijriMonth)+hijriYear+" AH");
                english_date_display.setText(currentMonth);
                calendarReload(month,year);
            }
        });
        calendar_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 15);
                calendar.add(Calendar.MONTH,1);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                currentMonth = dateFormat2.format(calendar.getTime());
                islamicCalendar.setTime(calendar.getTime());
                hijriMonth = islamicCalendar.get(IslamicCalendar.MONTH);
                hijriYear = islamicCalendar.get(IslamicCalendar.YEAR);
                calendar_date_display.setText(currentMonth(hijriMonth)+hijriYear+" AH");
                english_date_display.setText(currentMonth);
                calendarReload(month,year);

            }
        });

        this.islamicCalendar.setTime(this.calendar.getTime());
        hijriMonth = islamicCalendar.get(IslamicCalendar.MONTH);
        hijriYear = islamicCalendar.get(IslamicCalendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        currentMonth = dateFormat.format(calendar.getTime());
        calendar_date_display.setText(currentMonth(hijriMonth)+hijriYear+" AH");
        english_date_display.setText(currentMonth);
        calendarReload(month,year);
        Log.d("KKKKKKKKKKKK", String.valueOf(month));
        recyclerViewIslamicHolidays.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerHolidayAdapter(getContext(), holidayItems);
        recyclerViewIslamicHolidays.setAdapter(adapter);

        getHolidayData();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void calendarReload(int month, int year){
        this.calendar.set(5,  1);
        this.calendar.set(2, month);
        this.calendar.set(1, year);
        calenderAdapter = new CalenderAdapter(getContext(), (Calendar) this.calendar.clone());
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        this.recyclerView.setAdapter(calenderAdapter);
        recyclerView.setAdapter(calenderAdapter);
    }

    private String currentMonth(int monthNo){
        if (monthNo == 0) {
            monthName = "Muharram ";
        } else if (monthNo == 1) {
            monthName = "Safar ";
        } else if (monthNo == 2) {
            monthName = "Rabi-ul-Awwal ";
        } else if (monthNo == 3) {
            monthName = "Rabi-ul-Aakhir ";
        } else if (monthNo == 4) {
            monthName = "Jamadi-ul-Awwal ";
        } else if (monthNo == 5) {
            monthName = "Jamadi-ul-Aakhir ";
        } else if (monthNo == 6) {
            monthName = "Rajab ";
        } else if (monthNo == 7) {
            monthName = "Shaban ";
        } else if (monthNo == 8) {
            monthName = "Ramadan ";
        } else if (monthNo == 9) {
            monthName = "Shawwal ";
        } else if (monthNo == 10) {
            monthName = " Zulqaida ";
        } else if (monthNo == 11) {
            monthName = "Zulhijja ";
        }
        return monthName;
    }

    private void getHolidayData() {
        holidayItems.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.holyDaysUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray holiday = response.getJSONArray("holiday");
                    for (int i = 0; i < holiday.length(); i++) {
                        JSONObject holidayobject = holiday.getJSONObject(i);
                        String id = holidayobject.getString("id");
                        String hijri_date = holidayobject.getString("hijri_date");
                        String english_date = holidayobject.getString("english_date");
                        String title = holidayobject.getString("title");
                        holidayItems.add(new HolidayItem(title, hijri_date, english_date));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("CATCH_ERROR", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("VOLLEY_ERROR", error.getMessage());
            }
        });
        AppController.getInstance().addToRequest(request);
    }


}