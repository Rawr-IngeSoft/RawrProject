package com.example.david.rawr.Models;

/**
 * Created by david on 11/05/2015.
 */
public class Friend {

    private String petUsername, petName;
    private boolean connected = false;

    public Friend(){};

    public Friend(String petUsername, String petName) {
        this.petUsername = petUsername;
        this.petName = petName;
    }

    public String getPetUsername() {
        return petUsername;
    }

    public void setPetUsername(String petUsername) {
        this.petUsername = petUsername;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
