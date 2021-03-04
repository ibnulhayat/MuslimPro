package com.muslimguide.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.IslamicCalendar;
import android.icu.util.TimeZone;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.common.Common;
import com.muslimguide.androidapp.helper.NoInternet;
import com.muslimguide.androidapp.models.Ramadan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SplashScreenActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private List<String> prayerTimeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String mDate,mDate2,share_date;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private Gson gson = new Gson();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private SimpleDateFormat onlyDay = new SimpleDateFormat("EEEE", Locale.getDefault());
    private AlertDialog appControlDialog;
    private ImageView ivWifi,ivMobileData,ivCancel;
    private WifiManager wifi;
    private String city;
    private Runnable runnable;
    private String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Common.ramadanList  = new ArrayList<>();
        //startActivity();
        settings = getSharedPreferences(IslamicProHelper.PREFS_NAME, Context.MODE_PRIVATE);
        city = settings.getString(IslamicProHelper.USER_CITY, "");

        if (city.equals("")) {
            Common.permission = "request";
            startActivity(new Intent(SplashScreenActivity.this, LocationPermissionActivity.class));
            finish();
        }

        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        /* for Alarm */
//        String alarmOn = sharedPreferences.getString("AlarmOn", "");
//        if (alarmOn.isEmpty()){
//            editor.putString("AlarmOn", "on");
//            editor.commit();
//        }
        mRequestQueue = Volley.newRequestQueue(this);
        mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        mDate2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());



         latitude = settings.getString(IslamicProHelper.USER_LAT, "");
         longitude = settings.getString(IslamicProHelper.USER_LNG, "");

        if (latitude.isEmpty()&& longitude.isEmpty()){
            Common.ramadanList.add(new Ramadan("0","0","0","0","0","0"));
        }

        wifi = (WifiManager) SplashScreenActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (NoInternet.isConnected(SplashScreenActivity.this)){
            isEmptycheck();
        }
        else {
            showAlert();
        }

    }

    private void isEmptycheck(){
        String json = sharedPreferences.getString("Set", "");
        if (json.isEmpty()) {
            prayerTimeParsing(mDate, city);
        }else {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> arrayData = gson.fromJson(json, type);
            for (int i = 0; i < arrayData.size(); i++) {
                share_date = arrayData.get(5);
            }
            if (!mDate2.equals(share_date)){
                prayerTimeParsing(mDate, city);
            }
        }
    }

    private void startActivity() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000);

        startActivity(new Intent(SplashScreenActivity.this, HomePage.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convert_date(String raw_date) {

        Date final_date = null,final_date2 = null;
        String isMOnthName = null;
        String isdayth = null;
        try {
            final_date = new SimpleDateFormat("yyyy-MM-dd").parse(raw_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(final_date);
        int eYear = calendar.get(Calendar.YEAR);
        int eMonth = calendar.get(Calendar.MONTH);
        int eday =calendar.get(Calendar.DAY_OF_MONTH);
        String new_date = String.valueOf(eYear+"-"+eMonth+"-"+eday);
        try {
            final_date2 = new SimpleDateFormat("yyyy-MM-dd").parse(new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        IslamicCalendar calendar1 = new IslamicCalendar();
        calendar1.setTimeZone(TimeZone.getDefault());
        calendar1.setTime(final_date2);
        int isYear = calendar1.get(IslamicCalendar.YEAR);
        int isMontth = calendar1.get(IslamicCalendar.MONTH)+1;
        int isDay = calendar1.get(IslamicCalendar.DAY_OF_MONTH);


        Log.d("ISLAMICDATE", String.valueOf(isDay));
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

    private void prayerTimeParsing(final String mDate, final String locat) {
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

                            }
                        }
                    }
                    prayerTimeList.add(fajr);
                    prayerTimeList.add(dhuhr);
                    prayerTimeList.add(asr);
                    prayerTimeList.add(maghrib);
                    prayerTimeList.add(isha);
                    prayerTimeList.add(hijri_date);
                    prayerTimeList.add(city);
                    String json = gson.toJson(prayerTimeList);
                    editor.putString("Set", json);
                    editor.commit();


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d("PRAYER_JSON", e.getMessage());
                }
                ramajanJson(latitude, longitude);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("PRAYER_VOLLEY_ERROR", error.toString());
            }
        });
        mRequestQueue.add(request);
    }

    private void ramajanJson(final String lat, final String longi) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Apis.ramadan + lat + "&long=" + longi, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String toDaySehri = null, toDayIftar = null;
                String currDate = null, hijriDate = null, hijriDay = null, engDay = null;
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        if (object.has("timings")) {
                            JSONObject object1 = object.getJSONObject("timings");
                            toDaySehri = object1.getString("Imsak");
                            if (toDaySehri.length() > 0) {
                                toDaySehri = toDaySehri.substring(0, toDaySehri.length() - 5);
                                Date _24Hourfor1 = _24HourSDF.parse(toDaySehri);
                                toDaySehri = _12HourSDF.format(_24Hourfor1);
                            }
                            toDayIftar = object1.getString("Maghrib");
                            if (toDayIftar.length() > 0) {
                                toDayIftar = toDayIftar.substring(0, toDayIftar.length() - 5);
                                Date _24Hourfor1 = _24HourSDF.parse(toDayIftar);
                                toDayIftar = _12HourSDF.format(_24Hourfor1);
                            }

                        }
                        if (object.has("date")) {
                            JSONObject objectDate = object.getJSONObject("date");
                            String engDate = objectDate.getString("readable");
                            String returnDate = ramadanDate(engDate);
                            String[] dddd = returnDate.split("/");
                            currDate = dddd[0];
                            engDay = dddd[1];

                            if (objectDate.has("hijri")) {
                                JSONObject hijriObject = objectDate.getJSONObject("hijri");
                                hijriDate = hijriObject.getString("date");
                                hijriDay = hijriObject.getString("day");
                                if (hijriDay.equals("01")) {
                                    hijriDay = "1" + "st";
                                } else if (hijriDay.equals("02")) {
                                    hijriDay = "2" + "nd";
                                } else if (hijriDay.equals("03")) {
                                    hijriDay = "3" + "rd";
                                } else if (hijriDay.equals("04")) {
                                    hijriDay = "4" + "th";
                                } else if (hijriDay.equals("05")) {
                                    hijriDay = "5" + "th";
                                } else if (hijriDay.equals("06")) {
                                    hijriDay = "6" + "th";
                                } else if (hijriDay.equals("07")) {
                                    hijriDay = "7" + "th";
                                } else if (hijriDay.equals("08")) {
                                    hijriDay = "8" + "th";
                                } else if (hijriDay.equals("09")) {
                                    hijriDay = "9" + "th";
                                } else {
                                    hijriDay = hijriDay + "th";
                                }
                            }
                        }
                        Common.ramadanList.add(new Ramadan(currDate, hijriDate, hijriDay, toDaySehri, toDayIftar, engDay));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                startActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("RAMADAN_VOLLEY_ERROR", error.getMessage());
            }
        });
        mRequestQueue.add(request);

    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }


    private void showAlert(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View controlDialogView = factory.inflate(R.layout.internet_checker_dialog, null);
        appControlDialog = new AlertDialog.Builder(this).create();
        appControlDialog.setView(controlDialogView);

        ivWifi=controlDialogView.findViewById(R.id.ivWifi);
        ivMobileData =controlDialogView.findViewById(R.id.ivMobileData);
        ivCancel =controlDialogView.findViewById(R.id.ivCancel);

        ivWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.setWifiEnabled(true);
                appControlDialog.dismiss();
                try {
                    Handler handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(this,5000);
                            if (NoInternet.isConnected(SplashScreenActivity.this)){
                                Refresh.refreshActivity(SplashScreenActivity.this);
                                handler.removeCallbacks(runnable);
                            }
                           // Toast.makeText(SplashScreenActivity.this, "Tttt", Toast.LENGTH_SHORT).show();
                        }
                    };
                    handler.postDelayed(runnable,0);

                    Toast.makeText(SplashScreenActivity.this, "WiFi Enabling in 5sec Please Wait", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("WIFI_ERROR",e.getMessage());
                    throw new RuntimeException(e);


                }
            }
        });

        ivMobileData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoInternet.isConnected(SplashScreenActivity.this) || wifi.isWifiEnabled()) {

                    isEmptycheck();
                } else {
                    Intent dataSettings = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    startActivity(dataSettings);
                    appControlDialog.dismiss();
                }
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appControlDialog.dismiss();
            }
        });
        appControlDialog.show();
    }

    private  String ramadanDate(String raw_date) {

        Date final_date = null,final_date2 = null;
        try {
            final_date = new SimpleDateFormat("dd MMM yyyy").parse(raw_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(final_date);
        int eYear = calendar.get(Calendar.YEAR);
        int eMonth = calendar.get(Calendar.MONTH)+1;
        int eday =calendar.get(Calendar.DAY_OF_MONTH)+1;
        String new_date = String.valueOf(eYear+" "+eMonth+" "+eday);
        try {
            final_date2 = new SimpleDateFormat("yyyy MM dd").parse(new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = new SimpleDateFormat("dd MMM yyyy/EEEE").format(final_date2);


        return date;
    }

}
