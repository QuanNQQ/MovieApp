package com.example.movieapp.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.movieapp.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String TAG ="AlarmReceiver";
        String a = intent.getStringExtra("KEY_TITLE");
        Log.d(TAG, "onReceive: "+ a);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ChanelId1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(intent.getStringExtra("KEY_TITLE"))
                .setContentText(intent.getStringExtra("KEY_YEAR"))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());


    }
}
