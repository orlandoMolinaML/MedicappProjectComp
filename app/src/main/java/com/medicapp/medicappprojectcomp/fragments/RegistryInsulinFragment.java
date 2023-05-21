package com.medicapp.medicappprojectcomp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.medicapp.medicappprojectcomp.R;
import com.medicapp.medicappprojectcomp.databinding.FragmentRegistryInsulinBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistryInsulinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistryInsulinFragment extends Fragment {
    FragmentRegistryInsulinBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistryInsulinBinding.inflate(inflater);
        addCalendatInputText();
        addTimeCalendar();
        return binding.getRoot();
    }

    private void addTimeCalendar() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void addCalendatInputText() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialogBirthday = new DatePickerDialog(getContext(),
                R.style.datePickerDialog,(view, year1, monthOfYear, dayOfMonth) -> {
            String monthZero = String.format("%02d", (monthOfYear + 1));
            String dayZero = String.format("%02d", dayOfMonth);
            String selectedDate =  year1+"-"+ monthZero + "-" +dayZero;
            binding.dateEditText.setText(selectedDate);
        }, year, month, day);

        binding.dateEditText.setOnClickListener(v -> datePickerDialogBirthday.show());
        binding.dateEditText.setInputType(InputType.TYPE_NULL);
    }

}