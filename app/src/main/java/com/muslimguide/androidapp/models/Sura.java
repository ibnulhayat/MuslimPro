package com.muslimguide.androidapp.models;

public class Sura {
    private int number,numberOfAyahs;
    private String name,englishName,englishNameTranslation,revelationType;

    public Sura(int number, int numberOfAyahs, String name, String englishName, String englishNameTranslation, String revelationType) {
        this.number = number;
        this.numberOfAyahs = numberOfAyahs;
        this.name = name;
        this.englishName = englishName;
        this.englishNameTranslation = englishNameTranslation;
        this.revelationType = revelationType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfAyahs() {
        return numberOfAyahs;
    }

    public void setNumberOfAyahs(int numberOfAyahs) {
        this.numberOfAyahs = numberOfAyahs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getEnglishNameTranslation() {
        return englishNameTranslation;
    }

    public void setEnglishNameTranslation(String englishNameTranslation) {
        this.englishNameTranslation = englishNameTranslation;
    }

    public String getRevelationType() {
        return revelationType;
    }

    public void setRevelationType(String revelationType) {
        this.revelationType = revelationType;
    }
}
