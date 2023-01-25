package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doctorbookingapp.databinding.PatientProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_PatientProfile extends AppCompatActivity {

    String currentValue;
    private PatientProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private ArrayList<data_patientSchedules> modelScheduleArrayList;
    private Helper_AdapterSchedule_history adapterSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PatientProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        binding.usernameTv.setText("N/A");
        binding.emailTv.setText("N/A");
        binding.clientProfileTv.setText("N/A");
        binding.contactNumberTv.setText("N/A");
        binding.currentBalanceTv.setText("0");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadUserDetails();
        loadCurrentWallet();
        loadSchedules();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_PatientProfile.this, Activity_EditProfile.class));
            }
        });

        binding.cashInCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_CashInWallet.class);
                intent.putExtra("currentValue", currentValue);
                startActivity(intent);
            }
        });
    }

    private void loadCurrentWallet() {

        String uid = firebaseAuth.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet Amount");
        databaseReference.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String cashValue = "" + snapshot.child("cash").getValue();
                        currentValue = cashValue;
                        binding.currentBalanceTv.setText("Balance: " + "₱" + cashValue + ".00");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadUserDetails() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
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

                        binding.emailTv.setText(email);
                        binding.usernameTv.setText(name);
                        binding.clientProfileTv.setText(name + " Profile");
                        binding.locationTv.setText(location);
                        binding.contactNumberTv.setText(contactNumber);

                        Glide.with(Activity_PatientProfile.this)
                                .load(profileImage)
                                .placeholder(R.drawable.ic_person_dark)
                                .into(binding.profileTv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
                    if (data_patientSchedules.getPatientid().equals(firebaseAuth.getUid().toString())) {
                        modelScheduleArrayList.add(data_patientSchedules);
                        //notify user when logged


                    }

                }
                adapterSchedule = new Helper_AdapterSchedule_history(Activity_PatientProfile.this, modelScheduleArrayList);
                binding.appointmentRv.setAdapter(adapterSchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}