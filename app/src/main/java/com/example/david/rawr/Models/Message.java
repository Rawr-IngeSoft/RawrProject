package com.example.david.rawr.Models;

/**
 * Created by david on 09/05/2015.
 */
public class Message {

    private String sender;
    private String receiver;
    private String message;
    private String date;
    private String status;
    private boolean visible = true;

    public Message(String message, String sender, String receiver, String status, String date) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.status = status;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
