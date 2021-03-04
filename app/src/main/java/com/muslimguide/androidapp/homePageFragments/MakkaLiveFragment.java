package com.muslimguide.androidapp.homePageFragments;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.Player;
import com.muslimguide.androidapp.App.Apis;
import com.muslimguide.androidapp.R;
import com.muslimguide.androidapp.adapters.MakkaLiveAdapter;
import com.muslimguide.androidapp.common.Common;
import com.muslimguide.androidapp.models.MakkaLive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MakkaLiveFragment extends Fragment {

    private View mView;
    private MakkaLiveAdapter makkaLiveAdapter;
    private List<MakkaLive> makkaLives;
    private VideoView videoView;
    private RecyclerView rvLiveTvlist;
    private RequestQueue mRequestQueue;
    private ProgressDialog mProgressDialog;
    private ImageView landscapBtn;
    private boolean isClicked = false;
    private String tvName, liveTvUrl;

    public MakkaLiveFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_makka_live, container, false);

        // Toolbar visible
        Common.toolbarVisibility("visible","Makka Live",1);

        rvLiveTvlist = mView.findViewById(R.id.rvLiveTvlist);
        landscapBtn = mView.findViewById(R.id.landscapBtn);
        makkaLives = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Please Wait...!");

        landscapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getContext(), LiveVideoPlayActivity.class);
//                intent.putExtra("videoUrl", Common.plyTvUrl);
//                startActivity(intent);

            }
        });

        liveTvM();
        return mView;
    }

    //-------------Live Tv method--------------
    private void liveTvM() {
        mProgressDialog.show();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.tvUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = "";
                            String url = "";
                            String alt_url = "";
                            String image = "";
                            String alt_image = "";
                            String apk = "";
                            String yitid = "";
                            String id = "";
                            JSONArray jsonArray = response.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                name = hit.getString("name");
                                url = hit.getString("url");
                                alt_url = hit.getString("alt_url");
                                image = hit.getString("image");
                                alt_image = hit.getString("alt_image");
                                apk = hit.getString("apk");
                                yitid = hit.getString("yitid");
                                id = hit.getString("id");

                                if (i == 0) {
                                    if (isClicked == false) {
                                        startTv(name, url);
                                    }
                                }
                                mProgressDialog.dismiss();
                                makkaLives.add(new MakkaLive(name, url, alt_url, image, alt_image, apk, yitid, id));
                            }
                            makkaLiveAdapter = new MakkaLiveAdapter(makkaLives, getActivity(), new MakkaLiveAdapter.RecyclerViewClickLister() {
                                @Override
                                public void onClick(View view, int position) {
                                    liveTvUrl = makkaLives.get(position).getUrl();
                                    isClicked = true;
                                    tvName = makkaLives.get(position).getName();
                                    liveTvUrl = makkaLives.get(position).getUrl();
                                    startTv(tvName, liveTvUrl);
                                    mProgressDialog.dismiss();
                                }
                            });
                            rvLiveTvlist.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rvLiveTvlist.setAdapter(makkaLiveAdapter);
                            makkaLiveAdapter.notifyDataSetChanged();
                            mProgressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
            }
        });
        mRequestQueue.add(request);
    }
    //---------------End-------------

    //-------------Video player Method----------
    private void startTv(final String tvName, final String liveTvUrl) {
        videoView = (VideoView) mView.findViewById(R.id.video_view);
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                videoView.start();
                Common.plyTvName = tvName;
                Common.plyTvUrl = liveTvUrl;
            }
        });
        videoView.setRepeatMode(Player.REPEAT_MODE_ONE);
        videoView.setVideoURI(Uri.parse(liveTvUrl));
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        landscapBtn.setVisibility(View.VISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                landscapBtn.setVisibility(View.GONE);
                            }
                        }, 2000);
                        videoView.showControls();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        return true;
                }

                return false;
            }
        });
        videoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(Exception e) {
                Toast.makeText(getContext(), "Something Is Wrong!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    //---------------End-------------

    @Override
    public void onStop() {
        super.onStop();
        videoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
