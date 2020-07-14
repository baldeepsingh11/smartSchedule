package com.example.scrollview.model;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Tasks {
    private String imageURL;
    private String title;
    private Date date;
    private Time time;

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public Tasks() {
        imageURL="";
        title= "I love India";
        date=new Date();
        time= new Time(0);

    }



    public Tasks(String imageURL, String title, Date date, Time time) {
        this.imageURL = imageURL;
        this.title = title;
        this.date = date;
        this.time = time;
    }
}
