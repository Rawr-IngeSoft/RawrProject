package com.example.david.rawr.Models;

/**
 * Created by david on 24/04/2015.
 */
public class Post {
    private String petUsername, text, date;

    public Post(String petUsername, String text, String date) {
        if(petUsername != null) {
            this.petUsername = petUsername;
        }else{
            this.petUsername = "uknown";
        }
        this.text = text;
        if (date != null) {
            this.date = date;
        }else{
            this.date = "uknown";
        }
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
