package com.rikkei.training.chat.modle;

public class Conversation {
    String id ;
    String fullName;
    String imgPhoto;
    String lastMessage;
    int coutUnSeen;
    long lastTime;

    public Conversation() {
    }

    public Conversation(String id, String fullName, String imgPhoto, String lastMessage, int coutUnSeen, long lastTime) {
        this.id = id;
        this.fullName = fullName;
        this.imgPhoto = imgPhoto;
        this.lastMessage = lastMessage;
        this.coutUnSeen = coutUnSeen;
        this.lastTime = lastTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImgPhoto() {
        return imgPhoto;
    }

    public void setImgPhoto(String imgPhoto) {
        this.imgPhoto = imgPhoto;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getCoutUnSeen() {
        return coutUnSeen;
    }

    public void setCoutUnSeen(int coutUnSeen) {
        this.coutUnSeen = coutUnSeen;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
