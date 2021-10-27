package com.example.chatbot.model;

public class ChatsModel {
    private String message;

    public ChatsModel(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    private String sender;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
