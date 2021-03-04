package com.muslimguide.androidapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.muslimguide.androidapp.App.AppController;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.adapters.RecyclerDuaAdpater;
import com.muslimguide.androidapp.models.DuaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DuaFragment extends Fragment {

    private RecyclerView recyclerViewDua;
    private RecyclerDuaAdpater duaAdpater;
    private ArrayList<DuaItem> duaItems;


    public DuaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dua, container, false);
        duaItems=new ArrayList<>();
        recyclerViewDua=view.findViewById(R.id.recycler_duaList);
        recyclerViewDua.setLayoutManager(new LinearLayoutManager(getContext()));
        duaAdpater=new RecyclerDuaAdpater(getContext(), duaItems, new RecyclerDuaAdpater.OnItemClickListener() {
            @Override
            public void onItemClick(DuaItem item, int pos) {
                Bundle bundle=new Bundle();
                bundle.putString("catId", item.getDuaId());
                //set Fragmentclass Arguments
                DuaDetails duaDetails=new DuaDetails();
                duaDetails.setArguments(bundle);
                setFragment(duaDetails);
            }
        });
        recyclerViewDua.setAdapter(duaAdpater);
        getDuaItem();
        return view;
    }

    private void getDuaItem() {
        String link="http://192.168.1.26/islamic-app/dua.php?cat";
        JsonArrayRequest request=new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject object=response.getJSONObject(i);
                        String catId=object.getString("catId");
                        String name=object.getString("name");
                        String image=object.getString("image");
                        duaItems.add(new DuaItem(catId,name,image));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("CATCH_ERROR",e.getMessage());
                    }

                }
                duaAdpater.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY_ERROR",error.getMessage());
            }
        });
        AppController.getInstance().addToRequest(request);
    }
    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dua_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
