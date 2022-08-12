package com.example.movieapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.database.MovieDatabase;
import com.example.movieapp.interf.IReminder;
import com.example.movieapp.model.Reminder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ReminderListViewHolder> {
    List<Reminder> mReminderList;
    Context context;
    IReminder iReminder;

    public ReminderListAdapter(List<Reminder> mReminderList, Context context, IReminder iReminder) {
        this.mReminderList = mReminderList;
        this.context = context;
        this.iReminder = iReminder;
    }

    @NonNull
    @Override
    public ReminderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item , parent, false);
        return new ReminderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderListViewHolder holder, int position) {
        Reminder reminder = mReminderList.get(position);
        String year = reminder.getRelease_date();
        year = year.substring(0,4);

        holder.title.setText(reminder.getTitle()+" - "+ year + " - " + reminder.getVote_average() + "/10"  );
        holder.time.setText(reminder.getDate());
        Glide.with(context).load("https://image.tmdb.org/t/p/original/" + reminder.getPoster_path()).into(holder.imgPoster);

        holder.imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Are your sure to delete?");
                dialog.setCancelable(true);
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iReminder.deleteReminder(reminder);
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mReminderList.size();
    }

    public static  class  ReminderListViewHolder extends RecyclerView.ViewHolder{
        TextView title, time;
        ImageView imgPoster, imgArrow;


        public ReminderListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            time = itemView.findViewById(R.id.tv_time);
            imgPoster = itemView.findViewById(R.id.img_poster);
            imgArrow = itemView.findViewById(R.id.img_arrow);
        }
    }
}
