package com.example.x3033171.timetable;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultViewHolder extends RecyclerView.ViewHolder {
    public TextView resWeek, resPeriod, resName;
    public CheckBox addLecCB;
    public ResultViewHolder(@NonNull View itemView) {
        super(itemView);
        resWeek = itemView.findViewById(R.id.resWeek);
        resPeriod = itemView.findViewById(R.id.resPeriod);
        resName = itemView.findViewById(R.id.resName);
        addLecCB = itemView.findViewById(R.id.addLecCB);
    }
}
