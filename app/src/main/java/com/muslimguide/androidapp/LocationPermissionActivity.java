package com.muslimguide.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.muslimguide.androidapp.adapters.MyFragmentPagerAdapter;
import com.muslimguide.androidapp.common.Common;

import java.util.ArrayList;

public class LocationPermissionActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    private MyFragmentPagerAdapter sliderAdapter;
    private RadioGroup mPageGroup;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewPager=findViewById(R.id.viewPager);
        sliderAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),this,3);
        viewPager.setAdapter(sliderAdapter);
        mPageGroup = findViewById(R.id.page_group);
        settings=getSharedPreferences(IslamicProHelper.PREFS_NAME, Context.MODE_PRIVATE);
        if (!settings.getString(IslamicProHelper.USER_CITY,"").equals("")){
            startActivity(new Intent(getApplicationContext(),SplashScreenActivity.class));
            finish();
        }

        if (Common.permission.equals("request")||!TextUtils.isEmpty(Common.placeName)){
            viewPager.setCurrentItem(1);
            int radioButtonId = mPageGroup.getChildAt(1).getId();
            mPageGroup.check(radioButtonId);
        }

        mPageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton =mPageGroup.findViewById(checkedId);
                int index = mPageGroup.indexOfChild(checkedRadioButton);
                viewPager.setCurrentItem((index));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int radioButtonId = mPageGroup.getChildAt(i).getId();
                mPageGroup.check(radioButtonId);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


}
