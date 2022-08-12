package com.example.movieapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.movieapp.R;
import com.example.movieapp.databinding.ActivityDestinationBinding;

public class DestinationActivity extends AppCompatActivity {
    ActivityDestinationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle extras = getIntent().getExtras();
        Log.d("DES", "onCreate: "+ extras.getString("KEY"));
    }
}