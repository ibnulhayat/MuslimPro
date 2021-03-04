package com.muslimguide.androidapp.models;

public class MakkaLive {

    private String name, url, alt_url, image, alt_image, apk, yitid, id;

    public MakkaLive(String name, String url, String alt_url, String image, String alt_image, String apk, String yitid, String id) {
        this.name = name;
        this.url = url;
        this.alt_url = alt_url;
        this.image = image;
        this.alt_image = alt_image;
        this.apk = apk;
        this.yitid = yitid;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt_url() {
        return alt_url;
    }

    public void setAlt_url(String alt_url) {
        this.alt_url = alt_url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAlt_image() {
        return alt_image;
    }

    public void setAlt_image(String alt_image) {
        this.alt_image = alt_image;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getYitid() {
        return yitid;
    }

    public void setYitid(String yitid) {
        this.yitid = yitid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
