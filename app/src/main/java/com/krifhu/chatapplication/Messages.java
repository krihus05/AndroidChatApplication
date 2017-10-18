package com.krifhu.chatapplication;

/**
 * Created by Kristian on 13.10.2017.
 */

public class Messages {
    private String messageBody;
    private int messageID;
    private String sender;
    private String receiver;
    //private timestamp version;

    public Messages(String messageBody, int messageID, String sender, String receiver) {
        this.messageBody = messageBody;
        this.messageID = messageID;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
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
}
