package com.example.david.rawr.models;

/**
 * Created by david on 24/04/2015.
 */
public class Post {
    private String petUsername, text, date;

    public Post(String petUsername, String text, String date) {
        this.petUsername = petUsername;
        this.text = text;
        this.date = date;
    }

    public String getPetUsername() {
        return petUsername;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
