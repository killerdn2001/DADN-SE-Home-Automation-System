package com.example.btl;

public class LightRec {
    private int id_light;
    private long id_rec;
    private boolean status;

    public int getId_light() {
        return id_light;
    }

    public void setId_light(int id_light) {
        this.id_light = id_light;
    }

    public long getId_rec() {
        return id_rec;
    }

    public void setId_rec(long id_rec) {
        this.id_rec = id_rec;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LightRec() {
    }

    public LightRec(int id_light, long id_rec, boolean status) {
        this.id_light = id_light;
        this.id_rec = id_rec;
        this.status = status;
    }
}
