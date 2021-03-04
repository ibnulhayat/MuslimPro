package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muslimguide.androidapp.FullSuraActivity;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.Sura;

import java.util.ArrayList;
import java.util.List;

public class SuraListAdapter extends RecyclerView.Adapter<SuraListAdapter.ViewHolder> {
    private List<Sura> suraList;
    private Context context;

    public SuraListAdapter(List<Sura> suraList) {
        this.suraList = suraList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sura_list,viewGroup,false);
        context  = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Sura items = suraList.get(i);

        final String num = String.valueOf(items.getNumber());
        final String name = items.getName();
        final String engName = items.getEnglishName();
        final String englishNameMeaning = items.getEnglishNameTranslation();
        final String numOfAtahs = String.valueOf(items.getNumberOfAyahs());

        viewHolder.setSuraDetails(num,engName,name,englishNameMeaning,numOfAtahs);

        viewHolder.layoutSuraList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Sura NO: " + num, Toast.LENGTH_SHORT).show();
                Intent ayahIntent = new Intent(context, FullSuraActivity.class);
                ayahIntent.putExtra("SURA_NO",num);
                ayahIntent.putExtra("SURA_NAME",engName);
                ayahIntent.putExtra("ARABIC_NAME",name);
                context.startActivity(ayahIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return suraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber,tvEnglishName,tvMeaning,tvArabicName;
        LinearLayout layoutSuraList;
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            layoutSuraList = mView.findViewById(R.id.layoutSuraList);
        }

        public void setSuraDetails(String num,String engName,String arabicName,String meaning,String numOfAtahs){
            tvNumber = mView.findViewById(R.id.tvNumber);
            tvNumber.setText(num + ".");

            tvEnglishName = mView.findViewById(R.id.tvEnglishName);
            tvEnglishName.setText(engName);

            tvMeaning = mView.findViewById(R.id.tvMeaning);
            tvMeaning.setText(meaning + "(" +numOfAtahs + ")");

            tvArabicName = mView.findViewById(R.id.tvArabicName);
            tvArabicName.setText(arabicName);

        }
    }

    //for search
    public void filter(ArrayList<Sura> suraName) {
        suraList = new ArrayList<>();
        suraList.clear();
        suraList.addAll(suraName);
        notifyDataSetChanged();
    }
}
