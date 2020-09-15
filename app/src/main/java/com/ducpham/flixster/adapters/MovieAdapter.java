package com.ducpham.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducpham.flixster.R;
import com.ducpham.flixster.module.Movie;

import java.util.*;
import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Movie> movieList;
    Context context;
    public final int HIGH_RATE = 0, LOW_RATE = 1;

    public MovieAdapter(List<Movie> movieList, Context context){
        this.movieList = movieList;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case HIGH_RATE:
                View view1 = inflater.inflate(R.layout.item_movie_big,parent,false);
                viewHolder = new ViewHolderBig(view1);
                break;
            case LOW_RATE:
                View view2 = inflater.inflate(R.layout.item_movie,parent,false);
                viewHolder = new ViewHolder(view2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        switch (holder.getItemViewType()){
            case HIGH_RATE:
                ViewHolderBig holder1 = (ViewHolderBig) holder;
                ImageView imageView1 = holder1.bigPoster;
                Glide.with(context).asBitmap().load(movie.getBackdropPath()).into(imageView1);
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,movie.getTitle(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case LOW_RATE:
                ViewHolder holder2 = (ViewHolder) holder;
                configViewHolder(holder2,position);
                break;
            default:
                ViewHolder holder3 = (ViewHolder) holder;
                configViewHolder(holder3,position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (movieList.get(position).getVoteAverage() > 7.0) {
            return HIGH_RATE;
        } else if (movieList.get(position).getVoteAverage() <= 7.0 ) {
            return LOW_RATE;
        }
        return -1;
    }

    public void configViewHolder(@NonNull ViewHolder holder, int position) {
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

    public class ViewHolderBig extends RecyclerView.ViewHolder {
        ImageView bigPoster;
        public ViewHolderBig(@NonNull View itemView) {
            super(itemView);
            bigPoster = itemView.findViewById(R.id.bigPoster);

        }
    }
}
