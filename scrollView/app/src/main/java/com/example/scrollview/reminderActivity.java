package com.example.scrollview;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scrollview.data.AlarmReminderContract;
import com.example.scrollview.data.AlarmReminderDbHelper;
import com.example.scrollview.model.Tasks;
import com.example.scrollview.reminder.AlarmScheduler;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class reminderActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor>, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private static final String TAG = "reminderActivity";
    private static final int EXISTING_VEHICLE_LOADER = 0;
    private boolean mVehicleHasChanged = false;
    //Flag:Notification and Alarm button
    private boolean nFlag = false;
    private boolean aFlag = false;
    Calendar myCalendar = Calendar.getInstance();
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);

    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat  ;
    private String mRepeatNo ;
    private String mRepeatType ;
    private Uri mCurrentReminderUri;
    private String mActive ;

    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";


    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };

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

    TextInputEditText venu;
    EditText  mTitleText;
    //spinner (reminder type):
    Spinner spin;
    String[] type = {"Academics","Groups","Personal"} ;


    //Onclick for tick:
    public void  submit (View view) {
        if (mTitleText.getText().toString().length() == 0){
            mTitleText.setError("Reminder Title cannot be blank!");
        }
        Log.i("message12345", String.valueOf(mTitleText));
        Log.i("message12345", String.valueOf(mTitle));
        String text = spin.getSelectedItem().toString();
        saveReminder();
        task.setmCalendar(myCalendar);
        task.setType(text);
        task.setTitle(mTitleText.getText().toString());
        task.setVenu(venu.getText().toString());
        mPrefs = getSharedPreferences("com.example.scrollview",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        if(HomeFragment.emptyView.getVisibility()==View.VISIBLE)
        {
            HomeFragment.emptyView.setVisibility(View.GONE);
        }
        HomeFragment.savedTasks.add(task);
        Gson gson = new Gson();
        String json = gson.toJson(HomeFragment.savedTasks);
        prefsEditor.putString("tasks", json);
        Log.i(TAG,"task size"+ String.valueOf(HomeFragment.savedTasks.size()));
        prefsEditor.apply();
        HomeFragment.taskAdapter.notifyDataSetChanged();
        finish();

    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        venu = findViewById(R.id.venu);
        mDateText = (TextView) findViewById(R.id.event_date);
        mTimeText = (TextInputEditText)findViewById(R.id.event_time);
        spin = (Spinner) findViewById(R.id.spinner);
        mTitleText = (EditText) findViewById(R.id.event_name);
        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        if (mCurrentReminderUri == null) {

            setTitle(getString(R.string.editor_activity_title_new_reminder));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a reminder that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {

            setTitle(getString(R.string.editor_activity_title_edit_reminder));


            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);
        }


        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        myCalendar = Calendar.getInstance();
        mHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = myCalendar.get(Calendar.MINUTE);
        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH) + 1;
        mDay = myCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        //For type of reminder
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_row,type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        mDateText.setText(mDate);
        mTimeText.setText(mTime);

        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
           mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }


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
                mDateText.clearFocus();
            }

        };
/*
        mDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

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


        mTimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    final Calendar mcurrentTime = Calendar.getInstance();
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
                mTimeText.clearFocus();
            }

        });*/
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
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }

    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDateText.setText(mDate);
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "E, dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        mDateText.setText(sdf.format(myCalendar.getTime()));
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
        mTimeText.setText(aTime);
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




    public void saveReminder(){

     /*   if (mCurrentReminderUri == null ) {
            // Since no fields were modified, we can return early without creating a new reminder.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }
*/

        ContentValues values = new ContentValues();
       Log.i("msg",mTitle);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);


        // Set up calender for creating the notification
        myCalendar.set(Calendar.MONTH, --mMonth);
        myCalendar.set(Calendar.YEAR, mYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        myCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        myCalendar.set(Calendar.MINUTE, mMinute);
        myCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  myCalendar.getTimeInMillis();

        // Check repeat type
        if (mRepeatType.equals("Minute")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
        } else if (mRepeatType.equals("Hour")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
        } else if (mRepeatType.equals("Day")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
        } else if (mRepeatType.equals("Week")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
        } else if (mRepeatType.equals("Month")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
        }

        if (mCurrentReminderUri == null) {
            // This is a NEW reminder, so insert a new reminder into the provider,
            // returning the content URI for the new reminder.
            Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Create a new notification
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
            }

            Toast.makeText(this, "Alarm time is " + selectedTimestamp,
                    Toast.LENGTH_LONG).show();
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentReminderUri,         // Query the content URI for the current reminder
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
            int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);

            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String repeat = cursor.getString(repeatColumnIndex);
            String repeatNo = cursor.getString(repeatNoColumnIndex);
            String repeatType = cursor.getString(repeatTypeColumnIndex);
            String active = cursor.getString(activeColumnIndex);


            Log.i("title",title);
            // Update the views on the screen with the values from the database
            mTitleText.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
            mRepeatNoText.setText(repeatNo);
            mRepeatTypeText.setText(repeatType);
            mRepeatText.setText("Every " + repeatNo + " " + repeatType + "(s)");
            // Setup up active buttons
            // Setup repeat switch
            if (repeat.equals("false")) {
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText("Off");

            } else if (repeat.equals("true")) {
                mRepeatSwitch.setChecked(true);
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }
}