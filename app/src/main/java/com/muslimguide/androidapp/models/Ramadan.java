package com.muslimguide.androidapp.models;

public class Ramadan {
    String engDate;
    String hijriDate;
    String hijriDay;
    String sehriTime;
    String iftarTime;
    String engday;

    public Ramadan(String engDate, String hijriDate, String hijriDay, String sehriTime, String iftarTime, String engday) {
        this.engDate = engDate;
        this.hijriDate = hijriDate;
        this.hijriDay = hijriDay;
        this.sehriTime = sehriTime;
        this.iftarTime = iftarTime;
        this.engday = engday;
    }

    public String getEngDate() {
        return engDate;
    }

    public void setEngDate(String engDate) {
        this.engDate = engDate;
    }

    public String getHijriDate() {
        return hijriDate;
    }

    public void setHijriDate(String hijriDate) {
        this.hijriDate = hijriDate;
    }

    public String getHijriDay() {
        return hijriDay;
    }

    public void setHijriDay(String hijriDay) {
        this.hijriDay = hijriDay;
    }

    public String getSehriTime() {
        return sehriTime;
    }

    public void setSehriTime(String sehriTime) {
        this.sehriTime = sehriTime;
    }

    public String getIftarTime() {
        return iftarTime;
    }

    public void setIftarTime(String iftarTime) {
        this.iftarTime = iftarTime;
    }

    public String getEngday() {
        return engday;
    }

    public void setEngday(String engday) {
        this.engday = engday;
    }
}
