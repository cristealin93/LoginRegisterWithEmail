package com.lista.loginandregisterbyemail.Model;

public class User {

    private String userName,age,email;

    public User() {
    }

    public User(String userName, String age, String email) {
        this.userName = userName;
        this.age = age;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
