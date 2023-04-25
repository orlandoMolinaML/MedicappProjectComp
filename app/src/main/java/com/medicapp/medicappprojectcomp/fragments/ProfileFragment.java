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
import com.medicapp.medicappprojectcomp.databinding.FragmentProfileBinding;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    @Inject
    PermissionService permissionService;
    private FragmentProfileBinding binding;
    ImageView buttonCamera;
    Activity activity;
    IFragment interfFragment;

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            activity  = (Activity)  context;
            interfFragment = (IFragment) activity;

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
