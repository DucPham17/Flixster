package com.ducpham.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducpham.flixster.R;
import com.ducpham.flixster.module.Movie;

import java.util.*;
import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    List<Movie> movieList = new ArrayList<>();
    Context context;
    public MovieAdapter(List<Movie> movieList, Context context){
        this.movieList = movieList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        ViewHolder viewHolder = new ViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        ImageView poster = holder.poster;
        TextView title = holder.title;
        TextView overview = holder.overview;

        //check orientation
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Glide.with(context).asBitmap().load(movie.getBackdropPath()).into(poster);
        }
        else{
            Glide.with(context).asBitmap().load(movie.getPosterPath()).into(poster);
        }
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView overview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            overview = itemView.findViewById(R.id.overview);
        }
    }
}
