package com.leomarx.whereareyou.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by RicardoSantos on 10/01/17.
 */

public class MessageDAL {

    /**
     * Envia a mensagem para a DB.
     * @param msg
     */
    public void sendMessage(Message msg){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users_profiles");
        String messageId = mDatabase.child("users_profiles").child(msg.to).child("messages_recieved").push().getKey();
        msg.id=messageId;
        mDatabase.child(msg.to).child("messages_recieved").child(messageId).setValue(msg);
        mDatabase.child(msg.from).child("messages_sent").child(messageId).setValue(msg);
    }
}
