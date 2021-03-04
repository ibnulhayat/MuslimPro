package com.muslimguide.androidapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.muslimguide.androidapp.recever.Alarm;

public class YourService extends Service {
    Alarm alarm = new Alarm();
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        alarm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}