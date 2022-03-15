package com.rikkei.training.chat.modle;

public class User {
    private String fullName;
    private String numberPhone;
    private String birthday;
    private String imgUrl;
    private String email;
    private String password;

    public User(String fullName, String numberPhone, String birthday, String imgUrl, String email, String password) {
        this.fullName = fullName;
        this.numberPhone = numberPhone;
        this.birthday = birthday;
        this.imgUrl = imgUrl;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
