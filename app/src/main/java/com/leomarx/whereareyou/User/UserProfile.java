package com.leomarx.whereareyou.User;

import android.content.Context;
import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;
import com.leomarx.whereareyou.chat.Message;
import com.leomarx.whereareyou.friends.Friend;
import com.leomarx.whereareyou.maps.GPSTracker;
import com.leomarx.whereareyou.request.Request;

import java.util.ArrayList;

/**
 * Created by RicardoSantos on 08/01/17.
 */

@IgnoreExtraProperties
public class UserProfile {

    public String id;
    public String name;
    public String email;
    public ArrayList<Message> messages_sent;
    public ArrayList<Request> request_sent;
    public ArrayList<Request> request_recieved;
    public ArrayList<Message> message_recieved;
    public ArrayList<Friend> friends;

    public UserProfile() {
    }

    public Location getCurrentLocation(Context context) {
        // TODO - implement UserProfile.getCurrentLocation
        GPSTracker gps = new GPSTracker(context);
        return gps.getLocation();

    }

    public void newRequest() {
        // TODO - implement UserProfile.newRequest
        // throw new UnsupportedOperationException();
    }

    /**
     *
     * @param to
     */
    public void newMessage(String to) {
        // TODO - implement UserProfile.newMessage
        //throw new UnsupportedOperationException();
    }

    public void getMessages() {
        // TODO - implement UserProfile.getMessages
        //throw new UnsupportedOperationException();
    }

    public void getNewRequests() {
        // TODO - implement UserProfile.getNewRequests
        //throw new UnsupportedOperationException();
    }

    public void AddFriend() {
        // TODO - implement UserProfile.getNewRequests
        //throw new UnsupportedOperationException();
    }

    /**
     * Returns the Friend from the Friends List with that username.
     * If not found, return null
     * @param username
     * @return
     */
    public Friend getFriendFromUsername(String username){
        for (Friend f: this.friends) {
            if (f.username == username)
            {
                return f;
            }
        }
        return null;
    }
}