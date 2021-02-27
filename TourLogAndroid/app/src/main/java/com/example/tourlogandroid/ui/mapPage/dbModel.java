package com.example.tourlogandroid.ui.mapPage;

public class dbModel {
    private String name;
    private String info;
    private String image;
    private String address;
    private String iD;
    private boolean fav;

    public dbModel(String name, String info, String image, String address, String iD, boolean fav){
        this.name = name;
        this.info = info;
        this.image = image;
        this.address = address;
        this.iD = iD;
        this.fav = fav;
    }

    public String getiD() {
        return iD;
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

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public String getAddress() {
        return address;
    }
}
