
package com.example.youbi.myapp.model.Ad;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class Metadata {

    @SerializedName("totalPro")
    @Expose
    private Integer totalPro;
    @SerializedName("totalPrivate")
    @Expose
    private Integer totalPrivate;
    @SerializedName("all")
    @Expose
    private Integer all;
    @SerializedName("lines")
    @Expose
    private Integer lines;


    public Integer getTotalPro() {
        return totalPro;
    }

    public void setTotalPro(Integer totalPro) {
        this.totalPro = totalPro;
    }

    public Integer getTotalPrivate() {
        return totalPrivate;
    }

    public void setTotalPrivate(Integer totalPrivate) {
        this.totalPrivate = totalPrivate;
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Integer getLines() {
        return lines;
    }

    public void setLines(Integer lines) {
        this.lines = lines;
    }

}
