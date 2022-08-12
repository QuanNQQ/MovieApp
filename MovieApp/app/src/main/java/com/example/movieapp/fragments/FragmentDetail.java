package com.example.movieapp.fragments;

import static com.example.movieapp.cons.Constants.*;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.adapter.CastAdapter;
import com.example.movieapp.api.RetrofitClient;
import com.example.movieapp.api.ServiceInterface;
import com.example.movieapp.database.MovieDatabase;
import com.example.movieapp.databinding.FragmentDetailBinding;
import com.example.movieapp.interf.IReminder;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.CastResponse;
import com.example.movieapp.model.Reminder;
import com.example.movieapp.notification.AlarmReceiver;
import com.example.movieapp.vm.ReminderViewModel;
import com.google.android.material.timepicker.MaterialTimePicker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDetail extends Fragment {
    FragmentDetailBinding binding;
    FragmentDetailArgs args;
    private List<Cast> mListCast;
    private CastAdapter castAdapter;
    LinearLayoutManager layoutManager;
    private MaterialTimePicker timePicker;
    private AlarmManager alarmManager;
    Calendar calendar;
    int time;
    String times;

    ReminderViewModel reminderViewModel;
    IReminder iReminder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);

        getMovieDetails();

        setRcvCast();

        mListCast = new ArrayList<>();
        callApiGetCast();
        setTitle();
        createNotificationChanel();

        calendar = Calendar.getInstance();
       // onClickReminder();
        changeDate();

        return binding.getRoot();
    }

    private void changeDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                time = (int)calendar.getTimeInMillis();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                        updateCalender();

                        startAlarm();

                        reminderViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);

                        double vote_average = args.getCurrentMovie().getVote_average();

                        String title = args.getCurrentMovie().getTitle();

                        String poster_path = args.getCurrentMovie().getPoster_path();

                        String release_date = args.getCurrentMovie().getRelease_date();

                Reminder reminder = new Reminder(null,vote_average, title, poster_path, release_date,times);

                MovieDatabase.getInstance(requireContext()).reminderDAO().insertReminder(reminder);
                reminderViewModel.setUpList(MovieDatabase.getInstance(requireContext()).reminderDAO().getReminder());

                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
            private void updateCalender() {
                String Format = "dd/MM/yyyy hh:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.CHINESE);
                binding.tvCast.setText(sdf.format(calendar.getTime()));
                times = sdf.format(calendar.getTime());
            }
        };

        binding.btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setRcvCast() {
        layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        binding.rcvCast.setLayoutManager(layoutManager);
    }

    private void setTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(args.getCurrentMovie().getTitle());
    }


    private void onClickReminder() {
        binding.btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();

                reminderViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);

                double vote_average = args.getCurrentMovie().getVote_average();

                String title = args.getCurrentMovie().getTitle();

                String poster_path = args.getCurrentMovie().getPoster_path();

                String release_date = args.getCurrentMovie().getRelease_date();

//                Reminder reminder = new Reminder(null,vote_average, title, poster_path, release_date,date);
//
//                MovieDatabase.getInstance(requireContext()).reminderDAO().insertReminder(reminder);
//                reminderViewModel.setUpList(MovieDatabase.getInstance(requireContext()).reminderDAO().getReminder());

            }
        });
    }

    public void showDateTimePicker() {
       calendar = Calendar.getInstance();
        time = (int)calendar.getTimeInMillis();
        new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    startAlarm();
                }
            },
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)).show();

        String Format = "dd/MM/yyyy hh:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.CHINESE);

        binding.tvCast.setText(sdf.format(calendar.getTime()));
        times = sdf.format((calendar.getTime()));
        Toast.makeText(requireContext(), ""+ sdf.format(calendar.getTime()), Toast.LENGTH_LONG).show();

    }







    private void startAlarm() {
        String year = args.getCurrentMovie().release_date;
        String rate = args.getCurrentMovie().vote_average+"";
        year = year.substring(0,4);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("KEY_TITLE", args.getCurrentMovie().getTitle());
        intent.putExtra("KEY_YEAR","yar: "+ year + " Rate: " +rate );
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),(int) time, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChanel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        CharSequence name = "Android Reminder Channel";
        String description = "Channel for Alarm manager";
        int important = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("ChanelId1", name, important);
        channel.setDescription(description);

        NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void getMovieDetails() {
        args = FragmentDetailArgs.fromBundle(getArguments()) ;
        binding.tvOverview.setText(args.getCurrentMovie().overview);
        binding.tvRelease.setText(args.getCurrentMovie().release_date);
        binding.tvRating.setText(args.getCurrentMovie().vote_average +""+"/10");
        Glide.with(requireContext()).load("https://image.tmdb.org/t/p/original/" + args.getCurrentMovie().getPoster_path()).into(binding.imgPoster);
        if(MovieDatabase.getInstance(getContext()).movieDAO().getMovieById(args.getCurrentMovie().getId()) != null){
            binding.imgStar.setImageResource(R.drawable.ic_baseline_star_32);
        }
        else
            binding.imgStar.setImageResource(R.drawable.ic_baseline_star_outline_32);
    }

    private void callApiGetCast(){
        int movieId = args.getCurrentMovie().getId();
        if(API_KEY == null){
            Toast.makeText(requireContext(), "Api null", Toast.LENGTH_LONG).show();
            return;
        }

        ServiceInterface service = RetrofitClient.getInstans().create(ServiceInterface.class);

        Call<CastResponse> call = service.getCast(movieId, API_KEY);
        call.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                mListCast = response.body().getCast();
                castAdapter = new CastAdapter(mListCast, getActivity());
                binding.rcvCast.setAdapter(castAdapter);
//                Toast.makeText(requireContext(), ""+ mListCast.size(), Toast.LENGTH_LONG).show();
                Log.d("Kiby", "onResponse: "+ mListCast.size());
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {

            }
        });
    }

}