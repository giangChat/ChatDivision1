package com.rikkei.training.chat.modle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Conversation {
    String id ;
    String fullName;
    String idRoomChat;
    String imgPhoto;
    String lastMessage;
    int coutUnSeen;
    long lastTime;

    public Conversation() {
    }

    public Conversation(String id, String fullName,String idRoomChat, String imgPhoto, String lastMessage, int coutUnSeen, long lastTime) {
        this.id = id;
        this.fullName = fullName;
        this.idRoomChat = idRoomChat;
        this.imgPhoto = imgPhoto;
        this.lastMessage = lastMessage;
        this.coutUnSeen = coutUnSeen;
        this.lastTime = lastTime;
    }

    public String getIdRoomChat() {
        return idRoomChat;
    }

    public void setIdRoomChat(String idRoomChat) {
        this.idRoomChat = idRoomChat;
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

    public String setTextTimeSendMessage() {
        DateFormat obj = new SimpleDateFormat("HH:mm");
        DateFormat obj1 = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();
        Date timeMessage = new Date(lastTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastTime);
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(now.getTime());
        long time = now.getTime();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        calendarNow.setTimeInMillis(time);
        int nowDay = calendarNow.get(Calendar.DAY_OF_YEAR);
        int nowYear = calendarNow.get(Calendar.YEAR);
        if(nowYear == year){
            if(nowDay == dayOfYear ){
                return  String.format(obj.format(timeMessage));// Messages.convertSecondsToHMm(milliseconds);
            } else if(nowDay - dayOfYear == 1 ){
                return "Hôm qua";
            } else return String.format(obj1.format(timeMessage)); //Messages.convertSecondsToddMMyyy(milliseconds);
        }else {
            return  String.format(obj1.format(timeMessage));//Messages.convertSecondsToddMMyyy(milliseconds);
        }
    }
}
