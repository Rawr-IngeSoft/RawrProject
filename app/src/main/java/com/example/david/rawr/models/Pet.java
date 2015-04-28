package com.example.david.rawr.models;

/**
 * Created by david on 27/04/2015.
 */
public class Pet {

    private String idPet, petName, petType, petBirthday, petPictureUri;

    public Pet(String idPet, String petName, String petType, String petBirthday, String petPictureUri) {
        this.idPet = idPet;
        this.petName = petName;
        this.petType = petType;
        this.petBirthday = petBirthday;
        this.petPictureUri = petPictureUri;
    }

    public String getPetPictureUri() {
        return petPictureUri;
    }

    public String getIdPet() {
        return idPet;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetType() {
        return petType;
    }

    public String getPetBirthday() {
        return petBirthday;
    }
}
