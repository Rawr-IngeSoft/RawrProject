package com.example.david.rawr.Models;

/**
 * Created by Alejandro on 10/02/2015.
 */
public class Owner {
    private int picture;
    private String name;
    private String lastname;

    public Owner() {

    }

    public Owner(int picture, String name, String lastname) {
        this.picture = picture;
        this.name = name;
        this.lastname = lastname;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
