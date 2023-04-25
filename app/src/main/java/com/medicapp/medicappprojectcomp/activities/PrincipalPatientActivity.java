package com.medicapp.medicappprojectcomp.activities;

import static java.text.DateFormat.getDateInstance;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.medicapp.medicappprojectcomp.Interfaces.IFragment;
import com.medicapp.medicappprojectcomp.R;
import com.medicapp.medicappprojectcomp.databinding.ActivityPrincipalPatientBinding;
import com.medicapp.medicappprojectcomp.databinding.FragmentProfileBinding;
import com.medicapp.medicappprojectcomp.fragments.ChatFragment;

import com.medicapp.medicappprojectcomp.fragments.FoodMainFragment;
import com.medicapp.medicappprojectcomp.fragments.MapFragment;
import com.medicapp.medicappprojectcomp.fragments.MedicalDiagnosticFragment;
import com.medicapp.medicappprojectcomp.fragments.ProfileFragment;
import com.medicapp.medicappprojectcomp.servicies.LocationService;
import com.medicapp.medicappprojectcomp.servicies.PermissionService;
import com.medicapp.medicappprojectcomp.fragments.MainDeportFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PrincipalPatientActivity extends BaseActivity implements IFragment {
    ActivityPrincipalPatientBinding binding;

    FragmentProfileBinding bindingProfile;

    @Inject
    PermissionService permissionService;

    @Inject
    LocationService locationService;

	FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    private static final String TAG = PrincipalPatientActivity.class.getName();
    private Uri picturePath = null;
    private Bitmap bitMap=null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;

    static final int RESULT_LOAD_IMAGE = 3;
    static final int RESULT_LOAD_VIDEO = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalPatientBinding.inflate(getLayoutInflater());
        bindingProfile = FragmentProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();
		binding.navBar.setOnNavigationItemSelectedListener( bottomItemSelectedListener);
        binding.navBar.bringToFront();
        binding.navMenu.setNavigationItemSelectedListener(onNavigationItemSelected);
        binding.navMenu.bringToFront();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            loadFragment(item);
            return true;
        }
    };

    private void loadFragment(MenuItem item) {
        Fragment fragment = null;
        boolean fragmentTransaction = false;
        switch (item.getItemId()){
            case R.id.navigation_chat:
            case R.id.menu_chat:
                binding.navBar.setVisibility(View.VISIBLE);
                getSupportActionBar().hide();
                fragment = new ChatFragment();
                fragmentTransaction = true;
                break;
            case R.id.navigation_point:
            case R.id.menu_point:
                binding.navBar.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
                fragment = new MapFragment();
                fragmentTransaction = true;
                break;
            case R.id.menu_profile:
                binding.navBar.setVisibility(View.VISIBLE);
                getSupportActionBar().hide();
                fragment = new ProfileFragment();
                fragmentTransaction = true;
                break;
            case R.id.menu_diagnostics:
                getSupportActionBar().show();
                binding.navBar.setVisibility(View.INVISIBLE);
                fragment = new MedicalDiagnosticFragment();
                fragmentTransaction = true;
                break;
            case R.id.menu_food:
                getSupportActionBar().show();
                binding.navBar.setVisibility(View.INVISIBLE);
                fragment = new FoodMainFragment();
                fragmentTransaction = true;
                break;
            case R.id.menu_exit:
                exit();
                break;
        }
        if (fragmentTransaction) {
             changeFragment(fragment, item);
            binding.drawerLayout.closeDrawers();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseAuth.getCurrentUser() == null){
            exit();
        } else {
            user = firebaseAuth.getCurrentUser();
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void changeFragment(Fragment fragment, MenuItem item) {
        int commit = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                fragment = new ProfileFragment();
                fragmentTransaction = true;
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    NavigationView.OnNavigationItemSelectedListener  onNavigationItemSelected= new NavigationView.OnNavigationItemSelectedListener () {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            loadFragment(item);
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        permissionService.getLocationPermission(this);
        if (permissionService.isMLocationPermissionGranted()) {
            locationService.startLocation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.stopLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionService.PERMISSIONS_REQUEST_LOCATION) {
            permissionService.getLocationPermission(this);
            if (permissionService.isMLocationPermissionGranted()) {
               locationService.startLocation();
            }
        }
    }

    public void exit(){
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

	@Override
    public void load() {
        Toast.makeText(this, "INICIAR PRI ", Toast.LENGTH_SHORT).show();
        clickTake();
    }

    private void clickTake() {
        if (!permissionService.isMCameraPermissionGranted()) {
            permissionService.getCameraPermission(this);
        } else {
            takePhoto();
        }

    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String time = getDateInstance().format(new Date());
        String fileName = String.format("%s.jpg", time);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fileName);
        picturePath = FileProvider.getUriForFile(this, "com.medicapp.medicappprojectcomp.fileprovider", file);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picturePath);

        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitMap=null;
            loadImage();
        } else if (requestCode == RESULT_LOAD_IMAGE &&  RESULT_OK == resultCode && data != null) {
            picturePath= data.getData();
            try {
                bitMap = MediaStore.Images.Media.getBitmap( this.getContentResolver(),picturePath);
                bitMap=Bitmap.createScaledBitmap(bitMap,1000,1200,false);
                loadImage();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void loadImage() {
        bindingProfile.layoutVideo.removeAllViews();
        ImageView img= (ImageView) findViewById(R.id.imageUser);
        img.setImageURI(picturePath);
       /*
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        if(bitMap==null){
            imageView.setImageURI(picturePath);
        }else{
            imageView.setImageBitmap(bitMap);
        }
        imageView.getLayoutParams().height=10;
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        bindingProfile.layoutVideo.addView(imageView);

        */
    }

}
