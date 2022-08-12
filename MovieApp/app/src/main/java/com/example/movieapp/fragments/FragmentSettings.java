package com.example.movieapp.fragments;

import static com.example.movieapp.cons.Constants.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.databinding.FragmentSettingsBinding;

public class FragmentSettings extends Fragment {
    FragmentSettingsBinding binding;
    int i = 0;

    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    SeekBar seekBar;
    TextView seekProgress;
    int checkedItem ;
    int value;
    int progressValue;
    int progress;
    String TAG = "FragmentSetting";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        sharedPreferences = requireActivity().getSharedPreferences (SPF_SETTING, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        binding.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCategory();
            }
        });
        binding.tvMovieRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSeekbar();
            }
        });

        binding.tvShortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogShortBy();
            }
        });


        value = sharedPreferences.getInt(KEY_SORT_BY,0);
        checkedItem = sharedPreferences.getInt(KEY_CATEGORY, 0);
        progress = sharedPreferences.getInt(KEY_MOVIE_RATE, 0);


        if(value == 0)
            binding.tvShortBy.setText(TOP_RATE);
        else if(value ==1){
            binding.tvShortBy.setText(RELEASE_DATE);
        }

        if(checkedItem == 0){
            binding.tvCategory.setText(POPULAR_MOVIE);
        }

        if(checkedItem == 1){
            binding.tvCategory.setText(TOP_RATE_MOVIE);
        }

        if(checkedItem == 2){
            binding.tvCategory.setText(UP_COMING_MOVIE);
        }

        if(checkedItem == 3){
            binding.tvCategory.setText(NOW_PLAYING_MOVIE);
        }

        binding.tvMovieRate.setText(""+progress);

        //seekBar.setProgress(progress);
        return binding.getRoot();
    }

    private void showDialogShortBy() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(SORT_BY);
        dialog.setCancelable(true);
        String[] itemSortBy = { TOP_RATE, RELEASE_DATE};
        dialog.setSingleChoiceItems(itemSortBy, value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        value = 0;
                        break;
                    case 1:
                        value = 1;
                        break;
                }
            }
        });

        dialog.setNegativeButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.tvShortBy.setText(itemSortBy[value]);
                editor.putInt(KEY_SORT_BY, value);
                editor.apply();
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();

    }

    private void showDialogCategory() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(CATEGORY);
        alertDialog.setCancelable(true);
        String[] items = {POPULAR_MOVIE, TOP_RATE_MOVIE, UP_COMING_MOVIE, NOW_PLAYING_MOVIE};

        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        checkedItem = 0;
                        break;
                    case 1:
                        checkedItem = 1;
                        break;
                    case 2:
                         checkedItem = 2;
                        break;
                    case 3:
                        checkedItem = 3;
                        break;
                }
            }
        });

        alertDialog.setNegativeButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.tvCategory.setText(items[checkedItem]);
                editor.putInt(KEY_CATEGORY, checkedItem);
                editor.apply();
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private void ShowSeekbar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = FragmentSettings.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.seekbar_dialog,null);
        builder.setView(view);
        builder.setIcon(android.R.drawable.btn_star_big_on);
        builder.setTitle(MOVIE_WITH_RATE_FROM);

        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekProgress = (TextView) view.findViewById(R.id.tvSeekBarProgress);
        progress = sharedPreferences.getInt(KEY_MOVIE_RATE, 0);
        seekBar.setProgress(progress);
        seekProgress.setText(""+progress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                seekProgress.setText(""+progress);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        builder.setNegativeButton(OK,
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                binding.tvMovieRate.setText(""+ progressValue);
                editor.putInt(KEY_MOVIE_RATE, progressValue);
                editor.apply();
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: "+i);
        i= i+1;
    }
}