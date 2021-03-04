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
import com.muslimguide.androidapp.models.MakkaLive;

import java.util.List;

public class MakkaLiveAdapter extends RecyclerView.Adapter<MakkaLiveAdapter.ViewHolder> {

    private RecyclerViewClickLister mListener;
    private List<MakkaLive> makkaLives;
    private Context mContext;

    public interface RecyclerViewClickLister{
        void onClick(View view, int position);
    }

    public MakkaLiveAdapter(List<MakkaLive> makkaLives, Context mContext, RecyclerViewClickLister mListener ) {
        this.mListener = mListener;
        this.makkaLives = makkaLives;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.makka_live_item_view, viewGroup, false);
        mContext = viewGroup.getContext();
        return new ViewHolder(mView,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final String tvNam = makkaLives.get(i).getName();
        final String tvImg = makkaLives.get(i).getAlt_image();
        viewHolder.tvName.setText(tvNam);



    }

    @Override
    public int getItemCount() {
        return makkaLives.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecyclerViewClickLister mListener;
        TextView tvName;
        ImageView ivTVimage;

        public ViewHolder(@NonNull View itemView,RecyclerViewClickLister listener) {
            super(itemView);
            this.mListener=listener;
            tvName = itemView.findViewById(R.id.tvName);
            ivTVimage = itemView.findViewById(R.id.ivTVimage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
