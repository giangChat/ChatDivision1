package com.rikkei.training.chat.modle;

import com.rikkei.training.chat.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Messages {

    private String idSender;
    private boolean checkSeen;
    private String message;
    private long timeLong;
    private String type;
    private int status;// 1 2 3 4

    public Messages(String idSender, boolean checkSeen, String message, long timeLong, String type, int status) {
        this.idSender = idSender;
        this.checkSeen = checkSeen;
        this.message = message;
        this.timeLong = timeLong;
        this.type = type;
        this.status = status;
    }

    public Messages() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static List<Messages> handle(List<Messages> messagesList) {

        List<Messages> result = new ArrayList<>();
        //set lại status cho từng item message
        if (messagesList.size() == 0) {
            return messagesList;
        } else if (messagesList.size() == 1) {
            messagesList.get(0).setStatus(Constants.SINGLE);
            return messagesList;
        } else {
            String idSender = messagesList.get(0).getIdSender();
            List<List<Messages>> groupMessages = new ArrayList<>();
            List<Messages> messagesSub = new ArrayList<>();
            for (Messages m : messagesList) {
                if ( m.getIdSender().equals(idSender) ) {
                    messagesSub.add(m);
                } else {
                    idSender = m.idSender;
                    List<Messages> messagesSubCopy = new ArrayList<>();
                    messagesSubCopy.addAll(messagesSub);
                    groupMessages.add(messagesSubCopy);
                    messagesSub.clear();
                    messagesSub.add(m);
                }
            }
            groupMessages.add(messagesSub);
            for (List<Messages> group : groupMessages) {
                if (group.size() == 1) {
                    group.get(0).setStatus(Constants.SINGLE);
                    result.add(group.get(0));
                } else {
                    group.get(0).setStatus(Constants.START);
                    result.add(group.get(0));
                    for (int i = 1; i < group.size() - 1; i++) {
                        group.get(i).setStatus(Constants.CENTER);
                        result.add(group.get(i));
                    }
                    group.get(group.size() - 1).setStatus(Constants.END);
                    result.add(group.get(group.size() - 1));
                }
            }
        }
        return result;
    }
    public static String convertSecondsToHMm(long seconds) {
        DateFormat obj = new SimpleDateFormat("HH:mm");
        Date res = new Date(seconds);
        return String.format(obj.format(res));
    }
}
