package com.example.david.rawr.Models;

/**
 * Created by david on 09/05/2015.
 */
public class Message {

    private String person;
    private String message;

    public Message(String person, String message) {
        this.person = person;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
