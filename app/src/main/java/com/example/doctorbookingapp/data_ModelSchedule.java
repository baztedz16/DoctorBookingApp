package com.example.doctorbookingapp;

public class data_ModelSchedule {

    String uid, timestamp, date, time, amount, location, hospital, user;
    int count;

    public data_ModelSchedule() {
    }

    public data_ModelSchedule(String uid, String timestamp, String date, String time, String amount, String location, String hospital, int count, String user) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.location = location;
        this.hospital = hospital;
        this.count = count;
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
