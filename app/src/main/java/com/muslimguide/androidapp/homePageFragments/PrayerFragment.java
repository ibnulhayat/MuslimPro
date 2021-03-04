package com.muslimguide.androidapp.homePageFragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muslimguide.androidapp.HomePage;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.common.Common;
import com.muslimguide.androidapp.fragments.RamadanFragment;
import com.muslimguide.androidapp.models.Ramadan;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.Looper.getMainLooper;

public class PrayerFragment extends Fragment {

    private View mView;
    public static LinearLayout layoutPrayerTime, layoutRamadan;
    public static ConstraintLayout layoutDropdown, layoutCurrentTime;
    public static ImageView ivRotatedDropdown;
    private TextView tvFajr, tvDhuhr, tvAsr, tvMagrib, tvIsha, tvCurrentLocation, tvHijriDate;
    private ImageView ivFajrAlarm, ivDhuhrAlarm, ivAsrAlarm, ivMagribAlarm, ivIshaAlarm, ivNextPrayerIcon, ivDropNextPrayerIcon;
    private SharedPreferences sharedPreferences;
    private String mTime, mDate, mTime24, date;
    private TextView tvCurrentTime, tvCurrentDate, tvDropTime, tvTimeName, tvDropTimeName;
    private SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private double cutime;
    private Gson gson = new Gson();
    private TextView tvNumOfDay, tvEnglishDate, tvNameOfDay, tvRamadanSTime, tvRamadanITime;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    Calendar calendar = Calendar.getInstance();
    private String EVENT_DATE_TIME = "2019-05-06 23:59:59";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private Handler handler = new Handler();
    private Runnable runnable;
    private SharedPreferences.Editor editor;
    public String Sehri, Iftar;

    public PrayerFragment() {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_prayer, container, false);

        // Toolbar visible
        Common.toolbarVisibility("gone", "", 0);

        sharedPreferences = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // Alarm setting on and off
        String fajr = sharedPreferences.getString("fajr", "");
        String dhur = sharedPreferences.getString("dhur", "");
        String asr = sharedPreferences.getString("asr", "");
        String magrib = sharedPreferences.getString("magrib", "");
        String isha = sharedPreferences.getString("isha", "");
        Sehri = sharedPreferences.getString("Sehri", "");
        Iftar = sharedPreferences.getString("Iftar", "");
        if (fajr.isEmpty() && dhur.isEmpty() && asr.isEmpty() && magrib.isEmpty() && isha.isEmpty()) {

            editor.putString("fajr", "on");
            editor.putString("dhur", "on");
            editor.putString("asr", "on");
            editor.putString("magrib", "on");
            editor.putString("isha", "on");
            editor.commit();
        }

        layoutPrayerTime = mView.findViewById(R.id.layoutPrayerTime);
        layoutDropdown = mView.findViewById(R.id.layoutDropdown);
        layoutCurrentTime = mView.findViewById(R.id.layoutCurrentTime);
        ivRotatedDropdown = mView.findViewById(R.id.ivRotatedDropdown);
        ivNextPrayerIcon = mView.findViewById(R.id.ivNextPrayerIcon);
        ivDropNextPrayerIcon = mView.findViewById(R.id.ivDropNextPrayerIcon);

        HomePage.ivDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.dropDownMenu("open");

