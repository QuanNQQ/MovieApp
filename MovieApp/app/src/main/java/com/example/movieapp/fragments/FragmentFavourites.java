package com.example.movieapp.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.adapter.MovieFavoriteAdapter;
import com.example.movieapp.database.MovieDatabase;
import com.example.movieapp.interf.IMovie;
import com.example.movieapp.model.Movie;
import com.example.movieapp.vm.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavourites extends Fragment implements IMovie {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private SearchManager searchManager;
    private LinearLayoutManager layoutManager;
    private MovieDatabase movieDatabase;
    private MovieFavoriteAdapter movieFavoriteAdapter;
    private List<Movie> movieList = new ArrayList<>();
    String TAG = "FragmentFavorite";
    FavoriteViewModel favoriteViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = view.findViewById(R.id.rcv_favorite);

        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        movieList = MovieDatabase.getInstance(requireContext()).movieDAO().getMovie();

        movieFavoriteAdapter = new MovieFavoriteAdapter(movieList, getActivity(), FragmentFavourites.this);
        recyclerView.setAdapter(movieFavoriteAdapter);
        return view;
    }

    @Override
    public void onClickItem(Movie movie) {}

    @Override
    public void insertMovie(Movie movie) {}

    @Override
    public void getMovie(Movie movie) {}

    @Override
    public void delete(Movie movie) {
        MovieDatabase.getInstance(requireContext()).movieDAO().deleteMovie(movie);
        movieList = MovieDatabase.getInstance(requireContext()).movieDAO().getMovie();
        movieFavoriteAdapter = new MovieFavoriteAdapter(movieList, getActivity(), FragmentFavourites.this);
        recyclerView.setAdapter(movieFavoriteAdapter);
        favoriteViewModel.setUpNumber(MovieDatabase.getInstance(requireContext()).movieDAO().countMovie());

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_favorites, menu);

        searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieFavoriteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieFavoriteAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favourite:
                Toast.makeText(requireContext(), "Favourite", Toast.LENGTH_LONG).show();
            case R.id.setting:
                Toast.makeText(requireContext(), "Favourite", Toast.LENGTH_LONG).show();
        }

        if(id == R.id.search_view)
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}