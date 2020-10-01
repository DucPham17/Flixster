package com.ducpham.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducpham.flixster.DetailPage;
import com.ducpham.flixster.R;
import com.ducpham.flixster.module.Movie;

import org.parceler.Parcels;

import java.util.*;
import java.util.zip.Inflater;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
                final ImageView imageView1 = holder1.bigPoster;
                View bigLayout = holder1.layoutBig;
                Glide.with(context).asBitmap().load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(30, 5)).into(imageView1);
                bigLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("movie", Parcels.wrap(movie));
                        intent.setClass(context,DetailPage.class);
                        ActivityOptionsCompat actionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,(View) imageView1,"movieTrans");
                        context.startActivity(intent, actionCompat.toBundle());
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

    public void configViewHolder(@NonNull final ViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        final ImageView poster = holder.poster;
        TextView title = holder.title;
        TextView overview = holder.overview;

        //check orientation
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Glide.with(context).asBitmap().load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(30, 5)).into(poster);
        }
        else{
            Glide.with(context).asBitmap().load(movie.getPosterPath()).transform(new RoundedCornersTransformation(30, 5)).into(poster);
        }
        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        View layout = holder.layout;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("movie", Parcels.wrap(movie));
                intent.setClass(context,DetailPage.class);

                ActivityOptionsCompat actionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,(View) poster,"movieTrans");
                context.startActivity(intent, actionCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView overview;
        View layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            overview = itemView.findViewById(R.id.overview);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public class ViewHolderBig extends RecyclerView.ViewHolder {
        ImageView bigPoster;
        View layoutBig;
        public ViewHolderBig(@NonNull View itemView) {
            super(itemView);
            bigPoster = itemView.findViewById(R.id.bigPoster);
            layoutBig = itemView.findViewById(R.id.layoutBig);
        }
    }
}
