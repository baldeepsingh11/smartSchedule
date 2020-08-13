package com.example.scrollview;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.scrollview.model.Tasks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class scheduleReminderBroadcast extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "onReceive: entered");
        String title =intent.getStringExtra("name");
        //This is the intent of PendingIntent
        PendingIntent pIntentlogin;
        Intent intentAction = new Intent(context,ActionReceiver.class);

        //This is optional if you have more than one buttons and want to differentiate between two
        intentAction.putExtra("action","actionName");

        pIntentlogin = PendingIntent.getBroadcast(context,1,intentAction, PendingIntent.FLAG_UPDATE_CURRENT);


         Log.i("msg", "notification acriv");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hello")
                    .setSmallIcon(R.drawable.ic_icons8_checkmark)
                    .setContentTitle("Scrollview")
                    .setContentText(title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.bg_white,"Class Attendend",pIntentlogin)
                    .addAction(R.drawable.bg_white,"Not Attendend",pIntentlogin)
                    .setOngoing(true)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());

    }



}
