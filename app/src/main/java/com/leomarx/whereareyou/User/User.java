package com.leomarx.whereareyou.User;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/**
 * Created by RicardoSantos on 13/12/16.
 */

public class User {

    // Firebase instance variables and Initialize Firebase Auth
    private final static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final static FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

    /**
     * Check if the user is Logged on
     * @return boolean
     */
    public static boolean userIsLogged(){
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }

    public static String getDisplayName(){
        String mUsername;
        if(userIsLogged()) {
            mUsername = mFirebaseUser.getDisplayName();
            return mUsername;
        }
        else{
            return null;
        }
    }
    /**
     * Assumes that the user is logged
     * @return String
     */
    public static String getCurrentUserPhotoURL(){
        String mPhotoUrl;
        String mUsername;
        if(userIsLogged()){
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
                return mPhotoUrl;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}
