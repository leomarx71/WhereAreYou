package com.leomarx.whereareyou.request;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leomarx.whereareyou.account.AccountManager;
import com.leomarx.whereareyou.chat.Message;

/**
 * Created by RicardoSantos on 01/01/17.
 */

public class RequestDAL {

    private DatabaseReference mDatabase;
    private DatabaseReference mSentRequestsReference;
    private DatabaseReference mRecievedRequestsReference;
    private ValueEventListener mRequestListener;

    private static AccountManager am = new AccountManager();

    public RequestDAL(){}

    public void sendRequest(Request request){
        mDatabase = FirebaseDatabase.getInstance().getReference("users_profiles");
        String requestId = mDatabase.child("users_profiles").child(request.to).child("requests_recieved").push().getKey();
        request.id=requestId;
        mDatabase.child(request.to).child("requests_recieved").child(requestId).setValue(request);
        mDatabase.child(request.from).child("requests_sent").child(requestId).setValue(request);
    }


    public void startListner(){

    }


}
