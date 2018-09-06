package com.example.youbi.myapp.model.Account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccStatus {
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("accountType")
    @Expose
    private Integer accountType;
    @SerializedName("accountStatus")
    @Expose
    private String accountStatus;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

}
