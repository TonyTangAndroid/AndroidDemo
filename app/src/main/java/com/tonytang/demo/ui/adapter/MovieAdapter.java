package com.tonytang.demo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tonytang.demo.R;
import com.tonytang.demo.constants.Constants;
import com.tonytang.demo.entity.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context context;
    private List<Movie> dataList = new ArrayList<>();

    public MovieAdapter(Context context) {

        this.context = context;
    }

    public void updateDataSet(List<Movie> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent);
    }


    public MovieViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie currentItem = dataList.get(position);
        holder.tv_title.setText(currentItem.getTitle());
        Glide.with(context).load(Constants.BASIC_STATIC_URL + currentItem.getPoster_path()).into(holder.iv_cover);
        holder.tv_title.setText(currentItem.getTitle());

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_cover)
        public ImageView iv_cover;
        @Bind(R.id.tv_title)
        public TextView tv_title;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}