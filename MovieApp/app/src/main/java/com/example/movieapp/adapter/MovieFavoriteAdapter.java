package com.example.movieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.interf.IMovie;
import com.example.movieapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.FavoriteViewHolder> {

    private List<Movie> movieList;
    private List<Movie> movieListF;
    private Context context;
    IMovie iMovie;

    public MovieFavoriteAdapter(List<Movie> movieList, Context context, IMovie iMovie) {
        this.movieList = movieList;
        this.context = context;
        this.movieListF = movieList;
        this.iMovie = iMovie;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        Glide.with(context).load("https://image.tmdb.org/t/p/original/" + movie.getPoster_path()).into(holder.imgPoster);
        holder.tvReleaseDate.setText(movie.getRelease_date());
        holder.tvRating.setText(movie.vote_average + "" + "/10");
        holder.tvOverview.setText(movie.overview);
        holder.imgFavorite.setImageResource(R.drawable.ic_baseline_star_32);

        holder.imgFavorite.setOnClickListener(v -> {
            iMovie.delete(movie);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder{
        private TextView tvReleaseDate;
        private TextView tvTitle;
        private TextView tvRating;
        private TextView tvOverview;
        private ImageView imgPoster;
        private ImageView imgFavorite;

        public FavoriteViewHolder(View itemView){
            super(itemView);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvOverview = itemView.findViewById(R.id.tv_overview_detail);
            imgPoster = itemView.findViewById(R.id.img_poster);
            imgFavorite = itemView.findViewById(R.id.img_favorite);

        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    movieList = movieListF;
                } else {
                    List<Movie> filteredList = new ArrayList<>();
                    for (Movie row : movieListF) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    movieList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = movieList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movieList = (ArrayList<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
