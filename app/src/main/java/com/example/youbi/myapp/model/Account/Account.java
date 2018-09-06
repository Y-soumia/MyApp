package com.example.youbi.myapp.model.Account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Account {

    @SerializedName("account")
    @Expose
    private Account account;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("region")
    @Expose
    private Integer region;
    @SerializedName("companyAd")
    @Expose
    private Integer companyAd;
    @SerializedName("phoneHidden")
    @Expose
    private Integer phoneHidden;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("uuId")
    @Expose
    private String uuId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("accountType")
    @Expose
    private Integer accountType;

    /*public Account(String name, String email, String password, Integer accountType, String lang, Integer phoneHidden
            , String phone, Integer region){
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.lang = lang;
        this.phoneHidden = phoneHidden;
        this.phone = phone;
        this.region = region;
    }
    public Account(String email, String password){
        this.email = email;
        this.password = password;
    }*/
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getCompanyAd() {
        return companyAd;
    }

    public void setCompanyAd(Integer companyAd) {
        this.companyAd = companyAd;
    }

    public Integer getPhoneHidden() {
        return phoneHidden;
    }

    public void setPhoneHidden(Integer phoneHidden) {
        this.phoneHidden = phoneHidden;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
