package me.vanhely.kanshannews.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.List;


public class ThemeListData {

    private List<OthersEntity> others;

    public void setOthers(List<OthersEntity> others) {
        this.others = others;
    }

    public List<OthersEntity> getOthers() {
        return others;
    }

    public static class OthersEntity {

        public int color;
        @Expose
        @SerializedName("thumbnail")
        public String themeImage;
        @Expose
        @SerializedName("description")
        public String themeDesc;
        @Expose
        @SerializedName("id")
        public int themeId;
        @Expose
        @SerializedName("name")
        public String themeName;

    }


}
