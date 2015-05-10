package com.example.david.rawr.models;

/**
 * Created by david on 09/05/2015.
 */
public class Message {

    private String person;
    private String message;
    private String date;

    public Message(String person, String message, String date) {
        this.person = person;
        this.message = message;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
