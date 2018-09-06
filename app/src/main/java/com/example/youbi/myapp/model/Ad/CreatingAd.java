package com.example.youbi.myapp.model.Ad;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CreatingAd {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("region")
    @Expose
    private Integer region;
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("accountType")
    @Expose
    private Integer accountType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("phoneHidden")
    @Expose
    private Integer phoneHidden;
    @SerializedName("images")
    @Expose
    private ArrayList<ImageForInsert> images = null;

    public String getName() {
        return name;
    }

    public CreatingAd(){
    }
    public CreatingAd(String name,String phone,int region,int category, String subject, String body,int accountType,
            int phoneHidden){
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.category = category;
        this.subject = subject;
        this.body = body;
        this.lang = "fr";
        this.accountType = accountType;
        this.phoneHidden = phoneHidden;
        this.type = "s";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPhoneHidden() {
        return phoneHidden;
    }

    public void setPhoneHidden(Integer phoneHidden) {
        this.phoneHidden = phoneHidden;
    }

    public ArrayList<ImageForInsert> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageForInsert> images) {
        this.images = images;
    }


}