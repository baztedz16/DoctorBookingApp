package com.example.doctorbookingapp;

public class data_patientSchedules {
    String uid, timestamp, amount, time, date, sched_id, seen, patientid, reviews;
    int rating;

    public data_patientSchedules(String uid, String timestamp, String amount, String time, String date, String sched_id, String seen, String patientid, String reviews, int rating) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.amount = amount;
        this.time = time;
        this.date = date;
        this.sched_id = sched_id;
        this.seen = seen;
        this.patientid = patientid;
        this.reviews = reviews;
        this.rating = rating;
    }

    public data_patientSchedules() {

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSched_id() {
        return sched_id;
    }

    public void setSched_id(String sched_id) {
        this.sched_id = sched_id;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
