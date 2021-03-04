package com.muslimguide.androidapp.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.muslimguide.androidapp.HomePage;
import com.muslimguide.androidapp.IslamicProHelper;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.introSlider.SecondSlider;

import java.io.IOException;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;
import static com.muslimguide.androidapp.fragments.NearByMosqueFragment.REQUEST_LOCATION;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompassFragment extends Fragment implements SensorEventListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;


    MapView mMapView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    RotateAnimation ra;

    private Geocoder geocoder;
    public String locationName;
    SharedPreferences settings;

    Handler handler = new Handler();
    Runnable runnable;
    TextView tvComLocationParmition;
    public CompassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        image = view.findViewById(R.id.imageViewCompass);
        mMapView = view.findViewById(R.id.mapView);
        tvComLocationParmition = view.findViewById(R.id.tvComLocationParmition);
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,5000);
                if (checkLocationPermission() == true){
                    onAttach(getContext());
                    handler.removeCallbacks(runnable);
                }else {

                }
            }
        };

        if (checkLocationPermission() == false){
            tvComLocationParmition.setOnClickListener((View v)->{
                checkLocationPermission();
                tvComLocationParmition.setVisibility(View.GONE);
            });
        }else {
            tvComLocationParmition.setVisibility(View.GONE);
        }

        mMapView.onCreate(savedInstanceState);
        createView();
        return view;
    }

    private void createView(){
        handler.postDelayed(runnable,0);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);


    }



        @Override
        public void onDestroy () {
            super.onDestroy();
            mMapView.onDestroy();
        }

        @Override
        public void onLowMemory () {
            super.onLowMemory();
            mMapView.onLowMemory();
        }

        @Override
        public void onResume () {
            super.onResume();
            mMapView.onResume();

            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
        }

        @Override
        public void onPause () {
            super.onPause();
            mMapView.onPause();
            mSensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged (SensorEvent event){
            float degree = Math.round(event.values[0]);
            ra = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(210);
            ra.setFillAfter(true);
            image.startAnimation(ra);
            currentDegree = -degree;

        }

        @Override
        public void onAccuracyChanged (Sensor sensor,int accuracy){
            // not in use
        }

        @Override
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setAllGesturesEnabled(false);
            try {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.map_style));

                if (!success) {
                    Log.d("MAP_STYLE", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.d("MAP_STYLE_FAILD", "Can't find style. Error: ", e);
            }

        }
        public LatLng getLocationFromAddress (Context context, String strAddress){

            Geocoder coder = new Geocoder(context);
            List<Address> address;
            LatLng p1 = null;

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }

                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());

            } catch (IOException ex) {

                ex.printStackTrace();
                Log.d("CATCH_ERROR", ex.getMessage());
            }

            return p1;
        }


        @Override
        public void onStart () {
            super.onStart();
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }

        @Override
        public void onStop () {
            super.onStop();
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }

        @Override
        public void onConnected (@Nullable Bundle bundle){
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startLocationUpdates();
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation == null) {
                startLocationUpdates();
            }
            if (mLocation != null) {
                double c_lat = mLocation.getLatitude();
                double c_lng = mLocation.getLongitude();

                double m_lat = 21.3891;
                double m_lng = 39.8579;

                LatLng current = new LatLng(c_lat, c_lng);
                LatLng macca = new LatLng(m_lat, m_lng);

                geocoder = new Geocoder(getContext());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(c_lat, c_lng, 1);
                    locationName = addresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.d("LAT_LNG", c_lat + " " + c_lat);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(current)
                        .title(locationName)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                MarkerOptions markerOptionsMacca = new MarkerOptions()
                        .position(macca)
                        .title("Macca")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.kaaba_shorif_medium));
                PolylineOptions polylineOptions = new PolylineOptions().add(current).add(macca).width(8).color(Color.BLACK).geodesic(true);
                mMap.addMarker(markerOptions);
                mMap.addMarker(markerOptionsMacca);
                mMap.addPolyline(polylineOptions);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(current).bearing(0).zoom(2).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            } else {

                double m_lat = 21.3891;
                double m_lng = 39.8579;
                LatLng macca = new LatLng(m_lat, m_lng);
                settings = getActivity().getSharedPreferences(IslamicProHelper.PREFS_NAME, Context.MODE_PRIVATE);
                String city = settings.getString(IslamicProHelper.USER_CITY, "");

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(getLocationFromAddress(getContext(), city))
                        .title(city)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                MarkerOptions markerOptionsMacca = new MarkerOptions()
                        .position(macca)
                        .title("Macca")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.kaaba_shorif_medium));
                PolylineOptions polylineOptions = new PolylineOptions().add(getLocationFromAddress(getContext(), city)).add(macca).width(8).color(Color.BLACK).geodesic(true);
                mMap.addMarker(markerOptions);
                mMap.addMarker(markerOptionsMacca);
                mMap.addPolyline(polylineOptions);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(getLocationFromAddress(getContext(), city)).bearing(0).zoom(2).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

        protected void startLocationUpdates () {
            // Create the location request
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1000)
                    .setFastestInterval(1000);
            // Request location updates
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
            Log.d("reque", "--->>>>");
        }

        @Override
        public void onConnectionSuspended ( int i){
            Log.d("CUNNECTION_SUSPEND", "SUSPEND");
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
            Log.d("CUNNECTION_FAILD", "FAILD");
        }

        @Override
        public void onLocationChanged (Location location){

        }


        // Checking Location Permission
        public boolean checkLocationPermission () {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.title_location_permission)
                            .setMessage(R.string.text_location_permission)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                            SecondSlider.REQUEST_LOCATION);

                                }
                            })
                            .create()
                            .show();


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            SecondSlider.REQUEST_LOCATION);

                }
                return false;
            } else {
                return true;
            }

        }

}