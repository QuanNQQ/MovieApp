package com.example.movieapp.fragments;

import static com.example.movieapp.cons.Constants.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.adapter.MovieAdapter;
import com.example.movieapp.adapter.Pagination;
import com.example.movieapp.api.RetrofitClient;
import com.example.movieapp.api.ServiceInterface;
import com.example.movieapp.database.MovieDatabase;
import com.example.movieapp.databinding.FragmentMoviesBinding;
import com.example.movieapp.interf.IMovie;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.PopularMovieResponse;
import com.example.movieapp.vm.FavoriteViewModel;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMovies extends Fragment implements IMovie {
    FragmentMoviesBinding binding;
    String TAG = "FragmentMovie";
    private GridLayoutManager gridLayoutManager;
    private List<Movie> mListMovie = new ArrayList<>();
    private static final int STAR_PAGE = 1;
    private final int TOTAL_PAGE = 20;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int CURRENT_PAGE = STAR_PAGE;
    private MovieAdapter movieAdapter;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    FavoriteViewModel favoriteViewModel;
    private int select = 0;
    private int value ;
    Call<PopularMovieResponse> call;
    ServiceInterface service;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        gridLayoutManager = new GridLayoutManager(requireContext(), SPAN_COUNT_ONE);
        binding.rcvMovie.setLayoutManager(gridLayoutManager);

        sharedPreferences = requireActivity().getSharedPreferences (SPF_SETTING, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        select = sharedPreferences.getInt(KEY_CATEGORY,0);
        value = sharedPreferences.getInt(KEY_SORT_BY,0);

        callApiGetMovie();
        selectMovieGenre();

        return binding.getRoot();
    }

    private void selectMovieGenre() {
        binding.tvPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Popular Movie");
                binding.chooseMovieGenre.setVisibility(View.GONE);
                select = 0;
                callApiGetMovie();


            }
        });

        binding.tvTopRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("TopRate Movie");
                binding.chooseMovieGenre.setVisibility(View.GONE);
                select = 1;
                callApiGetMovie();


            }
        });

        binding.tvUpComing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Up Coming Movie");
                binding.chooseMovieGenre.setVisibility(View.GONE);
                select = 2;
                callApiGetMovie();

            }
        });

        binding.tvNowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Now Playing Movie");
                binding.chooseMovieGenre.setVisibility(View.GONE);
                select = 3;
                callApiGetMovie();

            }
        });
    }

    private void callApiGetMovie() {

        service = RetrofitClient.getInstans().create(ServiceInterface.class);
        binding.rcvMovie.addOnScrollListener(new Pagination(gridLayoutManager) {
            @Override
            protected void loadMoreItem() {
                isLoading = true;
                CURRENT_PAGE += 1;
                // Load next page...

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000); // 1 second delay...
            }
            @Override
            protected int getTotalPages() {
                return TOTAL_PAGE;
            }

            @Override
            protected boolean isLastPage() {
                return isLastPage;
            }

            @Override
            protected boolean isLoading() {
                return isLoading;
            }
        });

        if(select == 0){call = service.getPopularMovie(API_KEY, STAR_PAGE);}

        else if(select == 1){call = service.getTopRateMovie(API_KEY, STAR_PAGE);}

        else if(select == 2){call = service.getUpcomingMovie(API_KEY, STAR_PAGE);}

        else if(select == 3){call = service.getNowPlayingMovie(API_KEY, STAR_PAGE);}

        call.enqueue(new Callback<PopularMovieResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<PopularMovieResponse> call, Response<PopularMovieResponse> response) {
                mListMovie = response.body().getResults();

                List<Movie> mListMovieFilter = filterMovie(mListMovie);


                if(value == 0)
                   Collections.sort(mListMovieFilter, Movie.MovieSort);
                else if (value == 1)
                    Collections.sort(mListMovieFilter, Movie.MovieSortByDate);

                movieAdapter = new MovieAdapter(getActivity(), mListMovieFilter, gridLayoutManager, FragmentMovies.this);

                binding.rcvMovie.setAdapter(movieAdapter);
                if (CURRENT_PAGE <= TOTAL_PAGE) {
                    isLoading = true;
                    //movieAdapter.addBottomItem();

                } else {
                    isLastPage = true;
                }
                movieAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PopularMovieResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadNextPage() {
        binding.progressBar.setVisibility(View.VISIBLE);
        ServiceInterface service = RetrofitClient.getInstans().create(ServiceInterface.class);
        Call<PopularMovieResponse> call = null ;


        if(select == 0){call = service.getPopularMovie(API_KEY, CURRENT_PAGE);}

        else if(select == 1){call = service.getTopRateMovie(API_KEY, CURRENT_PAGE);}

        else if(select == 2){call = service.getUpcomingMovie(API_KEY, CURRENT_PAGE);}

        else if(select == 3){call = service.getNowPlayingMovie(API_KEY, CURRENT_PAGE);}

        call.enqueue(new Callback<PopularMovieResponse>() {
            @Override
            public void onResponse(Call<PopularMovieResponse> call, Response<PopularMovieResponse> response) {
                mListMovie = response.body().getResults();

                List<Movie> mListMovieFilter = filterMovie(mListMovie);
                // remove last empty item
                movieAdapter.removeLastEmptyItem();
                isLoading = false;

                if(value == 0)
                    Collections.sort(mListMovie, Movie.MovieSort);
                else if (value == 1)
                    Collections.sort(mListMovie, Movie.MovieSortByDate);

                movieAdapter.addAll(mListMovieFilter);
                movieAdapter.notifyDataSetChanged();

                if (CURRENT_PAGE != TOTAL_PAGE) {
                    movieAdapter.addBottomItem();

                } else {
                    isLastPage = true;
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PopularMovieResponse> call, Throwable t) {

            }
        });
    }

    public List<Movie> filterMovie(List<Movie> movies){
        List<Movie> mListMovieFilter = new ArrayList<>();
        for(int i = 0 ; i < movies.size(); i++ ){
            if(checkRateAndRelease(movies.get(i))) {
                mListMovieFilter.add(movies.get(i));
            }
            Log.d(TAG, "filterMovie: "+ mListMovieFilter.size());
        }
        return mListMovieFilter;

    }

    private boolean checkRateAndRelease(Movie movie){
            int start = sharedPreferences.getInt(KEY_MOVIE_RATE,0);
            if(movie.getVote_average() >= start ){
                Log.d(TAG, "checkRateAndRelease: "+movie.getTitle());
                return true;
            }


        return  false;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_toolbar, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favourite:
                    Toast.makeText(requireContext(), "Favourite", Toast.LENGTH_LONG).show();
            case R.id.setting:
                Toast.makeText(requireContext(), "Setting", Toast.LENGTH_LONG).show();

        }

        if(item.getItemId() == R.id.filter)
            binding.chooseMovieGenre.setVisibility(View.VISIBLE);

        if(item.getItemId()== R.id.grid){
            switchLayout();
            switchIcon(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchLayout() {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
            gridLayoutManager.setSpanCount(SPAN_COUNT_TWO);
        } else {
            gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
        }
        movieAdapter.notifyItemRangeChanged(0, movieAdapter.getItemCount());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void switchIcon(MenuItem item) {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_TWO) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_format_list_bulleted_24));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_grid_view_24));
        }
    }

    @Override
    public void onClickItem(Movie movie) {
        NavDirections action =
                FragmentMoviesDirections.actionFragmentMoviesToFragmentDetail(movie);
        Navigation.findNavController(getView()).navigate(action);

    }

    @Override
    public void insertMovie(Movie movie) {
        MovieDatabase.getInstance(requireContext()).movieDAO().insertMovie(movie);
        favoriteViewModel.setUpNumber(MovieDatabase.getInstance(requireContext()).movieDAO().countMovie());
    }

    @Override
    public void getMovie(Movie movie) {
    }

    @Override
    public void delete(Movie movie) {
        MovieDatabase.getInstance(requireContext()).movieDAO().deleteMovie(movie);
        favoriteViewModel.setUpNumber(MovieDatabase.getInstance(requireContext()).movieDAO().countMovie());
    }

    private void movieSort(){
        switch (value){
            case 0: Collections.sort(mListMovie, Movie.MovieSort);
            case 1: Collections.sort(mListMovie, Movie.MovieSortByDate);

        }
    }
}
