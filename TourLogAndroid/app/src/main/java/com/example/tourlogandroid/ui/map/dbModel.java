package com.example.tourlogandroid.ui.map;

public class dbModel {
    private String name;
    private String info;
    private String image;
    private boolean fav;

    public dbModel(String name, String info, String image, boolean fav){
        this.name = name;
        this.info = info;
        this.image = image;
        this.fav = fav;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getImage() {
        return image;
    }

    public boolean isFav() {
        return fav;
    }
}
