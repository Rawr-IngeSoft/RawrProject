package com.example.david.rawr.Models;

/**
 * Created by david on 27/04/2015.
 */
public class Pet {

    private String idPet, petName, petType, petBirthday, petPictureUri, petGender;

    public Pet(String petName, String petPictureUri) {
        this.petName = petName;
        this.petPictureUri = petPictureUri;
    }

    public Pet() {
    }

    public Pet(String idPet, String petName, String petType, String petBirthday, String petPictureUri, String gender) {
        this.idPet = idPet;
        this.petName = petName;
        this.petType = petType;
        this.petBirthday = petBirthday;
        this.petPictureUri = petPictureUri;
        this.petGender = gender;
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

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public void setPetBirthday(String petBirthday) {
        this.petBirthday = petBirthday;
    }

    public void setPetPictureUri(String petPictureUri) {
        this.petPictureUri = petPictureUri;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

}
