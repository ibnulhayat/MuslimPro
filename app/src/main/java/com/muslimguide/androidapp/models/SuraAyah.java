package com.muslimguide.androidapp.models;

public class SuraAyah {
    private int number;
    private String arabicText,engText;
    private int numberInSurah,juz,manzil,page,ruke,hizbQuarter;
    private boolean sajda;
    private String ayahAudioUrl;

    public SuraAyah(int number, String arabicText, String engText, int numberInSurah, int juz, int manzil, int page, int ruke, int hizbQuarter, boolean sajda,String ayahAudioUrl) {
        this.number = number;
        this.arabicText = arabicText;
        this.engText = engText;
        this.numberInSurah = numberInSurah;
        this.juz = juz;
        this.manzil = manzil;
        this.page = page;
        this.ruke = ruke;
        this.hizbQuarter = hizbQuarter;
        this.sajda = sajda;
        this.ayahAudioUrl = ayahAudioUrl;
    }

    public String getArabicText() {
        return arabicText;
    }

    public String getAyahAudioUrl() {
        return ayahAudioUrl;
    }

    public void setAyahAudioUrl(String ayahAudioUrl) {
        this.ayahAudioUrl = ayahAudioUrl;
    }

    public void setArabicText(String arabicText) {
        this.arabicText = arabicText;
    }

    public String getEngText() {
        return engText;
    }

    public void setEngText(String engText) {
        this.engText = engText;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }



    public int getNumberInSurah() {
        return numberInSurah;
    }

    public void setNumberInSurah(int numberInSurah) {
        this.numberInSurah = numberInSurah;
    }

    public int getJuz() {
        return juz;
    }

    public void setJuz(int juz) {
        this.juz = juz;
    }

    public int getManzil() {
        return manzil;
    }

    public void setManzil(int manzil) {
        this.manzil = manzil;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRuke() {
        return ruke;
    }

    public void setRuke(int ruke) {
        this.ruke = ruke;
    }

    public int getHizbQuarter() {
        return hizbQuarter;
    }

    public void setHizbQuarter(int hizbQuarter) {
        this.hizbQuarter = hizbQuarter;
    }

    public boolean isSajda() {
        return sajda;
    }

    public void setSajda(boolean sajda) {
        this.sajda = sajda;
    }
}
