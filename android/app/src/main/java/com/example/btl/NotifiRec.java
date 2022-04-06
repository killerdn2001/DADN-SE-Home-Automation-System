package com.example.btl;

public class NotifiRec {
    int id_room,status;
    String type,time;

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public NotifiRec() {
    }

    public NotifiRec(int id_room, int status, String type, String time) {
        this.id_room = id_room;
        this.status = status;
        this.type = type;
        this.time = time;
    }
}
