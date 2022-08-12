package com.example.movieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.interf.IReminder;
import com.example.movieapp.model.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    List<Reminder> mReminderList;
    Context context;
    public ReminderAdapter(List<Reminder> mListReminder, Context context) {
        this.mReminderList = mListReminder;
        this.context = context;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list, parent, false);
        return new ReminderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = mReminderList.get(position);
        String year = reminder.getRelease_date();
        year = year.substring(0,4);

        holder.title.setText(reminder.getTitle()+" - "+ year + " - " + reminder.getVote_average() + "/10"  );
        if(reminder.getDate() != null) {
            holder.time.setText(reminder.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return mReminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        ImageView imgPoster;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            time = itemView.findViewById(R.id.tv_time);

        }
    }

}
