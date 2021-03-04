package com.muslimguide.androidapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.IslamicCalendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.IslamicProHelper;
import com.muslimguide.androidapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrayerTimeFragment extends Fragment {
    private ImageView ivPreviousDay, ivNextDay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String formattedDate, jsonDate;
    private int count = 0;
    private TextView tvDate;
    private RequestQueue mRequestQueue;
    private TextView  tvPTFajr,tvPTDhuhr,tvPTAsr,tvPTMagrib,tvPTIsha;
    private ConstraintLayout constraintLayout4;
    private LinearLayout layoutPraTime;
    private ProgressBar progressBar;
    public PrayerTimeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prayer_time, container, false);

        constraintLayout4 = view.findViewById(R.id.constraintLayout4);
        layoutPraTime = view.findViewById(R.id.layoutPraTime);

        ivPreviousDay = view.findViewById(R.id.ivPreviousDay);
        ivNextDay = view.findViewById(R.id.ivNextDay);
        tvDate = view.findViewById(R.id.tvDate);


        tvPTFajr = view.findViewById(R.id.tvPTFajr);
        tvPTDhuhr = view.findViewById(R.id.tvPTDhuhr);
        tvPTAsr = view.findViewById(R.id.tvPTAsr);
        tvPTMagrib = view.findViewById(R.id.tvPTMagrib);
        tvPTIsha = view.findViewById(R.id.tvPTIsha);


        progressBar = view.findViewById(R.id.progressBar);



        calendar = Calendar.getInstance();
        formattedDate = dateFormat.format(calendar.getTime());
        jsonDate = jsonDateFormat.format(calendar.getTime());
        //tvDate.setText(formattedDate + "\n" + convert_date(jsonDate));

        mRequestQueue = Volley.newRequestQueue(getActivity());
        SharedPreferences settings = getActivity().getSharedPreferences(IslamicProHelper.PREFS_NAME, Context.MODE_PRIVATE);
        final String city = settings.getString(IslamicProHelper.USER_CITY, "");
        prayerTimeParsing(formattedDate, city);

        ivPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > -15) {
                    count--;
                    calendar.add(Calendar.DATE, -1);
                    formattedDate = dateFormat.format(calendar.getTime());
                    prayerTimeParsing(formattedDate, city);
                }
            }
        });
        ivNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < 15) {
                    count++;
                    calendar.add(Calendar.DATE, 1);
                    formattedDate = dateFormat.format(calendar.getTime());
                    prayerTimeParsing(formattedDate, city);
                }

            }
        });

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convert_date(String raw_date) {

//        Chronology iso = ISOChronology.getInstanceUTC();
//        Chronology hijri = IslamicChronology.getInstance();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(calendar.getTime());
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DATE);
//        LocalDate todayIso = new LocalDate(year, month, day, iso);
//        LocalDate hijriDate = new LocalDate(todayIso.toDateTimeAtStartOfDay(), hijri);

        Date final_date = null;
        String isMOnthName = null;
        String isdayth = null;
        try {
            final_date = new SimpleDateFormat("yyyy-MM-dd").parse(raw_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        IslamicCalendar calendar1 = new IslamicCalendar();
        calendar1.setTimeZone(TimeZone.getDefault());
        calendar1.setTime(final_date);
        int isYear = calendar1.get(IslamicCalendar.YEAR);
        int isMontth = calendar1.get(IslamicCalendar.MONTH);
        int isDay = calendar1.get(IslamicCalendar.DAY_OF_MONTH);

       // Log.d("DAY_MONTH_YEAR", String.valueOf(isYear + "+" + isMontth + "+" + isDay));

        if (isMontth == 0) {
            isMOnthName = "Muharram";
        } else if (isMontth == 1) {
            isMOnthName = "Safar";
        } else if (isMontth == 2) {
            isMOnthName = "Rabi-ul-Awwal";
        } else if (isMontth == 3) {
            isMOnthName = "Rabi-ul-Aakhir";
        } else if (isMontth == 4) {
            isMOnthName = "Jamadi-ul-Awwal";
        } else if (isMontth == 5) {
            isMOnthName = "Jamadi-ul-Aakhir";
        } else if (isMontth == 6) {
            isMOnthName = "Rajab";
        } else if (isMontth == 7) {
            isMOnthName = "Shaban";
        } else if (isMontth == 8) {
            isMOnthName = "Ramadan";
        } else if (isMontth == 9) {
            isMOnthName = "Shawwal";
        } else if (isMontth == 10) {
            isMOnthName = " Zulqaida";
        } else if (isMontth == 11) {
            isMOnthName = "Zulhijja";
        }
        if (isDay == 1) {
            isdayth = "st ";
        } else if (isDay == 2) {
            isdayth = "nd ";
        } else if (isDay == 3) {
            isdayth = "rd ";
        } else if (3 < isDay && isDay < 31) {
            isdayth = "th ";
        }
        return String.valueOf(isDay + isdayth + isMOnthName + ", " + isYear);
    }

    private void prayerTimeParsing(final String mDate, String locat) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, Apis.prayerTime + mDate + "&city=" + locat, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    String j_date = null;
                    String hijri_date = null;
                    String fajr = null;
                    String dhuhr = null;
                    String asr = null;
                    String maghrib = null;
                    String isha = null;
                    JSONObject object = new JSONObject(response);
                    String city = object.getString("city");
                    if (object.has("items")) {
                        JSONArray jsonArray = object.getJSONArray("items");
                        if (jsonArray.length() > 0) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject arrayObject = jsonArray.getJSONObject(j);
                                j_date = arrayObject.getString("date_for");
                                hijri_date = convert_date(j_date);
                                fajr = arrayObject.getString("fajr");
                                dhuhr = arrayObject.getString("dhuhr");
                                asr = arrayObject.getString("asr");
                                maghrib = arrayObject.getString("maghrib");
                                isha = arrayObject.getString("isha");
                                tvDate.setText(mDate + "\n" + hijri_date);
                            }
                        }
                    }

                    String ddd = new SimpleDateFormat("yyyy-M-d").format(Calendar.getInstance().getTime());

                    tvPTFajr.setText(fajr);
                    tvPTDhuhr.setText(dhuhr);
                    tvPTAsr.setText(asr);
                    tvPTMagrib.setText(maghrib);
                    tvPTIsha.setText(isha);
                    constraintLayout4.setVisibility(View.VISIBLE);
                    layoutPraTime.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
//                    if (ddd.equals(j_date)){
//                        prayerTimeList.add(fajr);
//                        prayerTimeList.add(dhuhr);
//                        prayerTimeList.add(asr);
//                        prayerTimeList.add(maghrib);
//                        prayerTimeList.add(isha);
//                        prayerTimeList.add(hijri_date);
//                        prayerTimeList.add(city);
//                        Gson gson = new Gson();
//                        String json = gson.toJson(prayerTimeList);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("Set", json);
//                        editor.commit();
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("PRAYER_JSON", e.getMessage());
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PRAYER_VOLLEY_ERROR", error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
        mRequestQueue.add(request);
    }

}
