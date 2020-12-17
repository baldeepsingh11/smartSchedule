package com.example.scrollview;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.scrollview.model.Schedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.scrollview.LoginActivity.timetable;


public class scheduleFragment extends Fragment {
    private static final String TAG = "scheduleFragment";
    RecyclerView recyclerView;
    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;
    TextView code;

    //Firbase
    FirebaseFirestore firebaseFirestore;

    final List<Schedule> schedules = new ArrayList<Schedule>();
    scheduleAdapter adapter;

    CollapsibleCalendar collapsibleCalendar;

    public scheduleFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView= inflater.inflate(R.layout.fragment_schedule, container, false);
        // Inflate the layout for this fragment
        expandableView = rootView.findViewById(R.id.expandable_schedule);
        arrowBtn = rootView.findViewById(R.id.arrow_btn);
        cardView = rootView.findViewById(R.id.schedule_card);
        recyclerView = rootView.findViewById(R.id.scheduleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collapsibleCalendar = rootView.findViewById(R.id.calendar);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDayChanged() {
                Log.i(TAG, "onDayChanged: ");
            }

            @Override
            public void onClickListener() {
                Log.i(TAG, "onClickListener: ");
            }

            @Override
            public void onDaySelect() {
               /* Day day = collapsibleCalendar.getSelectedDay();
                Date date = new Date(day.getYear(),day.getMonth(),day.getDay()-1);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.getDefault());
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay()+ sdf.format(date));*/
               schedules.clear();
               adapter.notifyDataSetChanged();
                schedules.addAll(timetable.get(getSelectedDay()));
                if (schedules.size()>0) Log.i(TAG, "onCreateView: "+schedules.get(0).getTime());
                else Log.i(TAG, "onCreateView: length is 0");
                adapter.notifyDataSetChanged();
               //getschedule(getSelectedDay());



            }

            @Override
            public void onItemClick(View view) {
                Log.i(TAG, "onItemClick: ");
            }

            @Override
            public void onDataUpdate() {
                Log.d(TAG, "onDataUpdate: ");

              //  getschedule(getSelectedDay());


            }

            @Override
            public void onMonthChange() {
                Log.d(TAG, "onMonthChange: ");
            }
             
            @Override
            public void onWeekChange(int i) {
                Log.i(TAG, "onWeekChange: ");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.getDefault());
                String dayOfWeek = sdf.format(date);
                schedules.clear();
                schedules.addAll(timetable.get(dayOfWeek.substring(0, 1).toLowerCase()+dayOfWeek.substring(1)));
                adapter.notifyDataSetChanged();

            }
        });



        adapter = new scheduleAdapter(getContext(),schedules);
        schedules.addAll(timetable.get(getSelectedDay()));
        adapter.notifyDataSetChanged();
        // getschedule(getSelectedDay());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        

        return rootView;
    }

    // Function for retrieving data based on day
    public void getschedule(String day) {
        firebaseFirestore.collection(day).document("ME").collection("Q1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");

                } else {
                    // Convert the whole Query Snapshot to a list
                    // of objects directly! No need to fetch each
                    // document.

                    List<Schedule> schedule = queryDocumentSnapshots.toObjects(Schedule.class);
                    // Add all to your list
                    schedules.addAll(schedule);
                    Log.d(TAG, "c: " + schedules);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    // Function for getting selected day of week

    /*public int getSelectedDay()
    {
        Day day = collapsibleCalendar.getSelectedDay();
         Date date =  new GregorianCalendar(day.getYear(),day.getMonth(),day.getDay()).getTime();//new Date(day.getYear(),day.getMonth(),day.getDay()-1);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.getDefault());
        Calendar myCalendar = Calendar.getInstance();
       myCalendar.set(Calendar.YEAR,day.getYear());
       myCalendar.set(Calendar.MONTH,day.getMonth());
       myCalendar.set(Calendar.DAY_OF_MONTH,day.getDay());
       myCalendar.setTime(date);
        int dayOfWeek   = myCalendar.get(Calendar.DAY_OF_WEEK)-1;
        Log.i(TAG, "getSelectedDay: " + myCalendar.get(Calendar.DAY_OF_WEEK) + day.getDay());
        return dayOfWeek ;
}*/
    public String getSelectedDay(){
        Day day = collapsibleCalendar.getSelectedDay();
        Date date =  new GregorianCalendar(day.getYear(),day.getMonth(),day.getDay()).getTime();//new Date(day.getYear(),day.getMonth(),day.getDay()-1);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE",Locale.getDefault());
        String dayOfWeek   = sdf.format(date);
        String editedDayOfweek = dayOfWeek.substring(0, 1).toLowerCase()+dayOfWeek.substring(1);
        Log.i(getClass().getName(), "Selected Day: "
                + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay()+ editedDayOfweek);

        return editedDayOfweek ;
    }


}
