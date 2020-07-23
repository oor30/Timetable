package com.example.x3033171.timetable.myLectures;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.x3033171.timetable.searchLecture.Model;
import com.example.x3033171.timetable.R;

import java.util.List;

public class MyLecRecyclerViewAdapter extends RecyclerView.Adapter<MyLecViewHolder> {

    private List<Model> list;

    public MyLecRecyclerViewAdapter(List<Model> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyLecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_post, parent, false);
        return new MyLecViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLecViewHolder holder, final int position) {
        holder.resName.setText(list.get(position).getName());
        holder.resWeek.setText(String.format("%s曜", list.get(position).getWeeks().toString()
                .replace("1", "月")
                .replace("2", "火")
                .replace("3", "水")
                .replace("4", "木")
                .replace("5", "金")
                .replace("[", "")
                .replace("]", "")));
        holder.resPeriod.setText(String.format("%s限", list.get(position).getPeriods().toString()
                .replace("[", "")
                .replace("]", "")));
        holder.checkBox.setChecked(list.get(position).isChecked());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setChecked(((CheckBox) v).isChecked());
            }
        });
        holder.lecCode = list.get(position).getLecCode();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Model> getList() {
        return list;
    }
}
