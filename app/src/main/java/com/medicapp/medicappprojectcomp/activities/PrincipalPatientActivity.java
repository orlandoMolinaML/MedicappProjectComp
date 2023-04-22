package com.medicapp.medicappprojectcomp.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.medicapp.medicappprojectcomp.R;
import com.medicapp.medicappprojectcomp.databinding.ActivityPrincipalPatientBinding;
import com.medicapp.medicappprojectcomp.fragments.ChatFragment;
import com.medicapp.medicappprojectcomp.fragments.MainDeportFragment;
import com.medicapp.medicappprojectcomp.fragments.MapFragment;
import com.medicapp.medicappprojectcomp.fragments.MedicalDiagnosticFragment;
import com.medicapp.medicappprojectcomp.fragments.ProfileFragment;
import com.medicapp.medicappprojectcomp.servicies.LocationService;
import com.medicapp.medicappprojectcomp.servicies.PermissionService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PrincipalPatientActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityPrincipalPatientBinding binding;

    @Inject
    PermissionService permissionService;

    @Inject
    LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();
        binding.navMenu.setNavigationItemSelectedListener(this);
        binding.navMenu.bringToFront();

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

        switch (item.getItemId()) {
            case android.R.id.home:
               binding.drawerLayout.openDrawer(GravityCompat.START);
               return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_chat:
                getSupportActionBar().hide();
                fragment = new ChatFragment();
                fragmentTransaction = true;
                break;
            case R.id.menu_profile:
                getSupportActionBar().hide();
                fragment = new ProfileFragment();
                fragmentTransaction = true;
                break;
            case R.id.menu_point:
                getSupportActionBar().show();
                fragment = new MapFragment();
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
                fragment = new MainDeportFragment();
                fragmentTransaction = true;
                break;
        }
        if (fragmentTransaction) {
            changeFragment(fragment, item);
            binding.drawerLayout.closeDrawers();
        }
        return true;
    }

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

}