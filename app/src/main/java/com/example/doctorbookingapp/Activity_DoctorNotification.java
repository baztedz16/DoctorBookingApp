package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doctorbookingapp.databinding.DoctorDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_DoctorNotification extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DoctorDashboardBinding binding;

    private ArrayList<data_patientSchedules> modelScheduleArrayList;
    private Helper_AdapterSchedule_doctors adapterSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DoctorDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        checkUser();
        loadSchedules();
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
                progressDialog.setMessage("Signing out. Please wait");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                startActivity(new Intent(Activity_DoctorNotification.this, Activity_Login.class));
            }
        });
        binding.profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_DoctorNotification.this, Activity_DoctorProfile.class));
            }
        });
        binding.addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_DoctorNotification.this, Activity_DoctorAddSchedule.class));
            }
        });
    }

    private void loadSchedules() { //default from the intatiate ng scredd

        modelScheduleArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patientSchedules");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelScheduleArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data_patientSchedules data_patientSchedules = ds.getValue(data_patientSchedules.class);
                    if (data_patientSchedules.getSeen() == "0") {
                        modelScheduleArrayList.add(data_patientSchedules);
                    }

                }
                adapterSchedule = new Helper_AdapterSchedule_doctors(Activity_DoctorNotification.this, modelScheduleArrayList);
                binding.schedulesRv.setAdapter(adapterSchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, Activity_Login.class));
            finish();
        } else {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String profileImage = "" + snapshot.child("profileImage").getValue();
                            Glide.with(Activity_DoctorNotification.this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.ic_person_dark)
                                    .into(binding.profileIv);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }
}