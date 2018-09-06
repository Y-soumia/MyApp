package com.example.youbi.myapp.model.Account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Token {

    @SerializedName("x-token")
    @Expose
    private String xToken;
    @SerializedName("tokenType")
    @Expose
    private String tokenType;
    @SerializedName("expireIn")
    @Expose
    private Integer expireIn;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    public String getXToken() {
        return xToken;
    }

    public void setXToken(String xToken) {
        this.xToken = xToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Integer expireIn) {
        this.expireIn = expireIn;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

}