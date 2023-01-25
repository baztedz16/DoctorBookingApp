package com.example.doctorbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.SplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_SplashScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private SplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> checkUser(), 1850);
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(Activity_SplashScreen.this, Activity_Login.class));
            finish();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userType = "" + snapshot.child("userType").getValue();
                            if (userType.equals("Patient")) {
                                startActivity(new Intent(Activity_SplashScreen.this, Activity_PatientDashboard.class));
                                finish();
                            } else if (userType.equals("Doctor")) {
                                startActivity(new Intent(Activity_SplashScreen.this, Activity_DoctorDashboard.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }
}