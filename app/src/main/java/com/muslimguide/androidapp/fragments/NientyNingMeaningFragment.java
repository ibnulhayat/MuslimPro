package com.muslimguide.androidapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.common.Common;

public class NientyNingMeaningFragment extends Fragment {

    private View mView;
    private TextView tvAribiName, tvMeaning;
    private ImageView leftBtn, rightBtn;
    private int counter;

    public NientyNingMeaningFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_nienty_ning_meaning, container, false);

        tvAribiName = mView.findViewById(R.id.tvAribiName);
        tvMeaning = mView.findViewById(R.id.tvMeaning);
        tvAribiName.setText(Common.aribName);
        tvMeaning.setText(Common.nameMeaning);
        counter = Integer.parseInt(Common.number);
        leftBtn = mView.findViewById(R.id.leftBtn);
        rightBtn = mView.findViewById(R.id.rightBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter>=1){
                    String aribiNames = Common.ninetyNine_names.get(counter).getArabicName();
                    String aribiMening = Common.ninetyNine_names.get(counter).getEnMeaning();
                    String nmbr= Common.ninetyNine_names.get(counter).getNumber();
                    tvAribiName.setText(aribiNames);
                    tvMeaning.setText(aribiMening);
                }
                counter--;
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter<=99){
                    String aribiNames = Common.ninetyNine_names.get(counter).getArabicName();
                    String aribiMening = Common.ninetyNine_names.get(counter).getEnMeaning();
                    String nmbr= Common.ninetyNine_names.get(counter).getNumber();
                    tvAribiName.setText(aribiNames);
                    tvMeaning.setText(aribiMening);
                }
                counter++;
            }
        });

        return mView;
    }
}
