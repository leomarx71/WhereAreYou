package com.leomarx.whereareyou.account;

import android.net.Uri;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Controls some operations related to user account.
 * Created by Ricardo Mota on 14/12/2016.
 */
public class AccountManager {


    public AccountManager(){

    }

    public String getName()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth != null)
        {
            if(auth.getCurrentUser() != null)
            {
                String name = auth.getCurrentUser().getDisplayName();

                if(name != null)
                {
                    return name;
                }

            }
        }

        return "";
    }

    public String getEmailAddress()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth != null)
        {
            if(auth.getCurrentUser() != null)
            {
                String email = auth.getCurrentUser().getEmail();
                String userString = auth.getCurrentUser().toString();
                if(email != null)
                {
                    return email;
                }

            }
        }

        return "";

    }

    public Uri getPhotoUri()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth != null)
        {
            if(auth.getCurrentUser() != null)
            {
                Uri photo = auth.getCurrentUser().getPhotoUrl();

                if(photo != null)
                {
                    return photo;
                }

            }
        }

        return null;
    }

    public String getUserId()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth != null)
        {
            if(auth.getCurrentUser() != null)
            {
                String uID = auth.getCurrentUser().getUid();

                if(uID != null)
                {
                    return uID;
                }

            }
        }

        return "";

    }

    /**
     * Logout the application.
     */
    public void logout(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        LoginManager manager = LoginManager.getInstance();

        if(auth != null)
        {
            auth.signOut();
        }

        if(manager != null)
        {
            manager.logOut();
        }

    }

    /**
     * Check if the current user is logged in.
     * @return - True if the user is logged in, false otherwise.
     */
    public boolean isUserLoggedIn(){

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth != null)
        {
            if(auth.getCurrentUser() != null)
            {
                return true;
            }
        }

        return false;

    }

}
