package com.muslimguide.androidapp.models;

public class HolidayItem {
    private String hName;
    private String aDate;
    private String eDate;

    public HolidayItem(String hName, String aDate, String eDate) {
        this.hName = hName;
        this.aDate = aDate;
        this.eDate = eDate;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }
}
