package com.example.movieapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.model.Cast;

import java.util.List;


public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private  List<Cast> mListCast;
    private Context context;

    public CastAdapter(List<Cast> mListCast, Context context) {
        this.mListCast = mListCast;
        this.context = context;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {
        Cast cast = mListCast.get(position);
        if(cast == null){
            return;
        }

        holder.tvCastName.setText(cast.getName());
        Glide.with(context).load("https://image.tmdb.org/t/p/original/" + cast.getProfile_path()).into(holder.imgCast);

    }

    @Override
    public int getItemCount() {

        return mListCast.size();
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvCastName;
        private final ImageView imgCast;

        public CastViewHolder(@NonNull View itemView){
            super(itemView);
            tvCastName = itemView.findViewById(R.id.tv_cast_name);
            imgCast = itemView.findViewById(R.id.img_cast);
        }
    }

}
