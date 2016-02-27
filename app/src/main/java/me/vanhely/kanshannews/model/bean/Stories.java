package me.vanhely.kanshannews.model.bean;

import java.io.Serializable;
import java.util.List;


public class Stories implements Serializable {

    private int type;
    private int id;
    private String title;
    public boolean isBanner = false;
    private List<String> images;

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getImages() {
        return images;
    }

}
