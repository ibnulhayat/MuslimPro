package com.muslimguide.androidapp.models;

public class NinetyNine_Names {

    String arabicName, englishName, number, enMeaning;

    public NinetyNine_Names(String arabicName, String englishName, String number, String enMeaning) {
        this.arabicName = arabicName;
        this.englishName = englishName;
        this.number = number;
        this.enMeaning = enMeaning;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEnMeaning() {
        return enMeaning;
    }

    public void setEnMeaning(String enMeaning) {
        this.enMeaning = enMeaning;
    }
}
