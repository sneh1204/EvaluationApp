package com.example.evaluationapp;

import java.io.Serializable;

public class Admin implements Serializable {

    private String uid, token, fullname, email;

    public Admin() {

    }

    public Admin(String uid, String token, String fullname, String email) {
        this.uid = uid;
        this.token = token;
        this.fullname = fullname;
        this.email = email;
    }

    public Admin(String fullname, String email, String token) {
        this.token = token;
        this.fullname = fullname;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
