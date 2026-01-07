package com.leomarx.whereareyou.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by RicardoSantos on 13/12/16.
 */

public class UserPresentation {

    public static Bitmap getUserPhoto(Context context){
        Bitmap bmp = null;
        URL url = null;
        try {
            url = new URL(User.getCurrentUserPhotoURL());
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch (MalformedURLException mue){
            // TODO: Set Message String for one from values
            Toast.makeText(context, mue.getMessage(),Toast.LENGTH_LONG).show();
        }
        catch (IOException ioe){
            // TODO: Set Message String for one from values
            Toast.makeText(context, ioe.getMessage(),Toast.LENGTH_LONG).show();
        }
        return bmp;
    }
}
