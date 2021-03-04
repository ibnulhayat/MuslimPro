package com.muslimguide.androidapp.introSlider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.muslimguide.androidapp.IslamicProHelper;
import com.muslimguide.androidapp.LocationPermissionActivity;
import com.muslimguide.androidapp.PlaceSearchActivity;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.common.Common;

import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SecondSlider extends Fragment {

    TextView user_city_name;
    Button buttonLocationPermission, btnChange;
    private ProgressBar progress_circular;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public final static int REQUEST_LOCATION = 199;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private Geocoder geocoder;
    public String locationName = "null";
    private double latitude, longitude;
    Handler handler = new Handler();
    Runnable runnable;
    private int isClick = 0;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                try {
                    geocoder = new Geocoder(getContext());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    locationName = addresses.get(0).getLocality();
                    settings = getContext().getSharedPreferences(IslamicProHelper.PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    String city = settings.getString(IslamicProHelper.USER_CITY, "");
                    if (!city.equals(locationName)) {
                        editor.putString(IslamicProHelper.USER_CITY, locationName);
                        editor.putString(IslamicProHelper.USER_LAT, String.valueOf(latitude));
                        editor.putString(IslamicProHelper.USER_LNG, String.valueOf(longitude));
                        editor.commit();
                    }
                    Log.d("LOCNAME_GOOGLE", addresses.get(0).getLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ERROR_HERE", e.getMessage());
                }

                Log.d("LOCATION_UPDATE_CURRENT", String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                mLocationManager.removeUpdates(mLocationListener);
            } else {
                Log.d("REMOVE_CURRENT_LOCATION", "Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private LocationManager mLocationManager;


    SharedPreferences settings;
    SharedPreferences.Editor editor;


    public SecondSlider() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second_slider, container, false);

        settings = getActivity().getSharedPreferences(IslamicProHelper.PREFS_NAME, Context.MODE_PRIVATE);
        editor=settings.edit();
        buttonLocationPermission = view.findViewById(R.id.buttonLocationPermission);
        user_city_name = view.findViewById(R.id.user_city_name);
        btnChange = view.findViewById(R.id.btn_changePage);
        progress_circular = view.findViewById(R.id.progress_circular);


        if (!TextUtils.isEmpty(Common.placeName)){

            user_city_name.setText(Common.placeName);
            btnChange.setVisibility(View.VISIBLE);
            btnChange.setTextColor(getResources().getColor(R.color.colorPrimary));
            editor.putString(IslamicProHelper.USER_LAT,getLatitudeFromAddress(getContext(),Common.placeName));
            editor.putString(IslamicProHelper.USER_LNG,getLogitudeFromAddress(getContext(),Common.placeName));

            editor.commit();
        }

        user_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PlaceSearchActivity.class));
                getActivity().finish();
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocationPermissionActivity.viewPager.setCurrentItem(2);
                if (user_city_name.getText().length() != 0) {
                    buttonLocationPermission.setEnabled(false);
                    editor.putString(IslamicProHelper.USER_CITY, user_city_name.getText().toString());
                    editor.commit();
                    LocationPermissionActivity.viewPager.setCurrentItem(2);
                } else if (Common.city.equals("GPS") || !settings.getString(IslamicProHelper.USER_CITY, "").equals("")) {
                    LocationPermissionActivity.viewPager.setCurrentItem(2);
                } else {
                    Toast.makeText(getContext(), "You must need to turn on your location or input your city", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonLocationPermission.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);;
                displayLocationSettingsRequest(getContext());
                checkLocationPermission();

                buttonLocationPermission.setVisibility(View.INVISIBLE);
                progress_circular.setVisibility(View.VISIBLE);
                isClick = 1;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this,5000);
                        if (isClick != 0) {
                            if (locationName.equals("null")) {
                                getCurrentLocation();
                            } else {
                                progress_circular.setVisibility(View.GONE);
                                user_city_name.setText(locationName);
                                user_city_name.setVisibility(View.VISIBLE);
                                user_city_name.setEnabled(false);
                                handler.removeCallbacks(runnable);
                                btnChange.setVisibility(View.VISIBLE);
                                btnChange.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        }

                    }
                };
                handler.postDelayed(runnable,0);
            }
        });


        return view;
    }
    public String getLogitudeFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        String lng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            lng=String.valueOf(location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            Log.d("CATCH_ERROR",ex.getMessage());
        }

        return lng;
    }


    public String getLatitudeFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        String lat = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            lat=String.valueOf(location.getLatitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            Log.d("CATCH_ERROR",ex.getMessage());
        }

        return lat;
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Toast.makeText(getContext(), "Please turn on your GPS", Toast.LENGTH_SHORT).show();
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
        }
        if (location != null) {
            Log.d("LOCATION_CURRENT_MANAGE", String.format("%f, %f", location.getLatitude(), location.getLongitude()));
        }
    }

    // Checking Location Permission
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    //Check GPS ON/OFF State and Request to On it
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("SUCCESS", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("REQUIRED", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("FAILED", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("UNAVAILABLE", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (checkLocationPermission()) {
//                startActivity(new Intent(LocationPermissionActivity.this,HomeActivity.class));
//                finish();
            } else {
                checkLocationPermission();
            }
        } else if (resultCode == RESULT_CANCELED) {
            //finish();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "onRequestPermissionsResult Granted", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    checkLocationPermission();
                }
                return;
            }

        }
    }


}
