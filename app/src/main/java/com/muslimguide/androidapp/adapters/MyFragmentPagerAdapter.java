package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.muslimguide.androidapp.introSlider.FirstSlider;
import com.muslimguide.androidapp.introSlider.SecondSlider;
import com.muslimguide.androidapp.introSlider.ThirdSlider;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    int tabCount;

    public MyFragmentPagerAdapter(FragmentManager fm, Context mContext, int tabCount) {
        super(fm);
        this.mContext = mContext;
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment mFragment = null;
        switch (i){
            case 0:
                mFragment = new FirstSlider();
                break;
            case 1:
                mFragment = new SecondSlider();
                break;
            default:
                return new ThirdSlider();
        }
        return mFragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        switch (position) {
//            case 0:
//                return "INTERNATIONAL";
//            case 1:
//                return "LEAGUE";
//            default:
//                return null;
//        }
//    }
}
