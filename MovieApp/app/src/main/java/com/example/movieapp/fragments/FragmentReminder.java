package com.example.movieapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.movieapp.adapter.ReminderListAdapter;
import com.example.movieapp.database.MovieDatabase;
import com.example.movieapp.databinding.FragmentReminderBinding;
import com.example.movieapp.interf.IMovie;
import com.example.movieapp.interf.IReminder;
import com.example.movieapp.model.Reminder;
import com.example.movieapp.vm.ReminderViewModel;

import java.util.List;

public class FragmentReminder extends Fragment implements IReminder {
    FragmentReminderBinding binding;
    private ReminderListAdapter reminderListAdapter;
    private LinearLayoutManager layoutManager;
    private List<Reminder> mReminderList;
    String TAG = "FragmentReminder";
    ReminderViewModel reminderViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReminderBinding.inflate(getLayoutInflater());
        setRecyclerview();

        reminderViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);

        reminderViewModel.getList().observe(requireActivity(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                reminderListAdapter = new ReminderListAdapter(reminders, getContext(), FragmentReminder.this);
                binding.recyclerview.setAdapter(reminderListAdapter);

            }
        });

        return binding.getRoot();
    }

    private void setRecyclerview() {
        mReminderList = MovieDatabase.getInstance(requireContext()).reminderDAO().getReminder();
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        reminderListAdapter = new ReminderListAdapter(mReminderList, getContext(), FragmentReminder.this);
        binding.recyclerview.setAdapter(reminderListAdapter);

    }

    @Override
    public void insertReminder(Reminder reminder) {

    }

    @Override
    public void deleteReminder(Reminder reminder) {
        MovieDatabase.getInstance(getContext()).reminderDAO().deleteReminder(reminder);
        reminderViewModel.setUpList(MovieDatabase.getInstance(getContext()).reminderDAO().getReminder());
    }
}