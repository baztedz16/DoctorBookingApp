package com.example.doctorbookingapp;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorbookingapp.databinding.PrivacyPolicyBinding;

public class Activity_PrivacyPolicy extends AppCompatActivity {

    private PrivacyPolicyBinding binding;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webView = binding.privacyHtml;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.privacypolicies.com/live/a706bd71-fbab-4a58-a76d-dbadc2e22ba7");

        binding.backBtn.setOnClickListener(v -> onBackPressed());
    }
}