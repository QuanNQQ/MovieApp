package com.example.movieapp.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoriteViewModel extends ViewModel {
    public final MutableLiveData<Integer> numberOfFavorite = new MutableLiveData<>();

    public void setUpNumber(Integer count){
        numberOfFavorite.setValue(count);
    }

    public LiveData<Integer> getCount(){
        return numberOfFavorite;
    }
}
