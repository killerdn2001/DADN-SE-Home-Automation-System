package com.example.btl;

public class User {
    private String name,email,phone,password,last_login,time_create;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getTime_create() {
        return time_create;
    }

    public void setTime_create(String time_create) {
        this.time_create = time_create;
    }

    public User(String name, String email, String phone, String password, String last_login, String time_create) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.last_login = last_login;
        this.time_create = time_create;
    }

    public User() {
    }

}
