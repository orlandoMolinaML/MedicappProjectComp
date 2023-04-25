package com.medicapp.medicappprojectcomp.fragments;

import static android.content.Context.LOCATION_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.medicapp.medicappprojectcomp.R;
import com.medicapp.medicappprojectcomp.adapters.AdapterPointMap;
import com.medicapp.medicappprojectcomp.databinding.FragmentMapBinding;
import com.medicapp.medicappprojectcomp.models.PositionMap;
import com.medicapp.medicappprojectcomp.servicies.GeocoderService;
import com.medicapp.medicappprojectcomp.servicies.LocationService;
import com.medicapp.medicappprojectcomp.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends Fragment {

    FragmentMapBinding binding;

    @Inject
    LocationService locationService;
    @Inject
    GeocoderService geocoderService;

    //Map interaction variables
    GoogleMap googleMap;
    static final int INITIAL_ZOOM_LEVEL = 18;
    Marker userPosition;
    Polyline userRoute;
    //light sensor
    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorEventListener;
    private LocationManager locationManager;
    Marker marker;
    LatLng latLngUser;
    private static AdapterPointMap adapter;
    List<PositionMap> points;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap map) {

           /* SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);null
            */
            //Setup the map

            googleMap = map;
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM_LEVEL));
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);

            userRoute = googleMap.addPolyline(new PolylineOptions()
                    .color(Color.RED)
                    .width(30.0f)
                    .geodesic(true));
            getLocation();
            loadHospital();
            //Setup the rest of the markers based in a json file

        }
    };

    @SuppressLint("MissingPermission")
    private void getLocation() {
        LocationServices.getFusedLocationProviderClient(getContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null) {
                    latLngUser = new LatLng(location.getLatitude(), location.getLongitude());
                    userPosition = googleMap.addMarker(new MarkerOptions()
                            .position(latLngUser)
                            .icon(BitmapUtils.getBitmapDescriptor(getContext(), R.drawable.ic_point))
                            .title("Usted esta aqui!")
                            .snippet("Y otra cosas")
                            .anchor(0.5f, 1.0f)
                            .zIndex(1.0f));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngUser));
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater);
        points=loadInfo();
        adapter= new AdapterPointMap(points);
        RecyclerView recyclerViewPoint=(RecyclerView) binding.listPoints;
        recyclerViewPoint.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPoint.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        binding.addressInput.getEditText().setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH || i== EditorInfo.IME_ACTION_GO){
                filterPoint();
                findPlace();
                return true;
            }
            return false;
        });
    }

    private void filterPoint() {
        points=loadInfo();
        points=points.stream().filter(c->c.getTitle().contains(binding.addressInput.getEditText().getText())).collect(Collectors.toList());;
        adapter.setList(points);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationService.setLocationCallback(new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                    updateUserPositionOnMap(locationResult);
            }
        });

    }

    private void findPlace(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(marker!=null){
                marker.remove();
            }
            try {
                geocoderService.findPlacesByNameInRadius(binding.addressInput.getEditText().getText().toString(), userPosition.getPosition()).forEach(address -> {
                    LatLng lat=new LatLng(address.getLatitude(), address.getLongitude());
                    marker= googleMap.addMarker(new MarkerOptions()
                            .title(address.getFeatureName())
                            .snippet(address.getAddressLine(0))
                            .position(lat)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(@NotNull SensorEvent sensorEvent) {
                if(googleMap != null){
                    if (sensorEvent.values[0] > 10000) {
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_night_style));
                    } else {
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_day_style));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
    }


    public void updateUserPositionOnMap(@NotNull LocationResult locationResult) {
        Location location = locationResult.getLastLocation();
        if (location != null) {
            LatLng lat=new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
            List<LatLng> points = userRoute.getPoints();
            points.add(lat);
            userRoute.setPoints(points);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
        }
    }
    private List<PositionMap> loadInfo(){
        List<PositionMap> pointsMap=new ArrayList<>();
        pointsMap.add(loadPoint(new LatLng(4.8097809252392985, -74.35124623145964),"Cafam EPS","Carrera 12"));
        pointsMap.add(loadPoint(new LatLng(4.806413234619617, -74.34988366933705), "Hospital San Rafael", "Cra 15 N 3"));
        pointsMap.add(loadPoint(new LatLng(4.808244084370125, -74.35322570169836),"Compensar EPS","Calle 9 N 12"));
        pointsMap.add(loadPoint(new LatLng(4.808805365787758, -74.35256051389047),"Estrategico IPS", "Calle 45 N 12"));
        return pointsMap;
    }
    public void loadHospital(){
        points.stream().forEach(p->{
            Address address=geocoderService.findPlacesByLatLng(p.getPosition());
            marker=googleMap.addMarker(new MarkerOptions()
                    .title(p.getTitle())
                    .snippet(address.getAddressLine(0))
                    .position(p.getPosition())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        });
    }

    private PositionMap loadPoint(LatLng lat, String name, String address){
        PositionMap position=new PositionMap();
        position.setPosition(lat);
        position.setTitle(name);
        position.setAddress(address);
        return position;

    }



}
