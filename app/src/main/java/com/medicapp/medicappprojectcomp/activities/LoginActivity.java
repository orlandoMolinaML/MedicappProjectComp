package com.medicapp.medicappprojectcomp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.medicapp.medicappprojectcomp.databinding.ActivityLoginBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity  extends BaseActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSend.setOnClickListener(v -> {
            startActivity(new Intent(this, PrincipalPatientActivity.class));
        });
    }
}