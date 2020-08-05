package com.example.scrollview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("msg", "notification acriv");
        NotificationCompat.Builder builder =new  NotificationCompat.Builder(context,"notifylemubit")
        .setSmallIcon(R.id.icon_only)
                .setContentTitle("Notification")
                .setContentText("Hello Everyone")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notification = NotificationManagerCompat.from(context);
        notification.notify(290,builder.build());
    }
}
