package com.example.david.rawr.Models;

/**
 * Created by david on 21/05/2015.
 */
public class FriendRequest {
    private String sender, animal, animalType, gender, birthday, ownerName, ownerLastName, petName;

    public FriendRequest(String sender, String petName, String animal, String animalType, String gender, String birthday, String ownerName, String ownerLastName) {
        this.sender = sender;
        this.animal = animal;
        this.animalType = animalType;
        this.gender = gender;
        this.birthday = birthday;
        this.ownerName = ownerName;
        this.ownerLastName = ownerLastName;
        this.petName = petName;
    }

    public String getPetName() {
        return petName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }
}
