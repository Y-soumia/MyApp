
package com.example.youbi.myapp.model.Ad;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageForInsert {

    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("default")
    @Expose
    private Integer _default;
    @SerializedName("order")
    @Expose
    private Integer order;

    public String getFile() { return file; }

    public void setFile(String file) { this.file = file; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDefault() { return _default; }

    public void setDefault(Integer _default) { this._default = _default; }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
