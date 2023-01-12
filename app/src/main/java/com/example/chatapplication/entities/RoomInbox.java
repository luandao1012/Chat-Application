package com.example.chatapplication.entities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RoomInbox {
    private String ID,name, lastMessage, timeLastMessage, image;
    private List<String> participants;
    private List<Message> messages;

    public RoomInbox() {
    }

    public RoomInbox(String ID, String name, String lastMessage, String timeLastMessage, String image, List<String> participants, List<Message> messages) {
        this.ID = ID;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timeLastMessage = timeLastMessage;
        this.image = image;
        this.participants = participants;
        this.messages = messages;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimeLastMessage() {
        return timeLastMessage;
    }

    public void setTimeLastMessage(String timeLastMessage) {
        this.timeLastMessage = timeLastMessage;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
