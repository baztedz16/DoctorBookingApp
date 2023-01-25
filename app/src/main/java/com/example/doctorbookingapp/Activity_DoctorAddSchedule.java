package com.example.doctorbookingapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doctorbookingapp.databinding.DoctorAddScheduleBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class Activity_DoctorAddSchedule extends AppCompatActivity {

    private DoctorAddScheduleBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private String date = "", time = "", amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DoctorAddScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        loadUserDetails();

        binding.hospitalTv.setText("IR3 Hospital Solutions Consultancy and Pharmaceuticals Inc.");

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_DoctorAddSchedule.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = month + "/" + dayOfMonth + "/" + year;
                        binding.selectDateEt.setText(date);
                    }
                };
            }
        });

        binding.selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Activity_DoctorAddSchedule.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, timeSetListener, hour, minute, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();


                timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay > 12) {
                            binding.selectTimeEt.setText(hourOfDay - 12 + ":" + (minute + " PM"));
                        } else if (hourOfDay == 12) {
                            binding.selectTimeEt.setText("12" + ":" + ((minute) + " PM"));
                        } else if (hourOfDay < 12) {
                            if (hourOfDay != 0) {
                                binding.selectTimeEt.setText((hourOfDay) + ":" + ((minute) + " AM"));
                            } else {
                                binding.selectTimeEt.setText("12" + ":" + ((minute) + " AM"));
                            }
                        }
                    }
                };
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSchedule();
            }
        });


    }

    private void loadUserDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = "" + snapshot.child("name").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String location = "" + snapshot.child("location").getValue();

                        binding.locationTv.setText(location);
                        binding.nameTv.setText("Dr. " + name);

                        Glide.with(Activity_DoctorAddSchedule.this)
                                .load(profileImage)
                                .placeholder(R.drawable.ic_person_dark)
                                .into(binding.profileTv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void submitSchedule() {
        date = binding.selectDateEt.getText().toString().trim();
        time = binding.selectTimeEt.getText().toString().trim();
        amount = binding.amountEt.getText().toString().trim();

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please select a specific date", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(time)) {
            Toast.makeText(this, "Please select a specific time", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(amount)) {
            Toast.makeText(this, "Field is empty. Please enter specified amount", Toast.LENGTH_SHORT).show();
        } else {
            createSchedule();
        }
    }

    private void createSchedule() {
        progressDialog.setMessage("Saving your schedule please wait");
        progressDialog.show();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("timestamp", timestamp);
        hashMap.put("date", date);
        hashMap.put("amount", amount);
        hashMap.put("time", time);
        hashMap.put("count", 0);
        hashMap.put("user", "");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schedules");
        databaseReference.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(binding.getRoot(), "Schedule availability created successfully. Thank you.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                        startActivity(new Intent(Activity_DoctorAddSchedule.this, Activity_DoctorDashboard.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(binding.getRoot(), "Schedule availability created unsuccessfully. Please try again. ", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                    }
                });
    }
}