package com.example.david.rawr.models;

import android.widget.Button;

/**
 * Created by Fabián Merchán on 20/04/2015.
 */
public class Post {

    private String buttonName;

    public Post(){}

    public Post(String buttonName){
        this.buttonName = buttonName;
    }

    public String getNombreBoton() {
        return buttonName;
    }

    public void setNombreBoton(String buttonName) {
        this.buttonName = buttonName;
    }
}
