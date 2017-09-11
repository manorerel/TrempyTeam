package com.example.hadar.trempyteam.Model;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.hadar.trempyteam.CreateNewTrempActivity;
import com.example.hadar.trempyteam.ListTrempActivity;
import com.example.hadar.trempyteam.MainAactivity;
import com.example.hadar.trempyteam.R;
import com.example.hadar.trempyteam.TrempDetailsActivity;

/**
 * Created by manor on 9/10/2017.
 */

public class JoinNotificationListener implements NotificationListener {

    private Activity _activity;

    public JoinNotificationListener(Activity activity){
        _activity = activity;
    }
    @Override
    public void notificationReceived(NotificationEvent event) {
        Context context = _activity.getApplicationContext();
        Intent intent = new Intent(context, ListTrempActivity.class);
        intent.putExtra("cameFrom","personalArea");
        intent.putExtra("isCreated", "true");
        PendingIntent contentIntent = PendingIntent.getActivity(_activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final int MY_NOTIFICATION_ID=1;
        NotificationManager notificationManager;
        Notification myNotification;

            myNotification = new Notification.Builder(context)
                    .setContentTitle("בקשת הצטרפות לטרמפ שלך")
                    .setContentText(event.getText())
                    .setTicker("Notification!")
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.h)
                    .build();

            notificationManager = (NotificationManager) _activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
    }
}
