package com.muslimguide.androidapp.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.adapters.RecyclerSaveAdapter;
import com.muslimguide.androidapp.database.SQLiteHelper;
import com.muslimguide.androidapp.models.SavedItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment {
    private RecyclerView listView;
    private RecyclerSaveAdapter adapter;
    private ArrayList<SavedItem> savedItems;
    private ArrayList<String> dateItem;

    SQLiteHelper sqLiteHelper;
    String cDate;


    public SavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        sqLiteHelper = new SQLiteHelper(getContext(), "ZikirDB.sqlite", null, 1);
        savedItems=new ArrayList<>();
        dateItem=new ArrayList<>();
        listView = view.findViewById(R.id.recyclerSavedItem);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        listView.setLayoutManager(mLayoutManager);
        adapter = new RecyclerSaveAdapter(getContext(), savedItems,dateItem);
        listView.setAdapter(adapter);

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM ZIKIR");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String mDate = cursor.getString(1);
            String zikir = cursor.getString(2);
            String t_count = cursor.getString(3);
            String max_count = cursor.getString(4);
            dateItem.add(mDate);
            savedItems.add(new SavedItem(mDate,zikir + "\t\t\t\t\t\t\t" + t_count + "/" + max_count));

        }
        adapter.notifyDataSetChanged();

        return view;
    }
}