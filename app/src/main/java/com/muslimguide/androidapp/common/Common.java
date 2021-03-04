package com.muslimguide.androidapp.common;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;


import com.muslimguide.androidapp.HomePage;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.homePageFragments.PrayerFragment;
import com.muslimguide.androidapp.models.NinetyNine_Names;
import com.muslimguide.androidapp.models.Ramadan;

import java.util.List;

public class Common {
    public static String permission="";
    public static String city="";
    public static double lat;
    public static double lng;
    public static String mapKey="AIzaSyBbYnWfoDdGCtyiR0UWBDoXZeat7y7X1Mc";
    public static String placeName="";

    public static List<NinetyNine_Names> ninetyNine_names;
    public static String number;
    public static String aribName;
    public static String nameMeaning;
    public static String f_Name;

    public static String plyTvName;
    public static String plyTvUrl;

    public static boolean isRecreate = false;
    public static List<Ramadan> ramadanList;

    public static void dropDownMenu(String state){

        if ("open".equals(state)){
            PrayerFragment.layoutPrayerTime.setVisibility(View.VISIBLE);
            PrayerFragment.ivRotatedDropdown.setVisibility(View.VISIBLE);
            PrayerFragment.layoutDropdown.setVisibility(View.VISIBLE);
            PrayerFragment.layoutCurrentTime.setVisibility(View.GONE);
            HomePage.constraintLayout.setVisibility(View.GONE);
        }else if ("close".equals(state)){
            PrayerFragment.layoutPrayerTime.setVisibility(View.GONE);
            PrayerFragment.ivRotatedDropdown.setVisibility(View.GONE);
            PrayerFragment.layoutDropdown.setVisibility(View.GONE);
            PrayerFragment.layoutCurrentTime.setVisibility(View.VISIBLE);
            HomePage.constraintLayout.setVisibility(View.VISIBLE);
        }
    }

    public static void toolbarVisibility(String state,String toolBarName,int color){
        if ("visible".equals(state)){
            HomePage.tvToolbar_Name.setVisibility(View.VISIBLE);
            HomePage.tvToolbar_Name.setText(toolBarName);
            HomePage.ivDropdown.setVisibility(View.GONE);
            HomePage.ivNavigation.setVisibility(View.GONE);
            if (color == 1){
                HomePage.constraintLayout.setBackgroundResource(R.color.colorPrimaryDark);
                HomePage.constraintLayout.setVisibility(View.VISIBLE);
            }else if (color == 0){
                HomePage.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
                HomePage.constraintLayout.setVisibility(View.VISIBLE);
            }else {
                HomePage.constraintLayout.setVisibility(View.GONE);
            }
        }else if ("gone".equals(state) && TextUtils.isEmpty("")){
            HomePage.tvToolbar_Name.setVisibility(View.GONE);
            HomePage.tvToolbar_Name.setText(toolBarName);
            HomePage.ivDropdown.setVisibility(View.VISIBLE);
            HomePage.ivNavigation.setVisibility(View.VISIBLE);
            HomePage.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
            HomePage.constraintLayout.setVisibility(View.VISIBLE);
        }
    }

}
