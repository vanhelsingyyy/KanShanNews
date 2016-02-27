package me.vanhely.kanshannews.model;

import java.io.Serializable;
import java.util.List;

import me.vanhely.kanshannews.model.bean.TopStories;

public class TopStoriesData implements Serializable{

    public List<TopStories> top_stories;

    public List<TopStories> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStories> top_stories) {
        this.top_stories = top_stories;
    }
}
