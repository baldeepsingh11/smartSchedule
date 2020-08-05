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
        Integer a = intent.getIntExtra("name", 0);




            Log.i("msg", "notification acriv");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hello")
                    .setSmallIcon(R.drawable.ic_icons8_checkmark)
                    .setContentTitle("Scrollview")
                    .setContentText(HomeFragment.savedTasks.get(a).getTitle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());

        }


}
