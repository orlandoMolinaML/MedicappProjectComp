package com.medicapp.medicappprojectcomp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.medicapp.medicappprojectcomp.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecoveryPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
    }
}