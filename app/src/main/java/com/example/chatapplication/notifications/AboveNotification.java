package com.example.chatapplication.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

public class AboveNotification extends ContextWrapper {

    private static final String ID = "some_id";
    private static final String NAME = "Chat Application";

    private NotificationManager notificationManager;


    public AboveNotification(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(ID, NAME, importance);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public Notification.Builder getNotifications(String title, String body, PendingIntent intent, Uri soundUri, String icon){
        return new Notification.Builder(getApplicationContext(), ID)
                .setContentIntent(intent)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setSmallIcon(Integer.parseInt(icon))
                .setAutoCancel(true);
    }

}
