package com.muslimguide.androidapp.recever;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.SplashScreenActivity;
import com.muslimguide.androidapp.homePageFragments.PrayerFragment;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Alarm extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private String fajr, dhuhr, asr, magrib, isha,sehri,iftar;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        // current time and SharedPreferences value in below
        String time = dateFormat.format(System.currentTimeMillis());
        long currTime = System.currentTimeMillis();
        long fatst = currTime+(1000*60*15);
        String time2 = dateFormat.format(fatst);
        sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        sehri = sharedPreferences.getString("Sehri","");
        iftar = sharedPreferences.getString("Iftar","");
        String json = sharedPreferences.getString("Set", "");
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> arrayData = gson.fromJson(json, type);
        for (int i = 0; i < arrayData.size(); i++) {
            fajr = arrayData.get(0);
            dhuhr = arrayData.get(1);
            asr = arrayData.get(2);
            magrib = arrayData.get(3);
            isha = arrayData.get(4);
        }
        if (time.contains(fajr)) {
            String text = context.getString(R.string.text);
            String pTimeName = context.getString(R.string.fajr);
            notification(context, pTimeName+" "+text+" "+ fajr);
        } else if (time.contains(dhuhr)) {
            String text = context.getString(R.string.text);
            String pTimeName = context.getString(R.string.dhuhr);
            notification(context, pTimeName+" "+text+" "+dhuhr);
        } else if (time.contains(asr)) {
            String text = context.getString(R.string.text);
            String pTimeName = context.getString(R.string.asr);
            notification(context, pTimeName+" "+text+" "+asr);
        } else if (time.contains(magrib)) {
            String text = context.getString(R.string.text);
            String pTimeName = context.getString(R.string.magrib);
            notification(context, pTimeName+" "+text+" "+magrib);
        } else if (time.contains(isha)) {
            String text = context.getString(R.string.text);
            String pTimeName = context.getString(R.string.isha);
            notification(context, pTimeName+" "+text+" "+isha);
        }
        if (time2.contains(sehri) && !sehri.isEmpty()){
            String text = context.getString(R.string.text2);
            String pTimeName = context.getString(R.string.sehri);
            notification(context, pTimeName+" "+text+" "+sehri+" "+context.getString(R.string.remaining_text));
        }else if (time2.contains(iftar) && !iftar.isEmpty()){
            String text = context.getString(R.string.text);
            String pTimeName = context.getString(R.string.iftar);
            notification(context, pTimeName+" "+text+" "+iftar+" "+context.getString(R.string.remaining_text));
        }
         //Toast.makeText(context, time+" = "+asr, Toast.LENGTH_SHORT).show();
        //Log.d("KKKKKKKKKKK", String.valueOf(time2+" = "+iftar));

        wl.release();
    }

    private void notification(Context context, String notiTitle) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder mBuilder = new Builder(context, "notify_001");
        Intent inggg = new Intent(context.getApplicationContext(), SplashScreenActivity.class);
        inggg.putExtra("newLink", notiTitle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, inggg, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setColor(context.getResources()
                .getColor(R.color.notification_bg));
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.png48);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle(notiTitle);
        mBuilder.setSound(defaultSoundUri, AudioManager.STREAM_NOTIFICATION);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setWhen(System.currentTimeMillis());
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23) {
            mBuilder.setColor(context.getResources()
                    .getColor(R.color.notification_bg));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "notify_001";
            NotificationChannel channel = new NotificationChannel(channelId, "muslimpro", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        }

        mNotificationManager.notify(0, mBuilder.build());
    }


    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}