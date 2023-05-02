package com.medicapp.medicappprojectcomp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.medicapp.medicappprojectcomp.R;
import com.medicapp.medicappprojectcomp.databinding.FragmentMedicalDiagnosticBinding;


public class MedicalDiagnosticFragment extends Fragment {

    FragmentMedicalDiagnosticBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMedicalDiagnosticBinding.inflate(inflater);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageFood.setVisibility(View.INVISIBLE);
        binding.imageSport.setVisibility(View.INVISIBLE);
        binding.buttonMore.setOnClickListener(buttonListener);
        binding.imageSport.setOnClickListener(imageSportListener);
        binding.imageFood.setOnClickListener(imageFoodListener);
        binding.buttonAdd.setOnClickListener(buttonAddIns);
        binding.buttonAddG.setOnClickListener(buttonAddGlu);
        binding.buttonRemember.setOnClickListener(buttonRemember);
    }
    View.OnClickListener buttonRemember=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                changeFragment(new ReminderFragment());
        }
    };
    View.OnClickListener buttonListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            binding.imageFood.setVisibility(View.VISIBLE);
            binding.imageSport.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener buttonAddIns=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeFragment(new RegistryInsulinFragment());
        }
    };

    View.OnClickListener buttonAddGlu=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeFragment(new RegistryGlucoseFragment());
        }
    };
    View.OnClickListener imageSportListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeFragment(new MainDeportFragment());
        }
    };

    View.OnClickListener imageFoodListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           changeFragment(new FoodMainFragment());
        }
    };
    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}