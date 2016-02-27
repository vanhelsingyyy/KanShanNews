package me.vanhely.kanshannews.model;


import java.io.Serializable;
import java.util.List;

import me.vanhely.kanshannews.model.bean.Stories;
import me.vanhely.kanshannews.model.bean.TopStories;

public class LatestData {

    private String date;

    private List<Stories> stories;

    private List<TopStories> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public void setTop_stories(List<TopStories> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public List<TopStories> getTop_stories() {
        return top_stories;
    }

}
