package com.muslimguide.androidapp.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.muslimguide.androidapp.App.IslamicProHelper;
import com.muslimguide.androidapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteFinder extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        RoutingListener {
    MapView mMapView;
    private com.google.android.gms.maps.GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;

    private double item_lat;
    private double item_lng;
    int[] COLORS = new int[]{R.color.sample_primary};
    List<Polyline> polylines;

    double c_lat,c_lng;




    String placeName;
    public RouteFinder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_google_map, container, false);
        polylines = new ArrayList<>();
        placeName=getArguments().getString("placeName");
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        item_lat = getLatFromAddress(getContext(),placeName);
        item_lng = getLngFromAddress(getContext(),placeName);


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

        mMapView.getMapAsync(this);

        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
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
//        mMap.getUiSettings().setScrollGesturesEnabled(false);
//        mMap.getUiSettings().setAllGesturesEnabled(false);
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


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            c_lat = mLocation.getLatitude();
            c_lng = mLocation.getLongitude();
            getRouteToShop(item_lat, item_lng,mLocation);


//            LatLng current=new LatLng(c_lat,c_lng);
//            LatLng des=new LatLng(item_lat,item_lng);
//
//            MarkerOptions markerOptions=new MarkerOptions()
//                    .position(current)
//                    .title("Your Location")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//            MarkerOptions markerOptionsDes=new MarkerOptions()
//                    .position(des)
//                    .title(placeName)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque));
//            mMap.addMarker(markerOptions);
//            mMap.addMarker(markerOptionsDes);
//            CameraPosition cameraPosition=new CameraPosition.Builder()
//                    .target(current).bearing(0).zoom(5).build();
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // Start marker
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(c_lat,c_lng)).title("Your Location");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(options);
            mMap.addMarker(options).showInfoWindow();


        }
    }

    protected void startLocationUpdates() {
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
    public void onConnectionSuspended(int i) {
        Log.d("CUNNECTION_SUSPEND", "SUSPEND");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("CUNNECTION_FAILD", "FAILD");
    }

    @Override
    public void onLocationChanged(Location location) {

    }
    private void getRouteToShop(double c_lat, double c_lng, Location mLocation) {
        Log.d("DES_LAT_LNG",item_lat+" "+item_lng);
        LatLng start = new LatLng(this.mLocation.getLatitude(), this.mLocation.getLongitude());
        LatLng end = new LatLng(item_lat, item_lng);
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(start, end)
                .key(IslamicProHelper.MAP_KEY)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Something was wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        LatLng start=new LatLng(c_lat,c_lng);
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
        }
        // End marker
        LatLng end=new LatLng(item_lat,item_lng);
        MarkerOptions options;
        options = new MarkerOptions();
        options.position(end).title(placeName);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.nearby_mosque));
        mMap.addMarker(options);
        CameraPosition cameraPosition=new CameraPosition.Builder()
                .target(new LatLng(c_lat,c_lng)).bearing(0).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                marker.showInfoWindow();
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
//                return true;
//            }
//        });

    }

    @Override
    public void onRoutingCancelled() {

    }
    private void erasePolylines(){
        for(Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        erasePolylines();
    }
}
