package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doctorbookingapp.databinding.DoctorProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_DoctorProfile extends AppCompatActivity {

    private DoctorProfileBinding binding;
    private WebView webView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private String specialty;

    private ArrayList<data_patientSchedules> modelScheduleArrayList;
    private Helper_Adapter_reviews adapterSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DoctorProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        binding.usernameTv.setText("N/A");
        binding.emailTv.setText("N/A");
        binding.specialtyTv.setText("N/A");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        loadUserDetails();
        doctorreview();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_DoctorProfile.this, Activity_EditProfile.class));
            }
        });
    }

    private void loadUserDetails() {
        if (firebaseUser.isEmailVerified()) {
            binding.statusTv.setText("Verified");
            binding.statusTv.setTextColor(Color.parseColor("#32CD32"));
        } else {
            binding.statusTv.setText("Not Verified");
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = "" + snapshot.child("email").getValue();
                        String name = "" + snapshot.child("name").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String userType = "" + snapshot.child("userType").getValue();
                        String location = "" + snapshot.child("location").getValue();
                        String contactNumber = "" + snapshot.child("contactNumber").getValue();

                        binding.contactNumberTv.setText(contactNumber);
                        binding.emailTv.setText(email);
                        binding.usernameTv.setText(name);
                        binding.doctorProfileTv.setText(name + " Profile");


                        Glide.with(Activity_DoctorProfile.this)
                                .load(profileImage)
                                .placeholder(R.drawable.ic_person_dark)
                                .into(binding.profileTv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void doctorreview() {
        modelScheduleArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patientSchedules");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelScheduleArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data_patientSchedules data_patientSchedules = ds.getValue(data_patientSchedules.class);
                    if (data_patientSchedules.getUid().equals(firebaseAuth.getUid().toString())) {
                        modelScheduleArrayList.add(data_patientSchedules);
                        //notify user when logged


                    }

                }
                adapterSchedule = new Helper_Adapter_reviews(Activity_DoctorProfile.this, modelScheduleArrayList);
                binding.reviewsRv.setAdapter(adapterSchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ratings() {
        modelScheduleArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patientSchedules");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelScheduleArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data_patientSchedules data_patientSchedules = ds.getValue(data_patientSchedules.class);
                    if (data_patientSchedules.getUid().equals(firebaseAuth.getUid().toString())) {
                        modelScheduleArrayList.add(data_patientSchedules);
                        //notify user when logged


                    }

                }
                adapterSchedule = new Helper_Adapter_reviews(Activity_DoctorProfile.this, modelScheduleArrayList);
                binding.reviewsRv.setAdapter(adapterSchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}