package com.leomarx.whereareyou.request;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leomarx.whereareyou.User.User;
import com.leomarx.whereareyou.account.AccountManager;
import com.leomarx.whereareyou.notification.Notification;
import com.leomarx.whereareyou.notification.NotificationController;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by RicardoSantos on 01/01/17.
 */

public class RequestListner {

    public static void startListner(){


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        AccountManager am = new AccountManager();

        mDatabase.child(am.getUserId()).child("requests_sent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Request request = dataSnapshot.getValue(Request.class);

                NotificationController nc = new NotificationController();
                // Create Notification

                Log.w(TAG, "OriginEmail: " + request.from + ", targetEmail " + request.to);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
