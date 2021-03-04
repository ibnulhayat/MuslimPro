package com.muslimguide.androidapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.adapters.NinetyNine_NamesAdapter;
import com.muslimguide.androidapp.common.Common;
import com.muslimguide.androidapp.models.NinetyNine_Names;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NientyNineNameFragment extends Fragment {

    private View mView;
    private RecyclerView nientyNineNameRecyclerView;
    private List<NinetyNine_Names> ninetyNine_names;
    private NinetyNine_NamesAdapter ninetyNine_namesAdapter;
    private RequestQueue requestQueue;

    //private String url = "http://api.alquran.cloud/v1/surah";

    public NientyNineNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_nienty_nine_name, container, false);

        nientyNineNameRecyclerView = mView.findViewById(R.id.nientyNineNameRecyclerView);
        ninetyNine_names = new ArrayList<>();
        Common.ninetyNine_names = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());

        ninentyNineName();

        return mView;
    }

    private void ninentyNineName() {

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Apis._99NmaesUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int a = 0; a < response.length(); a++) {
                        JSONObject object = response.getJSONObject(a);
                        String arabicName = object.getString("name");
                        String englishName = object.getString("transliteration");
                        String number = object.getString("number");

                        JSONObject en = object.getJSONObject("en");
                        String enMeaning = en.getString("meaning");

                        ninetyNine_names.add(new NinetyNine_Names(arabicName, englishName, number, enMeaning));
                        Common.ninetyNine_names.add(new NinetyNine_Names(arabicName, englishName, number, enMeaning));


                    }
                    ninetyNine_namesAdapter = new NinetyNine_NamesAdapter(ninetyNine_names, getContext(), new NinetyNine_NamesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(NinetyNine_Names item, int pos) {
                            Common.number = item.getNumber();
                            Common.aribName = item.getArabicName();
                            Common.nameMeaning = item.getEnMeaning();
                            Fragment mFragment =  new NientyNingMeaningFragment();
                            mChangeFragment_2(mFragment);
                        }
                    });
                    nientyNineNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    nientyNineNameRecyclerView.setAdapter(ninetyNine_namesAdapter);
                    ninetyNine_namesAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error_view", error.getMessage());
            }
        });

        requestQueue.add(request);

    }

    public void mChangeFragment_2(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_nintinine, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}
