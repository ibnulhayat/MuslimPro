package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.SavedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.muslimguide.androidapp.homePageFragments.TazbiFragment.sqLiteHelper;

public class RecyclerSaveAdapter extends RecyclerView.Adapter<RecyclerSaveAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SavedItem> savedItems;
    private ArrayList<String> dateItems;
    private boolean isOverride=false;


    public RecyclerSaveAdapter(Context mContext, ArrayList<SavedItem> savedItems, ArrayList<String> dateItems) {
        this.mContext = mContext;
        this.savedItems = savedItems;
        this.dateItems = dateItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_zikir,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SavedItem savedItem=savedItems.get(i);
        String mDate=dateItems.get(i);
        String zikir_1=savedItem.getZikir_name();
        String cDate= new SimpleDateFormat("dd MMM, yyyy").format(Calendar.getInstance().getTime());
        
        String dDate = null;

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM TBLDATE");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            dDate = cursor.getString(1);


        }

        if (TextUtils.isEmpty(dDate)){
            dDate=cDate;
        }
        if (cDate.equals(mDate)){
            viewHolder.mDate.setText("Today");
        }
        else {
            viewHolder.mDate.setText(mDate);
            viewHolder.mDate.setVisibility(View.GONE);
            viewHolder.txt_zikir_1.setVisibility(View.GONE);
        }


        if (mDate.equals(cDate)){
            if (isOverride==false){
                Toast.makeText(mContext, ""+isOverride, Toast.LENGTH_SHORT).show();
                viewHolder.mDate.setVisibility(View.VISIBLE);
                sqLiteHelper.insertMdata(cDate);
                isOverride=true;
            }
            else {
                Toast.makeText(mContext, ""+isOverride, Toast.LENGTH_SHORT).show();
                viewHolder.mDate.setVisibility(View.GONE);
            }
            viewHolder.txt_zikir_1.setText(zikir_1);


        }else {
            viewHolder.txt_zikir_1.setText(zikir_1);
        }

    }

    @Override
    public int getItemCount() {
        return savedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDate,txt_zikir_1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate=itemView.findViewById(R.id.txt_date_text);
            txt_zikir_1=itemView.findViewById(R.id.txt_subhanaAllah);
        }
    }
}
