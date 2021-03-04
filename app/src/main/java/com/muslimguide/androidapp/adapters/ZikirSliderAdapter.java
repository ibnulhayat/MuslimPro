package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muslimguide.androidapp.R;

import java.util.List;

public class ZikirSliderAdapter extends PagerAdapter {

    private Context context;
    private List<String> zikirArabic;
    private List<String> zikirEng;
    private List<String> zikirBangla;

    public ZikirSliderAdapter(Context context, List<String> zikirArabic, List<String> zikirEng, List<String> zikirBangla) {
        this.context = context;
        this.zikirArabic = zikirArabic;
        this.zikirEng = zikirEng;
        this.zikirBangla = zikirBangla;
    }

    @Override
    public int getCount() {
        return zikirArabic.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView textView = view.findViewById(R.id.textView);
//        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
//        linearLayout.setBackgroundResource(R.drawable.image_asr);

        textView.setText(zikirArabic.get(position)+"\n"+zikirEng.get(position)+"\n"+zikirBangla.get(position));
        textView.setTextColor(Color.BLACK);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}