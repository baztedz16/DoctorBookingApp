package com.example.doctorbookingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.PatientNewsfeedBinding;

public class Activity_PatientNewsfeed extends AppCompatActivity {

    private PatientNewsfeedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PatientNewsfeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}