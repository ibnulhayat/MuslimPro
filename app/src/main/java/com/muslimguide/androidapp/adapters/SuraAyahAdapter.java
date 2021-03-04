package com.muslimguide.androidapp.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jean.jcplayer.model.JcAudio;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.models.SuraAyah;

import java.util.List;

public class SuraAyahAdapter extends RecyclerView.Adapter<SuraAyahAdapter.ViewHolder> {
    private List<SuraAyah> ayahList;
    private List<JcAudio> jcAudioList;
    private Context context;
    private SparseArray<Float> progressMap = new SparseArray<>();

    public SuraAyahAdapter(List<SuraAyah> ayahList, List<JcAudio> jcAudioList) {
        this.ayahList = ayahList;
        this.jcAudioList = jcAudioList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sura_ayah_list, viewGroup, false);
        context = viewGroup.getContext();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final SuraAyah items = ayahList.get(i);
        final String number = String.valueOf(items.getNumberInSurah());
        final String arabicText = items.getArabicText();
        final String engText = items.getEngText();

        final String arabicNum = number.replace("1", "١").replace("2", "٢")
                .replace("3", "٣").replace("4", "٤").replace("5", "٥")
                .replace("6", "٦").replace("7", "٧").replace("8", "٨")
                .replace("9", "٩").replace("0", "٠");

        viewHolder.setAyahDetails(arabicText, engText, number, arabicNum);

        applyProgressPercentage(viewHolder, progressMap.get(i, 0.0f));

    }

    private void applyProgressPercentage(ViewHolder holder, float percentage) {
        //Log.d(TAG, "applyProgressPercentage() with percentage = " + percentage);
        LinearLayout.LayoutParams progress = (LinearLayout.LayoutParams) holder.viewProgress.getLayoutParams();
        LinearLayout.LayoutParams antiProgress = (LinearLayout.LayoutParams) holder.viewAntiProgress.getLayoutParams();


        progress.weight = percentage;
        holder.viewProgress.setLayoutParams(progress);

        antiProgress.weight = 1.0f - percentage;
        holder.viewAntiProgress.setLayoutParams(antiProgress);

    }

    @Override
    public int getItemCount() {
        return ayahList == null ? 0 : ayahList.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateProgress(JcAudio jcAudio, float progress, int pos) {

        int position = jcAudioList.indexOf(jcAudio);
        Log.d("POSITION", "Progress = " + position);
        progressMap.put(position, progress);
        if (progressMap.size() > 1) {
            for (int i = 0; i < progressMap.size(); i++) {
                if (progressMap.keyAt(i) != position) {
                    notifyItemChanged(progressMap.keyAt(i));
                    progressMap.delete(progressMap.keyAt(i));
                }
            }
        }
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAyahInArabic, tvArabicNum, tvAyahInEng;
        View mView;
        private View viewProgress;
        private View viewAntiProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            viewProgress = mView.findViewById(R.id.song_progress_view);
            viewAntiProgress = mView.findViewById(R.id.song_anti_progress_view);
        }

        public void setAyahDetails(String arabicAyah, String engAyah, String num, String arabicText) {
            tvAyahInArabic = mView.findViewById(R.id.tvAyahInArabic);
            tvAyahInArabic.setText(arabicAyah);

            tvArabicNum = mView.findViewById(R.id.tvArabicNum);
            tvArabicNum.setText(arabicText);

            tvAyahInEng = mView.findViewById(R.id.tvAyahInEng);
            tvAyahInEng.setText(num + ". " + engAyah);

        }
    }
}
