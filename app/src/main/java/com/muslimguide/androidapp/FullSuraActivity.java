package com.muslimguide.androidapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.service.JcPlayerManagerListener;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.App.AppController;
import com.muslimguide.androidapp.adapters.CustomLinearLayoutManager;
import com.muslimguide.androidapp.adapters.SuraAyahAdapter;
import com.muslimguide.androidapp.homePageFragments.QuranFragment;
import com.muslimguide.androidapp.models.SuraAyah;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FullSuraActivity extends AppCompatActivity implements JcPlayerManagerListener {
    private RecyclerView rvAyahList;
    private List<SuraAyah> ayahList;
    private SuraAyahAdapter suraAyahAdapter;
    private ProgressDialog progressDialog;
    private String suraNo, suraName, suraArabicName;
    private TextView tvSuraName;
    private List<String> engAyahList;
    private JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios;
    private static final String MY_PREFS_NAME = "MuslimPro";
    SharedPreferences.Editor editor;

    int lastVisibleItem;
    int lastReadAyat = 0;
    int count;
    private Cursor cursor;
    String databaseName;

    int numberInSurah;


    private File root;
    private String fileName = null;
    DownloadFile downloadFile;
    private boolean autoStart = false;
    boolean stop = false;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_sura);

        //SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


        tvSuraName = findViewById(R.id.tvSuraName);
        engAyahList = new ArrayList<>();
        jcAudios = new ArrayList<>();

        jcPlayerView = findViewById(R.id.jcplayer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            suraNo = bundle.getString("SURA_NO");
            suraName = bundle.getString("SURA_NAME");
            suraArabicName = bundle.getString("ARABIC_NAME");
            if (bundle.getInt("AYAT_POS") != 0) {
                lastReadAyat = bundle.getInt("AYAT_POS");
            }

            tvSuraName.setText(suraName);
        }
        databaseName = suraName.replace("-", "_").replace("'", "_");
        QuranFragment.quranDBHelper.queryData("CREATE TABLE IF NOT EXISTS " + databaseName + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, number VARCHAR, arabicAyah VARCHAR, engAyah VARCHAR, numberInSurah VARCHAR, url VARCHAR)");
        cursor = QuranFragment.quranDBHelper.getFullSura("SELECT * FROM " + databaseName);

        rvAyahList = findViewById(R.id.rvAyahList);
        ayahList = new ArrayList<>();
        layoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        suraAyahAdapter = new SuraAyahAdapter(ayahList, jcAudios);
        rvAyahList.setLayoutManager(layoutManager);
        rvAyahList.setHasFixedSize(true);
        rvAyahList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (count < lastVisibleItem)
                    count = lastVisibleItem;

            }
        });
        ((SimpleItemAnimator) rvAyahList.getItemAnimator()).setSupportsChangeAnimations(false);
        rvAyahList.setAdapter(suraAyahAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        if (cursor.getCount() == 0) {
            parseEngAyah();
        } else {
            getFullSura();
        }

//        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        //autoScroll();


    }
