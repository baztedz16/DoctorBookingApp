package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doctorbookingapp.databinding.PatientDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_PatientDashboard extends AppCompatActivity {

    EditText searchEt;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private PatientDashboardBinding binding;
    private ArrayList<data_ModelSchedule> modelScheduleArrayList;
    private Helper_AdapterSchedule adapterSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = PatientDashboardBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        searchEt = binding.searchEt;

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        checkUser();
        loadSchedules();


        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadSchedulesSearch(searchEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
                progressDialog.setMessage("Signing out. Please wait");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                startActivity(new Intent(Activity_PatientDashboard.this, Activity_Login.class));
            }
        });
        binding.profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_PatientDashboard.this, Activity_PatientProfile.class));
            }

        });
        binding.searchVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
                startActivityForResult(intent, 100);
            }
        });
    }


    private void loadSchedules() { //default from the intatiate ng scredd

        modelScheduleArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schedules");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelScheduleArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data_ModelSchedule modelSchedule = ds.getValue(data_ModelSchedule.class);
                    if (modelSchedule.getCount() < 1) {
                        modelScheduleArrayList.add(modelSchedule);
                    }

                }
                adapterSchedule = new Helper_AdapterSchedule(Activity_PatientDashboard.this, modelScheduleArrayList);
                binding.schedulesRv.setAdapter(adapterSchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadSchedulesSearch(String valueAdded) {
        Log.i("This is the input", valueAdded);
        modelScheduleArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Schedules");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelScheduleArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data_ModelSchedule modelSchedule = ds.getValue(data_ModelSchedule.class);
                    if (modelSchedule.getAmount().contains(valueAdded)) {
                        modelScheduleArrayList.add(modelSchedule);
                    }

                }
                adapterSchedule = new Helper_AdapterSchedule(Activity_PatientDashboard.this, modelScheduleArrayList);
                binding.schedulesRv.setAdapter(adapterSchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            binding.searchEt.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
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
                            Glide.with(Activity_PatientDashboard.this)
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