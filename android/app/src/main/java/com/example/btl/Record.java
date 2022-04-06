package com.example.btl;

public class Record {
    private long id;
    private int id_sensor,id_device,light, temp,level;
    private String status, time,email,type;
    private boolean auto;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getId_sensor() {
        return id_sensor;
    }

    public void setId_sensor(int id_sensor) {
        this.id_sensor = id_sensor;
    }

    public int getId_device() {
        return id_device;
    }

    public void setId_device(int id_device) {
        this.id_device = id_device;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public Record(long id, int id_sensor, int id_device, int light, int temp, int level, String status, String time, String email, String type, boolean auto) {
        this.id = id;
        this.id_sensor = id_sensor;
        this.id_device = id_device;
        this.light = light;
        this.temp = temp;
        this.level = level;
        this.status = status;
        this.time = time;
        this.email = email;
        this.type = type;
        this.auto = auto;
    }

    public Record() {
    }
}
