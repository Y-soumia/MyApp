package com.example.youbi.myapp.model.Ad;

import com.example.youbi.myapp.model.Ad.Ad;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by youbi on 18/07/2018.
 */

public class Ad_view {
    @SerializedName("ad")
    @Expose
    private Ad ad = null;


    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

/* public static class MetadataDeserializer implements JsonDeserializer<Metadata> {

        @Override
        public Metadata[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json instanceof JsonArray) {
                return new Gson().fromJson(json,Metadata[].class);
            }
            Metadata child = context.deserialize(json,Metadata.class);
            return new Metadata[] { child};
        }
    }*/
}
