package com.example.doctorbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.CheckEmailBinding;

public class Activity_CheckEmail extends AppCompatActivity {

    private CheckEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CheckEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_CheckEmail.this, Activity_Login.class));
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_CheckEmail.this, Activity_Login.class));
            }
        });
    }
}