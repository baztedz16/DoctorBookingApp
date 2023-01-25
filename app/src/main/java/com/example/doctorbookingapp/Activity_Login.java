package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.LoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String email = "", password = "";
    private LoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Login.this, Activity_Register.class));
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        binding.forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Login.this, Activity_ForgotPassword.class));
            }
        });
    }

    private void validateData() {

        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar snackbar = Snackbar
                    .make(binding.getRoot(), "Enter your valid email address.", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
        } else if (TextUtils.isEmpty(password)) {
            Snackbar snackbar = Snackbar
                    .make(binding.getRoot(), "Enter your valid password.", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
        } else {
            loginUser();
        }
    }

    private void loginUser() {

        progressDialog.setMessage("Signing in Please Wait");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        binding.passwordEt.getText().clear();
                        Snackbar snackbar = Snackbar
                                .make(binding.getRoot(), "The credentials you entered isn’t connected to an account. Create a new account.", 7000)
                                .setAction("REGISTER", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Activity_Login.this, Activity_Register.class));
                                    }
                                });
                        snackbar.show();
                        snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                    }
                });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();

                        String userType = "" + snapshot.child("userType").getValue();
                        Log.i("val:", snapshot.child("verification").getValue().toString());
//                        if(firebaseUser.isEmailVerified()){
                        if (userType.equals("Patient")) {

                            startActivity(new Intent(Activity_Login.this, Activity_PatientDashboard.class));
                            finish();
                        } else if (userType.equals("Doctor")) {

                            startActivity(new Intent(Activity_Login.this, Activity_DoctorDashboard.class));
                            finish();
                        }

//                        }else{
//                            Snackbar snackbar = Snackbar
//                                    .make(binding.getRoot(), "Please Verify your Account! Check Email", Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                            firebaseUser.sendEmailVerification();
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}