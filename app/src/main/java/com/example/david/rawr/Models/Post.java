package com.example.david.rawr.Models;

import android.util.Log;

/**
 * Created by david on 24/04/2015.
 */
public class Post {
    private String petUsername, text, date, photo;

    public Post(String petUsername, String text, String date, String photo) {
        if(petUsername.compareTo("null") != 0) {
            this.petUsername = petUsername;
        }else{
            this.petUsername = "unknown";

        }
        this.text = text;
        if (date.compareTo("null") != 0) {
            this.date = date;
        }else{
            this.date = "unknown";
        }
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }
}
