package com.example.x3033171.timetable.Todo;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.x3033171.timetable.R;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    public TextView date, time, name, title;

    public TodoViewHolder(View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date);
        time = itemView.findViewById(R.id.time);
        name = itemView.findViewById(R.id.name);
        title = itemView.findViewById(R.id.title);
    }
}
