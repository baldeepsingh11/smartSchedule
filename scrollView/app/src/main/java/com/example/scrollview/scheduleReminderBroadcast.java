package com.example.scrollview;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.scrollview.model.Schedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;
import static com.example.scrollview.LoginActivity.timetable;
import static com.example.scrollview.LoginActivity.user;

public class scheduleReminderBroadcast extends BroadcastReceiver {
    private static final String TAG = "scheduleReminderBro";
    private Context Gcontext;
    FirebaseFirestore fStore;
    //public Map<String,ArrayList<Schedule>> timetable = new HashMap<>();

    public String getToday(){

        Date date =  new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.getDefault());
        String dayOfWeek   = sdf.format(date);
        String editedDayOfWeek = dayOfWeek.substring(0, 1).toLowerCase()+dayOfWeek.substring(1);
        return editedDayOfWeek ;
    }
    public String getNextDay() {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DATE,1);
        Date date =  myCalendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.getDefault());
        String dayOfWeek   = sdf.format(date);
        return dayOfWeek.substring(0, 1).toLowerCase()+dayOfWeek.substring(1);
    }
    public void scheduleNextDay() {


    // first extract data from firebase or shared preferences


    //  call set alarm for each notification required on the next day using for loop with title and code from the data extracted above
    Calendar myCalendar = Calendar.getInstance();
    int min = myCalendar.get(Calendar.MINUTE);
    /*for(int i=min ; i<=min+2; i++)
    {
        setAlarm(1,i,i,"Anfal","1");
    }*/

    setNotifications(getToday());
    /*setAlarm(1,min,min,"Anfal","1");
        setAlarm(1,min+1,min+1,"Anfal","1");*/

    Log.i(TAG, "scheduleNext: phase 1 after for loop " + min);
    // set schedule next intent on next day 12 midnight

    setAlarm(1,min+2,1,"continueLoop","1");



}
    public void scheduleToday(){
    //schedule today's notifications and today's next intent
    }
    public String splitTime(String time){
        String[] strings = time.split("-");
        return strings[0];
    }
    public void setNotifications(String d) {

        final String day = d;
         fStore = FirebaseFirestore.getInstance();
         fStore.collection("1st year").document("Q1").collection(day).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Schedule> temp= new ArrayList<>();

                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            temp.add(document.toObject(Schedule.class));
                            document.getId();


                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                    Calendar sCalendar = Calendar.getInstance();
                    String dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                  //  timetable.put(day,temp);
                    Log.i(TAG, "onComplete: timetable "+ day + "   "+ new Gson().toJson(temp));


                    for (int i = 0; i < temp.size() ; i++) {
                            String time=temp.get(i).getTime();
                            String startTime =splitTime(time);
                            String[] strings = startTime.split(":");

                            setAlarm(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[1]),timetable.get(day).get(i).getName(),timetable.get(day).get(i).getCode());

                         //   setAlarm(1,34+i,i,"Anfal","1");


                            Log.i(TAG, "onComplete: in for loop"+temp.get(i).getName() + strings[1]);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: "+e);
                }
            });

        }
    private void setAlarm(int mHour,int mMinute,int ID,String title,String code) {
        Calendar myCalendar = Calendar.getInstance(Locale.getDefault());
        Log.i(TAG, "setAlarm: hour "+Integer.toString(myCalendar.get(Calendar.HOUR_OF_DAY)));
        boolean check=false;
        if (mHour>myCalendar.get(Calendar.HOUR_OF_DAY))
            check=true;
        Log.i(TAG, "setAlarm: hour"+Boolean.toString(check)+"mHour: "+Integer.toString(mHour)+" myCalendar: "+Integer.toString(myCalendar.get(Calendar.HOUR_OF_DAY)));

        if (myCalendar.get(Calendar.HOUR_OF_DAY)==mHour&&myCalendar.get(Calendar.MINUTE)<=mMinute)
            check=true;
        Log.i(TAG, "setAlarm: minute "+Boolean.toString(check)+"mMinute: "+Integer.toString(mMinute)+" myCalendar: "+Integer.toString(myCalendar.get(Calendar.MINUTE)));

        Log.i(TAG, "setAlarm: "+Boolean.toString(check));
        if (check) {
            Log.i(TAG, "setAlarm: entered");
            Intent intent = new Intent(Gcontext, scheduleReminderBroadcast.class);
            intent.putExtra("name", title);
            intent.putExtra("ID",code);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(Gcontext, ID, intent, 0);
            AlarmManager alarmManager = (AlarmManager) Gcontext.getSystemService(ALARM_SERVICE);
            myCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            myCalendar.set(Calendar.MINUTE, mMinute);
            myCalendar.set(Calendar.SECOND, 0);

            Log.i(TAG, "phase 1" + title);
            Log.i("minute", String.valueOf(myCalendar.SECOND));
            Log.i("minute", String.valueOf(myCalendar.MINUTE));

            Log.i(TAG, "setAlarm: + phase1" + title);




            long selectedTimestamp = myCalendar.getTimeInMillis();
            
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedTimestamp, pendingIntent);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Gcontext = context;
        Log.i(TAG, "onReceive: " + "today " + getToday() + "  "+getNextDay());
        Log.i("TAG", "onReceive: phase 1 entered");
        String title = intent.getStringExtra("name");
        String id = intent.getStringExtra("ID");
        Log.i(TAG, "onReceive: phase 1" + id);

        if(title.equals("continueLoop")){
            scheduleNextDay();
            Log.i(TAG, "onReceive: " + "phase 1 schedule next called");
        }

        else{
            // if intent is for building notification
            //This tramsis the intent of PendingIntent
            PendingIntent pIntentlogin;
            int NotificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            Intent intentAction = new Intent(context, ActionReceiver.class);
            intentAction.putExtra("NotificationId", NotificationId);

            //This is optional if you have more than one buttons and want to differentiate between two
            intentAction.putExtra("action", "actionName");

            pIntentlogin = PendingIntent.getBroadcast(context, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);


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

            Intent yesReceive = new Intent(context, ActionReceiver.class);
            yesReceive.putExtra("ID", id);
            yesReceive.setAction("YES_ACTION");
            PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(R.drawable.tick, "Yes", pendingIntentYes);


            Intent yesReceive2 = new Intent(context, ActionReceiver.class);
            yesReceive2.putExtra("ID", id);
            yesReceive2.setAction("STOP_ACTION");
            PendingIntent pendingIntentYes2 = PendingIntent.getBroadcast(context, 12345, yesReceive2, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(R.drawable.cross, "No", pendingIntentYes2);
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(NotificationId, builder.build());
        }








    }



}
