package me.vanhely.kanshannews.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/2/14 0014.
 */
public class ThemeLog extends DataSupport{

    private String themeImage;
    private String themeDesc;
    private int themeId;
    private String themeName;

    public String getThemeImage() {
        return themeImage;
    }

    public void setThemeImage(String themeImage) {
        this.themeImage = themeImage;
    }

    public String getThemeDesc() {
        return themeDesc;
    }

    public void setThemeDesc(String themeDesc) {
        this.themeDesc = themeDesc;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}
