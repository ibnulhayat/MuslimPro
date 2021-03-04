package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.DuaItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerDuaAdpater extends RecyclerView.Adapter<RecyclerDuaAdpater.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DuaItem item,int pos);
    }

    private Context mContext;
    private ArrayList<DuaItem> duaItems;
    private final OnItemClickListener listener;

    public RecyclerDuaAdpater(Context mContext, ArrayList<DuaItem> duaItems, OnItemClickListener listener) {
        this.mContext = mContext;
        this.duaItems = duaItems;
        this.listener = listener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_dua,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DuaItem duaItem=duaItems.get(i);
        String duaName=duaItem.getDuaName();
        String duaImage=duaItem.getDuaImage();

        viewHolder.txt_dua_name.setText(duaName);
        Picasso.get().load(duaImage).into(viewHolder.imgDuaImage);
        viewHolder.bind(duaItems.get(i), listener);

    }

    @Override
    public int getItemCount() {
        return duaItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_dua_name;
        ImageView imgDuaImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_dua_name=itemView.findViewById(R.id.txt_dua_name);
            imgDuaImage=itemView.findViewById(R.id.img_dua_photo);
        }
        public void bind(final DuaItem item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item,getAdapterPosition());
                }
            });
        }

    }
}
