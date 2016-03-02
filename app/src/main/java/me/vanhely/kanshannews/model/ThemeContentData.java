package me.vanhely.kanshannews.model;

import java.util.List;


public class ThemeContentData {

    private String description;
    private String background;
    private String name;
    private String image;
    private String image_source;

    private List<StoriesEntity> stories;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public String getBackground() {
        return background;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getImage_source() {
        return image_source;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public static class StoriesEntity {
        private int type;
        private int id;
        private String title;
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

}
