package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.NinetyNine_Names;

import java.util.List;

public class NinetyNine_NamesAdapter extends RecyclerView.Adapter<NinetyNine_NamesAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(NinetyNine_Names item, int pos);
    }

    private List<NinetyNine_Names> ninetyNine_names;
    private Context mContext;
    private final OnItemClickListener listener;

    public NinetyNine_NamesAdapter(List<NinetyNine_Names> ninetyNine_names, Context context,OnItemClickListener mlistener) {
        this.ninetyNine_names = ninetyNine_names;
        this.mContext = context;
        this.listener = mlistener;
    }

    @NonNull
    @Override
    public NinetyNine_NamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ninety_nine_item_view, viewGroup, false);
        mContext = viewGroup.getContext();

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NinetyNine_NamesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(ninetyNine_names.get(i), listener,i);

        final String number = ninetyNine_names.get(i).getNumber();
        final String aribicName = ninetyNine_names.get(i).getArabicName();
        final String englishName = ninetyNine_names.get(i).getEnglishName();
        final String banglaName = ninetyNine_names.get(i).getEnglishName();
        final String nameMeaning = ninetyNine_names.get(i).getEnMeaning();

        viewHolder.tvNumber.setText(number);
        viewHolder.tvAribicName.setText(aribicName);
        viewHolder.tvEnglishName.setText(englishName);
        viewHolder.tvBanglaName.setText(banglaName);

    }

    @Override
    public int getItemCount() {
        return ninetyNine_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumber, tvAribicName, tvEnglishName, tvBanglaName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvAribicName = itemView.findViewById(R.id.tvAribicName);
            tvEnglishName = itemView.findViewById(R.id.tvEnglishName);
            tvBanglaName = itemView.findViewById(R.id.tvBanglaName);
        }

        public void bind(final NinetyNine_Names item, final OnItemClickListener listener, final int pos) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item,pos);
                }
            });
        }
    }
}
