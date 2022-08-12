package com.example.movieapp;

import static com.example.movieapp.cons.Constants.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.movieapp.adapter.ReminderAdapter;
import com.example.movieapp.database.MovieDatabase;
import com.example.movieapp.databinding.ActivityMainBinding;
import com.example.movieapp.model.Reminder;
import com.example.movieapp.ui.EditProfileActivity;
import com.example.movieapp.vm.FavoriteViewModel;
import com.example.movieapp.vm.ReminderViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int i =0;
    MenuItem menuItem;

    private BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String name, date;
    private String email;
    private NavController navController;
    private NavHostFragment navHostFragment;
    int check_gender;
    private static final int REQUEST_CODE = 1;
    ActivityMainBinding binding;
    FavoriteViewModel favoriteViewModel;
    ReminderViewModel reminderViewModel;
    ActionBar actionBar;
    String TAG = "MainActivity";
    private LinearLayoutManager layoutManager;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> mListReminder = new ArrayList<>();
    SharedPreferences sharedPreferences1;
    private SharedPreferences.Editor editor1;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();
        sharedPreferences = getSharedPreferences(SHARED_PRF_NAME, Context.MODE_PRIVATE);
        showProfile();
        onClickEdit();
        sharedPreferences1 = getSharedPreferences(SPF_SETTING, Context.MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();
        addBottomNavigation();
        addDrawerLayout();
        countNumberOfFavorite();
        mListReminder = MovieDatabase.getInstance(this).reminderDAO().getReminder();
        setRecyclerview();
        observer();
        showAll();

    }

    private void observer() {

        reminderViewModel.getList().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                reminderAdapter = new ReminderAdapter(reminders, MainActivity.this);
                binding.recyclerview.setAdapter(reminderAdapter);
            }
        });


    }

    private void showAll() {
        binding.btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.bottomNavigation.setSelectedItemId(R.id.fragmentSettings);
                navController.navigate(R.id.fragmentReminder);
                drawerLayout.closeDrawers();

            }
        });

    }

    private void setRecyclerview() {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        reminderAdapter = new ReminderAdapter(mListReminder, this);
        binding.recyclerview.setAdapter(reminderAdapter);
    }

    private void countNumberOfFavorite() {
        BadgeDrawable badgeDrawable = binding.bottomNavigation.getOrCreateBadge(R.id.fragmentFavourites);
        badgeDrawable.setBackgroundColor(Color.RED);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setNumber(MovieDatabase.getInstance(this).movieDAO().countMovie());
        badgeDrawable.setVisible(true);
        favoriteViewModel.getCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                badgeDrawable.setNumber(integer);
            }
        });

    }

    private void onClickEdit() {
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEditProfile();
            }
        });
    }

    private void addBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void gotoEditProfile() {
        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void showProfile() {
        check_gender = sharedPreferences.getInt(GENDER, 0);
        name = sharedPreferences.getString(KEY_NAME, "");
        email = sharedPreferences.getString(KEY_EMAIL, "");
        date = sharedPreferences.getString(KEY_DATE_PICKER, "");
        String image = sharedPreferences.getString(KEY_IMAGE, "");

        if (name != null && email != null) {
            binding.tvName.setText(name);
            binding.tvEmail.setText(email);
            binding.tvDate.setText(date);
        }

        if (check_gender == 0) {
            binding.tvGender.setText(MALE);
        } else if (check_gender == 1) {
            binding.tvGender.setText(FEMALE);
        }

        if (!image.equalsIgnoreCase("")) {
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.imgAvatar.setImageBitmap(bitmap);
        }
    }

    private void addDrawerLayout() {
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragmentMovies, R.id.fragmentFavourites, R.id.fragmentSettings, R.id.fragmentAbouts)
                .setOpenableLayout(drawer)
                .build();

        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                navDestination.getId();
                if (navDestination.getId() == R.id.fragmentMovies) {
                    if(menuItem != null)
                        menuItem.setVisible(false);
                    int select = sharedPreferences1.getInt(KEY_CATEGORY, 0);

                    if (select == 0)
                        actionBar.setTitle(POPULAR_MOVIE);
                    else if (select == 1)
                        actionBar.setTitle(TOP_RATE_MOVIE);
                    else if (select == 2)
                        actionBar.setTitle(UP_COMING_MOVIE);
                    else if (select == 3)
                        actionBar.setTitle(NOW_PLAYING_MOVIE);


                }
                else
                if(menuItem != null){
                    menuItem.setVisible(false);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                showProfile();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        menuItem = menu.getItem(0);
        Log.d(TAG, "onCreateOptionsMenu: "+i);
        i = i+1;

//        menuItem.setVisible(false);

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }


}