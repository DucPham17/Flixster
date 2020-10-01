package com.ducpham.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.ducpham.flixster.module.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

import static com.ducpham.flixster.MainActivity.TAG;

public class DetailPage extends YouTubeBaseActivity {

    public static final String  API_STRING = "AIzaSyDVjtJjxea4YnyZfUt801Xomo8HpY79vSo";
    public static final String URL_BASE1 = "https://api.themoviedb.org/3/movie/";
    public static final String URL_BASE2 = "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    TextView title;
    TextView des;
    RatingBar rb;
    YouTubePlayerView youtube;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        title = findViewById(R.id.detailPageTitle);
        des = findViewById(R.id.detailPageDescription);
        rb = findViewById(R.id.ratingBar);
        youtube = findViewById(R.id.youtube);
        Intent detail = getIntent();
        final Movie movie = (Movie) Parcels.unwrap(detail.getParcelableExtra("movie"));
        title.setText(movie.getTitle());
        des.setText(movie.getOverview());
        rb.setRating((float) movie.getVoteAverage());
        final AsyncHttpClient client = new AsyncHttpClient();

        youtube.initialize(API_STRING, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                client.get(URL_BASE1 + movie.getId() + URL_BASE2, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONObject jsonObject = json.jsonObject;
                        try{
                            JSONArray results = jsonObject.getJSONArray("results");
                            JSONObject object =  results.getJSONObject(0);
                            String clipId = object.getString("key");

                            if(movie.getVoteAverage() > 7.0){
                                Log.d("Detail Page", "play");
                                youTubePlayer.loadVideo(clipId);
                                Log.d("Detail Page", String.valueOf(youTubePlayer.isPlaying()));
                            }
                            else{
                                youTubePlayer.cueVideo(clipId);
                            }

                        }catch (JSONException e) {
                            Log.e(TAG,"JSON exception",e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

}