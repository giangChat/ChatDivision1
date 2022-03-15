package com.rikkei.training.chat.modle;

public class StatusFriends {
    private String email;
    private String status;

    public StatusFriends(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public StatusFriends() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
