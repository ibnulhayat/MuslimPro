package com.muslimguide.androidapp.introSlider;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.SplashScreenActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdSlider extends Fragment {

    Button btnChange,btnAllow;


    public ThirdSlider() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_third, container, false);
        btnChange=view.findViewById(R.id.btn_changePage);
        btnAllow=view.findViewById(R.id.third_slider_btn_allow_notifications);
        btnChange.setTextColor(getResources().getColor(R.color.colorPrimary));
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), SplashScreenActivity.class));
                getActivity().finish();
            }
        });

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Allow notification", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
