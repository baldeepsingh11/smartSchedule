package com.example.scrollview;

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

public class ReminderBroadcast extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        List<Tasks> arrayItems;
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.scrollview",Context.MODE_PRIVATE);
        String serializedObject = sharedPreferences.getString("tasks", null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Tasks>>(){}.getType();
        arrayItems = gson.fromJson(serializedObject, type);
        Integer Id = intent.getIntExtra("name", 0);
        int position=-1;
        Log.i("TAG", "onReceive: entered");
        for (int i = 0; i <arrayItems.size() ; i++) {
            Log.i("for i", Integer.toString(i));
            Log.i("onreceive",Integer.toString(arrayItems.get(i).getID()) );
            if(arrayItems.get(i).getID()==Id){
                position=i;
                Log.i("position", Integer.toString(position));
                break;
            }
        }

           if(position!=-1){ Log.i("msg", "notification acriv");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "hello")
                    .setSmallIcon(R.drawable.ic_icons8_checkmark)
                    .setContentTitle("Scrollview")
                    .setContentText(arrayItems.get(position).getTitle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());}

        }



}
