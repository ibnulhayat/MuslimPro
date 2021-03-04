package com.muslimguide.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.muslimguide.androidapp.common.Common;

import java.util.Arrays;
import java.util.List;

public class PlaceSearchActivity extends AppCompatActivity {
    AutocompleteFilter typeFilter;


    AutocompleteSupportFragment autocompleteSupportFragment;
    PlacesClient placesClient;
    List<Place.Field> placeFeilds = Arrays.asList(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_place_search);

        Places.initialize(this, getString(R.string.placeApiKey));

// Create a new Places client instance.
        placesClient = Places.createClient(this);


        typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setTypeFilter(3)
                .build();
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_location_pickup);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i("PLACENAME", "Place: " + place.getName() + ", " + place.getLatLng());
                Common.placeName = place.getName();

                startActivity(new Intent(PlaceSearchActivity.this, LocationPermissionActivity.class));
                finish();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("PLACE_ERROR", "An error occurred: " + status);
            }
        });

    }
}
