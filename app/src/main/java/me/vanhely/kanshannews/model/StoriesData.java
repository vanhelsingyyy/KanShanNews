package me.vanhely.kanshannews.model;

import java.io.Serializable;
import java.util.List;

import me.vanhely.kanshannews.model.bean.Stories;


public class StoriesData implements Serializable {

    private String date;

    private List<Stories> stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public String getDate() {
        return date;
    }

    public List<Stories> getStories() {
        return stories;
    }

}
