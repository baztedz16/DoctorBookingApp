package com.example.doctorbookingapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.CashInWalletBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Activity_CashInWallet extends AppCompatActivity {

    private final int cash = 0;
    private String cashValue = "", currentValue;
    private CashInWalletBinding binding;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CashInWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cashValue = getIntent().getStringExtra("currentValue").toString();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCashValue();
            }
        });

    }

    private void submitCashValue() {
        currentValue = binding.cashInEt.getText().toString().trim();

        if (TextUtils.isEmpty(cashValue)) {
            Toast.makeText(Activity_CashInWallet.this, "Please enter any amount.", Toast.LENGTH_SHORT).show();
        } else {
            addCashValue(currentValue);
        }
    }

    private void addCashValue(String currentValue) {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String uid = firebaseAuth.getUid();

        Double newvalue;
        newvalue = Double.parseDouble(currentValue) + Double.parseDouble(cashValue);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("timestamp", timestamp);
        hashMap.put("cash", newvalue);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet Amount");
        databaseReference.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar snackbar = Snackbar
                                .make(binding.getRoot(), "Cash in transaction successfully.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar
                                .make(binding.getRoot(), "Cash in transaction unsuccessful.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setBackgroundTint(Color.parseColor("#FFFFFF"));
                    }
                });
    }
}