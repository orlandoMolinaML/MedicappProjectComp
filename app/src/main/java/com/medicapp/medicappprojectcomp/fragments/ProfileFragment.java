package com.medicapp.medicappprojectcomp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.medicapp.medicappprojectcomp.Interfaces.IFragment;
import com.medicapp.medicappprojectcomp.servicies.PermissionService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.medicapp.medicappprojectcomp.activities.PrincipalPatientActivity;
import com.medicapp.medicappprojectcomp.databinding.FragmentProfileBinding;
import com.medicapp.medicappprojectcomp.models.Patient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;


public class ProfileFragment extends Fragment {
    @Inject
    PermissionService permissionService;

    private FragmentProfileBinding binding;
    ImageView buttonCamera;
    Activity activity;
    IFragment interfFragment;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        buttonCamera = binding.buttonCamera;
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfFragment.load();
            }
        });

        View root = binding.getRoot();
        validUser();
        getProfile();
        return root;
    }

    private void getProfile() {
        DatabaseReference databaseRef = database.getReference("patients");
        Query query = databaseRef.orderByChild("email").equalTo(user.getEmail().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot element = snapshot.getChildren().iterator().next();
                Patient patient = new Patient();
                patient.setLastName((String) element.child("lastName").getValue());
                patient.setName((String) element.child("name").getValue());
                patient.setEmail((String) element.child("email").getValue());
                try {
                    patient.setBirthday(new SimpleDateFormat("yyyy-mm-dd").parse((String) element.child("birthday").getValue()));
                    patient.setDateDiagnostic(new SimpleDateFormat("yyyy-mm-dd").parse((String) element.child("dateDiagnostic").getValue()));

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                patient.setHeight((String) element.child("height").getValue());
                patient.setWeight((String) element.child("weight").getValue());
                loadView(patient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }

    private void loadView(Patient patient) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        binding.name.getEditText().setText(patient.getName());
        binding.userName.getEditText().setText(patient.getEmail());
        binding.birthday.getEditText().setText(dateFormat.format(patient.getBirthday()));
        binding.dateDiagnostic.getEditText().setText(dateFormat.format(patient.getDateDiagnostic()));
        binding.height.getEditText().setText(patient.getHeight());
        binding.weight.getEditText().setText(patient.getWeight());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        validUser();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            activity  = (Activity)  context;
            interfFragment = (IFragment) activity;

        }
    }

    private void validUser() {
        if(firebaseAuth.getCurrentUser() == null){
            if (this.getContext() instanceof PrincipalPatientActivity) {
                PrincipalPatientActivity activity = (PrincipalPatientActivity) getContext();
                activity.exit();
            }
        } else {
            user = firebaseAuth.getCurrentUser();
        }
    }

    private void clickAdd() {
        boolean change=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if ( !permissionService.isMPhotoPermission() && !permissionService.isMVideoPermission()) {
                permissionService.getReadPhotosAndVideo(getActivity());
            } else {
                change=true;
            }
        } else {
            if(!permissionService.isMReadExternalStoragePermissionGranted()){
                permissionService.getReadExternalStoragePermission(getActivity());
            }else {
                change=true;
            }
        }
        /*
        if(change){
            if (binding.toggleVideo.isChecked()) {
                Intent intentImage = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentImage, RESULT_LOAD_VIDEO);
            } else {
                Intent intentImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentImage, RESULT_LOAD_IMAGE);
            }
        }
        */

    }
}
