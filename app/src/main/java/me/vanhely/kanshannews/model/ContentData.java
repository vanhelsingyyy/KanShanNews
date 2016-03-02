package me.vanhely.kanshannews.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LinMax on 2016/2/28.
 */
public class ContentData implements Serializable {

    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private int id;
    private List<?> js;
    private List<String> css;
    public ExtraData extraData = new ExtraData();


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<?> getJs() {
        return js;
    }

    public void setJs(List<?> js) {
        this.js = js;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }



}
