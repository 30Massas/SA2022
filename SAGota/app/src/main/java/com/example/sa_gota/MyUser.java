package com.example.sa_gota;

public class MyUser {

    public String email;
    public String name;
    public String course;
    public String display;

    public MyUser(String email, String name, String course, String display){
        this.email = email;
        this.name = name;
        this.course = course;
        this.display = display;
    }

    public MyUser(){

    }

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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
