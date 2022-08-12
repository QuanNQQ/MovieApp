package com.example.movieapp.adapter;

import static com.example.movieapp.cons.Constants.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.database.MovieDatabase;
import com.example.movieapp.interf.IMovie;
import com.example.movieapp.model.Movie;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> mListMovie;
    private final Context context;

    private GridLayoutManager gridLayoutManager;
    MovieDatabase movieDatabase;
    IMovie iMovie;

    public MovieAdapter(Context context, List<Movie> mListMovie, GridLayoutManager gridLayoutManager, IMovie iMovie) {
        this.mListMovie = mListMovie;
        this.context = context;
        this.gridLayoutManager = gridLayoutManager;
        this.movieDatabase = MovieDatabase.getInstance(context);
        this.iMovie = iMovie;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        }
        return new MovieViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        int spanCount = gridLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE) {
            return VIEW_TYPE_LIST;
        } else {
            return VIEW_TYPE_GRID;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mListMovie != null && mListMovie.get(position) != null) {
            Movie movie = mListMovie.get(position);
            holder.tvTitle.setText(movie.getTitle());
            Glide.with(context).load("https://image.tmdb.org/t/p/original/" + movie.getPoster_path()).into(holder.imgPoster);

            if (getItemViewType(position) == VIEW_TYPE_LIST) {
                holder.tvReleaseDate.setText(movie.getRelease_date());
                holder.tvRating.setText(movie.vote_average + "" + "/10");
                holder.tvOverview.setText(movie.overview);
                holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MovieDatabase.getInstance(context).movieDAO().getMovieById(movie.getId()) == null) {
                            holder.imgFavorite.setImageResource(R.drawable.ic_baseline_star_32);
                            iMovie.insertMovie(movie);
                        } else if (MovieDatabase.getInstance(context).movieDAO().getMovieById(movie.getId()) != null) {
                            holder.imgFavorite.setImageResource(R.drawable.ic_baseline_star_outline_32);
                            iMovie.delete(movie);
                        }
                    }
                });

                if (MovieDatabase.getInstance(context).movieDAO().getMovieById(movie.getId()) != null) {
                    holder.imgFavorite.setImageResource(R.drawable.ic_baseline_star_32);
                } else
                    holder.imgFavorite.setImageResource(R.drawable.ic_baseline_star_outline_32);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iMovie.onClickItem(movie);
                }
            });
        }
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView tvReleaseDate;
        private TextView tvTitle;
        private TextView tvRating;
        private TextView tvOverview;
        private ImageView imgPoster;
        private CardView cardView;
        ImageView imgFavorite;

        public MovieViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_LIST) {
                tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
                tvTitle = itemView.findViewById(R.id.tv_movie_title);
                tvRating = itemView.findViewById(R.id.tv_rating);
                tvOverview = itemView.findViewById(R.id.tv_overview_detail);
                imgPoster = itemView.findViewById(R.id.img_poster);
                cardView = itemView.findViewById(R.id.card_view);
                imgFavorite = itemView.findViewById(R.id.img_favorite);
            } else {
                imgPoster = itemView.findViewById(R.id.image_small);
                tvTitle = itemView.findViewById(R.id.title_small);
            }
        }
    }





    @Override
    public int getItemCount() {
        if (mListMovie != null)
            return mListMovie.size();
        return 0;
    }

    public void add(Movie movie) {
        mListMovie.add(movie);
        notifyItemInserted(mListMovie.size()-1);
    }

    public void addAll(List<Movie> movies) {
        for (Movie m : movies) {
            add(m);
        }
    }
    //add Empty item...

    public void addBottomItem() {
        add(null);
        notifyItemInserted(mListMovie.size() -1);
    }

    public void removeLastEmptyItem() {
        int position = mListMovie.size() - 1 ;
        Movie item = getItem(position);

        if (item == null) {
            mListMovie.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Movie getItem(int position)  {
        return mListMovie.get(position);
    }

}
