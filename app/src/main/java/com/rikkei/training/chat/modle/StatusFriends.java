package com.rikkei.training.chat.modle;

public class StatusFriends {
    private String id;
    private String status;
    private String idChat;

    public StatusFriends(String id, String status, String idChat) {
        this.id = id;
        this.status = status;
        this.idChat = idChat;
    }

    public StatusFriends() {
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
