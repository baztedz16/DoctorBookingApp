package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.EditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_EditProfile extends AppCompatActivity {

    private EditProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = EditProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                String uid = firebaseAuth.getUid();

                if (!binding.email.getText().toString().contains("@gmail.com")) {
                    Snackbar snackbar = Snackbar
                            .make(binding.getRoot(), "Please enter Valid Email!!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                } else if (!binding.pass2.getText().toString().equals(binding.pass3.getText().toString())) {
                    Snackbar snackbar = Snackbar
                            .make(binding.getRoot(), "Password Mismatch!!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                } else if (binding.pass2.getText().toString().isEmpty() || binding.pass3.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(binding.getRoot(), "Please Fillup Password!!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                } else {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(firebaseUser.getEmail(), binding.curpass.getText().toString());
                    firebaseAuth.getCurrentUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().updatePassword(binding.pass2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Message", "Password updated");

                                                } else {
                                                    Log.d("Message", "Error password not updated");
                                                }
                                            }
                                        });
//                                        firebaseAuth.getCurrentUser().updateEmail(binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    firebaseAuth.signOut();
//                                                    startActivity(new Intent(Activity_EditProfile.this, Activity_Login.class));
//                                                } else {
//                                                    Log.d("Message", "Error password not updated");
//                                                }
//                                            }
//
//                                        });

                                    } else {
                                        Log.d("Message", "Error auth failed");
                                    }
                                }
                            });
                }


//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
//                databaseReference.child(uid)
//                        .setValue(hashMap)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Snackbar snackbar = Snackbar
//                                        .make(binding.getRoot(), "Cash in transaction successfully.", Snackbar.LENGTH_LONG);
//                                snackbar.show();
//                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                                onBackPressed();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Snackbar snackbar = Snackbar
//                                        .make(binding.getRoot(), "Cash in transaction unsuccessful.", Snackbar.LENGTH_LONG);
//                                snackbar.show();
//                                snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
//                            }
//                        });
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}