
package com.example.youbi.myapp.model.Ad;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ad {

    @SerializedName("ad")
    @Expose
    private Integer adId;
    @SerializedName("listId")
    @Expose
    private Integer listId;
    @SerializedName("region")
    @Expose
    private Integer region;
    @SerializedName("city")
    @Expose
    private Integer city;
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("accountType")
    @Expose
    private Integer accountType;
    @SerializedName("phoneHidden")
    @Expose
    private Integer phoneHidden;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("images")
    @Expose
    private ArrayList<Image> images = null;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("params")
    @Expose
    private List<Param> params = null;
    @SerializedName("saved")
    @Expose
    private Integer saved;
    @SerializedName("uuId")
    @Expose
    private String uuId;

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getPhoneHidden() {
        return phoneHidden;
    }

    public void setPhoneHidden(Integer phoneHidden) {
        this.phoneHidden = phoneHidden;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public Integer getSaved() {
        return saved;
    }

    public void setSaved(Integer saved) {
        this.saved = saved;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

}
