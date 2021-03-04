package com.muslimguide.androidapp.introSlider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.karan.churi.PermissionManager.PermissionManager;
import com.muslimguide.androidapp.LocationPermissionActivity;
import com.muslimguide.androidapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstSlider extends Fragment {

    Button btnChange;
    PermissionManager permission;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final static int REQUEST_LOCATION = 199;




    public FirstSlider() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_first_slider, container, false);
        btnChange=view.findViewById(R.id.btn_changePage);
        btnChange.setTextColor(getResources().getColor(R.color.colorPrimary));

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationPermissionActivity.viewPager.setCurrentItem(1);

//                if (checkLocationPermission()){
//                    if (btnChange.getText().equals("NEXT")){
//
//                    }
//                    else {
//                        checkLocationPermission();
//                    }
//
//                }
//                else {
//                    checkLocationPermission();
//                }

            }
        });

        return view;
    }
//    // Checking Location Permission
//    public boolean checkLocationPermission() {
//        btnChange.setText("NEXT");
//        btnChange.setTextColor(getResources().getColor(R.color.colorPrimary));
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                new AlertDialog.Builder(getContext())
//                        .setTitle(R.string.title_location_permission)
//                        .setMessage(R.string.text_location_permission)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        REQUEST_LOCATION);
//                            }
//                        })
//                        .create()
//                        .show();
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (checkLocationPermission()){
//
//            }
//            else {
//                checkLocationPermission();
//            }
//        } else if (resultCode == RESULT_CANCELED) {
//            //finish();
//        }
//
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(getContext(),
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(getContext(), "onRequestPermissionsResult Granted", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    checkLocationPermission();
//
//                }
//                return;
//            }
//
//        }
//    }


}
