package com.example.allu.trackyourpal.POJO;

import java.util.ArrayList;

/**
 * Created by allu on 4/9/17.
 */

public class User {
    public String Username;
    public String Emailid;
    public int request;

    public String UID;
    ArrayList Friends;
    ArrayList Tours;

    public User(String Username, String Emailid) {
        this.Username = Username;
        this.Emailid = Emailid;
        Friends = new ArrayList();
        Tours  = new ArrayList();
    }

    public User(){

    }
}
