package me.vanhely.kanshannews.model;

import java.io.Serializable;

/**
 * Created by LinMax on 2016/2/29.
 */
public class ExtraData implements Serializable{

    private int long_comments;
    private int popularity;
    private int normal_comments;
    private int comments;
    private int short_comments;

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setNormal_comments(int normal_comments) {
        this.normal_comments = normal_comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getLong_comments() {
        return long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getNormal_comments() {
        return normal_comments;
    }

    public int getComments() {
        return comments;
    }

    public int getShort_comments() {
        return short_comments;
    }

    @Override
    public String toString() {
        return "ExtraData{" +
                "long_comments=" + long_comments +
                ", popularity=" + popularity +
                ", normal_comments=" + normal_comments +
                ", comments=" + comments +
                ", short_comments=" + short_comments +
                '}';
    }


}
