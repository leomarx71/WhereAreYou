package com.leomarx.whereareyou.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leomarx.whereareyou.account.AccountManager;

/**
 * Created by RicardoSantos on 08/01/17.
 */

public class ChatController {

    private static AccountManager am = new AccountManager();

    public void sendMessage(String toID , String body){
        Message msg = new Message("",am.getUserId(),toID,body);
        MessageDAL msgDAL = new MessageDAL();
        msgDAL.sendMessage(msg);
    }
}
