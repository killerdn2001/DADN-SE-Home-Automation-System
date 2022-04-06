package com.example.btl;

public class Location {
    private int id,light,temp;

    public Location(){}

    public Location(int id, int light, int temp) {
        this.id = id;
        this.light = light;
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
