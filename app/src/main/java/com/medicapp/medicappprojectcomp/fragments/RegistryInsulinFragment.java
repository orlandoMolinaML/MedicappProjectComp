package com.medicapp.medicappprojectcomp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.medicapp.medicappprojectcomp.databinding.FragmentRegistryInsulinBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistryInsulinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistryInsulinFragment extends Fragment {
    FragmentRegistryInsulinBinding binding;
    Calendar calendar;
    Integer year;
    Integer month;
    Integer day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        binding.dateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistryInsulinBinding.inflate(inflater);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout textDate=binding.dateTextInput;
        calendar=Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        textDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
            }
        });
    }
}