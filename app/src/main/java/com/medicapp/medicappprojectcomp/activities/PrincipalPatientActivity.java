package com.medicapp.medicappprojectcomp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.medicapp.medicappprojectcomp.R;
import com.medicapp.medicappprojectcomp.databinding.ActivityPrincipalPatientBinding;
import com.medicapp.medicappprojectcomp.fragments.ChatFragment;
import com.medicapp.medicappprojectcomp.fragments.FoodMainFragment;
import com.medicapp.medicappprojectcomp.fragments.MapFragment;
import com.medicapp.medicappprojectcomp.fragments.MedicalDiagnosticFragment;
import com.medicapp.medicappprojectcomp.fragments.ProfileFragment;
import com.medicapp.medicappprojectcomp.servicies.LocationService;
import com.medicapp.medicappprojectcomp.servicies.PermissionService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PrincipalPatientActivity extends BaseActivity{
    ActivityPrincipalPatientBinding binding;
    @Inject
    PermissionService permissionService;
    @Inject
    LocationService locationService;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalPatientBinding.inflate(getLayoutInflater());
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

}