package com.example.chatapplication.entities;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class RoomInbox implements Serializable {
    private String ID, lastMessage, timeLastMessage, dateLastMessage, senderLastMessage;
    private int isSeenLastMessage;
    private List<String> participants;
    private List<Message> messages;
    private HashMap<String, String> image;
    private HashMap<String, String> name;

    public RoomInbox() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


    public String getDateLastMessage() {
        return dateLastMessage;
    }

    public void setDateLastMessage(String dateLastMessage) {
        this.dateLastMessage = dateLastMessage;
    }

    public String getSenderLastMessage() {
        return senderLastMessage;
    }

    public void setSenderLastMessage(String senderLastMessage) {
        this.senderLastMessage = senderLastMessage;
    }

    public HashMap<String, String> getImage() {
        return image;
    }

    public String getImageFromID(String id){
        HashMap<String, String> image = getImage();
        return image.get(id);
    }

    public void setImage(HashMap<String, String> image) {
        this.image = image;
    }

    public HashMap<String, String> getName() {
        return name;
    }
    public String getNameFromID(String id){
        HashMap<String, String> name = getName();
        return name.get(id);
    }

    public void setName(HashMap<String, String> name) {
        this.name = name;
    }

    public int getIsSeenLastMessage() {
        return isSeenLastMessage;
    }

    public void setIsSeenLastMessage(int isSeenLastMessage) {
        this.isSeenLastMessage = isSeenLastMessage;
    }
}
