package com.example.david.rawr.Models;

/**
 * Created by david on 21/05/2015.
 */
public class FriendRequest {
    private String petUsername, petType, petRace, petGender, petBirthday, ownerName, ownerLastName, petName, ownerUsername;
    private String petPicture, ownerPicture;

    public FriendRequest(String petUsername, String petType, String petRace, String petGender, String petBirthday, String ownerName, String ownerLastName, String petName, String ownerUsername, String petPicture, String ownerPicture) {
        this.petUsername = petUsername;
        this.petType = petType;
        this.petRace = petRace;
        this.petGender = petGender;
        this.petBirthday = petBirthday;
        this.ownerName = ownerName;
        this.ownerLastName = ownerLastName;
        this.petName = petName;
        this.ownerUsername = ownerUsername;
        this.petPicture = petPicture;
        this.ownerPicture = ownerPicture;
    }

    public String getPetUsername() {
        return petUsername;
    }

    public String getPetType() {
        return petType;
    }

    public String getPetRace() {
        return petRace;
    }

    public String getPetGender() {
        return petGender;
    }

    public String getPetBirthday() {
        return petBirthday;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public String getPetName() {
        return petName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getPetPicture() {
        return petPicture;
    }

    public String getOwnerPicture() {
        return ownerPicture;
    }
}
