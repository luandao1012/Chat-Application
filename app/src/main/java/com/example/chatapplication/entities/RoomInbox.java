package com.example.chatapplication.entities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RoomInbox {
    private String name, lastMessage, timeLastMessage;
    private List<User> participants;
    private List<Message> messages;

    public RoomInbox(String name, String lastMessage, String timeLastMessage, List<User> participants, List<Message> messages) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.timeLastMessage = timeLastMessage;
        this.participants = participants;
        this.messages = messages;
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

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
//    public User getUser(){
//        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        for(User i:participants){
//            if ( != firebaseUser){
//                return i;
//            }
//        }
//    }
}
