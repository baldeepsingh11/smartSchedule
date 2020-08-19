package com.example.scrollview;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class scheduleReminderBroadcast extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "onReceive: entered");
        String title =intent.getStringExtra("name");
        String id = intent.getStringExtra("ID");
        //This is the intent of PendingIntent
        PendingIntent pIntentlogin;
        int NotificationId=(int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Intent intentAction = new Intent(context,ActionReceiver.class);
        intentAction.putExtra("NotificationId",NotificationId);

        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("action","actionName");

        pIntentlogin = PendingIntent.getBroadcast(context,1,intentAction, PendingIntent.FLAG_UPDATE_CURRENT);


         Log.i("msg", "notification acriv");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hello")
                    .setSmallIcon(R.drawable.ic_icons8_checkmark)
                    .setContentTitle("Scrollview")
                    .setContentText(title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent yesReceive = new Intent(context,ActionReceiver.class);
        yesReceive.putExtra("ID",id);
        yesReceive.setAction("YES_ACTION");
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.tick, "Yes", pendingIntentYes);


        Intent yesReceive2 = new Intent(context,ActionReceiver.class);
        yesReceive2.putExtra("ID",id);
        yesReceive2.setAction("STOP_ACTION");
        PendingIntent pendingIntentYes2 = PendingIntent.getBroadcast(context, 12345, yesReceive2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.cross, "No", pendingIntentYes2);
// notificationId is a unique int for each notification that you must define
            notificationManager.notify(NotificationId, builder.build());

    }



}
