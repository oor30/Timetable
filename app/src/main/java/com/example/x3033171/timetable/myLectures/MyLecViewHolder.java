package com.example.x3033171.timetable.myLectures;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.x3033171.timetable.R;

public class MyLecViewHolder extends RecyclerView.ViewHolder {
    public TextView resWeek, resPeriod, resName;
    public CheckBox checkBox;
    public String lecCode;

    public MyLecViewHolder(@NonNull View itemView) {
        super(itemView);
        resWeek = itemView.findViewById(R.id.resWeek);
        resPeriod = itemView.findViewById(R.id.resPeriod);
        resName = itemView.findViewById(R.id.resName);
        checkBox = itemView.findViewById(R.id.addLecCB);
    }
}
