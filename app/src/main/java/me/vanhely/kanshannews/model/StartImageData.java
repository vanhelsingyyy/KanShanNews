package me.vanhely.kanshannews.model;

import com.google.gson.annotations.Expose;

/**
 * 启动图片的Data
 */
public class StartImageData {

    @Expose
    private String text;
    @Expose
    private String img;

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return The img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img The img
     */
    public void setImg(String img) {
        this.img = img;
    }


}
