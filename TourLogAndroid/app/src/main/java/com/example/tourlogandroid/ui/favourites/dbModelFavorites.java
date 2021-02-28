package com.example.tourlogandroid.ui.favourites;

public class dbModelFavorites {
    private String name;
    private String address;
    private String iD;

    public dbModelFavorites(){}

    public dbModelFavorites(String name, String address, String iD){
        this.name = name;
        this.address = address;
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
