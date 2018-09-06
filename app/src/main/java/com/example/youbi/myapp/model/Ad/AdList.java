
package com.example.youbi.myapp.model.Ad;

import java.util.ArrayList;

import com.example.youbi.myapp.model.Ad.Ad;
import com.example.youbi.myapp.model.Ad.Metadata;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdList {

    @SerializedName("ads")
    @Expose
    private ArrayList<Ad> ads = null;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;


    public ArrayList<Ad> getAd() {
        return ads;
    }

    public void setAds(ArrayList<Ad> ads) {
        this.ads = ads;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
