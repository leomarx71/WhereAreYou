package com.leomarx.whereareyou.utils;

/**
 * Contains useful methods for input validation.
 * Created by Ricardo Mota on 19/12/2016.
 */
public class InputManager {


    /**
     * Checks if a given email address is valid or not.
     * @param emailAddress - The given email address.
     * @return - True if email is valid, false otherwise.
     */
    public static boolean isValidEmailAddress(String emailAddress){
        if (emailAddress == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
        }
    }


    /**
     * Check if a string is valid.
     * @param value - The string value.
     * @return - True if valid, false otherwise.
     */
    public static boolean isValidString(String value){
        if (value == null || value.length() == 0){
            return false;
        }
        return true;
    }


    /**
     * Check if a string  is valid.
     * @param value - The string value.
     * @return - True if valid, false otherwise.
     */
    public static boolean isValidString(String value, int minLenght){
        if (value == null){
            return false;
        }

        if(value.length() < 6){
            return false;
        }

        return true;
    }
}
