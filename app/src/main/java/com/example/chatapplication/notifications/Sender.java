package com.example.chatapplication.notifications;

public class Sender {
    private Data data;
    private String receiver;

    public Sender() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Sender(Data data, String receiver) {
        this.data = data;
        this.receiver = receiver;
    }
}
