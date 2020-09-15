package com.ducpham.flixster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.ducpham.flixster.adapters.MovieAdapter;
import com.ducpham.flixster.module.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movieList;
    RecyclerView recycleView;
    androidx.appcompat.widget.Toolbar topAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topAppBar = findViewById(R.id.top_app_bar);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG,"Success");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG,"results: "+ results.toString());
                    movieList = Movie.fromJsonArray(results);
                  //  Log.d(TAG,"movieList size" + movieList.size());
                    Log.d(TAG,movieList.get(0).getPosterPath());
                    recycleView = findViewById(R.id.recyclerView);
                    Log.d(TAG, String.valueOf(movieList.size()));
                    MovieAdapter movieAdapter = new MovieAdapter(movieList,MainActivity.this);
                    recycleView.setAdapter(movieAdapter);
                    recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } catch (JSONException e) {
                    Log.e(TAG,"JSON exception",e);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG,"Failed");
            }
        });
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorite:
                        recycleView.smoothScrollToPosition(0);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}