package com.muslimguide.androidapp.fragments;


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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.App.AppController;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.Refresh;
import com.muslimguide.androidapp.SplashScreenActivity;
import com.muslimguide.androidapp.adapters.RecyclerMosqueAdapter;
import com.muslimguide.androidapp.common.Common;
import com.muslimguide.androidapp.helper.NoInternet;
import com.muslimguide.androidapp.models.NearestMostItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByMosqueFragment extends Fragment {

    RecyclerView recyclerMosque;
    RecyclerMosqueAdapter mosqueAdapter;
    ArrayList<NearestMostItem> nearestMostItems;
    SharedPreferences settings;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final static int REQUEST_LOCATION = 199;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;

    private Geocoder geocoder;
    public String locationName;
    private double user_lat,user_lng;
    Handler handler = new Handler();
    Runnable runnable;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                user_lat=location.getLatitude();
                user_lng=location.getLongitude();
                Common.lat=user_lat;
                Common.lat=user_lng;
                Log.d("LOCATION_UPDATE_CURRENT", String.format("%f, %f", user_lat, user_lng));
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
    private RelativeLayout relativeLayout;




    public NearByMosqueFragment() {
        // Required empty public constructor
    }



//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (this.isVisible()) {
//            if (!isVisibleToUser) {
//                loaderView.setVisibility(View.GONE);
//
//            }
//            if (isVisibleToUser) {
//                if (NoInternet.isConnected(getContext())) {
//                    noInternetConnection.setVisibility(View.GONE);
//                    rvLiveScore.setVisibility(View.VISIBLE);
//                    rvLiveScore.setAdapter(favTeamLiveScoreAdapter);
//                    NewsFragment.fbNativeAction("fbnative");
//
//                } else {
//                    noInternetConnection.setVisibility(View.VISIBLE);
//                    rvLiveScore.setVisibility(View.GONE);
//                }
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_near_by_mosque, container, false);
        nearestMostItems=new ArrayList<>();
        recyclerMosque=view.findViewById(R.id.recycler_mosque);
        relativeLayout=view.findViewById(R.id.loading_layout);

        Common.toolbarVisibility("visible","Near Mosque",1);

        if (checkLocationPermission()){
            mLocationManager = (LocationManager)getActivity(). getSystemService(Context.LOCATION_SERVICE);
            getCurrentLocation();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this,5000);
                    if (!nearestMostItems.isEmpty()){
                        handler.removeCallbacks(runnable);
                        Log.e("TRUMMETHOD", "run: 1" );
                        relativeLayout.setVisibility(View.GONE);
                    }else {
                        mLocationManager = (LocationManager)getActivity(). getSystemService(Context.LOCATION_SERVICE);
                        getCurrentLocation();
                        Log.e("TRUMMETHOD", "run: 2" );
                        getMosqueData(user_lat,user_lng);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }
            };
            handler.postDelayed(runnable,0);
        } else {
            displayLocationSettingsRequest(getContext());
            checkLocationPermission();
            final Handler handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this,5000);
                    if (!nearestMostItems.isEmpty()){
                        handler.removeCallbacks(runnable);
                        Log.e("TRUMMETHOD_2", "run: 1" );
                        relativeLayout.setVisibility(View.GONE);
                    }else {
                        mLocationManager = (LocationManager)getActivity(). getSystemService(Context.LOCATION_SERVICE);
                        getCurrentLocation();
                        Log.e("TRUMMETHOD_2", "run: 2" );
                        getMosqueData(user_lat,user_lng);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }
            };
            handler.postDelayed(runnable,0);
        }


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerMosque.setLayoutManager(mLayoutManager);
        mosqueAdapter=new RecyclerMosqueAdapter(getContext(), nearestMostItems, new RecyclerMosqueAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NearestMostItem item, int pos) {
//                Bundle bundle=new Bundle();
//                bundle.putString("placeName", item.getPlaceName());
//                //set Fragmentclass Arguments
//                RouteFinder googleMap=new RouteFinder();
//                googleMap.setArguments(bundle);
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction =
//                        fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_container, googleMap);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                double item_lat;
                double item_lng;
                item_lat = getLatFromAddress(getContext(),item.getPlaceName());
                item_lng = getLngFromAddress(getContext(),item.getPlaceName());
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir//"+item_lat+","+item_lng+"/@"+item_lat+","+item_lng+",12z")));
            }
        });
        recyclerMosque.setAdapter(mosqueAdapter);
        return view;
    }
    public double getLatFromAddress(Context context, String strAddress) {
        double returnLat = 0;
        Geocoder geocoder = new Geocoder(context,context.getResources().getConfiguration().locale);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(strAddress, 1);
            Address address = addresses.get(0);

            returnLat = address.getLatitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnLat;
    }
    public double getLngFromAddress(Context context, String strAddress) {
        double returnlng = 0;
        Geocoder geocoder = new Geocoder(context,context.getResources().getConfiguration().locale);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(strAddress, 1);
            Address address = addresses.get(0);

            returnlng = address.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnlng;
    }
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


    private void getMosqueData(double latitude, double longitude) {

        String link= Apis.BASEUrl +"mosque.php?lat="+String.valueOf(latitude)+"&long="+String.valueOf(longitude)+"&radius=800";
        Log.d("LAT_LNG",String.valueOf(latitude)+" "+String.valueOf(longitude));

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ("OK".equals(response.getString("status")));
                    JSONArray results=response.getJSONArray("results");
                    for (int i=0;i<results.length();i++){
                        JSONObject restItem=results.getJSONObject(i);
                        String place_id=restItem.getString("place_id");
                        String name=restItem.getString("name");
                        String icon=restItem.getString("icon");
                        float distance=getLocationFromAddress(getContext(),name);
                        if (distance!=0&&distance<100){
                            nearestMostItems.add(new NearestMostItem(name,String.format("%.1f", distance)+" km",icon));
                        }

                    }

                    mosqueAdapter.notifyDataSetChanged();
                   if (!nearestMostItems.isEmpty()){
                       relativeLayout.setVisibility(View.GONE);
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("CATCH_ERROR",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley_ERROR",""+error.getMessage());
            }
        });
        AppController.getInstance().addToRequest(request);
    }
    public float getLocationFromAddress(Context context, String strAddress) {
        float distanceInMeters = 0;
        Geocoder geocoder = new Geocoder(context,context.getResources().getConfiguration().locale);
        List<Address> addresses = null;
        LatLng latLng = null;
        try {
            addresses = geocoder.getFromLocationName(strAddress, 1);
            Address address = addresses.get(0);
            Location loc1 = new Location("");
            loc1.setLatitude(user_lat);
            loc1.setLongitude(user_lng);

            Location loc2 = new Location("");
            loc2.setLatitude(address.getLatitude());
            loc2.setLongitude(address.getLongitude());

            distanceInMeters = loc1.distanceTo(loc2)/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distanceInMeters;
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)){
            displayLocationSettingsRequest(getContext());
        }

        else {
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
            user_lat=location.getLatitude();
            user_lng=location.getLongitude();
            Common.lat=user_lat;
            Common.lat=user_lng;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (checkLocationPermission()){

            }
            else {
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
