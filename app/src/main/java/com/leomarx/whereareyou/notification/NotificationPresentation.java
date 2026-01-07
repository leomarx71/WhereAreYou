package com.leomarx.whereareyou.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.leomarx.whereareyou.R;
import com.leomarx.whereareyou.account.AccountManager;
import com.leomarx.whereareyou.menu.MenuActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for showing and canceling presentation
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NotificationPresentation {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Presentation";
    private static final AccountManager wayLMController = new AccountManager();
    private static final String TAG = MenuActivity.class.getSimpleName();
    static Bitmap picture = null;
    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of presentation notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void setBitmapFromURL(Resources res, String src) {
        try {
            URL url = new URL(src); // Se a string 'URL("")' que carrega foto default
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            picture = BitmapFactory.decodeStream(input);
            Log.d(TAG, "Social Photo loaded OK for notification: ");
        } catch (IOException e) {
            // Log exception
            picture = BitmapFactory.decodeResource(res, R.drawable.ic_unknown);
            Log.d(TAG, "Social Photo couldn't be loaded for notification: ");
        }
    }

    public static void notify(final Context context,
                              final NotificationDTO notificationDTO, final int number) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        Uri photoUri = wayLMController.getPhotoUri();
        String myPhotoURL;
        if(photoUri == null)
        {
            myPhotoURL = "";
        }else
        {
            myPhotoURL = wayLMController.getPhotoUri().toString();
        }
        setBitmapFromURL(res, myPhotoURL);

        /**
         * TODO: Change This
         */
        final String ticker = notificationDTO.getTitle();
        final String title = notificationDTO.getTitle();
        final String text = notificationDTO.getBody();
        final String summaryText = "";

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.ic_way_icon)
                .setContentTitle(title)
                .setContentText(text)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Porto, Portugal&mode=w")
                                ),
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )
                )

                // Show expanded text content on devices running Android 4.1 or
                // later.
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(picture)
                        .setBigContentTitle(title)
                        .setSummaryText(summaryText))

                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.
                .addAction(
                        R.drawable.ic_notif_action_yes,
                        res.getString(R.string.notif_yes),
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Porto, Portugal&mode=w")
                                /*Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                        .setType("text/plain")
                                        .putExtra(Intent.EXTRA_TEXT, "Hey Friend meet me in this location"), "WAY"*/
                                ),
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )
                )
                .addAction(
                        R.drawable.ic_notif_action_no,
                        res.getString(R.string.notif_no),
                        null)
                .addAction(
                        R.drawable.ic_notif_action_ignore,
                        res.getString(R.string.notif_ignore),
                        null)
                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, NotificationDTO, int)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }

}
