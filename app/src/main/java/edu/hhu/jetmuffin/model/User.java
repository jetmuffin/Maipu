package edu.hhu.jetmuffin.model;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by JetMuffin on 15/11/4.
 */
public class User {

    private String email;
    private String password;
    private String nickname;
    private String city;
    private String motto;
    private String birthday;
    private String interest;

    public User(){
        super();
    }

    public User(SharedPreferences sp){
        this.email = sp.getString("email"," ");
        this.password = sp.getString("password"," ");
        this.nickname = sp.getString("nickname"," ");
        this.city = sp.getString("city"," ");
        this.motto = sp.getString("motto"," ");
        this.birthday = sp.getString("birthday","");
        this.interest = sp.getString("interest","");
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", city='" + city + '\'' +
                ", motto='" + motto + '\'' +
                '}';
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
}
