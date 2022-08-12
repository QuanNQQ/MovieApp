package com.example.movieapp.ui;
import static com.example.movieapp.cons.Constants.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.movieapp.MainActivity;
import com.example.movieapp.R;
import com.example.movieapp.databinding.ActivityEditProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    private Calendar calendar;
    private SharedPreferences.Editor editor;
    int checkRadioId;
    private int check_radio;
    String TAG = "EditProfileActivity";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        calendar = Calendar.getInstance();
        sharedPreferences = getSharedPreferences(SHARED_PRF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        showInformation();

        changeDate();

        clickDone();

        clickCancel();

        ChangeProfile();

        changeRadioButton();

    }





    private void changeRadioButton() {
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioSelected();
            }
        });
    }

    private void ChangeProfile() {
        binding.imgUpAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {CAMERA, GALLERY};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals(CAMERA)){
                            if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_DENIED){
                                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[] {Manifest.permission.CAMERA}, PICK_IMAGE_CAPTURE);
                            }
                            else {
                                Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intentTakePicture, PICK_IMAGE_CAPTURE);
                            }
                        }
                        else if(options[which].equals(GALLERY)){
                            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(gallery, PICK_IMAGE_GALLERY);
                        }
                    }
                });
                builder.show();
            }
        });
    }
    private void clickCancel() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clickDone() {
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putData();
            }
        });
    }

    private void changeDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(EditProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        updateCalender();
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
            private void updateCalender() {
                String Format = "dd/MM/yyyy hh:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.CHINESE);
                binding.tvDatePicker.setText(sdf.format(calendar.getTime()));
            }
        };

        binding.tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditProfileActivity.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
    }

    private void onRadioSelected() {
     checkRadioId = binding.radioGroup.getCheckedRadioButtonId();
        if(checkRadioId == R.id.radio_male){
            check_radio = 0;
        }
        else if(checkRadioId == R.id.radio_female){
            check_radio = 1;
        }
    }

    private void putData() {
        editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, binding.edtName.getText().toString());
        editor.putString(KEY_EMAIL, binding.edtEmail.getText().toString());
        editor.putInt(GENDER , check_radio);
        editor.putString(KEY_DATE_PICKER, binding.tvDatePicker.getText().toString());

        BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imgUpAvatar.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] arr = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(arr, Base64.DEFAULT);
        editor.putString(KEY_IMAGE, encodeImage);
        editor.apply();

        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showInformation() {
        int check = sharedPreferences.getInt(GENDER, 0);
        String name = sharedPreferences.getString(KEY_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String date_picker = sharedPreferences.getString(KEY_DATE_PICKER, "");
        String image = sharedPreferences.getString(KEY_IMAGE, "");

        binding.edtName.setText(name);
        binding.edtEmail.setText(email);
        if (!date_picker.equals("")) {
            binding.tvDatePicker.setText(date_picker);
        }

        if(check == 0){
            binding.radioMale.setChecked(true);
        }
        else if(check == 1){
            binding.radioFemale.setChecked(true);
        }


        if(!image.equalsIgnoreCase("")){
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
            binding.imgUpAvatar.setImageBitmap(bitmap);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == PICK_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null){
           Bundle extras = data.getExtras();
           Bitmap bitmap = (Bitmap) extras.get("data");
           binding.imgUpAvatar.setImageBitmap(bitmap);
       }
       if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data != null){
           Uri uri = data.getData();
           try {
               InputStream inputStream = getContentResolver().openInputStream(uri);
               Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
               binding.imgUpAvatar.setImageBitmap(bitmap);

           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
       }

    }

}