                String json = sharedPreferences.getString("Set", "");
                if (json.isEmpty()) {
                    tvFajr.setText(R.string.textEmpty);
                    tvDhuhr.setText(R.string.textEmpty);
                    tvAsr.setText(R.string.textEmpty);
                    tvMagrib.setText(R.string.textEmpty);
                    tvIsha.setText(R.string.textEmpty);
                } else {
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrayData = gson.fromJson(json, type);
                    for (int i = 0; i < arrayData.size(); i++) {
                        tvFajr.setText(arrayData.get(0));
                        tvDhuhr.setText(arrayData.get(1));
                        tvAsr.setText(arrayData.get(2));
                        tvMagrib.setText(arrayData.get(3));
                        tvIsha.setText(arrayData.get(4));
                    }
                    alarOnOffShow();
                }
            }
        });
        ivRotatedDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.dropDownMenu("close");
            }
        });


        tvFajr = mView.findViewById(R.id.tvFajr);
        tvDhuhr = mView.findViewById(R.id.tvDhuhr);
        tvAsr = mView.findViewById(R.id.tvAsr);
        tvMagrib = mView.findViewById(R.id.tvMagrib);
        tvIsha = mView.findViewById(R.id.tvIsha);
        tvCurrentLocation = mView.findViewById(R.id.tvCurrentLocation);
        tvHijriDate = mView.findViewById(R.id.tvHijriDate);
        tvCurrentTime = mView.findViewById(R.id.tvCurrentTime);
        tvCurrentDate = mView.findViewById(R.id.tvCurrentDate);
        tvDropTime = mView.findViewById(R.id.tvDropTime);
        tvTimeName = mView.findViewById(R.id.tvTimeName);
        tvDropTimeName = mView.findViewById(R.id.tvDropTimeName);
        // alarm image view
        ivFajrAlarm = mView.findViewById(R.id.ivFajrAlarm);
        ivDhuhrAlarm = mView.findViewById(R.id.ivDhuhrAlarm);
        ivAsrAlarm = mView.findViewById(R.id.ivAsrAlarm);
        ivMagribAlarm = mView.findViewById(R.id.ivMagribAlarm);
        ivIshaAlarm = mView.findViewById(R.id.ivIshaAlarm);

        // iftar and Sehri time Text View

        tvNumOfDay = mView.findViewById(R.id.tvNumOfDay);
        tvEnglishDate = mView.findViewById(R.id.tvEnglishDate);
        tvNameOfDay = mView.findViewById(R.id.tvNameOfDay);
        tvRamadanSTime = mView.findViewById(R.id.tvRamadanSTime);
        layoutRamadan = mView.findViewById(R.id.layoutRamadan);
        tvRamadanITime = mView.findViewById(R.id.tvRamadanITime);

        // ===== Ramadan Countdown Start =====//
        linear_layout_2 = mView.findViewById(R.id.linear_layout_2);
        tv_days = mView.findViewById(R.id.tv_days);
        tv_hour = mView.findViewById(R.id.tv_hour);
        tv_minute = mView.findViewById(R.id.tv_minute);
        tv_second = mView.findViewById(R.id.tv_second);
        countDownStart(EVENT_DATE_TIME);


        //=====Ramadan On click======
        PrayerFragment.layoutRamadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drawer.closeDrawers();
                mChangeFragment_2(new RamadanFragment());
                Common.toolbarVisibility("visible", "", 2);
            }
        });

        //calendar.add(Calendar.DATE, 1);
        date = dateFormat.format(calendar.getTime());


        if (Common.ramadanList.size() > 0) {
            for (int j = 0; j < Common.ramadanList.size(); j++) {

                String jDate = Common.ramadanList.get(j).getEngDate();
                if (date.equals(jDate)) {
                    tvRamadanSTime.setText(Common.ramadanList.get(j).getSehriTime());
                    tvRamadanITime.setText(Common.ramadanList.get(j).getIftarTime());
                    tvNumOfDay.setText(Common.ramadanList.get(j).getHijriDay() + " Ramadan");
                    tvEnglishDate.setText(Common.ramadanList.get(j).getEngDate());
                    tvNameOfDay.setText(Common.ramadanList.get(j).getEngday());
                    editor.putString("Sehri", String.valueOf(Common.ramadanList.get(j).getSehriTime()));
                    editor.putString("Iftar", String.valueOf(Common.ramadanList.get(j).getIftarTime()));
                    editor.commit();
                    layoutRamadan.setVisibility(View.VISIBLE);
                } else if (date.equals("0")) {
                    layoutRamadan.setVisibility(View.GONE);
                }
            }
        }

        // alarm set on click lissener

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        ivFajrAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String al_fajr = sharedPreferences.getString("fajr", "");
                if (al_fajr.equals("on")) {
                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fajr", "off");
                    editor.commit();
                    ivFajrAlarm.setImageResource(R.drawable.alarm_off);
                } else {
                    ivFajrAlarm.setImageResource(R.drawable.alarm_on);
                    editor.putString("fajr", "on");
                    editor.commit();
                }

            }
        });
        ivDhuhrAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dhur = sharedPreferences.getString("dhur", "");
                if (dhur.equals("on")) {
                    editor.putString("dhur", "off");
                    editor.commit();
                    ivDhuhrAlarm.setImageResource(R.drawable.alarm_off);
                } else {
                    ivDhuhrAlarm.setImageResource(R.drawable.alarm_on);
                    editor.putString("dhur", "on");
                    editor.commit();
                }

            }
        });
        ivAsrAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asr = sharedPreferences.getString("asr", "");
                if (asr.equals("on")) {
                    editor.putString("asr", "off");
                    editor.commit();
                    ivAsrAlarm.setImageResource(R.drawable.alarm_off);
                } else {
                    ivAsrAlarm.setImageResource(R.drawable.alarm_on);
                    editor.putString("asr", "on");
                    editor.commit();
                }

            }
        });
        ivMagribAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String magrib = sharedPreferences.getString("magrib", "");
                if (magrib.equals("on")) {
                    editor.putString("magrib", "off");
                    editor.commit();
                    ivMagribAlarm.setImageResource(R.drawable.alarm_off);
                } else {
                    ivMagribAlarm.setImageResource(R.drawable.alarm_on);
                    editor.putString("magrib", "on");
                    editor.commit();
                }

            }
        });
        ivIshaAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isha = sharedPreferences.getString("isha", "");
                if (isha.equals("on")) {
                    editor.putString("isha", "off");
                    editor.commit();
                    ivIshaAlarm.setImageResource(R.drawable.alarm_off);
                } else {
                    ivIshaAlarm.setImageResource(R.drawable.alarm_on);
                    editor.putString("isha", "on");
                    editor.commit();
                }

            }
        });

        // Hijridate and Location set
        String json = sharedPreferences.getString("Set", "");
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> arrayData = gson.fromJson(json, type);
        assert json != null;
        if (!json.isEmpty()) {
            assert arrayData != null;
            for (int i = 0; i < arrayData.size(); i++) {
                tvHijriDate.setText(arrayData.get(5));
                tvCurrentLocation.setText(arrayData.get(6));
            }
        }
        // set date
        mDate = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        tvCurrentDate.setText(mDate);

        // set Background image homePage and Menu
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());
                mTime24 = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
                cutime = Double.parseDouble(mTime24.replaceAll(":", "."));
                bgSet(cutime);
                //tvCurrentTimes.setText(""+mTime);
                someHandler.postDelayed(this, 1000);
            }
        }, 10);


        return mView;

    }


    private void countDownStart(final String time) {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(time);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));
                    } else {
                        layoutRamadan.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public void mChangeFragment_2(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void alarOnOffShow() {
        final String fajr = sharedPreferences.getString("fajr", "");
        final String dhur = sharedPreferences.getString("dhur", "");
        final String asr = sharedPreferences.getString("asr", "");
        final String magrib = sharedPreferences.getString("magrib", "");
        final String isha = sharedPreferences.getString("isha", "");
        if (fajr.equals("on")) {
            ivFajrAlarm.setImageResource(R.drawable.alarm_on);
        } else {
            ivFajrAlarm.setImageResource(R.drawable.alarm_off);
        }
        if (dhur.equals("on")) {
            ivDhuhrAlarm.setImageResource(R.drawable.alarm_on);
        } else {
            ivDhuhrAlarm.setImageResource(R.drawable.alarm_off);
        }
        if (asr.equals("on")) {
            ivAsrAlarm.setImageResource(R.drawable.alarm_on);
        } else {
            ivAsrAlarm.setImageResource(R.drawable.alarm_off);
        }
        if (magrib.equals("on")) {
            ivMagribAlarm.setImageResource(R.drawable.alarm_on);
        } else {
            ivMagribAlarm.setImageResource(R.drawable.alarm_off);
        }
        if (isha.equals("on")) {
            ivIshaAlarm.setImageResource(R.drawable.alarm_on);
        } else {
            ivIshaAlarm.setImageResource(R.drawable.alarm_off);
        }
    }

    private void bgSet(double pTime) {
        try {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("Set", "");
            if (json.isEmpty()) {

            } else {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                List<String> arrayData = gson.fromJson(json, type);
                for (int i = 0; i < arrayData.size() - 2; i++) {
                    Date _12Hourfor1 = _12HourSDF.parse(arrayData.get(0));
                    Date _12Hourfor2 = _12HourSDF.parse(arrayData.get(1));
                    Date _12Hourfor3 = _12HourSDF.parse(arrayData.get(2));
                    Date _12Hourfor4 = _12HourSDF.parse(arrayData.get(3));
                    Date _12Hourfor5 = _12HourSDF.parse(arrayData.get(4));
                    String time1 = _24HourSDF.format(_12Hourfor1);
                    String time2 = _24HourSDF.format(_12Hourfor2);
                    String time3 = _24HourSDF.format(_12Hourfor3);
                    String time4 = _24HourSDF.format(_12Hourfor4);
                    String time5 = _24HourSDF.format(_12Hourfor5);
                    double fajr = Double.parseDouble(time1.replaceAll(":", "."));
                    double dhu = Double.parseDouble(time2.replaceAll(":", "."));
                    double asr = Double.parseDouble(time3.replaceAll(":", "."));
                    double mag = Double.parseDouble(time4.replaceAll(":", "."));
                    double ish = Double.parseDouble(time5.replaceAll(":", "."));

                    if (fajr <= pTime && pTime < dhu) {
                        HomePage.mainBacground.setBackgroundResource(R.drawable.fazr);
                        HomePage.nav_view.setBackgroundResource(R.drawable.fazr);

                        ivNextPrayerIcon.setImageResource(R.drawable.dhuhr_icon);
                        ivDropNextPrayerIcon.setImageResource(R.drawable.dhuhr_icon);
                        tvTimeName.setText("Next Prayer Dhuhr");
                        tvDropTimeName.setText("Next Prayer Dhuhr");
                        tvCurrentTime.setText(arrayData.get(1));
                        tvDropTime.setText(arrayData.get(1));
                    } else if (dhu <= pTime && pTime < asr) {
                        HomePage.mainBacground.setBackgroundResource(R.drawable.dhuhr);
                        HomePage.nav_view.setBackgroundResource(R.drawable.dhuhr);

                        ivNextPrayerIcon.setImageResource(R.drawable.asr_icon);
                        ivDropNextPrayerIcon.setImageResource(R.drawable.asr_icon);
                        tvTimeName.setText("Next Prayer Asr");
                        tvDropTimeName.setText("Next Prayer Asr");
                        tvCurrentTime.setText(arrayData.get(2));
                        tvDropTime.setText(arrayData.get(2));
                    } else if (asr <= pTime && pTime < mag) {
                        HomePage.mainBacground.setBackgroundResource(R.drawable.asr);
                        HomePage.nav_view.setBackgroundResource(R.drawable.asr);

                        ivNextPrayerIcon.setImageResource(R.drawable.magrib_icon);
                        ivDropNextPrayerIcon.setImageResource(R.drawable.magrib_icon);
                        tvTimeName.setText("Next Prayer Magrib");
                        tvDropTimeName.setText("Next Prayer Magrib");
                        tvCurrentTime.setText(arrayData.get(3));
                        tvDropTime.setText(arrayData.get(3));
                    } else if (mag <= pTime && pTime < ish) {
                        HomePage.mainBacground.setBackgroundResource(R.drawable.magrib);
                        HomePage.nav_view.setBackgroundResource(R.drawable.magrib);

                        ivNextPrayerIcon.setImageResource(R.drawable.isha_icon);
                        ivDropNextPrayerIcon.setImageResource(R.drawable.isha_icon);
                        tvTimeName.setText("Next Prayer Isha");
                        tvDropTimeName.setText("Next Prayer Isha");
                        tvCurrentTime.setText(arrayData.get(4));
                        tvDropTime.setText(arrayData.get(4));
                    } else if (ish <= pTime && pTime < 23.59 || 00.00 <= pTime && pTime < fajr) {
                        HomePage.mainBacground.setBackgroundResource(R.drawable.isha);
                        HomePage.nav_view.setBackgroundResource(R.drawable.isha);

                        ivNextPrayerIcon.setImageResource(R.drawable.fazr_icon);
                        ivDropNextPrayerIcon.setImageResource(R.drawable.fazr_icon);
                        tvTimeName.setText("Next Prayer Fajr");
                        tvDropTimeName.setText("Next Prayer Fajr");
                        tvCurrentTime.setText(arrayData.get(0));
                        tvDropTime.setText(arrayData.get(0));
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
