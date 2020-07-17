package com.example.scrollview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.scrollview.model.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class reminderActivity extends AppCompatActivity {

    private static final String TAG = "reminderActivity";


    //Flag:Notification and Alarm button
    private boolean nFlag = false;
    private boolean aFlag = false;
    final Calendar myCalendar = Calendar.getInstance();


    Tasks task = new Tasks();
    SharedPreferences mPrefs;
    //notification and alarm onclick listener
    public void notification(View view) {

        if(nFlag)
        {
            nFlag = false;
            view.setAlpha(0.5f);
        }
        else
        {
            nFlag=true;
            view.setAlpha(1);
        }

    }
    public void alarm(View view) {

        if(aFlag)
        {
            aFlag = false;
            view.setAlpha(0.5f);

        }
        else
        {
            aFlag=true;
            view.setAlpha(1);
        }

    }

    //date and time:EditText
    TextInputEditText date;
    TextInputEditText time;
    TextInputEditText venu;
    TextInputEditText name;
    //spinner (reminder type):
    Spinner spin;
    String[] type = {"Academics","Groups","Personal"} ;


    //Onclick for tick:
    public void  submit (View view) {

        String text = spin.getSelectedItem().toString();

        task.setmCalendar(myCalendar);
        task.setType(text);
        task.setTitle(name.getText().toString());
        task.setVenu(venu.getText().toString());

        mPrefs = getSharedPreferences("com.example.scrollview",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        /*List<Tasks> tasks = new ArrayList<>();
        tasks = getList();
        tasks.add(task);*/
        HomeFragment.savedTasks.add(task);
        HomeFragment.taskAdapter.notifyDataSetChanged();
        Gson gson = new Gson();
        String json = gson.toJson(HomeFragment.savedTasks);
        prefsEditor.putString("tasks", json);
        Log.i(TAG, "submit: json string after editing"+ json);
        prefsEditor.apply();
        finish();

    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        venu = findViewById(R.id.venu);
        date = (TextInputEditText) findViewById(R.id.event_date);
        time = (TextInputEditText)findViewById(R.id.event_time);
        spin = (Spinner) findViewById(R.id.spinner);
        name = findViewById(R.id.event_name);



        //For type of reminder
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_row,type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        //Taking date in Edit Text using DatePicker and on focus change listener
        final DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {


            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                date.clearFocus();
            }

        };

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(reminderActivity.this, datelistener, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();




                }
            }

        });

        //Taking time in EditText using onFocusChange listener and TimePicker Dialog box


        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    final int AMPM = mcurrentTime.get(Calendar.AM_PM);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(reminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            updateTime(selectedHour,selectedMinute);
                            myCalendar.set(Calendar.HOUR_OF_DAY,selectedHour);

                        }
                    }, hour, minute, false);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
                time.clearFocus();
            }

        });
        venu.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    venu.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    return true;
                }
                return false;
            }
        });



    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "E, dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        date.setText(sdf.format(myCalendar.getTime()));
    }                    //feeding date to Edit Text
    private void updateTime(int hours, int mins) {


        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);


        // Append in a StringBuilder
        String aTime = String.valueOf(hours) + ':' +
                minutes + " " + timeSet;
        myCalendar.set(Calendar.HOUR_OF_DAY,hours);
        myCalendar.set(Calendar.MINUTE,mins);
        time.setText(aTime);
    }  //converting to 12 hour format and feeding in Edit text
    private List<Tasks> getList() {
        List<Tasks> arrayItems;
      //  SharedPreferences sharedPreferences =getSharedPreferences("com.example.scrollview",Context.MODE_PRIVATE);
        String serializedObject = mPrefs.getString("tasks", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Tasks>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            Log.i("TAG", serializedObject);
            Log.i("size", String.valueOf(arrayItems.size()));
            return arrayItems;
        }
        else
        {
            List<Tasks> dTasks = new ArrayList<>();
           // dTasks.add(new Tasks());
            Log.i(TAG, "serializedObjectNull");
            return dTasks;


        }

    }
 }