package com.muslimguide.androidapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

public class Refresh {
    public static void finishActivity(Activity activity){
        activity.finish();
    }
    public static void finishAppCompatActivity(AppCompatActivity appCompatActivity){
        appCompatActivity.finish();
    }
    public static void refreshActivity(Activity activity){
        activity.recreate();
    }
    public static void refreshAppCompatActivity(AppCompatActivity appCompatActivity){
        appCompatActivity.recreate();
    }
}
