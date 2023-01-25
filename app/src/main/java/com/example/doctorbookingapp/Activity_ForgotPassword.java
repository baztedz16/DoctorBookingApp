package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.ForgotPasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Activity_ForgotPassword extends AppCompatActivity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^" +
            "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+");

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private String email = "";
    private ForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ForgotPasswordBinding.inflate(getLayoutInflater());
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

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        firebaseUser = firebaseAuth.getCurrentUser();
        email = binding.emailEt.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Enter your valid email", Toast.LENGTH_SHORT).show();
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(Activity_ForgotPassword.this, Activity_CheckEmail.class));
            recoverPassword();
        }
    }

    private void recoverPassword() {
        progressDialog.setMessage("Sending password recovery to your " + email);
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_ForgotPassword.this, "Reset password sent to your " + email, Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_ForgotPassword.this, "Failed to send due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}