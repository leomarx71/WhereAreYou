package com.leomarx.whereareyou.friends;

/**
 * Created by RicardoSantos on 08/01/17.
 */

public class Friend {

    public String id;
    public String email;
    public String username;

    public Friend(){

    }

    public Friend(String email, String username){
        this.id = id;
        this.email = username;
    }

    public String getFriendEmail(){
        return this.email;
    }

    public String getFriendUsername(){
        return this.username;
    }

}
