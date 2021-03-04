package com.muslimguide.androidapp.models;

public class SavedItem {
    String mDate;
    String zikir_name;

    public SavedItem(String mDate, String zikir_name) {
        this.mDate = mDate;
        this.zikir_name = zikir_name;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getZikir_name() {
        return zikir_name;
    }

    public void setZikir_name(String zikir_name) {
        this.zikir_name = zikir_name;
    }
}
