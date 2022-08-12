package com.example.movieapp.vm;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderViewModel extends ViewModel {
    private  MutableLiveData<List<Reminder>> reminderList = new MutableLiveData<>();

    public void setUpList(List<Reminder> reminder){
        reminderList.setValue(reminder);
    }

    public MutableLiveData<List<Reminder>> getList(){
        return reminderList;
    }


}
