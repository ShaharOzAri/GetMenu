package com.example.getmenu.Model;

public class Student {
    public String avatarUrl;
    public int id;
    public String name;
    public int phone;
    public String address;
    public Boolean cb;

    public Student(String avatarUrl, int id, String name, int phone, String address, Boolean cb) {
        this.avatarUrl = avatarUrl;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.cb = cb;
    }


}
