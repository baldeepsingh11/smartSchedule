package com.example.scrollview.model;

import android.widget.ProgressBar;

import java.security.PrivateKey;

public class Attendence {
    private String code;
    private String name;
    private  String profName;
    private String number;
    private String emailID;
    private String profileURL;
    private double present;
    private double total;
    private double percentage;




    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getProfName() {
        return profName;
    }

    public String getNumber() {
        return number;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public double getPresent() {
        return present;
    }

    public double getTotal() {
        return total;
    }

    public double getPercentage() {
        return (present / total )*100;
    }

    public Attendence()
    {
        code = "MIN 106";
        name="Engineering Thermodynamics";
        profName = "Dhananshri M joglekar";
        present =0;
        total =0;
        percentage =0;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public void setPresent(double present) {
        this.present = present;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Attendence(String code, String name, String profName, String number, String emailID, String profileURL, double present, double total, double percentage) {
        this.code = code;
        this.name = name;
        this.profName = profName;
        this.number = number;
        this.emailID = emailID;
        this.profileURL = profileURL;
        this.present = present;
        this.total = total;
        this.percentage = percentage;
    }
}