//    public void autoScroll() {
//        final int speedScroll = 1000;
//        final Handler handler = new Handler();
//        count = lastReadAyat;
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (count < suraAyahAdapter.getItemCount()) {
//                    rvAyahList.smoothScrollToPosition(++count);
//                }
//                handler.postDelayed(this, speedScroll);
//            }
//        };
//        handler.postDelayed(runnable, 0);
//    }


    private void getFullSura() {
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int number = Integer.parseInt(cursor.getString(1));
            String arabicAyah = cursor.getString(2);
            String engAyah = cursor.getString(3);
            numberInSurah = Integer.parseInt(cursor.getString(4));
            String url = cursor.getString(5);
            Log.d("URL_GET", url);
            ayahList.add(new SuraAyah(number, arabicAyah, engAyah, numberInSurah, 0, 0, 0, 0, 0, false, url));
            if (numberInSurah >= lastReadAyat) {
                jcAudios.add(JcAudio.createFromURL(numberInSurah + ". " + engAyah, url));
            }
//            downloadFile=new DownloadFile(FullSuraActivity.this);
//            downloadFile.execute(url,String.valueOf(numberInSurah));
        }
        jcPlayerView.initPlaylist(jcAudios, FullSuraActivity.this);
        rvAyahList.scrollToPosition(lastReadAyat - 1);
        suraAyahAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        //jcPlayerView.createNotification();
        //Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();
    }

    private void parseEngAyah() {
        progressDialog.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.suraAyahInEnglish + suraNo, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ayahArray = response.getJSONArray("ayahs");
                    for (int i = 0; i < ayahArray.length(); i++) {
                        JSONObject object = ayahArray.getJSONObject(i);
                        String engAyah = object.getString("text");
                        engAyahList.add(engAyah);
                    }
                    parseAllAyah();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(FullSuraActivity.this, "Catch Error!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FullSuraActivity.this, "Parse Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void parseAllAyah() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.suraAyahInArabic + suraNo, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ayahArray = response.getJSONArray("ayahs");
                    for (int i = 0; i < ayahArray.length(); i++) {
                        JSONObject object = ayahArray.getJSONObject(i);
                        String arabicAyah = object.getString("text");
                        String engAyah = engAyahList.get(i);
                        int number = object.getInt("number");
                        numberInSurah = object.getInt("numberInSurah");
                        int juz = object.getInt("juz");
                        int manzil = object.getInt("manzil");
                        int page = object.getInt("page");
                        int ruku = object.getInt("ruku");
                        int hizbQuarter = object.getInt("hizbQuarter");
                        boolean sajda = object.getBoolean("sajda");
                        String url = Apis.ayahAudio + String.valueOf(number);
//                        downloadFile=new DownloadFile(FullSuraActivity.this);
//                        downloadFile.execute(url,String.valueOf(numberInSurah));

                        ayahList.add(new SuraAyah(number, arabicAyah, engAyah, numberInSurah, juz, manzil, page, ruku, hizbQuarter, sajda, url));
                        QuranFragment.quranDBHelper.insertSura(databaseName, String.valueOf(number), arabicAyah, engAyah, String.valueOf(numberInSurah), url);
                        if (numberInSurah >= lastReadAyat) {
                            jcAudios.add(JcAudio.createFromURL(numberInSurah + ". " + engAyah, url));
                        }
                    }
                    jcPlayerView.initPlaylist(jcAudios, FullSuraActivity.this);
                    rvAyahList.scrollToPosition(lastReadAyat - 1);
                    suraAyahAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    //Toast.makeText(FullSuraActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(FullSuraActivity.this, "Catch Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FullSuraActivity.this, "Parse Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void updateProgress(final JcStatus jcStatus) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // calculate progress
                float progress;
                float progress1 = (float) (jcStatus.getDuration() - jcStatus.getCurrentPosition())
                        / (float) jcStatus.getDuration();
                progress = 1.0f;
                Log.d("KSDKSJKDKSDJ", String.valueOf(progress1));
                suraAyahAdapter.updateProgress(jcStatus.getJcAudio(), progress,lastReadAyat);
                //rvAyahList.scrollToPosition(suraAyahAdapter.getItemCount());

            }
        });
    }


    @Override
    public void onCompletedAudio() {

    }

    @Override
    public void onContinueAudio(@NotNull JcStatus jcStatus) {

    }

    @Override
    public void onJcpError(@NotNull Throwable throwable) {

    }

    @Override
    public void onPaused(@NotNull JcStatus jcStatus) {

    }

    @Override
    public void onPlaying(@NotNull JcStatus jcStatus) {

    }

    @Override
    public void onPreparedAudio(@NotNull JcStatus jcStatus) {

    }

    @Override
    public void onTimeChanged(@NotNull JcStatus jcStatus) {
        updateProgress(jcStatus);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jcPlayerView.kill();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putString("suraNo", suraNo);
        editor.putString("suraName", suraName);
        editor.putString("suraArabicName", suraArabicName);
        editor.putInt("ayatPos", lastVisibleItem);
        editor.commit();
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {

        private Context context;

        public DownloadFile(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(String... url) {
            int count;
            try {
                URL url1 = new URL(url[0]);
                URLConnection conexion = url1.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();

                // downlod the file
                root = Environment.getExternalStorageDirectory();
                File file = new File(root.getAbsolutePath() + "/MuslimPro/" + suraNo);
                if (!file.exists()) {
                    file.mkdirs();
                }
                fileName = root.getAbsolutePath() + "/MuslimPro/" + suraNo + "/" + url[1] + ".mp3";
                Log.d("filename", fileName);
                InputStream input = new BufferedInputStream(url1.openStream());
                OutputStream output = new FileOutputStream(fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }
    }
}
