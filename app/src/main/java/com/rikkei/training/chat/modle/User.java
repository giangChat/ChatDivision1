package com.rikkei.training.chat.modle;

public class User {
    private String id;
    private String fullName;
    private String numberPhone;
    private String birthDay;
    private String imgUrl;
    private String email;
    private String passWord;

    public User(String id, String fullName, String numberPhone, String birthDay, String imgUrl, String email, String passWord) {
        this.id=id;
        this.fullName = fullName;
        this.numberPhone = numberPhone;
        this.birthDay = birthDay;
        this.imgUrl = imgUrl;
        this.email = email;
        this.passWord = passWord;
    }

    public User() {
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

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthday) {
        this.birthDay = birthday;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String password) {
        this.passWord = password;
    }
}
