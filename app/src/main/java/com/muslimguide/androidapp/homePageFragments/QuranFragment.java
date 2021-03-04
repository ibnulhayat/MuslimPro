package com.muslimguide.androidapp.homePageFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.karan.churi.PermissionManager.PermissionManager;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.App.AppController;
import com.muslimguide.androidapp.FullSuraActivity;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.adapters.SuraListAdapter;
import com.muslimguide.androidapp.common.Common;
import com.muslimguide.androidapp.database.QuranDBHelper;
import com.muslimguide.androidapp.models.Sura;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuranFragment extends Fragment {
    private RecyclerView rvSuraList;
    private SuraListAdapter suraListAdapter;
    private List<Sura> suraList;
    private ProgressDialog progressDialog;
    private EditText etSeatch;
    private LinearLayout lastReadPositionLayout;
    private static final String MY_PREFS_NAME="MuslimPro";
    private TextView tvSuraName,tvArabicName,tvSuraPosition;
    private String suraNo,suraName,suraArabicName;
    private int lastPos;

    public static QuranDBHelper quranDBHelper;
    private Cursor cursor;

    private PermissionManager permission;


    private View view;

    public QuranFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quran, container, false);
        // Toolbar visible
        Common.toolbarVisibility("visible", "Al-Quran", 1);

//        permission = new PermissionManager() {
//        };
//        permission.checkAndRequestPermissions(getActivity());


        quranDBHelper = new QuranDBHelper(getContext(), "QURANDB.sqlite", null, 1);
        quranDBHelper.queryData("CREATE TABLE IF NOT EXISTS SURANAME(Id INTEGER PRIMARY KEY AUTOINCREMENT, suraNum VARCHAR, arabicName VARCHAR, engName VARCHAR,engMeaning VARCHAR,numOfAyah VARCHAR,relevationType VARCHAR)");


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        suraNo = prefs.getString("suraNo", "");
        suraName = prefs.getString("suraName", "");
        suraArabicName = prefs.getString("suraArabicName", "");
        lastPos = prefs.getInt("ayatPos", 0);

        rvSuraList = view.findViewById(R.id.rvSuraList);
        etSeatch = view.findViewById(R.id.etSeatch);
        tvSuraName = view.findViewById(R.id.tvSuraName);
        tvSuraPosition = view.findViewById(R.id.tvSuraPosition);
        tvArabicName = view.findViewById(R.id.tvArabicName);

        suraList = new ArrayList<>();
        suraListAdapter = new SuraListAdapter(suraList);
        rvSuraList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSuraList.setAdapter(suraListAdapter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Sura Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        lastReadPositionLayout = view.findViewById(R.id.lastReadPositionLayout);

        if (TextUtils.isEmpty(prefs.getString("suraNo", ""))) {
            lastReadPositionLayout.setVisibility(View.GONE);
        } else {
            lastReadPositionLayout.setVisibility(View.VISIBLE);
            tvSuraName.setText(suraName);
            tvArabicName.setText(suraArabicName);
            tvSuraPosition.setText(" (" + String.valueOf(lastPos) + ") ");
        }

        lastReadPositionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullSuraActivity.class);
                intent.putExtra("SURA_NO", suraNo);
                intent.putExtra("SURA_NAME", suraName);
                intent.putExtra("ARABIC_NAME", suraArabicName);
                intent.putExtra("AYAT_POS", lastPos);
                startActivity(intent);
            }
        });


        etSeatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchData(s.toString());
            }
        });

        cursor = quranDBHelper.getSuraList("SELECT * FROM SURANAME");
        if (cursor.getCount() == 0) {
            parseAllSura();
        } else {
            getDataFromDatabase();
        }

        return view;
    }


    private void getDataFromDatabase() {

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String suraNum = cursor.getString(1);
            String arabicName = cursor.getString(2);
            String englishName = cursor.getString(3);
            String engMeaning = cursor.getString(4);
            String numOfAyah = cursor.getString(5);
            String relavationType = cursor.getString(6);
            suraList.add(new Sura(Integer.parseInt(suraNum), Integer.parseInt(numOfAyah), arabicName, englishName, engMeaning, relavationType));
        }
        suraListAdapter.notifyDataSetChanged();
    }

    public void searchData(String text) {
        if (!text.equals("")) {
            final ArrayList <Sura> searchName = new ArrayList <>();
            for (Sura model : suraList) {
                if (model.getEnglishName().toLowerCase().contains(text.toLowerCase())) {
                    searchName.add(model);
                }
            }
            suraListAdapter.filter(searchName);
        }
    }

    private void parseAllSura() {
        progressDialog.show();
        JsonArrayRequest request = new JsonArrayRequest( Apis.suraList, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i=0;i<response.length();i++) {
                    try {
                        JSONObject suraObj = response.getJSONObject(i);
                        int num = suraObj.getInt("number");
                        String name = suraObj.getString("name");
                        String engName = suraObj.getString("englishName");
                        String englishNameTranslation = suraObj.getString("englishNameTranslation");
                        int numberOfAyahs = suraObj.getInt("numberOfAyahs");
                        String revelationType = suraObj.getString("revelationType");
                        suraList.add(new Sura(num, numberOfAyahs, name, engName, englishNameTranslation, revelationType));
                        quranDBHelper.insertSuraName(String.valueOf(num),name,engName,englishNameTranslation,
                                String.valueOf(numberOfAyahs),revelationType);

                    }catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Toast Error!", Toast.LENGTH_SHORT).show();
                    }
                }
                suraListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Parse Error! : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()){
            if (!isVisibleToUser){

            }
            if (isVisibleToUser){
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                suraNo = prefs.getString("suraNo", "");
                suraName = prefs.getString("suraName", "");
                suraArabicName = prefs.getString("suraArabicName", "");
                lastPos = prefs.getInt("ayatPos", 0);

                lastReadPositionLayout.setVisibility(View.VISIBLE);
                tvSuraName.setText(suraName);
                tvArabicName.setText(suraArabicName);
                tvSuraPosition.setText("("+String.valueOf(lastPos)+")");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permission.checkResult(requestCode,permissions, grantResults);
        //To get Granted Permission and Denied Permission
        ArrayList<String> granted=permission.getStatus().get(0).granted;
        ArrayList<String> denied=permission.getStatus().get(0).denied;
    }
}
