package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.RegisterAccountBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Activity_Register extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{6,}" +               //at least 6 characters
            "$");

    private static final Pattern MOBILE_NUMBER = Pattern.compile("^" +
            "(?=.*[0-9])" +
            ".{13,}");

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RegisterAccountBinding binding;
    private String name = "", email = "", password = "", confirmPassword = "", location = "", mobileNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Register.this, Activity_Login.class));
            }
        });

        binding.privacyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Register.this, Activity_PrivacyPolicy.class));
            }
        });
    }

    private void validateData() {
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        confirmPassword = binding.confirmPasswordEt.getText().toString().trim();
        location = binding.addressEt.getText().toString().trim();
        mobileNumber = binding.contactEt.getText().toString().trim();
        binding.contactEt.setText("+63");
        binding.contactEt.setHint("+63");


        if (TextUtils.isEmpty(name)) {
            Snackbar snackbar = Snackbar
                    .make(binding.getRoot(), "Field is empty. Enter your full name.", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Snackbar snackbar = Snackbar
                    .make(binding.getRoot(), "Field is empty. Enter your valid password.", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));

        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Snackbar snackbar = Snackbar
                    .make(binding.getRoot(), "Your password must match and must be at least 6 or more characters!", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));

        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Enter an Confirm Password", Toast.LENGTH_SHORT).show();

        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();

        } else if (password.length() < 6) {
            Snackbar snackbar = Snackbar
                    .make(binding.getRoot(), "Your password must match and must be at least 6 or more characters!", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));

        } else if (TextUtils.isEmpty(location)) {
            Snackbar snackbar = Snackbar
                    .make(binding.getRoot(), "Field is empty. Enter your valid location.", Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));

        } else if (!MOBILE_NUMBER.matcher(mobileNumber).matches()) {
            Toast.makeText(this, "Please input your valid mobile number", Toast.LENGTH_SHORT).show();

        } else if (mobileNumber.length() != 13) {
            Toast.makeText(this, "Please input your valid mobile number", Toast.LENGTH_SHORT).show();

        } else {
            createUserAccount();
        }
    }

    private void createUserAccount() {
        progressDialog.setMessage("Signing up account please wait");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        progressDialog.setTitle("Saving user info");
        long timestamp = System.currentTimeMillis();
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("location", location);
        hashMap.put("contactNumber", mobileNumber);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "Patient");
        hashMap.put("timestamp", timestamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(binding.getRoot(), "Your account is ready. Thank you!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                        startActivity(new Intent(Activity_Register.this, Activity_Login.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}