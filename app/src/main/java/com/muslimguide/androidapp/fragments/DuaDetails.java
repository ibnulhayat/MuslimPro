package com.muslimguide.androidapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.muslimguide.androidapp.App.AppController;
import com.muslimguide.androidapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DuaDetails extends Fragment {

    String id;
    TextView txt_dua_details_name;


    public DuaDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dua_details, container, false);
        id=getArguments().getString("catId");
        txt_dua_details_name=view.findViewById(R.id.txt_dua_details_name);
        getDetails();

        return view;
    }

    private void getDetails() {
        String link="http://192.168.1.26/islamic-app/dua.php?getCat="+id;
        final JsonArrayRequest request=new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject object=response.getJSONObject(i);
                        String content=object.getString("content");
                        txt_dua_details_name.setText(content);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("CATCH_ERROR",e.getMessage());
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY_ERROR",error.getMessage());
            }
        });
        AppController.getInstance().addToRequest(request);
    }

}
