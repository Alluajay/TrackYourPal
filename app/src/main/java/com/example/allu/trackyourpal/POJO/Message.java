package com.example.allu.trackyourpal.POJO;

/**
 * Created by allu on 4/9/17.
 */

public class Message {
    public String Uid,Message,Username;

    public Message(String uid, String message,String Username) {
        Uid = uid;
        Message = message;
        this.Username = Username;
    }
    public Message(){

    }
}
