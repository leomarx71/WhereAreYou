package com.leomarx.whereareyou.User;

import com.leomarx.whereareyou.chat.ChatController;
import com.leomarx.whereareyou.request.RequestController;

/**
 * Created by RicardoSantos on 09/01/17.
 */

public class UserProfileController {

    public UserProfileController(){

    }

    /**
    public void loadProfile(){
        AccountManager am = new AccountManager();
        UserProfile up = new UserProfile();
        up.loadProfile(am.getUserId());
    }
     */

    public void sendMessage(String toID, String body){
        ChatController cc = new ChatController();
        cc.sendMessage(toID,body);
    }

    public void sendRequest(String toID){
        RequestController.createRequest(toID);
    }
}
