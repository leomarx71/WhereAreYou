package com.leomarx.whereareyou.notification;

import android.content.Context;

/**
 * Created by RicardoSantos on 06/12/16.
 */

public class NotificationController {
    public void showNotification(final Context context) {
        NotificationDTO ndto = new NotificationDTO();
        NotificationPresentation.notify(context,ndto,1);
    }
}
