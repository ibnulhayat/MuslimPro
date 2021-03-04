package com.muslimguide.androidapp.recever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AutoStart extends BroadcastReceiver {
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.setAlarm(context);
            Toast.makeText(context, "ACTION_BOOT_COMPLETED", Toast.LENGTH_SHORT).show();
        }
    }
}