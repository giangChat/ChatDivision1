package com.rikkei.training.chat.modle;

import java.sql.Time;
import java.util.Date;

public class Messages {

    private String idReceiver;
    private String idSender;
    private boolean checkSeen;
    private String message;
    private long timeLong;
    private String type;

    public Messages(String idReceiver, String idSender, boolean checkSeen, String message, long timeLong, String type) {
        this.idReceiver = idReceiver;
        this.idSender = idSender;
        this.checkSeen = checkSeen;
        this.message = message;
        this.timeLong = timeLong;
        this.type = type;
    }

    public Messages() {
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public boolean isCheckSeen() {
        return checkSeen;
    }

    public void setCheckSeen(boolean checkSeen) {
        this.checkSeen = checkSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(long timeLong) {
        this.timeLong = timeLong;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
