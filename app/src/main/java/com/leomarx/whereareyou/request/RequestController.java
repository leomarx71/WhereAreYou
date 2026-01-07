package com.leomarx.whereareyou.request;

import com.leomarx.whereareyou.R;
import com.leomarx.whereareyou.notification.NotificationController;

/**
 * Created by RicardoSantos on 01/01/17.
 */

public class RequestController {

    /**
     * Create a Location Request for Sharing Location.
     * @param targetID
     */
    public static void createRequest(String targetID){
        Request newRequest = new Request(targetID);
        RequestDAL requestDAL = new RequestDAL();
        requestDAL.sendRequest(newRequest);
    }

    public static void recieveRequest(Request request){
        NotificationController nc = new NotificationController();
        //nc.createNotification("New Request from " + request.from, "Share Location ?");
    }
}
