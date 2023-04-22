package com.medicapp.medicappprojectcomp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.medicapp.medicappprojectcomp.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textTitle.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

    }
}