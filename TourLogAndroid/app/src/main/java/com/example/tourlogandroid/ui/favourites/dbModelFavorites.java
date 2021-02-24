package com.example.tourlogandroid.ui.favourites;

public class dbModelFavorites {
    private String name;
    private String address;

    private dbModelFavorites(){}

    private dbModelFavorites(String name,String address){
        this.name = name;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
