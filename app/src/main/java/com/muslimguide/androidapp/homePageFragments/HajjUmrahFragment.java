package com.muslimguide.androidapp.homePageFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.common.Common;

public class HajjUmrahFragment extends Fragment {

    private View mView;

    public HajjUmrahFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hajj_umrah, container, false);

        // Toolbar visible
        Common.toolbarVisibility("visible","",0);


        return mView;
    }

}
