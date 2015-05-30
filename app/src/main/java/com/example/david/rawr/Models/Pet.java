package com.example.david.rawr.Models;

import android.util.Log;

/**
 * Created by david on 27/04/2015.
 */
public class Pet {

    private String idPet, petName, petType, petBirthday, path, petGender;
    private boolean selected = false;

    public Pet(String petName, String path) {
        this.petName = petName;
        this.path = path;
    }

    public Pet() {
    }

    public Pet(String idPet, String petName, String petType, String petBirthday, String path, String gender) {
        this.idPet = idPet;
        this.petName = petName;
        this.petType = petType;
        this.petBirthday = petBirthday;
        this.path = path;
        this.petGender = gender;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSelected(String selected) {

        if (selected.equals("'true'")){
            this.selected = true;
        }else{
            this.selected = false;
        }
    }

    public boolean isSelected() {

        return selected;
    }

    public String getPetPictureUri() {
        return path;
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
        this.path = petPictureUri;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

}
