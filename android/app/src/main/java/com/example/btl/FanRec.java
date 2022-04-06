package com.example.btl;

public class FanRec {
    private int id_fan,status;
    private long id_rec;

    public FanRec() {
    }

    public FanRec(int id_fan, long id_rec, int status) {
        this.id_fan = id_fan;
        this.id_rec = id_rec;
        this.status = status;
    }

    public int getId_fan() {
        return id_fan;
    }

    public void setId_fan(int id_fan) {
        this.id_fan = id_fan;
    }

    public long getId_rec() {
        return id_rec;
    }

    public void setId_rec(long id_rec) {
        this.id_rec = id_rec;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
