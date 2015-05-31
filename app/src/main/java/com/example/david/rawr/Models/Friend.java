package com.example.david.rawr.Models;

/**
 * Created by david on 11/05/2015.
 */
public class Friend implements Comparable{

    private String petUsername, petName, profilePicture;
    private boolean connected = false;
    private int priority = 0;
    public Friend(){};

    public Friend(String petUsername, String petName, String profilePicture) {
        this.petUsername = petUsername;
        this.petName = petName;
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
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
        if (connected){
            priority = 1;
        }else{
            priority = 0;
        }
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Object another) {
        Friend friend = (Friend) another;
        if (friend.getPriority() >= priority){
            return 1;
        }else{
            return -1;
        }
    }
}
