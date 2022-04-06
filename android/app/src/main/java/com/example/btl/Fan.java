package com.example.btl;

public class Fan {
    private int id,id_location,status;

    public Fan(){}

    public Fan(int id, int id_location, int status) {
        this.id = id;
        this.id_location = id_location;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_location() {
        return id_location;
    }

    public void setId_location(int id_location) {
        this.id_location = id_location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
