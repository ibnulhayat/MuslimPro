package com.muslimguide.androidapp.homePageFragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.adapters.ZikirSliderAdapter;
import com.muslimguide.androidapp.common.Common;
import com.muslimguide.androidapp.database.SQLiteHelper;
import com.muslimguide.androidapp.fragments.SavedFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TazbiFragment extends Fragment {
    ViewPager viewPager;
    TabLayout indicator;

    List<String> zikirEng;
    List<String> zikirArabic;
    List<String> zikirBangla;
    int[] tazbiImageList=new int[]{
      R.drawable.tazbi_3,
      R.drawable.tazbi_2,
      R.drawable.tazbi_3,
      R.drawable.tazbi_2
    };

    Button btn_tazbi_33_99,btn_tazbi_reset,btn_tazbi_save_file;
    boolean type_33=false;

    TextView txt_tazbi_max,txt_tazbi_count,txt_tazbi_repeat;
    ImageView img_tazbi;

    int count=0,repeat=0,img_count=0,maxCount=0;

    String zikirName;
    public static SQLiteHelper sqLiteHelper;
    ArrayList<String> dbDates;
    boolean matches=false;



    public TazbiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tazbi, container, false);

        // Toolbar visible
        Common.toolbarVisibility("visible","Tazbi",1);


        sqLiteHelper=new SQLiteHelper(getContext(),"ZikirDB.sqlite",null,1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS ZIKIR (Id INTEGER PRIMARY KEY AUTOINCREMENT, mDate VARCHAR, zikirName VARCHAR, timeCount VARCHAR, timeLimit VARCHAR)");
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS TBLDATE (Id INTEGER PRIMARY KEY AUTOINCREMENT, mDate)");
        dbDates=new ArrayList<>();

        btn_tazbi_33_99=view.findViewById(R.id.btn_tazbi_33_99);
        btn_tazbi_reset=view.findViewById(R.id.btn_tazbi_reset);
        btn_tazbi_save_file=view.findViewById(R.id.btn_tazbi_save_file);

        txt_tazbi_max=view.findViewById(R.id.txt_tazbi_max);
        txt_tazbi_count=view.findViewById(R.id.txt_tazbi_count);
        txt_tazbi_repeat=view.findViewById(R.id.txt_tazbi_repeat);

        img_tazbi=view.findViewById(R.id.img_tazbi);




        viewPager=view.findViewById(R.id.viewPager);
        indicator=view.findViewById(R.id.indicator);



        zikirArabic = new ArrayList<>();
        zikirArabic.add("سبحان الله");
        zikirArabic.add("الحمد لله");
        zikirArabic.add("لا إله إلا الله");
        zikirArabic.add("الله أكبر");

        zikirEng = new ArrayList<>();
        zikirEng.add("Subhan Allah");
        zikirEng.add("Alhamdulillah");
        zikirEng.add("La ilaaha illa Allah");
        zikirEng.add("Allahu Akbar");

        zikirBangla = new ArrayList<>();
        zikirBangla.add("সুবহানাল্লাহ");
        zikirBangla.add("আলহামদুলিল্লাহ");
        zikirBangla.add("লা ইলাহা ইল্লা আল্লাহ");
        zikirBangla.add("আল্লাহু আকবার");


        viewPager.setAdapter(new ZikirSliderAdapter(getContext(), zikirArabic,zikirEng,zikirBangla));
        indicator.setupWithViewPager(viewPager, true);

        zikirName=zikirEng.get(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                zikirName=zikirEng.get(viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        btn_tazbi_33_99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDate = new SimpleDateFormat("dd MMM, yyyy").format(Calendar.getInstance().getTime());
                if (count!=0&&maxCount!=0){
                    sqLiteHelper.insertData(mDate, zikirName,String.valueOf(count),String.valueOf(maxCount));
                }
                if (type_33==false){
                    btn_tazbi_33_99.setText("33");
                    txt_tazbi_max.setText("33");
                    txt_tazbi_count.setText("0");
                    txt_tazbi_repeat.setText("");
                    img_tazbi.setImageResource(R.drawable.tazbi_1);
                    repeat=0;
                    count=0;
                    img_count=0;
                    type_33=true;
                }
                else {
                    btn_tazbi_33_99.setText("99");
                    txt_tazbi_max.setText("99");
                    txt_tazbi_count.setText("0");
                    txt_tazbi_repeat.setText("");
                    img_tazbi.setImageResource(R.drawable.tazbi_1);
                    repeat=0;
                    count=0;
                    img_count=0;
                    type_33=false;
                }
            }
        });

        txt_tazbi_count.setText("0");
        txt_tazbi_max.setText("99");

        btn_tazbi_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_tazbi_33_99.setText("99");
                txt_tazbi_max.setText("99");
                txt_tazbi_count.setText("0");
                txt_tazbi_repeat.setText("");
                img_tazbi.setImageResource(R.drawable.tazbi_1);
                repeat=0;
                count=0;
                img_count=0;
                type_33=false;
            }
        });


        img_tazbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maxCountText=txt_tazbi_max.getText().toString();
                maxCount=Integer.parseInt(maxCountText);
                if (count<maxCount){
                    count++;
                    txt_tazbi_count.setText(String.valueOf(count));

                }
                else if (count==maxCount){
                    repeat++;
                    count=0;
                    txt_tazbi_repeat.setText("("+String.valueOf(repeat)+")");
                    txt_tazbi_count.setText("0");
                    img_count=0;
                }
                else
                    return;
                if (img_count<tazbiImageList.length){
                    img_tazbi.setImageResource(tazbiImageList[img_count]);
                    img_count++;

                }
                else if (img_count==tazbiImageList.length){
                    img_tazbi.setImageResource(R.drawable.tazbi_3);
                    img_count=1;
                }
            }
        });

        btn_tazbi_save_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SavedFragment());
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        String mDate = new SimpleDateFormat("dd MMM, yyyy").format(Calendar.getInstance().getTime());
        if (count!=0&&maxCount!=0){
            sqLiteHelper.insertData(mDate, zikirName,String.valueOf(count),String.valueOf(maxCount));
        }
    }
    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.tazbi_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
